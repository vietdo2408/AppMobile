package com.quocviet.appmobile.filters;

import android.text.TextUtils;
import android.widget.Filter;

import com.quocviet.appmobile.Adapter.AllComicAdapter;
import com.quocviet.appmobile.Adapter.ListComicAdminAdapter;
import com.quocviet.appmobile.Model.ModelComic;

import java.util.ArrayList;

public class FilterComicUsers extends Filter {

    private ArrayList<ModelComic> filterList;
    private AllComicAdapter allComicAdapter;

    public FilterComicUsers(ArrayList<ModelComic> filterList, AllComicAdapter allComicAdapter) {
        this.filterList = filterList;
        this.allComicAdapter = allComicAdapter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {

        FilterResults results = new FilterResults();
        if ( constraint != null && TextUtils.isEmpty(constraint)) {
            constraint = constraint.toString().toUpperCase();
            ArrayList<ModelComic> filteredModels = new ArrayList<>();

            for (int i=0;i<filteredModels.size();i++) {
                if(filterList.get(i).getNameComic().toUpperCase().contains(constraint)){
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
        allComicAdapter.comicArrayList = (ArrayList<ModelComic>)filterResults.values;
        allComicAdapter.notifyDataSetChanged();
    }
}
