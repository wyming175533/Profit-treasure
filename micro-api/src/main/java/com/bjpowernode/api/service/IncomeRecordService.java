package com.bjpowernode.api.service;

import com.bjpowernode.api.model.IncomeVo;

import java.util.List;

public interface IncomeRecordService {

    public List<IncomeVo> selectIncomeInfo(Integer uid,Integer pageNo,Integer pageSize);
    public Integer IncomeRecords(Integer uid);
        //计算预期收益，产品满标，计算收益
    void generateIncomePlan();
    //收益返回
     void generateIncomeBack();

}
