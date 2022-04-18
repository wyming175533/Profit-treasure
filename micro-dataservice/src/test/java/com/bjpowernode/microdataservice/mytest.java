package com.bjpowernode.microdataservice;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class mytest {
    @Test
    public void test(){
        System.out.println(new Date().getTime());
    }

    @Test
    public void test2(){

        String key=DateFormatUtils.format( DateUtils.addDays(new Date(),-1),"yyyyMMdd");
        System.out.println(key);
    }

}
