package com.bjpowernode.microtimedtask;

import com.bjpowernode.api.service.IncomeRecordService;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component("taskManager")
public class TaskManager {

    @DubboReference(interfaceClass = IncomeRecordService.class,version = "1.0")
    private IncomeRecordService incomeRecordService;

//    @Scheduled(cron = "0/10 * * * * ?")
//    public void testCro(){
//        System.out.println(new Date());
//    }
  //  @Scheduled(cron = "0 0 2 * * ?")
    public void invokeIncome(){
        incomeRecordService.generateIncomePlan();
    }

}
