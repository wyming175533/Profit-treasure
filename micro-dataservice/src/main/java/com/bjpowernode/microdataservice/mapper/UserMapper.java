package com.bjpowernode.microdataservice.mapper;

import com.bjpowernode.api.po.User;

public interface UserMapper {
    int selectRegisterCount();

    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}