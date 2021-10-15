package com.pageOfficeServer.model;

public class TemplateParamOptionModel {

    private int fid;

    //TemplateParamEntryModel fentryId
    private int fentryId;

    //选项值
    private String foptionValue;

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public int getFentryId() {
        return fentryId;
    }

    public void setFentryId(int fentryId) {
        this.fentryId = fentryId;
    }

    public String getFoptionValue() {
        return foptionValue;
    }

    public void setFoptionValue(String foptionValue) {
        this.foptionValue = foptionValue;
    }

}
