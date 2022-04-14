package com.bjpowernode.microdataservice.service;

import com.bjpowernode.Util.YLBUtil;
import com.bjpowernode.api.po.RechargeRecord;


import com.bjpowernode.api.service.RechargeService;
import com.bjpowernode.microdataservice.mapper.RechargeRecordMapper;

import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@DubboService(interfaceClass = RechargeService.class,version = "1.0")
public class RechargeServiceImpl implements RechargeService {

    @Resource
    private RechargeRecordMapper rechargeRecordMapper;
    @Override
    public List<RechargeRecord> queryByUserId(Integer userId,
                                              Integer pageNo,
                                              Integer pageSize) {
        List<RechargeRecord> rechargeList = new ArrayList<>();
        if( userId != null && userId > 0 ){
            pageNo = YLBUtil.PageNO(pageNo);
            pageSize = YLBUtil.PageSize(pageSize);
            rechargeList = rechargeRecordMapper.selectByUserId(userId, YLBUtil.offset(pageNo,pageSize),pageSize);
        }
        return rechargeList;
    }

    @Override
    public Integer queryCountByUserId(Integer userId) {
        Integer countNums = 0;
        if( userId != null && userId > 0 ){
            countNums = rechargeRecordMapper.selectCountRechargeNumByUserId(userId);
        }
        return countNums;
    }
}
