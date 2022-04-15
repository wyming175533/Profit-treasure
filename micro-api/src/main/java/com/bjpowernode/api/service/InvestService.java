package com.bjpowernode.api.service;

import com.bjpowernode.api.model.InvestInfo;
import com.bjpowernode.api.model.MyInvestVo;
import com.bjpowernode.api.model.ServiceResult;

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

    /** 投资服务
     * @param uid 用户id
     * @param productId 产品id
     * @param bidMoney 投资金额
     * @return 结果
     */
    ServiceResult invest(Integer uid, Integer productId, BigDecimal bidMoney);
}
