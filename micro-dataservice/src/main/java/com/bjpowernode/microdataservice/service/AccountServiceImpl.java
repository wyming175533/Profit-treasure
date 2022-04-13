package com.bjpowernode.microdataservice.service;

import com.bjpowernode.api.po.FinanceAccount;
import com.bjpowernode.api.service.AccountService;
import com.bjpowernode.microdataservice.mapper.FinanceAccountMapper;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService(interfaceClass = AccountService.class,version = "1.0")
public class AccountServiceImpl implements AccountService {

    @Resource
    private FinanceAccountMapper financeMapper;

    @Override
    public FinanceAccount queryAccountMoney(Integer uid) {
        FinanceAccount account=null;
        if(uid!=null){
         account= financeMapper.selectByUid(uid);
        }
        return account;
    }
}
