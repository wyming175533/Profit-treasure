package com.bjpowernode.microweb.Interceptors;

import com.alibaba.fastjson.JSONObject;
import com.bjpowernode.Consts.YLBKEY;
import com.bjpowernode.api.po.User;
import com.bjpowernode.code.ResponseCode;
import com.bjpowernode.vo.Result;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        User user= (User) request.getSession().getAttribute(YLBKEY.USER_SESSION);
        if (user==null){
            if("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {//对ajax的操作
                //来源是ajax,返回数据
                Result<String> result  = new Result<>();
                result.setResult(false);
                result.setData("");
               result.setEnum(ResponseCode.User_LOGIN_REQUEST);

                //输出result
                response.setContentType("application/json;charset=utf-8");
                PrintWriter out = response.getWriter();
                out.println(JSONObject.toJSONString(result));
                out.flush();
                out.close();
            }



            response.sendRedirect(request.getContextPath()+"/user/login");

        }else{
            return true;
        }


        return false;
    }
}
