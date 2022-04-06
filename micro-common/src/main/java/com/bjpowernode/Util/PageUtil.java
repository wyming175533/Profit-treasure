package com.bjpowernode.Util;

public class PageUtil {
    public static Integer PageNO(Integer PageNO){

        if(PageNO==null||PageNO<1){
            PageNO=1;
        }

        return PageNO;
    }
    public static Integer PageSize(Integer PageSize){

        if(PageSize==null||PageSize<1||PageSize>100){
            PageSize=1;
        }

        return PageSize;
    }
}
