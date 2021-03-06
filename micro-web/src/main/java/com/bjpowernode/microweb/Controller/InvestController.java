package com.bjpowernode.microweb.Controller;

import com.bjpowernode.Consts.YLBConsts;
import com.bjpowernode.Consts.YLBKEY;
import com.bjpowernode.Util.YLBUtil;
import com.bjpowernode.api.model.MyInvestVo;
import com.bjpowernode.api.model.ServiceResult;
import com.bjpowernode.api.po.User;
import com.bjpowernode.code.ResponseCode;
import com.bjpowernode.vo.PageVo;
import com.bjpowernode.vo.Result;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class InvestController extends BaseController{

    /** 跳转到个人投资记录前的处理
     * @param session
     * @param model
     * @param pageNo 第几页
     * @return 携带投资信息list集合，分页所需条件至myInvest
     */
    @GetMapping("/invest/myInvest")
    public String MyInvestShow(HttpSession session, Model model,
                               @RequestParam(required = false,defaultValue = "1")Integer pageNo){
     User user= (User) session.getAttribute(YLBKEY.USER_SESSION);
    if(user!=null){
        List<MyInvestVo> listVo=new ArrayList<>();
        pageNo=YLBUtil.PageNO(pageNo);
        Integer records=investService.InvestRecords(user.getId());
        if(records>0) {
            listVo = investService.selectMyInvestByUid(user.getId(), pageNo, YLBConsts.YLB_PRODUCT_INVESTPAGESIZE);
        }
        PageVo page=new PageVo(pageNo,YLBConsts.YLB_PRODUCT_INVESTPAGESIZE,records);
       model.addAttribute("listVo",listVo);
       model.addAttribute("pageVo",page);
    }

        return "myInvest";
    }

    @ResponseBody
    @PostMapping("/invest/product")
    public Result<String> investProduct(HttpSession session, Integer productId, BigDecimal bidMoney){
        Result<String> result=Result.fail();
        if(productId!=null && productId>0 && bidMoney!=null && (bidMoney.intValue()%100==0)){
            User user= (User) session.getAttribute(YLBKEY.USER_SESSION);
            if(user!=null){
            ServiceResult serviceResult=investService.invest(user.getId(),productId,bidMoney);
            if(serviceResult.isResult()){
                result=Result.ok();
                redisOpreation.incrScoreZSet(YLBKEY.INVEST_TOP_ORDER,user.getPhone(),bidMoney.doubleValue());
            }else{
                result.setMsg(serviceResult.getMsg());
            }
            }

        }else{
            result=Result.erro(ResponseCode.PARAM_EMPTY);
        }


        return result;

    }


}
