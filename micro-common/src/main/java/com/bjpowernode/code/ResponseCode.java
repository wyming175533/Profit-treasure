package com.bjpowernode.code;

/**
 * 枚举定义返回结果，包括成功，未知错误，手机号格式错误
 */
public enum ResponseCode {

    RES_SUCCESS(101,"成功"),
    UN_KNOWN(201,"未知错误——————"),
    PHONE_ERR(202,"手机号格式错误"),
    PARAM_EMPTY(203,"手机号不能为空"),
    User_EXISTS(304,"用户已经存在")
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
