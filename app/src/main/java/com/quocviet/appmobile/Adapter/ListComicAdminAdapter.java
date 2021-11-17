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
import com.quocviet.appmobile.databinding.RowComicBinding;
import com.quocviet.appmobile.filters.FilterComic;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ListComicAdminAdapter extends RecyclerView.Adapter<ListComicAdminAdapter.HolderListComicAdmin> implements Filterable {
    private Context context;

    public ArrayList<ModelComic> comicArrayList, filterList;
    private RowComicBinding binding;
    private FilterComic filter;

    public ListComicAdminAdapter(Context context, ArrayList<ModelComic> comicArrayList) {
        this.context = context;
        this.comicArrayList = comicArrayList;
        this.filterList = comicArrayList;
    }

    @NonNull
    @Override
    public HolderListComicAdmin onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowComicBinding.inflate(LayoutInflater.from(context),parent,false);
        return new HolderListComicAdmin(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderListComicAdmin holder, int position) {
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
                Intent intent = new Intent(context, ComicDetailAdminActivity.class);
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
            filter = new FilterComic(filterList, this);
        }
        return filter;
    }

    class HolderListComicAdmin extends RecyclerView.ViewHolder {
        ImageView imageComic;
        ProgressBar progressBar;
        TextView nameComic;
        public HolderListComicAdmin(@NonNull View itemView) {
            super(itemView);
            imageComic = binding.imageComicIv;
            progressBar = binding.progressBar;
            nameComic = binding.nameComicTv;
        }
    }
}
