package com.quocviet.appmobile.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.api.LogDescriptor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.quocviet.appmobile.Model.ModelChapter;
import com.quocviet.appmobile.databinding.ActivityAddChapterBinding;

import java.util.ArrayList;
import java.util.HashMap;

public class AddChapterActivity extends AppCompatActivity {

    private ActivityAddChapterBinding binding;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private Uri uriChapter = null;
    private String comicId,chapterSize;
    private  static final int PDF_PICK_CODE = 1000;
    private static final String TAG = "ADD_CHAPTER_TAG";
    private ArrayList<ModelChapter> chapters;
    private String nameChapter="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddChapterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        Intent intent = getIntent();
        comicId = intent.getStringExtra("comicId");
        chapterSize = intent.getStringExtra("listChapterSize");

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.attachBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pdfPickInternet();

            }
        });
        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });

    }

    private void validateData() {
        Log.d(TAG,"validateData: validating data...");
        nameChapter = binding.titleEt.getText().toString().trim();

        if(TextUtils.isEmpty(nameChapter)) {
            Toast.makeText(this,"Enter Title...",Toast.LENGTH_SHORT).show();
        }else if (uriChapter == null) {
            Toast.makeText(this,"Pick Pdf...",Toast.LENGTH_SHORT).show();
        }
        else {
            uploadChapterToStorage();
        }
    }

    private void uploadChapterToStorage() {
        Log.d(TAG, "uploadChapterToStorage: upload file chapter to storage");
        progressDialog.setMessage("Uploading Chapter...");
        progressDialog.show();
        long timestamp = System.currentTimeMillis();
        String filePathAndName = "Chapters/" +timestamp;
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(filePathAndName);
        storageReference.putFile(uriChapter)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d(TAG,"onSuccess: uploaded to storage...");
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());
                        String uploadChapter = ""+uriTask.getResult();
                        uploadChapterToDb(uploadChapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Log.d(TAG,"onFailure: upload failed due to"+e.getMessage());
                        Toast.makeText(AddChapterActivity.this,"Upload failed due to"+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void uploadChapterToDb(String uploadChapter) {
        Log.d(TAG, "uploadChapterToDb: Uploading Chapter to Db ...");
        progressDialog.setMessage("Uploading Chapter info...");

        String id;
        if (chapterSize.isEmpty()) {
            id =""+0;
        }
        else {
            id =""+(Integer.parseInt(chapterSize));
        }
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("nameChapter",""+nameChapter);
        hashMap.put("urlChapter",""+uploadChapter);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Comic");
        ref.child(comicId).child("chapters").child(id)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        onBackPressed();
                        Log.d(TAG,"onSuccess: Successfully uploaded...");
                        Toast.makeText(AddChapterActivity.this,"Successfully uploaded...",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Log.d(TAG,"onFailure: Failure to upload to db due to"+e.getMessage());
                        Toast.makeText(AddChapterActivity.this,"Failure to upload to db due to"+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void pdfPickInternet() {
        Log.d(TAG,"pdfPickInternet: starting pdf pick intent");
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Pdf"),PDF_PICK_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == PDF_PICK_CODE){
                Log.d(TAG,"onActivityResult: PDF Picked");
                uriChapter = data.getData();
                Log.d(TAG,"onActivityResult: URI: "+uriChapter);
                binding.titleUrlTv.setText(""+uriChapter);
            }
        }
        else {
            Log.d(TAG,"onActivityResult: cancelled picking pdf");
            Toast.makeText(this,"cancelled picking pdf",Toast.LENGTH_SHORT).show();
        }
    }
}