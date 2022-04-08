package com.bjpowernode.Util;

public class YLBUtil {
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

    public static boolean ifNullZero(Integer id){
        boolean flag =false;
        if(id==null || id<0)
            flag=true;
        return flag;
    }
    public  static Integer offset (Integer pageNO,Integer pageSize)
    {
        return (pageNO-1)*pageSize;
    }

    public static boolean regex(String str){
        boolean flag=false;
        if(str!=null){
            flag=str.matches("^1[1-9]\\d{9}$");

        }
        return  flag;
    }
}
