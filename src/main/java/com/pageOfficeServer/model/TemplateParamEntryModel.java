package com.pageOfficeServer.model;

public class TemplateParamEntryModel {


    private int fentryId;

    //TemplateParamModel fid
    private String fid;

    //变量名称
    private String name;

    //变量名称集合
    private String[] names;

    //变量字段类型：如数字、字符串、日期
    private String type;

    //变量长度
    private int length;

    private String syskey;

    //变量类型：1，系统变量；2，自定义变量；3，下拉选项
    private String paramType;

    public String[] getNames() {
        return names;
    }

    public void setNames(String[] names) {
        this.names = names;
    }

    public int getFentryId() {
        return fentryId;
    }

    public void setFentryId(int fentryId) {
        this.fentryId = fentryId;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getParamType() {
        return paramType;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }

    public String getSyskey() {
        return syskey;
    }

    public void setSyskey(String syskey) {
        this.syskey = syskey;
    }
}
