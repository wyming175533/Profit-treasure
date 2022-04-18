package com.bjpowernode.microtimedtask;

import com.bjpowernode.Util.HttpClientUtils;
import com.bjpowernode.api.service.IncomeRecordService;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component("taskManager")
public class TaskManager {

    @DubboReference(interfaceClass = IncomeRecordService.class,version = "1.0")
    private IncomeRecordService incomeRecordService;

    @Value("${micro.pay.url}")
    private String url;

    @Value("${micro.real.url}")
    private String url2;

    /**
     * 调用数据服务，更新收益计划
     */
//    @Scheduled(cron = "0/10 * * * * ?")
//    public void testCro(){
//        System.out.println(new Date());
//    }
  @Scheduled(cron = "0 0 2 * * ?")
    public void invokeIncomePlan(){
        incomeRecordService.generateIncomePlan();
    }

    //  @Scheduled(cron = "0 0 3 * * ?")

    /**
     * 调用数据服务，更新用户资金
     */
    @Scheduled(cron = "0 5 2 * * ?")

    public void invokeIncomeBack(){
        incomeRecordService.generateIncomeBack();
    }


    @Scheduled(cron = "0 0/20 * * * ?")
    public void KQquery(){
        try {
            HttpClientUtils.doGet(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void RemoveRealName(){
        System.out.println("定时任务，开始清空实名验证次数");
        try {
            HttpClientUtils.doGet(url2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
