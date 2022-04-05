package com.bjpowernode.microdataservice.service;

import com.bjpowernode.api.service.InvestService;
import com.bjpowernode.microdataservice.mapper.IncomeRecordMapper;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.math.BigDecimal;
@DubboService(interfaceClass = InvestService.class, version = "1.0")
public class InvestServiceImpl  implements InvestService {
    @Resource
    private IncomeRecordMapper incomeRecordMapper;
    @Override
    public BigDecimal statisticsInvestSumAllMoney() {
        return incomeRecordMapper.selectStatisticsInvestSumAllMoney();
    }
}
