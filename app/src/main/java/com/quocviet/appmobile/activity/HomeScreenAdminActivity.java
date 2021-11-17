package com.quocviet.appmobile.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.quocviet.appmobile.Fragments.OptionsAdminFragment;
import com.quocviet.appmobile.Fragments.AllComicFragment;
import com.quocviet.appmobile.Fragments.HomeFragment;
import com.quocviet.appmobile.Fragments.PersonFragment;
import com.quocviet.appmobile.R;
import com.quocviet.appmobile.databinding.ActivityHomeScreenAdminBinding;

public class HomeScreenAdminActivity extends AppCompatActivity {
    private ActivityHomeScreenAdminBinding binding;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeScreenAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bottomNavigationView = binding.bottomNavigation;
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,new HomeFragment()).commit();
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.nav_comic:
                            selectedFragment = new AllComicFragment();
                            break;
                        case R.id.nav_comic_admin:
                            selectedFragment = new OptionsAdminFragment();
                            break;
                        case R.id.nav_profile:
                            selectedFragment = new PersonFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, selectedFragment).commit();
                    return true;
                }
            };
}