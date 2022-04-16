package com.bjpowernode.Consts;

public class YLBKEY {
    //用户注册人数
    public static final  String USR_REGISTER_COUNT ="USR:REGISTER:COUNT";
    //平台投资总钱数
    public static final String INVEST_PRODUCT_ALLMONEY = "INVEST:PRODUCT:ALLMONEY";
    //总利率
    public static final String PRODUCT_RATE = "PRODUCT:RATE";
    //短信验证码
    public static final String SMS_REGISTER_KEY = "SMS:REGISTER:AUTHCODE:";
    //用户认证次数
    public static final String REAL_NAME_TIMES = "RNT:";
    //session中当前登录用户
    public static final String USER_SESSION = "loginUser";
    //投资排行榜
    public static final String INVEST_TOP_ORDER ="INVEST:ORDER:TOP";
    //订单号后的自增，利用时间戳到毫秒+redis单线程
    public static final String RECHARGE_NO = "RECHARGE:KQ:SEQ";
    //订单记录，订单号和时间
    public static final String RECHARGE_NO_LIST ="RECHARGE:NO:LIST";
    //充值状态充值中
    public static final Integer RECHARGE_STATUS_RECHARGEING =0 ;
    //充值成功
    public static final Integer RECHARGE_STATUS_RECHARGED =1 ;
    //充值失败
    public static final Integer RECHARGE_STATUS_RECHARGEERR=2 ;
    //充值类型：充值
    public static final Integer RECHARGE_YES =1 ;
    //充值类型：提现
    public static final Integer RECHARGE_REMOVE =2 ;
    //redis中产品类型key
    public static String DIC_PRODUCT_KEY="DIC:PRODUCT:TYPE";
}
