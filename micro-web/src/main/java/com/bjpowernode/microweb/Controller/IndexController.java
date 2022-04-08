package com.bjpowernode.microweb.Controller;

import com.bjpowernode.api.po.Product;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.util.List;

import static com.bjpowernode.Consts.YLBConsts.*;

@Controller
public class IndexController extends BaseController{


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
        List<Product> xinList=productService.FindListByType(PRODUCT_TYPE_XINSHOUBAO,1,1);
        List<Product> sanList=productService.FindListByType(PRODUCT_TYPE_SANBIAO,1,8);
        List<Product> youList=productService.FindListByType(PRODUCT_TYPE_YOUXUAN,1,4);


        model.addAttribute("UserRegisterCount",UserRegisterCount);//用户注册数量
        model.addAttribute("bid_money",bid_money);//总金额
        model.addAttribute("avg_rate",avg_rate);//平均收益率
        model.addAttribute("xinList",xinList);
        model.addAttribute("sanList",sanList);
        model.addAttribute("youList",youList);
        return"index";
    }

}
