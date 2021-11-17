package com.quocviet.appmobile.Model;

import java.util.ArrayList;
import java.util.List;

public class ModelComic {
    String comicId, uid, nameComic, imageComic, descriptionComic;
    long timestamp, viewsCount, downloadsCount;
    ArrayList<String> categories;
    ArrayList<ModelChapter> chapters;

    public ModelComic() {
    }

    public ModelComic(String comicId, String uid, String nameComic, String imageComic, String descriptionComic, long timestamp, long viewsCount, long downloadsCount, ArrayList<String> categories, ArrayList<ModelChapter> chapters) {
        this.comicId = comicId;
        this.uid = uid;
        this.nameComic = nameComic;
        this.imageComic = imageComic;
        this.descriptionComic = descriptionComic;
        this.timestamp = timestamp;
        this.viewsCount = viewsCount;
        this.downloadsCount = downloadsCount;
        this.categories = categories;
        this.chapters = chapters;
    }

    public ArrayList<ModelChapter> getChapters() {
        return chapters;
    }

    public void setChapters(ArrayList<ModelChapter> chapters) {
        this.chapters = chapters;
    }

    public String getComicId() {
        return comicId;
    }

    public void setComicId(String comicId) {
        this.comicId = comicId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNameComic() {
        return nameComic;
    }

    public void setNameComic(String nameComic) {
        this.nameComic = nameComic;
    }

    public String getImageComic() {
        return imageComic;
    }

    public void setImageComic(String imageComic) {
        this.imageComic = imageComic;
    }

    public String getDescriptionComic() {
        return descriptionComic;
    }

    public void setDescriptionComic(String descriptionComic) {
        this.descriptionComic = descriptionComic;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getViewsCount() {
        return viewsCount;
    }

    public void setViewsCount(long viewsCount) {
        this.viewsCount = viewsCount;
    }

    public long getDownloadsCount() {
        return downloadsCount;
    }

    public void setDownloadsCount(long downloadsCount) {
        this.downloadsCount = downloadsCount;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }
}