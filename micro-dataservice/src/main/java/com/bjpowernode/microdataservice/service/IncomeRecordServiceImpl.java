package com.bjpowernode.microdataservice.service;

import com.bjpowernode.Consts.YLBConsts;
import com.bjpowernode.Consts.YLBKEY;
import com.bjpowernode.Util.YLBUtil;
import com.bjpowernode.api.model.IncomeVo;
import com.bjpowernode.api.po.BidInfo;
import com.bjpowernode.api.po.IncomeRecord;
import com.bjpowernode.api.po.Product;
import com.bjpowernode.api.service.IncomeRecordService;
import com.bjpowernode.microdataservice.mapper.BidInfoMapper;
import com.bjpowernode.microdataservice.mapper.FinanceAccountMapper;
import com.bjpowernode.microdataservice.mapper.IncomeRecordMapper;
import com.bjpowernode.microdataservice.mapper.ProductMapper;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

@DubboService(interfaceClass = IncomeRecordService.class,version = "1.0")
public class IncomeRecordServiceImpl implements IncomeRecordService{
    @Resource
    private IncomeRecordMapper incomeRecordMapper;

    @Resource
    private ProductMapper productMapper;

    @Resource
    private BidInfoMapper bidInfoMapper;
    @Resource
    private FinanceAccountMapper accountMapper;
    /**分页查询用户收益信息
     * @param uid
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public List<IncomeVo> selectIncomeInfo(Integer uid, Integer pageNo, Integer pageSize) {
        pageNo= YLBUtil.PageNO(pageNo);
        pageSize=YLBUtil.PageSize(pageSize);
        Integer offset=YLBUtil.offset(pageNo,pageSize);

        List<IncomeVo> list=incomeRecordMapper.selectIncomeInfo(uid,offset,pageSize);
        return list;
    }

    /**查询用户收益总记录数
     * @param uid
     * @return
     */
    @Override
    public Integer IncomeRecords(Integer uid) {
        Integer count=incomeRecordMapper.selectIncomeRecordsByUid(uid);
        if(count==null)
            count=0;
        return count;
    }

    /**
     * 生成收益计划，修改产品状态，执行中增加同步锁，避免引起多线程对该方法进行多次请求
     */
    @Transactional
    @Override
    public  synchronized void  generateIncomePlan() {
        //查询符合条件的产品/满表
        List<Product> productList=productMapper.selectByStatus(YLBKEY.PRODUCT_STATUS_FULL);
        for(Product product:productList){
            //根据产品查所有投资记录
            BigDecimal dateRate=null;//日利率
            BigDecimal incomeMoney=null;
            Date incomeDate=null;
            List<BidInfo> bidInfoList=bidInfoMapper.selectByProductId(product.getId());
            for(BidInfo bidInfo:bidInfoList){

                dateRate=product.getRate().divide(new BigDecimal("100"),
                10, RoundingMode.HALF_UP).divide(new BigDecimal("365"),10,RoundingMode.HALF_UP);

                //判断产品周期（通过类型）
                if(product.getProductType()== YLBConsts.PRODUCT_TYPE_XINSHOUBAO)//新手宝
                {
                    incomeDate= DateUtils.addDays(product.getProductFullTime(),1+product.getCycle());
                    incomeMoney=bidInfo.getBidMoney().multiply(dateRate).multiply(new BigDecimal(product.getCycle()));
                    //周期为天
                }else{
                    //周期为月
                    incomeDate=DateUtils.addDays(DateUtils.addDays(product.getProductFullTime(),1),product.getCycle()*30);
                    incomeMoney=bidInfo.getBidMoney().multiply(dateRate).multiply(new BigDecimal(product.getCycle()*30));

                }
                //把计算结果存放到收益表
                IncomeRecord incomeRecord=new IncomeRecord();
                incomeRecord.setBidId(bidInfo.getId());
                incomeRecord.setBidMoney(bidInfo.getBidMoney());
                incomeRecord.setIncomeDate(incomeDate);
                incomeRecord.setIncomeMoney(incomeMoney);
                incomeRecord.setIncomeStatus(YLBKEY.INCOME_STATUS_NORET);
                incomeRecord.setUid(bidInfo.getUid());
                incomeRecord.setProdId(product.getId());
                incomeRecordMapper.insert(incomeRecord);
            }
            //更新产品状态
        product.setProductStatus(YLBKEY.PRODUCT_STATUS_INCOMEING);
            int rows=productMapper.updateByPrimaryKeySelective(product);
            if(rows<1){
                throw  new RuntimeException("生成收益计划，更新产品状态失败");
            }
        }

    }



    @Transactional
    @Override
    public void generateIncomeBack() {

        //1.获取到期的产品数据。 查询income_record
        List<IncomeRecord> incomeRecords = incomeRecordMapper.selectExpireIncomeDate();

        int rows = 0;
        for(IncomeRecord ir : incomeRecords){
            //2.更新资金账户
            rows =accountMapper.updateAccountMoneyByIncomeBack(ir.getUid(),ir.getBidMoney(),ir.getIncomeMoney());
            if( rows < 1 ){
                throw new RuntimeException("收益返还，更新资金余额失败");
            }

            //3.修改income_record的status=1
            rows  = incomeRecordMapper.updateStatus(ir.getId(), YLBKEY.INCOME_STATUS_RET);
            if( rows  < 1 ){
                throw new RuntimeException("收益返还，更新收益表状态为1失败");
            }
        }

    }
}
