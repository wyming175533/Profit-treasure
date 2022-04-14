package com.bjpowernode.microweb.Controller;

import com.bjpowernode.Consts.YLBConsts;
import com.bjpowernode.Consts.YLBKEY;
import com.bjpowernode.api.po.RechargeRecord;
import com.bjpowernode.api.po.User;

import com.bjpowernode.vo.PageVo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class RechargeController extends BaseController {

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
