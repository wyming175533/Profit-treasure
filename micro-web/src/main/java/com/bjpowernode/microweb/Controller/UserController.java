package com.bjpowernode.microweb.Controller;

import com.bjpowernode.Util.YLBUtil;
import com.bjpowernode.api.po.User;
import com.bjpowernode.code.ResponseCode;
import com.bjpowernode.vo.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController extends BaseController {

    @GetMapping("/user/page/register")
    public String Register(Model model){

        return "register";
    }

    /**
     * @param phone 注册用户的手机号
     * @return 结果信息，包括状态码，提示信息，true/false，data
     */
    @ResponseBody
    @GetMapping("/user/phone")
    public Result Phone(@RequestParam("phone")  String phone){
    Result result=Result.fail();
        if(StringUtils.isBlank(phone)){
            result=Result.erro(ResponseCode.PARAM_EMPTY);
        }else if(!YLBUtil.regex(phone)){
            result=Result.erro(ResponseCode.PHONE_ERR);
        }else{
            User user=userService.selectUserByPhone(phone);
            if(user!=null){
                result=Result.erro(ResponseCode.User_EXISTS);
            }else{
                result=Result.ok();
            }
        }
        System.out.println(result.toString()+"-------------------------------------");
        return result;
    }

}
