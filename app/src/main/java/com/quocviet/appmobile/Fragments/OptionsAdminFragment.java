package com.quocviet.appmobile.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.quocviet.appmobile.Adapter.OptionsAdminAdapter;
import com.quocviet.appmobile.databinding.FragmentOptionsAdminBinding;

public class OptionsAdminFragment extends Fragment {
    private FragmentOptionsAdminBinding binding;
    private OptionsAdminAdapter optionsAdminAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOptionsAdminBinding.inflate(LayoutInflater.from(getContext()),container,false);
        optionsAdminAdapter = new OptionsAdminAdapter(getActivity().getSupportFragmentManager(),FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        binding.viewPaper.setAdapter(optionsAdminAdapter);
        binding.tabLayout.setupWithViewPager(binding.viewPaper);
        return binding.getRoot();
    }
}
