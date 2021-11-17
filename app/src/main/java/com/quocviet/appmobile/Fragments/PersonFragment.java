package com.quocviet.appmobile.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.quocviet.appmobile.MyApplication;
import com.quocviet.appmobile.R;
import com.quocviet.appmobile.activity.EditProfileActivity;
import com.quocviet.appmobile.activity.HomeScreenAdminActivity;
import com.quocviet.appmobile.activity.HomeScreenUserActivity;
import com.quocviet.appmobile.activity.MainActivity;
import com.quocviet.appmobile.databinding.FragmentPersonBinding;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PersonFragment extends Fragment {
    private FragmentPersonBinding binding;

    //firebase auth, for loading user data using uid
    private FirebaseAuth firebaseAuth;

    private static final String TAG = "PROFILE_TAG";

    private GoogleSignInClient googleSignInClient;

    public PersonFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        binding = FragmentPersonBinding.inflate(LayoutInflater.from(getContext()),container,false);
        //setup firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        loadUserInfo();

        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               signOut();
            }
        });
        binding.profileEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkUser();
            }
        });

        return binding.getRoot();
    }

    private void signOut() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(user.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //get user type
                        String typeLogin = ""+snapshot.child("typeLogin").getValue();
                        if (typeLogin.equals("gmail")) {
                            firebaseAuth.signOut();
                            startActivity(new Intent(getActivity(), MainActivity.class));
                        }
                        else if (typeLogin.equals("google")) {
                            firebaseAuth.signOut();
                            startActivity(new Intent(getActivity(),MainActivity.class));
                        }
                        else if (typeLogin.equals("facebook")) {
                            LoginManager.getInstance().logOut();
                            startActivity(new Intent(getActivity(),MainActivity.class));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d(TAG, "onCancelled: Sign Out due to"+error.getMessage());
                    }
                });

    }

    private void loadUserInfo() {
        Log.d(TAG, "loadUserInfo: Loading user info..."+firebaseAuth.getUid());
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String email = ""+snapshot.child("email").getValue();
                        String name = ""+snapshot.child("name").getValue();
                        String profileImage = ""+snapshot.child("profileImage").getValue();
                        String timestamp = ""+snapshot.child("timestamp").getValue();
                        String uid = ""+snapshot.child("uid").getValue();
                        String userType = ""+snapshot.child("userType").getValue();

                        String formatDate = MyApplication.formatTimestamp(Long.parseLong(timestamp));
                        //set data to ui
                        binding.accountTypeTv.setText(userType);
                        binding.memberDateTv.setText(formatDate);
                        binding.nameTv.setText(name);
                        if(profileImage != "") {
                            Picasso.get()
                                    .load(profileImage)
                                    .placeholder(R.drawable.ic_person_gray)
                                    .into(binding.profileIv, new Callback() {
                                        @Override
                                        public void onSuccess() {
                                        }

                                        @Override
                                        public void onError(Exception e) {

                                        }
                                    });
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d(TAG, "onCancelled: "+error.getMessage());
                    }
                });
    }

    private void checkUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {

        }
        else {
            Intent intent = new Intent(getActivity(),EditProfileActivity.class);
            startActivity(intent);
        }
    }

}
