package com.bjpowernode.microdataservice.service;

import com.bjpowernode.Consts.YLBConsts;
import com.bjpowernode.Consts.YLBKEY;
import com.bjpowernode.Util.YLBUtil;
import com.bjpowernode.api.model.ServiceResult;
import com.bjpowernode.api.po.RechargeRecord;


import com.bjpowernode.api.service.RechargeService;
import com.bjpowernode.microdataservice.mapper.FinanceAccountMapper;
import com.bjpowernode.microdataservice.mapper.RechargeRecordMapper;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@DubboService(interfaceClass = RechargeService.class,version = "1.0")
public class RechargeServiceImpl implements RechargeService {

    @Resource
    private RechargeRecordMapper rechargeRecordMapper;
    @Resource
    private FinanceAccountMapper accountMapper;
    @Override
    public List<RechargeRecord> queryByUserId(Integer userId,
                                              Integer pageNo,
                                              Integer pageSize) {
        List<RechargeRecord> rechargeList = new ArrayList<>();
        if( userId != null && userId > 0 ){
            pageNo = YLBUtil.PageNO(pageNo);
            pageSize = YLBUtil.PageSize(pageSize);
            rechargeList = rechargeRecordMapper.selectByUserId(userId, YLBUtil.offset(pageNo,pageSize),pageSize);
        }
        return rechargeList;
    }

    @Override
    public Integer queryCountByUserId(Integer userId) {
        Integer countNums = 0;
        if( userId != null && userId > 0 ){
            countNums = rechargeRecordMapper.selectCountRechargeNumByUserId(userId);
        }
        return countNums;
    }

    @Override
    public boolean insertRechargeReord(RechargeRecord rechargeRecord) {
        if(rechargeRecord!=null) {
            int rows = rechargeRecordMapper.insertSelective(rechargeRecord);
            if (rows > 0)
                return true;
        }
        return false;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ServiceResult handlerKqNotify(String orderId, String payResult, String payAmount) {
    int rows=0;
    ServiceResult result=new ServiceResult(false);
    //1????????????,??????????????????
    RechargeRecord recharge=rechargeRecordMapper.selectByRechargeNo(orderId);
    if(recharge!=null){
        //??????????????????,??????????????????????????????????????????????????????0????????????
        if(recharge.getRechargeStatus()!= YLBKEY.RECHARGE_STATUS_RECHARGED){
            //????????????????????????
            String fen=recharge.getRechargeMoney().multiply(new BigDecimal(100)).
                    stripTrailingZeros().toPlainString();//?????????????????????????????????0
            if(fen.equals(payAmount)){
                //4??????????????????????????????
                if("10".equals(payResult)){
                    //5????????????????????????????????????????????????
                rows=accountMapper.updateAccountMoneyInc(recharge.getUid(),recharge.getRechargeMoney());
                if(rows<1){
                    throw new RuntimeException("???????????????????????????????????????");
                }
                //6????????????????????????
                    rows=rechargeRecordMapper.updateRecharge(recharge.getId(),YLBKEY.RECHARGE_STATUS_RECHARGED);

                    if(rows<1){
                        throw new RuntimeException("?????????????????????????????????????????????");
                    }

                    result.setResult(true);
                    result.setMsg("??????");
                    result.setCode(30000);

                }else{
                    rows=rechargeRecordMapper.updateRecharge(recharge.getId(),YLBKEY.RECHARGE_STATUS_RECHARGEERR);
                    if( rows < 1){
                        throw new RuntimeException("??????????????????????????????????????????2??????");
                    }
                }

            }
        }
    }

        return result;
    }

    @Override
    public void modifyRechargeStatus(String orderId, Integer rechargeStatusRechargeerr) {
        int rows=rechargeRecordMapper.updateRechargeByOrderId(orderId,rechargeStatusRechargeerr);
        if(rows<0){
            throw  new RuntimeException("????????????????????????");
        }
    }

    @Override
    public RechargeRecord queryByRechargeNo(String rechargeNo) {
        RechargeRecord re= rechargeRecordMapper.selectByRechargeNo(rechargeNo);
        return re;

    }


}
