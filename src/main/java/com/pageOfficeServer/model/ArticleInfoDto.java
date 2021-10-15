package com.pageOfficeServer.model;

import java.util.List;

public class ArticleInfoDto {

    private int farticleId;

    private String name;

    private String markName;

    private List<ArticleContentDto> articleContentDtoList;

    private List<String> bookMarks;

    public int getFarticleId() {
        return farticleId;
    }

    public void setFarticleId(int farticleId) {
        this.farticleId = farticleId;
    }

    public String getMarkName() {
        return markName;
    }

    public void setMarkName(String markName) {
        this.markName = markName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ArticleContentDto> getArticleContentDtoList() {
        return articleContentDtoList;
    }

    public void setArticleContentDtoList(List<ArticleContentDto> articleContentDtoList) {
        this.articleContentDtoList = articleContentDtoList;
    }

    public List<String> getBookMarks() {
        return bookMarks;
    }

    public void setBookMarks(List<String> bookMarks) {
        this.bookMarks = bookMarks;
    }
}
