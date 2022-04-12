package com.bjpowernode.api.service;

import com.bjpowernode.api.po.User;

public interface UserService {
    Integer registerAllUserCount();

    User selectUserByPhone(String phone);

    User insertUser(String phone, String password, String loginIp, String device);

}
