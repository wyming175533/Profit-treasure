package com.bjpowernode.microdataservice.mapper;

import com.bjpowernode.api.po.RechargeRecord;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

public interface RechargeRecordMapper {

    List<RechargeRecord> selectByUserId(@Param("userId") Integer userId,
                                        @Param("offSet") Integer offSet,
                                        @Param("rows") Integer rows);

    //用户充值总记录数
    Integer selectCountRechargeNumByUserId(@Param("userId") Integer userId);

    int deleteByPrimaryKey(Integer id);

    int insert(RechargeRecord record);

    int insertSelective(RechargeRecord record);

    RechargeRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RechargeRecord record);

    int updateByPrimaryKey(RechargeRecord record);

    RechargeRecord selectByRechargeNo(String orderId);

    int updateRecharge(Integer id, Integer rechargeStatusRecharged);

    int updateRechargeByOrderId(String orderId, Integer rechargeStatusRechargeerr);
}
