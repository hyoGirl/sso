package com.sso.common.model;

import java.io.Serializable;

public class SSOTip implements Serializable{

    private static final long serialVersionUID = 1L;

    private Integer code;

    private String msg;


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

    public SSOTip() {
    }

    public SSOTip(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "SSOTip{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }

    public static SSOTip success(String msg){
        SSOTip tip=new SSOTip();
        tip.code=200;
        tip.msg=msg;
        return tip;
    }


    public static SSOTip error(String msg){
        SSOTip tip=new SSOTip();
        tip.code=500;
        tip.msg=msg;
        return tip;
    }

}
