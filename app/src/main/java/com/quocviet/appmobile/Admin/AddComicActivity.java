package com.quocviet.appmobile.Admin;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.quocviet.appmobile.databinding.ActivityAddComicBinding;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class AddComicActivity extends AppCompatActivity {

    private ActivityAddComicBinding binding;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private ArrayList<String> categoryTitleArrayList,categoryIdArrayList;
    private ArrayList<String> selectedCategoryId;
    private static final int PERMISSION_CODE = 1000;
    private static final String TAG = "ADD_COMIC_TAG";
    private Uri imageUri = null;
    private ArrayList<Integer> listCategory = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddComicBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        loadCategories();
        binding.progressBar.setVisibility(View.INVISIBLE);
        binding.imageComicIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(checkSelfPermission(Manifest.permission.CAMERA) ==
                            PackageManager.PERMISSION_DENIED ||
                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                                    PackageManager.PERMISSION_DENIED) {
                        String [] permission = {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permission,PERMISSION_CODE);
                    }
                    else {
                        pickImageGallery();
                    }
                }
                else {
                    pickImageGallery();
                }
            }
        });
        binding.categoryTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoriesPickDialog();
            }
        });
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageGallery();
                } else {
                    Toast.makeText(this,"Permission denied...",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private  String nameComic = "", descriptionComic = "";
    private void validateData() {
        Log.d(TAG, "validateData: validating data...");
        nameComic = binding.namEt.getText().toString().trim();
        descriptionComic = binding.descriptionEt.getText().toString().trim();
        if (TextUtils.isEmpty(nameComic)) {
            Toast.makeText(this, "Enter Title...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(descriptionComic)) {
            Toast.makeText(this, "Enter Description...", Toast.LENGTH_SHORT).show();
        } else if (selectedCategoryId.isEmpty()) {
            Toast.makeText(this, "Pick Category...", Toast.LENGTH_SHORT).show();
        }
        else if(imageUri != null){
            uploadImage();
        }

    }
    private void uploadImage() {
        Log.d(TAG, "uploadImage: Uploading Comic image...");
        progressDialog.setMessage("Updating profile image");
        progressDialog.show();

        String filePathAndName = "ComicImage/"+System.currentTimeMillis();

        //storage reference
        StorageReference reference = FirebaseStorage.getInstance().getReference(filePathAndName);
        reference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d(TAG, "onSuccess: Profile image uploaded");
                        Log.d(TAG, "onSuccess: Getting url of loaded image");
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl() ;
                        while (!uriTask.isSuccessful());
                        String uploadedImageUrl = ""+uriTask.getResult();

                        Log.d(TAG, "onSuccess: Uploaded Image URL: "+uploadedImageUrl);
                        updateInfoComic(uploadedImageUrl);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: Failed to upload image due to"+e.getMessage());
                        progressDialog.dismiss();
                        Toast.makeText(AddComicActivity.this,"Failed to upload image due to"+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void updateInfoComic (String imageUri) {
        Log.d(TAG, "updateProfile: Updating user profile");
        progressDialog.setMessage("Updating user profile...");
        progressDialog.show();
        String uid = firebaseAuth.getUid();
        long timestamp = System.currentTimeMillis();
        //setup data update in db
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("uid",""+uid);
        hashMap.put("comicId",""+timestamp);
        hashMap.put("nameComic",""+nameComic);
        hashMap.put("descriptionComic",""+descriptionComic);
        hashMap.put("timestamp",timestamp);
        hashMap.put("viewsCount",0);
        hashMap.put("downloadsCount",0);
        if(imageUri != null){
            hashMap.put("imageComic",""+imageUri);
        }

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Comic");
        databaseReference.child(""+timestamp)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "onSuccess: Updated Profile Success...");
                        progressDialog.dismiss();
                        Toast.makeText(AddComicActivity.this,"Updated Comic Success",Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: Failed to update db due to"+e.getMessage());
                        progressDialog.dismiss();
                        Toast.makeText(AddComicActivity.this,"Failed to update db due to"+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
        HashMap<String, Object> hashMapCategory = new HashMap<>();
        for(int i =0;i<selectedCategoryId.size();i++) {
            hashMapCategory.put(""+i,selectedCategoryId.get(i));
            databaseReference.child(""+timestamp).child("categories").setValue(hashMapCategory);
        }
    }

    private void loadCategories() {
        Log.d(TAG,"loadCategories: Loading categories...");
        categoryTitleArrayList = new ArrayList<>();
        categoryIdArrayList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryTitleArrayList.clear();
                categoryIdArrayList.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    String categoryId = ""+ds.child("categoryId").getValue();
                    String  categoryTitle = ""+ds.child("category").getValue();
                    categoryTitleArrayList.add(categoryTitle);
                    categoryIdArrayList.add(categoryId);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void categoriesPickDialog() {
        Log.d(TAG,"categoryPickDialog: showing category pick dialog");
        selectedCategoryId = new ArrayList<>();
        String[] categoriesArray = new String[categoryTitleArrayList.size()];
        for (int i = 0; i < categoryTitleArrayList.size(); i++){
            categoriesArray[i] = categoryTitleArrayList.get(i);
        }
        final boolean[] checkedPickCategory = new boolean[categoriesArray.length];
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn thể loại")
                .setMultiChoiceItems(categoriesArray, checkedPickCategory, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                    if(isChecked) {
                        if(!listCategory.contains(position)) {
                            listCategory.add(position);
                        }
                        else {
                            listCategory.remove(position);
                        }
                    }
                }})
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        selectedCategoryId.clear();
                        String item = "",id = "";
                        for (int i = 0; i<listCategory.size();i++) {
                            item = item + categoryTitleArrayList.get(listCategory.get(i));
                            id =""+ categoryIdArrayList.get(listCategory.get(i));;
                            selectedCategoryId.add(id);
                            if (i != listCategory.size() - 1) {
                                item = item + ", ";
                            }

                        }
                        Log.d(TAG, "onClick: "+selectedCategoryId);
                        binding.categoryTv.setText(item);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                    }
                }).create().show();

    }

    private void pickImageGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryActivityResultLauncher.launch(intent);

    }

    private ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Log.d(TAG, "onActivityResult: " + imageUri);
                        Intent data = result.getData();
                        imageUri = data.getData();
                        Log.d(TAG, "onActivityResult: Picked From Gallery: "+imageUri);
                        binding.progressBar.setVisibility(View.VISIBLE);
                        Picasso.get().load(imageUri).fit().into(binding.imageComicIv, new Callback() {
                            @Override
                            public void onSuccess() {
                                binding.progressBar.setVisibility(View.GONE);
                            }
                            @Override
                            public void onError(Exception e) {

                            }
                        });
                    }
                    else {
                        Toast.makeText(AddComicActivity.this,"Cancelled",Toast.LENGTH_SHORT).show();
                    }
                }
            });
}