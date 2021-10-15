package com.pageOfficeServer.model;

public class ArticleContentDto {

    private int farticleContentId;

    private String article;

    private String[] markNames;

    private int[] fentryIds;

    public int getFarticleContentId() {
        return farticleContentId;
    }

    public void setFarticleContentId(int farticleContentId) {
        this.farticleContentId = farticleContentId;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public int[] getFentryIds() {
        return fentryIds;
    }

    public void setFentryIds(int[] fentryIds) {
        this.fentryIds = fentryIds;
    }

    public String[] getMarkNames() {
        return markNames;
    }

    public void setMarkNames(String[] markNames) {
        this.markNames = markNames;
    }
}
