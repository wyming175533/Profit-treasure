package com.bjpowernode.vo;

import com.bjpowernode.code.ResponseCode;

import static com.bjpowernode.code.ResponseCode.RES_SUCCESS;
import static com.bjpowernode.code.ResponseCode.UN_KNOWN;

public class Result<T> {
    private boolean result;
    private Integer code;
    private String msg;
    private T data;

    public Result() {

    }

    @Override
    public String toString() {
        return "Result{" +
                "result=" + result +
                ", code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public Result(boolean result, Integer code, String msg, T data) {
        this.result = result;
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    public static Result fail(){
            Result<String> result=new Result<>();
            result.setResult(false);
            result.setData("");
            result.setEnum(UN_KNOWN);//以枚举方式赋值    未知错误
            return result;
    }
    public static Result ok(){
        Result<String> result=new Result<>();
        result.setResult(true);
        result.setData("");
        result.setEnum(RES_SUCCESS);//以枚举方式赋值   成功
        return result;
    }
    public static  Result erro(ResponseCode resp){
        Result<String> result=new Result<>();
        result.setResult(false);
        result.setData("");
        result.setEnum(resp);
        return result;
    }
    public void  setEnum(ResponseCode resp){
                this.setCode(resp.getCode());
                this.setMsg(resp.getMsg());

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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
