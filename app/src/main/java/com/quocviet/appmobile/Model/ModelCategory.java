package com.quocviet.appmobile.Model;

public class ModelCategory {
    String categoryId,category,uid;
    long timestamp;

    public ModelCategory() {

    }

    public ModelCategory(String categoryId, String category, String uid, long timestamp) {
        this.categoryId = categoryId;
        this.category = category;
        this.uid = uid;
        this.timestamp = timestamp;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
