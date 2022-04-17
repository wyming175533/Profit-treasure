package com.bjpowernode.microdataservice.mapper;

import com.bjpowernode.api.po.FinanceAccount;

import java.math.BigDecimal;

public interface FinanceAccountMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FinanceAccount record);

    int insertSelective(FinanceAccount record);

    FinanceAccount selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FinanceAccount record);

    int updateByPrimaryKey(FinanceAccount record);

    void insertUidAndMoney(FinanceAccount financeAccount);

    FinanceAccount selectByUid(Integer uid);

    FinanceAccount selectAccountByUIdForUpDate(Integer uid);

    int updateAccountMoney(Integer uid, BigDecimal bidMoney);

    int updateAccountMoneyInc(Integer uid, BigDecimal rechargeMoney);
}