package com.hhd.breath.app.model;

/**
 * Created by familylove on 2016/5/20.
 */
public class BreathSuccessUser {

    private String code ;
    private BreathDataUser data ;
    private String msg ;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BreathDataUser getData() {
        return data;
    }

    public void setData(BreathDataUser data) {
        this.data = data;
    }
}
