package com.bjpowernode.microdataservice.service;

import com.bjpowernode.Util.YLBUtil;
import com.bjpowernode.api.model.IncomeVo;
import com.bjpowernode.api.service.IncomeRecordService;
import com.bjpowernode.microdataservice.mapper.IncomeRecordMapper;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.util.List;

@DubboService(interfaceClass = IncomeRecordService.class,version = "1.0")
public class IncomeRecordServiceImpl implements IncomeRecordService{
    @Resource
    private IncomeRecordMapper incomeRecordMapper;
    @Override
    public List<IncomeVo> selectIncomeInfo(Integer uid, Integer pageNo, Integer pageSize) {
        pageNo= YLBUtil.PageNO(pageNo);
        pageSize=YLBUtil.PageSize(pageSize);
        Integer offset=YLBUtil.offset(pageNo,pageSize);

        List<IncomeVo> list=incomeRecordMapper.selectIncomeInfo(uid,offset,pageSize);
        return list;
    }

    @Override
    public Integer IncomeRecords(Integer uid) {
        Integer count=incomeRecordMapper.selectIncomeRecordsByUid(uid);
        if(count==null)
            count=0;
        return count;
    }
}
