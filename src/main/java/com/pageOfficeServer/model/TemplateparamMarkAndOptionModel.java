package com.pageOfficeServer.model;

import java.util.List;

public class TemplateparamMarkAndOptionModel {
    //fentryId
    private int fentryId;

    //文本or 下拉选项
    private String paramType;

    //变量名称
    private String name;

    //变量类型
    private String type;

    //长度
    private int length;

    //书签信息
    private String markName;

    //书签名称
    private List<String> markNames;

    //标签信息
    private List<String> markInfos;

    //单个标签
    private String markInfo;

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

    public void setParamType(String paramType) {
        this.paramType = paramType;
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

    public String getMarkName() {
        return markName;
    }

    public void setMarkName(String markName) {
        this.markName = markName;
    }

    public List<String> getMarkNames() {
        return markNames;
    }

    public void setMarkNames(List<String> markNames) {
        this.markNames = markNames;
    }

    public List<String> getMarkInfos() {
        return markInfos;
    }

    public void setMarkInfos(List<String> markInfos) {
        this.markInfos = markInfos;
    }

    public String getMarkInfo() {
        return markInfo;
    }

    public void setMarkInfo(String markInfo) {
        this.markInfo = markInfo;
    }

    public List<String> getValue() {
        return value;
    }

    public void setValue(List<String> value) {
        this.value = value;
    }

    public boolean isInText() {
        return isInText;
    }

    public void setInText(boolean inText) {
        isInText = inText;
    }
}
