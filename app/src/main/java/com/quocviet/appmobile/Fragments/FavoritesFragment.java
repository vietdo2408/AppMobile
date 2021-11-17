package com.quocviet.appmobile.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.quocviet.appmobile.Adapter.ListFavoritesComicAdapter;
import com.quocviet.appmobile.Model.ModelComic;
import com.quocviet.appmobile.databinding.FragmentFavoriteBinding;

import java.util.ArrayList;

public class FavoritesFragment extends Fragment {
    private FragmentFavoriteBinding binding;
    private FirebaseAuth firebaseAuth;
    private ArrayList<ModelComic> comicArrayList;
    private ListFavoritesComicAdapter listFavoritesComicAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        binding = FragmentFavoriteBinding.inflate(LayoutInflater.from(getContext()),container,false);
        firebaseAuth = FirebaseAuth.getInstance();
        loadFavoriteBooks();
        return binding.getRoot();
    }
    private void loadFavoriteBooks() {
        comicArrayList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid()).child("Favorites")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        comicArrayList.clear();
                        for (DataSnapshot ds : snapshot.getChildren())
                        {
                            String comicId = ""+ds.child("comicId").getValue();
                            ModelComic modelComic = new ModelComic();
                            modelComic.setComicId(comicId);
                            comicArrayList.add(modelComic);
                        }
                        binding.comicRv.setLayoutManager(new GridLayoutManager(getContext(),3));
                        listFavoritesComicAdapter = new ListFavoritesComicAdapter(getContext(),comicArrayList);
                        binding.comicRv.setAdapter(listFavoritesComicAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}
