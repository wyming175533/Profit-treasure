package com.bjpowernode.microdataservice.service;

import com.bjpowernode.api.service.UserService;
import com.bjpowernode.microdataservice.mapper.UserMapper;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService(interfaceClass = UserService.class,version = "1.0")
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    /**
     * @return 返回注册总人数
     */
    @Override
    public Integer registerAllUserCount() {
        return userMapper.selectRegisterCount();
    }
}
