package com.pageOfficeServer.model;

/**
 * 模板变量列表
 */
public class TemplateParamModel {

    private int fid;

    private String templateId;

    private String templateName;

    //合同业务类型 ：如养殖；药物疫苗
    private String bizType;

    //合同类型：如 养鸡 养猪
    private String crtType;

    //1：合同生成模板；2合同解除模板；3合同变更模板
    private String templateType;

    //1：新物价；2，旧物价
    private String businessType;

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getCrtType() {
        return crtType;
    }

    public void setCrtType(String crtType) {
        this.crtType = crtType;
    }

    public String getTemplateType() {
        return templateType;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }
}
