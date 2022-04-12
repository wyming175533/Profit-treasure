package com.bjpowernode.code;

/**
 * 枚举定义返回结果，包括成功，未知错误，手机号格式错误
 */
public enum ResponseCode {

    RES_SUCCESS(101,"成功"),
    UN_KNOWN(201,"未知错误——————"),
    PHONE_ERR(202,"手机号格式错误"),
    PHONE_DIFF_ERR(204,"注册手机号和认证手机号不一致"),
    PARAM_EMPTY(203,"参数不能为空"),
    User_EXISTS(304,"用户已经存在"),
    PASSWORD_LENGTH_ERR(405,"参数长度有误"),
    AuthCode_ERR(506,"验证码不正确"),
    IDCARD_REALNAME_DIFF(607,"用户身份信息不匹配")

    ;



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
