package com.quocviet.appmobile.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import androidx.viewpager.widget.ViewPager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.quocviet.appmobile.Model.ModelCategory;
import com.quocviet.appmobile.databinding.FragmentAllComicBinding;

import java.util.ArrayList;

public class AllComicFragment extends Fragment {

    private  FragmentAllComicBinding binding;
    private  ViewPaperComicAdapter viewPaperComicAdapter;
    public ArrayList<ModelCategory> categoryArrayList;
    private static String TAG = "LOAD_TAB_TAG";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAllComicBinding.inflate(LayoutInflater.from(getContext()),container,false);
        setupViewPagerAdapter(binding.viewPager);
        binding.tabLayout.setupWithViewPager(binding.viewPager);
        return binding.getRoot();
    }


    private void setupViewPagerAdapter(ViewPager viewPager){
        viewPaperComicAdapter = new ViewPaperComicAdapter(getChildFragmentManager(),FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,getContext());
        categoryArrayList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                categoryArrayList.clear();
                for(DataSnapshot ds : snapshot.getChildren()){
                    ModelCategory model = ds.getValue(ModelCategory.class);
                    categoryArrayList.add(model);
                    viewPaperComicAdapter.addFragment(ComicUsersFragment.newInstance(
                            ""+model.getCategoryId(),
                            ""+model.getCategory(),
                            ""+model.getUid()
                    ),model.getCategory());
                    viewPaperComicAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled( DatabaseError error) {

            }
        });
        viewPager.setOffscreenPageLimit(categoryArrayList.size()-1);
        viewPager.setAdapter(viewPaperComicAdapter);
    }
    public class ViewPaperComicAdapter extends FragmentPagerAdapter {

        private ArrayList<ComicUsersFragment> fragmentsList = new ArrayList<>();
        private ArrayList<String> fragmentsTitleList = new ArrayList<>();
        private Context context;

        public ViewPaperComicAdapter( FragmentManager fm, int behavior, Context context) {
            super(fm);
            this.context = context;
            notifyDataSetChanged();
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentsList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentsList.size();
        }


        private void addFragment(ComicUsersFragment fragment, String title) {
            fragmentsList.add(fragment);
            fragmentsTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentsTitleList.get(position);
        }
    }
}
