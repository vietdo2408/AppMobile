package com.quocviet.appmobile.Model;

import java.util.ArrayList;

public class ModelCategoryHomePage {
    private String nameCategoryHomePage;
    private ArrayList<ModelComic> comicArrayList;

    public ModelCategoryHomePage(String nameCategoryHomePage, ArrayList<ModelComic> comicArrayList) {
        this.nameCategoryHomePage = nameCategoryHomePage;
        this.comicArrayList = comicArrayList;
    }

    public String getNameCategoryHomePage() {
        return nameCategoryHomePage;
    }

    public void setNameCategoryHomePage(String nameCategoryHomePage) {
        this.nameCategoryHomePage = nameCategoryHomePage;
    }

    public ArrayList<ModelComic> getComicArrayList() {
        return comicArrayList;
    }

    public void setComicArrayList(ArrayList<ModelComic> comicArrayList) {
        this.comicArrayList = comicArrayList;
    }
}
