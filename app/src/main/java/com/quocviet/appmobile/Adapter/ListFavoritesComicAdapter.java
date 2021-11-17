package com.quocviet.appmobile.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.quocviet.appmobile.Admin.ComicDetailAdminActivity;
import com.quocviet.appmobile.Model.ModelComic;
import com.quocviet.appmobile.activity.DetailComicActivity;
import com.quocviet.appmobile.databinding.RowComicBinding;
import com.quocviet.appmobile.filters.FilterComic;
import com.quocviet.appmobile.filters.FilterComicFavorites;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ListFavoritesComicAdapter extends RecyclerView.Adapter<ListFavoritesComicAdapter.HolderListFavoritesComic> {
    private Context context;
    public ArrayList<ModelComic> comicArrayList,filterList;
    private RowComicBinding binding;
    private FilterComicFavorites filter;

    private static final String TAG = "FAV_BOOK_TAG";

    public ListFavoritesComicAdapter(Context context, ArrayList<ModelComic> comicArrayList) {
        this.context = context;
        this.comicArrayList = comicArrayList;
        this.filterList = comicArrayList;
    }

    @NonNull
    @Override
    public HolderListFavoritesComic onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowComicBinding.inflate(LayoutInflater.from(context),parent,false);
        return new HolderListFavoritesComic(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderListFavoritesComic holder, int position) {
        ModelComic modelComic = comicArrayList.get(position);
        loadComic(modelComic,holder);
    }

    @Override
    public int getItemCount() {
        return comicArrayList.size();
    }

    public class HolderListFavoritesComic extends RecyclerView.ViewHolder {
        ImageView imageComic;
        ProgressBar progressBar;
        TextView nameComic;
        public HolderListFavoritesComic(@NonNull View itemView) {
            super(itemView);
            imageComic = binding.imageComicIv;
            progressBar = binding.progressBar;
            nameComic = binding.nameComicTv;
        }
    }

    private void loadComic(ModelComic model, ListFavoritesComicAdapter.HolderListFavoritesComic holder) {
        String comicId = model.getComicId();
        Log.d(TAG, "loadBookDetails: Book Details pf Book Id: "+comicId);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Comic");
        ref.child(comicId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String nameComic = ""+snapshot.child("nameComic").getValue();
                        String imageComic = ""+snapshot.child("imageComic").getValue();
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
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
}
