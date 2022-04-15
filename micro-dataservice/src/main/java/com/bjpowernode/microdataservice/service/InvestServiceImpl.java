package com.bjpowernode.microdataservice.service;

import com.bjpowernode.Consts.YLBConsts;
import com.bjpowernode.Util.YLBUtil;
import com.bjpowernode.Util.RedisOpreation;
import com.bjpowernode.Consts.YLBKEY;
import com.bjpowernode.api.model.InvestInfo;
import com.bjpowernode.api.model.MyInvestVo;
import com.bjpowernode.api.model.ServiceResult;
import com.bjpowernode.api.po.BidInfo;
import com.bjpowernode.api.po.FinanceAccount;
import com.bjpowernode.api.po.Product;
import com.bjpowernode.api.service.InvestService;
import com.bjpowernode.microdataservice.mapper.BidInfoMapper;
import com.bjpowernode.microdataservice.mapper.FinanceAccountMapper;
import com.bjpowernode.microdataservice.mapper.IncomeRecordMapper;
import com.bjpowernode.microdataservice.mapper.ProductMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@DubboService(interfaceClass = InvestService.class, version = "1.0")
public class InvestServiceImpl  implements InvestService {
    @Resource
    private IncomeRecordMapper incomeRecordMapper;
    @Resource
    private BidInfoMapper bidInfoMapper;
    @Resource
    private RedisOpreation redisOpreation;
    @Resource
    private FinanceAccountMapper accountMapper;
    @Resource
    private ProductMapper productMapper;
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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ServiceResult invest(Integer uid, Integer productId, BigDecimal bidMoney) {
        ServiceResult result=new ServiceResult(false);
        if(productId!=null && productId>0 && bidMoney!=null && (bidMoney.intValue()%100==0)&&uid!=null){
            FinanceAccount userAccount=accountMapper.selectAccountByUIdForUpDate(uid);
            if(userAccount!=null){
                if(YLBUtil.CompareBigMoney(userAccount.getAvailableMoney(),bidMoney)){
                    //根据产品id获取产品
                    Product product=productMapper.selectByPrimaryKey(productId);
                    if(product!=null && product.getProductStatus()==0){
                        //产品存在且未满表
                        if(YLBUtil.CompareBigMoney(bidMoney,product.getBidMinLimit()) &&
                                YLBUtil.CompareBigMoney(product.getBidMaxLimit(),bidMoney) &&
                                YLBUtil.CompareBigMoney(product.getLeftProductMoney(),bidMoney)){
                            //满足投资条件  资操作,投资操作中出现错误应该抛出异常，使得数据库进行回滚操作
                            //更新用户金额信息
                            int row=0;
                            row=accountMapper.updateAccountMoney(uid,bidMoney);
                            if(row<1){
                                throw  new RuntimeException("更新账户金额失败");
                            }
                            //更新产品信息
                            row=0;
                            row=productMapper.updateProductMoney(productId,bidMoney);
                            if(row<1){
                                throw  new RuntimeException("更新产品金额失败");
                            }
                            //创建交易记录
                            BidInfo bidInfo=new BidInfo();
                            bidInfo.setUid(uid);
                            bidInfo.setBidMoney(bidMoney);
                            bidInfo.setBidStatus(1);
                            bidInfo.setBidTime(new Date());
                            bidInfo.setProductId(productId);
                            bidInfoMapper.insert(bidInfo);
                            //更新产品状态
                            if(product.getLeftProductMoney().compareTo(bidMoney)==0){
                                //剩余可投金额等于投资金额
                                Product upProduct=new Product();
                                upProduct.setId(product.getId());
                                upProduct.setProductStatus(1);
                                upProduct.setProductFullTime(new Date());
                            }
                            result.setResult(true);

                        }else{
                            result.setCode(YLBConsts.BID_MONEY_ERR);
                            result.setMsg("投资金额异常");
                        }

                    }


                }else{
                    //剩余金额不足
                    result.setCode(YLBConsts.USER_ACCOUNT_NOTMONEY);
                    result.setMsg("用户剩余金额不足");
                }
            }else{
                //没有该用户
                result.setCode(YLBConsts.USER_ACCOUNT_NOTHAVE);
                result.setMsg("没有查询到该用户");
            }
        }else{
            //参数列表有误
            result.setCode(YLBConsts.PARAM_ERR);
            result.setMsg("给定参数有误");

        }




        return result;
    }

}
