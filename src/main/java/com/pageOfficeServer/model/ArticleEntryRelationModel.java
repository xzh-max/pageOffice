package com.pageOfficeServer.model;

public class ArticleEntryRelationModel {

    /**
     * 条款内容id
     */
    private int farticleContentId;

    /**
     * 变量id
     */
    private int fentryId;

    public int getFarticleContentId() {
        return farticleContentId;
    }

    public void setFarticleContentId(int farticleContentId) {
        this.farticleContentId = farticleContentId;
    }

    public int getFentryId() {
        return fentryId;
    }

    public void setFentryId(int fentryId) {
        this.fentryId = fentryId;
    }
}
