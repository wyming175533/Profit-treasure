package com.bjpowernode.microweb.Controller;

import com.bjpowernode.microweb.Service.HelpService;
import com.bjpowernode.api.service.InvestService;
import com.bjpowernode.api.service.ProductService;
import com.bjpowernode.api.service.UserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

@Controller
public class BaseController {
    @DubboReference(interfaceClass = UserService.class,version = "1.0")
    protected UserService userService;
    @DubboReference(interfaceClass = InvestService.class, version = "1.0")
    protected InvestService investService;
    @DubboReference(interfaceClass = ProductService.class,version = "1.0")
    protected ProductService productService;
    @Resource
    protected HelpService helpService;
}
