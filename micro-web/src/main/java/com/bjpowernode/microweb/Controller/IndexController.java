package com.bjpowernode.microweb.Controller;

import com.bjpowernode.api.po.Product;
import com.bjpowernode.api.service.InvestService;
import com.bjpowernode.api.service.ProductService;
import com.bjpowernode.api.service.UserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class IndexController {

    @DubboReference(interfaceClass = UserService.class,version = "1.0")
    private UserService userService;
    @DubboReference(interfaceClass = InvestService.class, version = "1.0")
    private InvestService investService;
    @DubboReference(interfaceClass = ProductService.class,version = "1.0")
    private ProductService productService;
    /**
     * 进入web首页
     * @param model 模型
     * @return
     */
    @GetMapping({"/index","","/"})
    public String  Index(Model model){
        Integer UserRegisterCount=userService.registerAllUserCount();
        BigDecimal bid_money=investService.statisticsInvestSumAllMoney();
        BigDecimal avg_rate=productService.computeAvgRate();
        List<Product> xinList=productService.FindListByType(0,1,1);
        model.addAttribute("UserRegisterCount",UserRegisterCount);//用户注册数量
        model.addAttribute("bid_money",bid_money);//总金额
        model.addAttribute("avg_rate",avg_rate);//平均收益率
        model.addAttribute("xinList",xinList);
        return"index";
    }

}
