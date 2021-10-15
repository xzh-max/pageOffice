package com.pageOfficeServer.model;

public class ParamBookMarkModel {

    //id
    private int id;
    //模板id
    private int fid;
    //变量id
    private int fparamId;
    //变量类型1：自定义变量；2：自定义条款
    private String type;
    //书签名称
    private String bookMark;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public int getFparamId() {
        return fparamId;
    }

    public void setFparamId(int fparamId) {
        this.fparamId = fparamId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBookMark() {
        return bookMark;
    }

    public void setBookMark(String bookMark) {
        this.bookMark = bookMark;
    }
}
