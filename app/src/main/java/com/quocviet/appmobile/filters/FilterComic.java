package com.quocviet.appmobile.filters;

import android.text.TextUtils;
import android.widget.Filter;

import com.quocviet.appmobile.Adapter.CategoryAdapter;
import com.quocviet.appmobile.Adapter.ListComicAdminAdapter;
import com.quocviet.appmobile.Model.ModelCategory;
import com.quocviet.appmobile.Model.ModelComic;

import java.util.ArrayList;

public class FilterComic extends Filter {

    private ArrayList<ModelComic> filterList;
    private ListComicAdminAdapter listComicAdminAdapter;

    public FilterComic(ArrayList<ModelComic> filterList, ListComicAdminAdapter listComicAdminAdapter) {
        this.filterList = filterList;
        this.listComicAdminAdapter = listComicAdminAdapter;
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
        listComicAdminAdapter.comicArrayList = (ArrayList<ModelComic>)filterResults.values;
        listComicAdminAdapter.notifyDataSetChanged();
    }
}
