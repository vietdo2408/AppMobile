package com.quocviet.appmobile.filters;

import android.text.TextUtils;
import android.widget.Filter;

import com.quocviet.appmobile.Adapter.AllComicAdapter;
import com.quocviet.appmobile.Adapter.ListChapterAdapter;
import com.quocviet.appmobile.Model.ModelChapter;
import com.quocviet.appmobile.Model.ModelComic;

import java.util.ArrayList;

public class FilterChapters extends Filter {

    private ArrayList<ModelChapter> filterList;
    private ListChapterAdapter listChapterAdapter;

    public FilterChapters(ArrayList<ModelChapter> filterList, ListChapterAdapter listChapterAdapter) {
        this.filterList = filterList;
        this.listChapterAdapter = listChapterAdapter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {

        FilterResults results = new FilterResults();
        if ( constraint != null && TextUtils.isEmpty(constraint)) {
            constraint = constraint.toString().toUpperCase();
            ArrayList<ModelChapter> filteredModels = new ArrayList<>();

            for (int i=0;i<filteredModels.size();i++) {
                if(filterList.get(i).getNameChapter().toUpperCase().contains(constraint)){
                    filteredModels.add(filterList.get(i));
                }
            }
            results.count = filteredModels.size();
            results.values = filteredModels;
        }
        else {
            results.count = filterList.size();
            results.values = filterList;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        listChapterAdapter.chapterArrayList = (ArrayList<ModelChapter>)filterResults.values;
        listChapterAdapter.notifyDataSetChanged();
    }
}
