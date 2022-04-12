package com.bjpowernode.microweb.Controller;

import com.bjpowernode.Util.YLBUtil;
import com.bjpowernode.api.po.User;
import com.bjpowernode.api.service.UserService;
import com.bjpowernode.code.ResponseCode;
import com.bjpowernode.microweb.Service.UserAndSmsCheckService;
import com.bjpowernode.vo.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
public class UserController extends BaseController {
    @Resource
    private UserAndSmsCheckService USservice;
    @Resource
    private UserService userService;
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

    @GetMapping("/user/realName")
    public String RealName(String phone,Model model){
        model.addAttribute("phone",phone);
        return "realName";
    }
    @ResponseBody
    @PostMapping("/user/checked")
    public Result<String> checked(String phone,String idCard,String realName,String registerPhone){
        Result<String> result=Result.fail();
        if(StringUtils.isAllBlank(phone,idCard,realName,registerPhone)){
            result=Result.erro(ResponseCode.PARAM_EMPTY);
        }else if(!YLBUtil.regex(phone)){
            result=Result.erro(ResponseCode.PHONE_ERR);
        }else if(registerPhone!=phone){
            result=Result.erro(ResponseCode.PHONE_DIFF_ERR);
        }
        else if(1<0)//@todo 验证身份证号格式
             {}
        else if(realName.length()<2){
            //姓名小于两位错误
        }else if(!USservice.checkIdcard(idCard,realName)){
            result=Result.erro(ResponseCode.IDCARD_REALNAME_DIFF);
        }else{
            result=Result.ok();
        }


        return result;

    }



    @ResponseBody
    @PostMapping("/user/register")
    public Result<String> Register(String phone, String code, String password, HttpServletRequest request){
        Result<String> result=Result.fail();
        //参数判断
        if(StringUtils.isAllBlank(phone,code,password)){
            result.setEnum(ResponseCode.PARAM_EMPTY);
        }else if(!YLBUtil.regex(phone)){
            result.setEnum(ResponseCode.PHONE_ERR);
        }else if(password.length()<32){
            result.setEnum(ResponseCode.PASSWORD_LENGTH_ERR);
        }else if(!USservice.checkAuthCode(phone,code)){
            result.setEnum(ResponseCode.AuthCode_ERR);
        }else{
        //注册用户
            String LoginIp=request.getRemoteAddr();
            String Device=request.getHeader("User-Agent");
            User user =userService.insertUser(phone,password,LoginIp,Device);
            if(user!=null){

                result=Result.ok();
            }
         //跳转到实名认证界面
        }

        return  result;
    }

}
