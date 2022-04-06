package com.bjpowernode.microweb.Controller;

import com.bjpowernode.PageVo;
import com.bjpowernode.Util.PageUtil;
import com.bjpowernode.YLBConsts;
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
    /**
     * @param ptype 产品类型
     * @param pageno 第几页
     * @param model MOdel
     * @return 返回到更多产品展示
     */
    @GetMapping("/product/show")
    public String moreProduct(@RequestParam("ptype") Integer ptype,
                              @RequestParam(required = false,defaultValue = "1")Integer pageno, Model model){

        List<Product> ProductList=new ArrayList<>();
        //判断传入的参数
        PageVo pageVo=null;
        if(helpService.checkProductType(String.valueOf(ptype))){
        pageno=PageUtil.PageNO(pageno);
        ProductList=productService.FindListByType(ptype,pageno,YLB_PRODUCT_PAGESIZE);


            Integer record=productService.queryRecordCount(ptype);
             pageVo=new PageVo(pageno, YLB_PRODUCT_PAGESIZE,record);

        }
        model.addAttribute("productList",ProductList);
        model.addAttribute("page",pageVo);
        model.addAttribute("type",ptype);
        //@todo 计算排名
        return "products";
    }

    @GetMapping("/product/productInfo")
    public String  productInfo(Model model){
        return "productInfo";
    }

}
