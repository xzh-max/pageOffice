package com.pageOfficeServer.model;

import java.util.List;

public class ArticleContentModel {

    //条款内容id
    private int farticleContentId;

    //条款id，一个条款id对应多个条款内容id
    private int farticleId;

    //条款内容
    private String article;

    //变量id列表，一个条款内容id对应多个变量id
    private List<Integer> fentryId;

    private String Markname;

    public int getFarticleId() {
        return farticleId;
    }

    public void setFarticleId(int farticleId) {
        this.farticleId = farticleId;
    }

    public String getArticle() {
        return article;
    }

    public int getFarticleContentId() {
        return farticleContentId;
    }

    public void setFarticleContentId(int farticleContentId) {
        this.farticleContentId = farticleContentId;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public List<Integer> getFentryId() {
        return fentryId;
    }

    public void setFentryId(List<Integer> fentryId) {
        this.fentryId = fentryId;
    }

    public String getMarkname() {
        return Markname;
    }

    public void setMarkname(String markname) {
        Markname = markname;
    }
}
