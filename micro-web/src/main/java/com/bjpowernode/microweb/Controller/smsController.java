package com.bjpowernode.microweb.Controller;

import com.bjpowernode.Util.YLBUtil;
import com.bjpowernode.code.ResponseCode;
import com.bjpowernode.microweb.Service.UserAndSmsCheckService;
import com.bjpowernode.vo.Result;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class smsController {
    @Resource
    private UserAndSmsCheckService sendService;

    @PostMapping("/sms/send/authCode")
    @ResponseBody
    public Result<String> smsSend(String phone){
        Result<String> result=Result.fail();
        //判断手机号是否合法
        if(!YLBUtil.regex(phone)){
            result.setEnum(ResponseCode.PHONE_ERR);
        }else{
            if(sendService.userRegisterSmsSend(phone)){//判断
                result=Result.ok();
            }
        }
     return result;
    }


}
