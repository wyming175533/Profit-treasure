package com.bjpowernode.microweb.settings;

import com.bjpowernode.microweb.Interceptors.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class ApplicationMvcConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        LoginInterceptor loginInterceptor=new LoginInterceptor();
        String [] addPath={"/invest/**","recharge/**","/user/**"};
        String [] excludePath={"/user/login",
        "/user/logout",
        "/user/login",
        "/user/register",
        "/user/phone","/user/page/register"};
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns(addPath)
                .excludePathPatterns(excludePath);
    }
}
