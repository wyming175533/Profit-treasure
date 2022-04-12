package com.bjpowernode.vo;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.io.Serializable;

public class BusinessResult implements Serializable {

    private static final long serialVersionUID = -4859538247983803017L;
    private boolean result;
    private int code;
    private String msg;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public BusinessResult(boolean result, int code, String msg) {
        this.result = result;
        this.code = code;
        this.msg = msg;
    }

    public BusinessResult() {
    }
}
