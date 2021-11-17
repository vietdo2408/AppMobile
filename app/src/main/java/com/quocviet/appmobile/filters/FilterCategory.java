package com.quocviet.appmobile.filters;

import android.text.TextUtils;
import android.widget.Filter;

import com.quocviet.appmobile.Adapter.CategoryAdapter;
import com.quocviet.appmobile.Model.ModelCategory;

import java.util.ArrayList;

public class FilterCategory extends Filter {

    private ArrayList<ModelCategory> filterList;
    private CategoryAdapter categoryAdapter;

    public FilterCategory(ArrayList<ModelCategory> filterList, CategoryAdapter categoryAdapter) {
        this.filterList = filterList;
        this.categoryAdapter = categoryAdapter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {

        FilterResults results = new FilterResults();
        if ( constraint != null && TextUtils.isEmpty(constraint)) {
            constraint = constraint.toString().toUpperCase();
            ArrayList<ModelCategory> filteredModels = new ArrayList<>();

            for (int i=0;i<filteredModels.size();i++) {
                if(filterList.get(i).getCategory().toUpperCase().contains(constraint)){
                    //add to filtered list
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
        categoryAdapter.categoryArrayList = (ArrayList<ModelCategory>)filterResults.values;
        categoryAdapter.notifyDataSetChanged();
    }
}
