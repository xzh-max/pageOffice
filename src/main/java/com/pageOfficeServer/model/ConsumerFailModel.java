package com.pageOfficeServer.model;

public class ConsumerFailModel {
    private int id;

    private String mqexchange;

    private String mqtagkey;

    private String msg;

    private String exceptionInfo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMqexchange() {
        return mqexchange;
    }

    public void setMqexchange(String mqexchange) {
        this.mqexchange = mqexchange;
    }

    public String getMqtagkey() {
        return mqtagkey;
    }

    public void setMqtagkey(String mqtagkey) {
        this.mqtagkey = mqtagkey;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getExceptionInfo() {
        return exceptionInfo;
    }

    public void setExceptionInfo(String exceptionInfo) {
        this.exceptionInfo = exceptionInfo;
    }
}
