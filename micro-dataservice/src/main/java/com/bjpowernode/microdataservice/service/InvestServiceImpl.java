package com.bjpowernode.microdataservice.service;

import com.bjpowernode.Util.YLBUtil;
import com.bjpowernode.Util.RedisOpreation;
import com.bjpowernode.Consts.YLBKEY;
import com.bjpowernode.api.model.InvestInfo;
import com.bjpowernode.api.model.MyInvestVo;
import com.bjpowernode.api.service.InvestService;
import com.bjpowernode.microdataservice.mapper.BidInfoMapper;
import com.bjpowernode.microdataservice.mapper.IncomeRecordMapper;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@DubboService(interfaceClass = InvestService.class, version = "1.0")
public class InvestServiceImpl  implements InvestService {
    @Resource
    private IncomeRecordMapper incomeRecordMapper;
    @Resource
    private BidInfoMapper bidInfoMapper;
    @Resource
    private RedisOpreation redisOpreation;
    /**
     * @return 返回成交金额总数
     */
    @Override
    public BigDecimal statisticsInvestSumAllMoney() {
        BigDecimal AllMoney= (BigDecimal) redisOpreation.getKey(YLBKEY.INVEST_PRODUCT_ALLMONEY);
        if(AllMoney==null){
            synchronized (this){
                if((AllMoney= (BigDecimal) redisOpreation.getKey(YLBKEY.INVEST_PRODUCT_ALLMONEY))==null){
                    AllMoney=incomeRecordMapper.selectStatisticsInvestSumAllMoney();
                    redisOpreation.setKey(YLBKEY.INVEST_PRODUCT_ALLMONEY,AllMoney,30);
                }
            }
        }
        return AllMoney;
    }

    /**
     * @param id             产品id
     * @param pageno         第几页
     * @param InvestPagesize 每页展示投资记录数量
     * @return
     */
    @Override
    public List<InvestInfo> selectInvestInfo(Integer id, Integer pageno, Integer InvestPagesize) {
        if(YLBUtil.ifNullZero(id))
            return new ArrayList<InvestInfo>();

        pageno= YLBUtil.PageNO(pageno);
        InvestPagesize= YLBUtil.PageSize(InvestPagesize);
        int offset= YLBUtil.offset(pageno,InvestPagesize);
        List<InvestInfo> list=bidInfoMapper.selectInvestInfo(id,offset,InvestPagesize);
        System.out.println(list.toString());
        return list;
    }

    @Override
    public List<MyInvestVo>  selectMyInvestByUid(Integer id, Integer pageNo, Integer pageSize) {
        if(YLBUtil.ifNullZero(id))
            return new ArrayList<MyInvestVo>();
        pageNo=YLBUtil.PageNO(pageNo);
        pageSize=YLBUtil.PageSize(pageSize);
        Integer offset=YLBUtil.offset(pageNo,pageSize);
        List<MyInvestVo> listVo=bidInfoMapper.selectMyInvestByUid(id,offset,pageSize);
        System.out.println(listVo.toString());
        return listVo;
    }

    @Override
    public Integer InvestRecords(Integer uid) {
        Integer count=0;
        if(uid!=null){
           count=  bidInfoMapper.selectInvestRecordByUid(uid);
        }
        return count;
    }

}
