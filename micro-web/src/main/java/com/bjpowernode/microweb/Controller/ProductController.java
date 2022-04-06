package com.bjpowernode.microweb.Controller;

import com.bjpowernode.Util.PageUtil;
import com.bjpowernode.api.po.Product;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

import static com.bjpowernode.YLBConsts.YLB_PRODUCT_PAGESIZE;

@Controller
public class ProductController extends BaseController {
    @GetMapping("/product/show")
    public String moreProduct(@RequestParam("ptype") Integer ptype,
                              @RequestParam(required = false,defaultValue = "1")Integer pageno,
                              Model model){

        List<Product> ProductList=new ArrayList<>();
        if(helpService.checkProductType(String.valueOf(ptype))){
        pageno=PageUtil.PageNO(pageno);
        ProductList=productService.FindListByType(ptype,pageno,YLB_PRODUCT_PAGESIZE);
        }
        model.addAttribute("productList",ProductList);
        return "products";
    }

}
