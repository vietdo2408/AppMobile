package com.quocviet.appmobile.Adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quocviet.appmobile.Admin.ComicDetailAdminActivity;
import com.quocviet.appmobile.Model.ModelComic;
import com.quocviet.appmobile.activity.DetailComicActivity;
import com.quocviet.appmobile.databinding.RowComicHorizontalBinding;
import com.quocviet.appmobile.filters.FilterComicHorizontal;
import com.quocviet.appmobile.filters.FilterComicUsers;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ComicHorizontalAdapter extends RecyclerView.Adapter<ComicHorizontalAdapter.ComicHorizontalHolder> implements Filterable {
    private RowComicHorizontalBinding binding;
    private Context context;
    public ArrayList<ModelComic> comicArrayList,filterList;
    private FilterComicHorizontal filter;

    public ComicHorizontalAdapter(Context context, ArrayList<ModelComic> comicArrayList) {
        this.context = context;
        this.comicArrayList = comicArrayList;
        this.filterList = comicArrayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ComicHorizontalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowComicHorizontalBinding.inflate(LayoutInflater.from(context),parent,false);
        return new ComicHorizontalHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ComicHorizontalHolder holder, int position) {
        ModelComic modelComic = comicArrayList.get(position);
        String comicId = modelComic.getComicId();
        String nameComic = modelComic.getNameComic();
        String imageComic = modelComic.getImageComic();

        holder.nameComic.setText(nameComic);
        Picasso.get().load(imageComic).fit().into(holder.imageComic, new Callback() {
            @Override
            public void onSuccess() {
                holder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(Exception e) {

            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailComicActivity.class);
                intent.putExtra("comicId",comicId);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return comicArrayList.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new FilterComicHorizontal(filterList,this);
        }
        return filter;
    }


    public class ComicHorizontalHolder extends RecyclerView.ViewHolder {
        private ImageView imageComic;
        private TextView nameComic;
        private ProgressBar progressBar;
        public ComicHorizontalHolder(@NonNull View itemView) {
            super(itemView);
            imageComic = binding.imageComicIv;
            nameComic = binding.nameComicTv;
            progressBar = binding.progressBar;
        }
    }
}
