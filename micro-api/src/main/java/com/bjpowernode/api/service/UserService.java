package com.bjpowernode.api.service;

import com.bjpowernode.api.model.ServiceResult;
import com.bjpowernode.api.po.User;

public interface UserService {
    Integer registerAllUserCount();

    User selectUserByPhone(String phone);

    User insertUser(String phone, String password, String loginIp, String device);

    /**
     * @param user
     * @return
     */
    User update(User user);

    ServiceResult loginCheck(String phone, String password, String login_ip, String login_device);
}

