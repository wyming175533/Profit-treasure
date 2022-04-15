package com.bjpowernode.api.service;

import com.bjpowernode.api.model.IncomeVo;

import java.util.List;

public interface IncomeRecordService {

    public List<IncomeVo> selectIncomeInfo(Integer uid,Integer pageNo,Integer pageSize);
    public Integer IncomeRecords(Integer uid);

}
