package com.bjpowernode.microweb.Controller;

import com.bjpowernode.Consts.YLBConsts;
import com.bjpowernode.Consts.YLBKEY;
import com.bjpowernode.Util.YLBUtil;
import com.bjpowernode.api.model.MyInvestVo;
import com.bjpowernode.api.po.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class InvestController extends BaseController{

    @GetMapping("/invest/myInvest")
    public String MyInvestShow(HttpSession session, Model model){
    User user= (User) session.getAttribute(YLBKEY.USER_SESSION);
    if(user!=null){
       List<MyInvestVo> listVo=investService.selectMyInvestByUid(user.getId(),1, YLBConsts.YLB_PRODUCT_INVESTPAGESIZE);
        model.addAttribute("myInvest",listVo);
    }

        return "myInvest";
    }


}
