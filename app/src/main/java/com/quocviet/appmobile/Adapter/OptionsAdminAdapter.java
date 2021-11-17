package com.quocviet.appmobile.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.quocviet.appmobile.Fragments.AdminCategoriesFragment;
import com.quocviet.appmobile.Fragments.AdminComicFragment;


public class OptionsAdminAdapter extends FragmentStatePagerAdapter {

    public OptionsAdminAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new AdminCategoriesFragment();
            case 1:
                return new AdminComicFragment();
            default:
                return new AdminCategoriesFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position) {
            case 0:
                title = "Thể loại";
                break;
            case 1:
                title = "Truyện tranh";
                break;
        }
        return title;
    }
}
