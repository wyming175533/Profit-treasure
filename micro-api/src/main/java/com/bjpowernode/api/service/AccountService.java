package com.bjpowernode.api.service;

import com.bjpowernode.api.po.FinanceAccount;

public interface AccountService {
    FinanceAccount queryAccountMoney(Integer uid);
}
