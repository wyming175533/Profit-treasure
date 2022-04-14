package com.bjpowernode.microweb.Controller;

import com.bjpowernode.Util.RedisOpreation;
import com.bjpowernode.api.service.*;
import com.bjpowernode.microweb.Service.HelpService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

@Controller
public class BaseController {
    //用户
    @DubboReference(interfaceClass = UserService.class,version = "1.0")
    protected UserService userService;
    //投资
    @DubboReference(interfaceClass = InvestService.class, version = "1.0")
    protected InvestService investService;
    //产品
    @DubboReference(interfaceClass = ProductService.class,version = "1.0")
    protected ProductService productService;
    //redis
    @Resource
    protected RedisOpreation redisOpreation;
    //账户
    @DubboReference(interfaceClass = AccountService.class,version = "1.0")
    protected AccountService accountService;
    //充值服务
    @DubboReference(interfaceClass = RechargeService.class,version = "1.0")
    protected RechargeService rechargeService;

}

