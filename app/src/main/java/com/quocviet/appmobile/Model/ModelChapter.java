package com.quocviet.appmobile.Model;

public class ModelChapter {
    String nameChapter;
    String urlChapter;

    public ModelChapter() {
    }

    public ModelChapter(String nameChapter, String urlChapter) {
        this.nameChapter = nameChapter;
        this.urlChapter = urlChapter;
    }

    public String getNameChapter() {
        return nameChapter;
    }

    public void setNameChapter(String nameChapter) {
        this.nameChapter = nameChapter;
    }

    public String getUrlChapter() {
        return urlChapter;
    }

    public void setUrlChapter(String urlChapter) {
        this.urlChapter = urlChapter;
    }
}
