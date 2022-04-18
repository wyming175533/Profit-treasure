package com.bjpowernode.code;

/**
 * 枚举定义返回结果，包括成功，未知错误，手机号格式错误
 */
public enum ResponseCode {
    UN_KNOWN(000,"未知错误——————"),

    RES_SUCCESS(111,"成功"),

    PHONE_ERR(202,"手机号格式错误"),
    REAL_NAME_ERR(203,"姓名格式有误"),
    IDCARD_REALNAME_ERR(204,"身份证号格式错误"),

    PHONE_DIFF_ERR(303,"注册手机号和认证手机号不一致"),
    AuthCode_ERR(304,"验证码不正确"),
    IDCARD_REALNAME_DIFF(305,"用户信息不匹配"),

    User_EXISTS(404,"用户已经存在"),

    PARAM_EMPTY(505,"参数不能为空"),
    PASSWORD_LENGTH_ERR(506,"参数长度有误"),

    RNE_TIMES_ERR(601,"超出当日认证次数"),

    USER_LOGIN_ERR(701,"请先登录"),
    User_LOGIN_REQUEST(20000,"请登录");



    private Integer code;
    private String msg;

    ResponseCode() {
    }


    ResponseCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
