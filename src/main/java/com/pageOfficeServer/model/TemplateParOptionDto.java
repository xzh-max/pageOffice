package com.pageOfficeServer.model;

import java.util.List;

public class TemplateParOptionDto {

    //fentryId
    private int fentryId;

    //文本or下拉项
    private String paramType;

    //标签信息
    private List<String> markInfos;

    //单个标签
    private String markInfo;

    //下拉选项变量名称
    private String name;

    //下拉选项值
    private List<String> value;

    //是否已经插入文本：true 已经插入；false 没有插入
    private boolean isInText;

    public int getFentryId() {
        return fentryId;
    }

    public void setFentryId(int fentryId) {
        this.fentryId = fentryId;
    }

    public String getParamType() {
        return paramType;
    }

    public String getMarkInfo() {
        return markInfo;
    }

    public void setMarkInfo(String markInfo) {
        this.markInfo = markInfo;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }

    public boolean isInText() {
        return isInText;
    }

    public void setInText(boolean inText) {
        isInText = inText;
    }

    public List<String> getMarkInfos() {
        return markInfos;
    }

    public void setMarkInfos(List<String> markInfos) {
        this.markInfos = markInfos;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getValue() {
        return value;
    }

    public void setValue(List<String> value) {
        this.value = value;
    }
}
