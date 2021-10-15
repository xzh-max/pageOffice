package com.pageOfficeServer.model;

public class TemplateArticleModel {

    //条款id
    private int farticleId;

    //模板fid
    private int fid;

    //条款名称
    private String name;

    public int getFarticleId() {
        return farticleId;
    }

    public void setFarticleId(int farticleId) {
        this.farticleId = farticleId;
    }

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
