package com.quocviet.appmobile.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.quocviet.appmobile.Adapter.ComicHorizontalAdapter;
import com.quocviet.appmobile.Adapter.MySliderAdapter;
import com.quocviet.appmobile.Model.ModelComic;
import com.quocviet.appmobile.databinding.FragmentHomeBinding;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ArrayList<ModelComic> comicArrayList,imageComicSliderArrayList;
    private MySliderAdapter mySliderAdapter;
    private ComicHorizontalAdapter comicHorizontalAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(LayoutInflater.from(getContext()),container,false);
        loadComicToSlider();
        loadNewComic();
        return binding.getRoot();
    }

    private void loadComicToSlider() {
        long currentTime = System.currentTimeMillis();
        long sevenDay = currentTime - 86400000*7;
        imageComicSliderArrayList = new ArrayList<>();
        imageComicSliderArrayList.clear();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Comic");
        reference.orderByChild("timestamp").startAt(sevenDay).endAt(currentTime).limitToLast(3)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren())
                        {
                            ModelComic modelComic = ds.getValue(ModelComic.class);
                            imageComicSliderArrayList.add(modelComic);
                        }
                        binding.sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
                        mySliderAdapter = new MySliderAdapter(getContext(),imageComicSliderArrayList);
                        binding.sliderView.setSliderAdapter(mySliderAdapter);
                        binding.sliderView.setScrollTimeInSec(3);
                        binding.sliderView.setAutoCycle(true);
                        binding.sliderView.startAutoCycle();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void loadNewComic() {
        comicArrayList = new ArrayList<>();
        long currentTime = System.currentTimeMillis();
        long sevenDay = currentTime - 86400000*7;
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Comic");
        ref.orderByChild("timestamp").startAt(sevenDay).endAt(currentTime).limitToLast(5)
                .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    comicArrayList.clear();
                    for (DataSnapshot ds: snapshot.getChildren()) {
                        ModelComic model = ds.getValue(ModelComic.class);
                        comicArrayList.add(model);
                    }
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL,false);
                    binding.newComicRv.setLayoutManager(linearLayoutManager);
                    comicHorizontalAdapter = new ComicHorizontalAdapter(getContext(),comicArrayList);
                    binding.newComicRv.setAdapter(comicHorizontalAdapter);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }});
    }

}
