package com.quocviet.appmobile.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.quocviet.appmobile.Adapter.CategoryAdapter;
import com.quocviet.appmobile.Adapter.ListComicAdminAdapter;
import com.quocviet.appmobile.Admin.AddComicActivity;
import com.quocviet.appmobile.Model.ModelCategory;
import com.quocviet.appmobile.Model.ModelComic;
import com.quocviet.appmobile.R;
import com.quocviet.appmobile.databinding.FragmentAdminComicBinding;
import com.quocviet.appmobile.databinding.FragmentAllComicBinding;

import java.util.ArrayList;


public class AdminComicFragment extends Fragment {
    private FragmentAdminComicBinding binding;
    private ListComicAdminAdapter listComicAdminAdapter;
    private ArrayList<ModelComic> comicArrayList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAdminComicBinding.inflate(LayoutInflater.from(getContext()),container,false);
        binding.addComicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AddComicActivity.class));
            }
        });
        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                binding.swipeRefreshLayout.setRefreshing(false);
                loadComic();
            }
        });
        binding.swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                loadComic();
            }
        });
        loadComic();
        return binding.getRoot();
    }
    private void loadComic() {
        comicArrayList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Comic");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                comicArrayList.clear();
                for (DataSnapshot ds: snapshot.getChildren()) {
                    ModelComic model = ds.getValue(ModelComic.class);
                    comicArrayList.add(model);
                }
                binding.comicRv.setLayoutManager(new GridLayoutManager(getContext(),3));
                listComicAdminAdapter = new ListComicAdminAdapter(getContext(),comicArrayList);
                binding.comicRv.setAdapter(listComicAdminAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}