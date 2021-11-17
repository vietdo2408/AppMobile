package com.quocviet.appmobile.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.quocviet.appmobile.Adapter.ListChapterAdapter;
import com.quocviet.appmobile.Adapter.ListChapterUserAdapter;
import com.quocviet.appmobile.Admin.ComicDetailAdminActivity;
import com.quocviet.appmobile.Model.ModelChapter;
import com.quocviet.appmobile.MyApplication;
import com.quocviet.appmobile.R;
import com.quocviet.appmobile.databinding.ActivityComicDetailAdminBinding;
import com.quocviet.appmobile.databinding.ActivityDetailComicBinding;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailComicActivity extends AppCompatActivity {
    private ActivityDetailComicBinding binding;
    private String comicId, nameComic, descriptionComic;
    boolean isInMyFavorite = false;
    private ArrayList<ModelChapter> chapterArrayList;
    private ListChapterUserAdapter listChapterUserAdapter;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailComicBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        chapterArrayList = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        comicId = intent.getStringExtra("comicId");
        loadComicDetails();
        loadChapters();
        checkIsFavorite();
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.isFavoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isInMyFavorite){
                    MyApplication.removeFromFavorite(DetailComicActivity.this,comicId);
                }
                else {
                    MyApplication.addToFavorite(DetailComicActivity.this,comicId);
                }
            }
        });
    }
    private void loadChapters() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Comic");
        ref.child(comicId).child("chapters")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        chapterArrayList.clear();
                        for (DataSnapshot ds: snapshot.getChildren()) {
                            ModelChapter model = ds.getValue(ModelChapter.class);
                            chapterArrayList.add(model);
                        }
                        listChapterUserAdapter = new ListChapterUserAdapter(DetailComicActivity.this,chapterArrayList);
                        binding.chapterRv.setAdapter(listChapterUserAdapter);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void loadComicDetails() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Comic");
        ref.child(comicId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        nameComic = ""+snapshot.child("nameComic").getValue();
                        descriptionComic = ""+snapshot.child("descriptionComic").getValue();
                        String imageComic = ""+snapshot.child("imageComic").getValue();
                        String viewsCount = ""+snapshot.child("viewsCount").getValue();

                        Picasso.get()
                                .load(imageComic)
                                .fit()
                                .into(binding.imageComicIv, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        binding.progressBar.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onError(Exception e) {

                                    }
                                });
                        binding.comicNameTv.setText(nameComic);
                        binding.comicDescriptionTv.setText(descriptionComic);
                        binding.viewsCountTv.setText(viewsCount.replace("null","VIEWS"));
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void checkIsFavorite(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid()).child("Favorites").child(comicId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        isInMyFavorite = snapshot.exists(); // true: if exists, false if not exists
                        if (isInMyFavorite){
                            binding.isFavoriteBtn.setBackgroundResource(R.drawable.ic_favorite_red);
                        }
                        else {
                            binding.isFavoriteBtn.setBackgroundResource(R.drawable.ic_favorite_white);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
}