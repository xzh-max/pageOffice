package com.pageOfficeServer.model;

import java.util.List;

public class TemplateParMarkDto {

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

    //下拉选项值
    private List<String> optionValues;

    //前端插入的书签名称
    private List<String> bookMarks;

    public String getMarkName() {
        return markName;
    }

    public void setMarkName(String markName) {
        this.markName = markName;
    }

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

    public List<String> getOptionValues() {
        return optionValues;
    }

    public void setOptionValues(List<String> optionValues) {
        this.optionValues = optionValues;
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

    public List<String> getBookMarks() {
        return bookMarks;
    }

    public void setBookMarks(List<String> bookMarks) {
        this.bookMarks = bookMarks;
    }
}
