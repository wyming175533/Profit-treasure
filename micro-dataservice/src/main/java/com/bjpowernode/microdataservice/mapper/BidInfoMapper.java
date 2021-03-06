package com.bjpowernode.microdataservice.mapper;


import com.bjpowernode.api.model.InvestInfo;
import com.bjpowernode.api.model.MyInvestVo;
import com.bjpowernode.api.po.BidInfo;

import java.util.List;

public interface BidInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BidInfo record);

    int insertSelective(BidInfo record);

    BidInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BidInfo record);

    int updateByPrimaryKey(BidInfo record);

    List<InvestInfo> selectInvestInfo(Integer id, int offset, Integer pagesize);

    List<MyInvestVo> selectMyInvestByUid(Integer id, Integer offset, Integer pageSize);

    Integer selectInvestRecordByUid(Integer uid);

    List<BidInfo> selectByProductId(Integer pid);
}