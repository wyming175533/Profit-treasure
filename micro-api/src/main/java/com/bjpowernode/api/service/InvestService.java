package com.bjpowernode.api.service;

import com.bjpowernode.api.model.InvestInfo;
import com.bjpowernode.api.model.MyInvestVo;

import java.math.BigDecimal;
import java.util.List;

public interface InvestService {
    BigDecimal statisticsInvestSumAllMoney();

    /**
     * @param id 产品id
     * @param pageno 第几页
     * @param InvestPagesize 每页展示投资记录数量
     * @return
     */
    List<InvestInfo> selectInvestInfo(Integer id, Integer pageno, Integer InvestPagesize);

    List<MyInvestVo> selectMyInvestByUid(Integer id, Integer i, Integer pageSize);

    Integer InvestRecords(Integer uid);
}
