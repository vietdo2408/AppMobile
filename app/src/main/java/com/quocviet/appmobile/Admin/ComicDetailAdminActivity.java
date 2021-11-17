package com.quocviet.appmobile.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.quocviet.appmobile.Adapter.ListChapterAdapter;
import com.quocviet.appmobile.Adapter.ListComicAdminAdapter;
import com.quocviet.appmobile.Model.ModelChapter;
import com.quocviet.appmobile.Model.ModelComic;
import com.quocviet.appmobile.databinding.ActivityComicDetailAdminBinding;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ComicDetailAdminActivity extends AppCompatActivity {

    private ActivityComicDetailAdminBinding binding;
    private String comicId, nameComic, descriptionComic, nameChapter,urlChapter;
    boolean isInMyFavorite = false;
    private ArrayList<ModelChapter> chapterArrayList;
    private ListChapterAdapter listChapterAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityComicDetailAdminBinding.inflate(getLayoutInflater());
        chapterArrayList = new ArrayList<>();
        Intent intent = getIntent();
        comicId = intent.getStringExtra("comicId");
        loadComicDetails();
        loadChapters();

        binding.addChapterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ComicDetailAdminActivity.this, AddChapterActivity.class);
                intent1.putExtra("comicId",comicId);
                intent1.putExtra("listChapterSize",""+chapterArrayList.size());
                startActivity(intent1);
            }
        });
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteComic();
            }
        });


        setContentView(binding.getRoot());
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
                listChapterAdapter = new ListChapterAdapter(ComicDetailAdminActivity.this,chapterArrayList);
                binding.recyclerChapter.setAdapter(listChapterAdapter);
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

    private void deleteComic() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Comic");
        ref.child(comicId).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(ComicDetailAdminActivity.this,"Xóa thành công truyện tranh",Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ComicDetailAdminActivity.this,"Xóa không thành công truyện tranh",Toast.LENGTH_SHORT).show();
                    }
                });
    }

}