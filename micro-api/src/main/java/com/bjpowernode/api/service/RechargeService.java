package com.bjpowernode.api.service;

import com.bjpowernode.api.po.RechargeRecord;

import java.util.List;

public interface RechargeService {

    /**
     * 分页用户充值记录
     * @param userId   userId
     * @param pageNo   页号
     * @param pageSize 每页大小
     * @return
     */
    List<RechargeRecord> queryByUserId(Integer userId, Integer pageNo, Integer pageSize);

    /**
     * 查询用户充值总记录数量
     * @param userId 用户id
     * @return
     */
    Integer queryCountByUserId(Integer userId);
}
