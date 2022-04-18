package com.bjpowernode.pay.Controller;

import com.bjpowernode.Consts.YLBKEY;
import com.bjpowernode.api.model.ServiceResult;
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
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Enumeration;
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


    //接收快钱的异步通知
    @GetMapping("/kq/notify")
    @ResponseBody
    public String kqNotify(HttpServletRequest request){
        System.out.println("接收快钱的异步通知");
        StringBuilder builder = new StringBuilder();
        Enumeration<String> names = request.getParameterNames();
        while(names.hasMoreElements()){ //枚举的遍历
            String name  = names.nextElement();
            String value  = request.getParameter(name);
            builder.append(name).append("=").append(value).append("&");

        }
        System.out.println("参数："+builder.toString());

        ServiceResult result=kuaiQianService.handlerNotif(request);
        kuaiQianService.removeOrderFromRedis(request.getParameter("orderId"));

        //用户中心页面（外围地址）
        return "<result>1</result><redirecturl>http://43.129.246.251:7410/ylb/user/myCenter</redirecturl>";
    }

    @ResponseBody
    @GetMapping("/kq/query")
    public Result<String> kqQuery(String rechargeNo){
        Result<String> result=Result.fail();

        ServiceResult serviceResult=kuaiQianService.handlerQuery(rechargeNo);
        if(serviceResult.isResult()){
            result=Result.ok();
        }

        return result;
    }

    @ResponseBody
    @GetMapping("/kq/task/query")
    public String kqTaskQUery(){
        System.out.println("kq/task/query.....");
        kuaiQianService.handlerTaskQuery();
        return  "ok";
    }


}
