package com.bjpowernode.microweb.Controller;

import com.bjpowernode.Consts.YLBKEY;
import com.bjpowernode.api.po.FinanceAccount;
import com.bjpowernode.api.po.User;
import com.bjpowernode.microweb.view.InvestTopBean;
import com.bjpowernode.vo.PageVo;
import com.bjpowernode.Util.YLBUtil;
import com.bjpowernode.api.model.InvestInfo;
import com.bjpowernode.api.po.Product;
import com.bjpowernode.microweb.view.ProductView;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.bjpowernode.Consts.YLBConsts.YLB_PRODUCT_INVESTPAGESIZE;
import static com.bjpowernode.Consts.YLBConsts.YLB_PRODUCT_PAGESIZE;

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
        if(redisOpreation.checkProductType(String.valueOf(ptype))){
        pageno= YLBUtil.PageNO(pageno);
        ProductList=productService.FindListByType(ptype,pageno,YLB_PRODUCT_PAGESIZE);


            Integer record=productService.queryRecordCount(ptype);
            pageVo=new PageVo(pageno, YLB_PRODUCT_PAGESIZE,record);

        }
        model.addAttribute("productList",ProductList);
        model.addAttribute("page",pageVo);
        model.addAttribute("type",ptype);
        //@todo 计算排名
        List<InvestTopBean> list=new ArrayList<>();
        Set<ZSetOperations.TypedTuple<String>> typedTuples = redisOpreation.reverseRangeZSet(YLBKEY.INVEST_TOP_ORDER, 0, 4);
        typedTuples.forEach(
                zset->{
                    list.add(new InvestTopBean(zset.getValue(),zset.getScore()));
                }
        );
        model.addAttribute("topList",list);


        return "products";
    }

    /**
     *
     * @param id 产品主键
     * @return 投资详情页
     */
    @GetMapping("/product/productInfo")
    public String  productInfo(Model model, @RequestParam("id") Integer id,
                               @RequestParam(required = false,defaultValue = "1")Integer pageno
                                    , HttpSession session) {
        String mssg="内容没有找到";
        if(YLBUtil.ifNullZero(id)){
            model.addAttribute("mssg",mssg);
            return "erroPage";
        }
        Product product= productService.FindByProductId(id);
        if(product==null){
            model.addAttribute("mssg",mssg);
            return "erroPage";
        }
        ProductView productView = ProductView.turnView(product);;

        model.addAttribute("info",product);
        model.addAttribute("view",productView);
        //获取产品投资情况信息
        List<InvestInfo> investList=investService.selectInvestInfo(product.getId(),pageno,YLB_PRODUCT_INVESTPAGESIZE);
        model.addAttribute("investList",investList);

        User user= (User) session.getAttribute(YLBKEY.USER_SESSION);
        if(user!=null){
            FinanceAccount account= accountService.queryAccountMoney(user.getId());
            model.addAttribute("availableMoney",account.getAvailableMoney());
        }
        //@todo 右侧排行

        return "productInfo2";
    }

}
