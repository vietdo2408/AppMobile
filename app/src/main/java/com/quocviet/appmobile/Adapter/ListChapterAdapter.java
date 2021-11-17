package com.quocviet.appmobile.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quocviet.appmobile.Model.ModelChapter;
import com.quocviet.appmobile.databinding.RowChapterBinding;
import com.quocviet.appmobile.filters.FilterChapters;
import com.quocviet.appmobile.filters.FilterComic;

import java.util.ArrayList;

public class ListChapterAdapter extends RecyclerView.Adapter<ListChapterAdapter.HolderListChapter> implements Filterable {
    private Context context;
    public ArrayList<ModelChapter> chapterArrayList,filterList;
    private RowChapterBinding binding;
    private FilterChapters filter;

    public ListChapterAdapter(Context context, ArrayList<ModelChapter> chapterArrayList) {
        this.context = context;
        this.chapterArrayList = chapterArrayList;
        this.filterList = chapterArrayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ListChapterAdapter.HolderListChapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowChapterBinding.inflate(LayoutInflater.from(context),parent,false);
        return new HolderListChapter(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ListChapterAdapter.HolderListChapter holder, int position) {
        ModelChapter model = chapterArrayList.get(position);
        String nameChapter = model.getNameChapter();
        String urlChapter = model.getUrlChapter();
        holder.nameChapter.setText(nameChapter);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return chapterArrayList.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new FilterChapters(filterList, this);
        }
        return filter;
    }

    public class HolderListChapter extends RecyclerView.ViewHolder {
        private TextView nameChapter;
        public HolderListChapter(@NonNull View itemView) {
            super(itemView);
            nameChapter = binding.nameChapterTv;
        }
    }
}
