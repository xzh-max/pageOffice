package com.pageOfficeServer.model;

public class TemplateParamMarkInfoModel {

    //TemplateParamEntryModel fentryId
    private int fentryId;

    private int fid;

    //标签信息，如PO_dept
    private String fmark;

    //标签值，如 回收价格
    private String fvalue;

    public int getFentryId() {
        return fentryId;
    }

    public void setFentryId(int fentryId) {
        this.fentryId = fentryId;
    }

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public String getFmark() {
        return fmark;
    }

    public void setFmark(String fmark) {
        this.fmark = fmark;
    }

    public String getFvalue() {
        return fvalue;
    }

    public void setFvalue(String fvalue) {
        this.fvalue = fvalue;
    }
}
