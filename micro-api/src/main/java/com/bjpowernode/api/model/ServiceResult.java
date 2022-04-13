package com.bjpowernode.api.model;

import java.io.Serializable;

public class ServiceResult implements Serializable {
    private static final long serialVersionUID = -6368945218914495532L;
    private  boolean result;
    private Integer code;
    private String msg;
    private Object data;

    @Override
    public String toString() {
        return "ServiceResult{" +
                "result=" + result +
                ", code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public ServiceResult() {
    }

    public ServiceResult(boolean result) {
        this.result = result;
    }

    public ServiceResult(boolean result, Integer code, String msg, Object data) {
        this.result = result;
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
