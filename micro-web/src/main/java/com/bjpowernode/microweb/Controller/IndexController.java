package com.bjpowernode.microweb.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    /**
     * 进入web首页
     * @param model 模型
     * @return
     */
    @GetMapping("/index")
    public String  Index(Model model){

        return"index";
    }

}
