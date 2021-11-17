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
import com.quocviet.appmobile.activity.ReadComicActivity;
import com.quocviet.appmobile.databinding.RowChapterBinding;
import com.quocviet.appmobile.filters.FilterChapters;
import com.quocviet.appmobile.filters.FilterChaptersUser;

import java.util.ArrayList;

public class ListChapterUserAdapter extends RecyclerView.Adapter<ListChapterUserAdapter.HolderListChapterUser> implements Filterable {
    private Context context;
    public ArrayList<ModelChapter> chapters,filterList;
    private RowChapterBinding binding;
    private FilterChaptersUser filter;

    public ListChapterUserAdapter(Context context, ArrayList<ModelChapter> chapterArrayList) {
        this.context = context;
        this.chapters = chapterArrayList;
        this.filterList = chapterArrayList;
    }

    @NonNull
    @Override
    public ListChapterUserAdapter.HolderListChapterUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowChapterBinding.inflate(LayoutInflater.from(context),parent,false);
        return new HolderListChapterUser(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ListChapterUserAdapter.HolderListChapterUser holder, int position) {
        ModelChapter model = chapters.get(position);
        String nameChapter = model.getNameChapter();
        String urlChapter = model.getUrlChapter();
        holder.nameChapter.setText(nameChapter);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ReadComicActivity.class);
                intent.putExtra("urlChapter",""+urlChapter);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chapters.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new FilterChaptersUser(filterList, this);
        }
        return filter;
    }

    public class HolderListChapterUser extends RecyclerView.ViewHolder {
        private TextView nameChapter;
        public HolderListChapterUser(@NonNull View itemView) {
            super(itemView);
            nameChapter = binding.nameChapterTv;
        }
    }
}
