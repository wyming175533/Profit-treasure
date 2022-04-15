package com.bjpowernode.Util;

import java.math.BigDecimal;

public class YLBUtil {

    /** 金额比较
     * @param big1 用户剩余金额
     * @param big2 投资金额
     * @return true 用户金额大于投资金额，可投资
     */
    public static boolean CompareBigMoney(BigDecimal big1,BigDecimal big2){
        boolean res=false;
        if(big1!=null && big2!=null){

            return  big1.compareTo(big2)>=0;
        }else {
            throw  new RuntimeException("BigDecimal数据为空！");
        }

    }




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

    public static boolean regexphone(String str){
        boolean flag=false;
        if(str!=null){
            flag=str.matches("^1[1-9]\\d{9}$");

        }
        return  flag;
    }
    public static boolean regexIdCard(String str){
        boolean flag=false;
        if(str!=null){
            flag=str.matches("(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)");

        }
        return  flag;
    }
}
