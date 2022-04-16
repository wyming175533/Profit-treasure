package com.bjpowernode.microweb.Controller;

import com.alibaba.fastjson.JSONObject;
import com.bjpowernode.Consts.YLBConsts;
import com.bjpowernode.Consts.YLBKEY;
import com.bjpowernode.Util.HttpClientUtils;
import com.bjpowernode.api.po.RechargeRecord;
import com.bjpowernode.api.po.User;

import com.bjpowernode.vo.PageVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class RechargeController extends BaseController {
    @Value("${micro.pay.url}")
    private String microPayChargeNoUrl;
    //url: http://localhost:9000/pay/apply/rechargeNo
    //    receaction: http://localhost:9000/pay/kq/receweb
    @Value("${micro.pay.receaction}")
    private String microPayRechargeReceUrl;

    @GetMapping("/recharge/toRecharge")
    public String toRecharge(Model model){
    String rechargeNo="";
    try{
        String json= HttpClientUtils.doGet(microPayChargeNoUrl+"?channel=kq");
        JSONObject jsonObject=JSONObject.parseObject(json);
        if(jsonObject!=null){
            rechargeNo=jsonObject.getString("data");
        }
    }catch (Exception e){
    e.printStackTrace();
    }
    model.addAttribute("receurl",microPayRechargeReceUrl);
    model.addAttribute("rechargeNo",rechargeNo);


        return "toRecharge";
    }




    //分页查看充值记录
    @GetMapping("/recharge/myRecharge")
    public String myRecharge(@RequestParam(value = "pageNo",defaultValue = "1") Integer pageNo,
                             HttpSession session,
                             Model model){
        User user  = (User) session.getAttribute(YLBKEY.USER_SESSION);
        List<RechargeRecord> rechargeList = new ArrayList<>();
        PageVo page  = new PageVo();

        Integer countRecharge = rechargeService.queryCountByUserId(user.getId());
        if( countRecharge > 0 ){
            rechargeList = rechargeService.queryByUserId(user.getId(),pageNo, YLBConsts.YLB_PRODUCT_INVESTPAGESIZE);
            page  = new PageVo(pageNo,YLBConsts.YLB_PRODUCT_INVESTPAGESIZE,countRecharge);
        }
        model.addAttribute("rechargeList",rechargeList);
        model.addAttribute("page",page);

        return "myRecharge";

    }
}
