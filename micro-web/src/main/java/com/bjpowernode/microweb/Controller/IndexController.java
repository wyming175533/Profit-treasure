package com.bjpowernode.microweb.Controller;

import com.bjpowernode.api.service.InvestService;
import com.bjpowernode.api.service.UserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;

@Controller
public class IndexController {

    @DubboReference(interfaceClass = UserService.class,version = "1.0")
    private UserService userService;
    @DubboReference(interfaceClass = InvestService.class, version = "1.0")
    private InvestService investService;
    /**
     * 进入web首页
     * @param model 模型
     * @return
     */
    @GetMapping("/index")
    public String  Index(Model model){
        Integer UserRegisterCount=userService.registerAllUserCount();
        BigDecimal bid_money=investService.statisticsInvestSumAllMoney();
        model.addAttribute("UserRegisterCount",UserRegisterCount);
        model.addAttribute("bid_money",bid_money);
        return"index";
    }

}
