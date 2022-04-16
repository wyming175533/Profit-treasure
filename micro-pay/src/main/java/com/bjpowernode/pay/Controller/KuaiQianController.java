package com.bjpowernode.pay.Controller;

import com.bjpowernode.Consts.YLBKEY;
import com.bjpowernode.api.po.RechargeRecord;
import com.bjpowernode.api.service.RechargeService;
import com.bjpowernode.pay.Service.KuaiQianService;
import com.bjpowernode.vo.Result;
import jdk.jfr.consumer.RecordedClass;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

@Controller
public class KuaiQianController{
@Resource
private KuaiQianService kuaiQianService;

@DubboReference(version = "1.0")
private RechargeService rechargeService;

    /**生成对应渠道的订单号
     * @param channel 渠道名
     * @return
     */

    @ResponseBody
    @GetMapping("/apply/rechargeNo")
    public Result<String> getRechargeNo(String channel){
        System.out.println("rechargeNo");
    if(StringUtils.isBlank(channel)){
        channel="kq";   //默认支付渠道
    }
    String RechargeNo=kuaiQianService.generateRechargeNo(channel);
        Result<String> result=Result.ok();
        result.setData(RechargeNo);
        return result;
    }

    @GetMapping("/kq/receweb")
public String receRequestFromWeb(@RequestParam("rechargeMoney") BigDecimal money,
                                 @RequestParam("channel") String channel,
                                 @RequestParam("userId") String userId,
                                 @RequestParam("rechargeNo") String rechargeNo,
                                 @RequestParam("phone") String phone,
                                 Model model){
        try {
            Map<String,String> param=kuaiQianService.generateKqFormData(userId,rechargeNo,money,phone);

            RechargeRecord rechargeRecord=new RechargeRecord();
            rechargeRecord.setChannel(channel);
            rechargeRecord.setRechargeDesc(param.get("productDesc"));
            rechargeRecord.setRechargeNo(rechargeNo);
            rechargeRecord.setRechargeMoney(money);
            rechargeRecord.setUid(Integer.valueOf(userId));
            rechargeRecord.setRechargeTime(new Date());
            rechargeRecord.setRechargeStatus(YLBKEY.RECHARGE_STATUS_RECHARGEING);
            rechargeRecord.setAction(YLBKEY.RECHARGE_YES);
            boolean res=rechargeService.insertRechargeReord(rechargeRecord);

            model.addAllAttributes(param);
        }catch (Exception e){
            e.printStackTrace();
        }


        return "kqForm";
}




}
