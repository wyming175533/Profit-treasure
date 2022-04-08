package com.bjpowernode.microdataservice.mapper;

import com.bjpowernode.api.model.InvestInfo;
import com.bjpowernode.api.po.BidInfo;
import com.bjpowernode.api.po.IncomeRecord;

import java.math.BigDecimal;
import java.util.List;

public interface IncomeRecordMapper {
    BigDecimal selectStatisticsInvestSumAllMoney();
    int deleteByPrimaryKey(Integer id);

    int insert(IncomeRecord record);

    int insertSelective(IncomeRecord record);

    IncomeRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(IncomeRecord record);

    int updateByPrimaryKey(IncomeRecord record);

}