package com.quocviet.appmobile.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.quocviet.appmobile.Adapter.AllComicAdapter;
import com.quocviet.appmobile.Adapter.ListComicAdminAdapter;
import com.quocviet.appmobile.Model.ModelComic;
import com.quocviet.appmobile.R;
import com.quocviet.appmobile.databinding.FragmentComicUsersBinding;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class ComicUsersFragment extends Fragment {

    private String categoryId;
    private String category;
    private String uid;

    private ArrayList<ModelComic> comicArrayList;
    private AllComicAdapter allComicAdapter;
    private FragmentComicUsersBinding binding;
    private static final String TAG= "COMIC_LOAD_TAG";

    public ComicUsersFragment() {
    }

    public static ComicUsersFragment newInstance(String categoryId, String category, String uid) {
        ComicUsersFragment fragment = new ComicUsersFragment();
        Bundle args = new Bundle();
        args.putString("categoryId", categoryId);
        args.putString("category", category);
        args.putString("uid", uid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryId = getArguments().getString("categoryId");
            category = getArguments().getString("category");
            uid = getArguments().getString("uid");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentComicUsersBinding.inflate(LayoutInflater.from(getContext()),container,false);
        Log.d(TAG,"onCreateView: Category: "+category);
        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                binding.swipeRefreshLayout.setRefreshing(false);
                loadCategorizedBooks();
            }
        });
        binding.swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                loadCategorizedBooks();
            }
        });
        loadCategorizedBooks();
        return binding.getRoot();
    }

    private void loadCategorizedBooks() {
        comicArrayList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Comic");
        ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        comicArrayList.clear();
                        for (DataSnapshot ds : snapshot.getChildren()){
                            ModelComic modelComic = ds.getValue(ModelComic.class);
                            if (modelComic.getCategories().contains(categoryId)) {
                                comicArrayList.add(modelComic);
                                Log.d(TAG, "onDataChange: "+modelComic);
                            }
                        }
                        binding.comicRv.setLayoutManager(new GridLayoutManager(getContext(),3));
                        allComicAdapter = new AllComicAdapter(getContext(),comicArrayList);
                        allComicAdapter.notifyDataSetChanged();
                        binding.comicRv.setAdapter(allComicAdapter);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}