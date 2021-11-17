package com.quocviet.appmobile.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.quocviet.appmobile.Admin.ComicDetailAdminActivity;
import com.quocviet.appmobile.Model.ModelComic;
import com.quocviet.appmobile.activity.DetailComicActivity;
import com.quocviet.appmobile.databinding.RowSliderBinding;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MySliderAdapter extends SliderViewAdapter<MySliderAdapter.HolderMySlider> {
    private Context context;
    private ArrayList<ModelComic> imageSliderArrayList;
    private RowSliderBinding binding;

    public MySliderAdapter(Context context, ArrayList<ModelComic> imageSliderArrayList) {
        this.context = context;
        this.imageSliderArrayList = imageSliderArrayList;
    }

    @Override
    public HolderMySlider onCreateViewHolder(ViewGroup parent) {
        binding = RowSliderBinding.inflate(LayoutInflater.from(context),parent,false);
        return new HolderMySlider(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(HolderMySlider viewHolder, int position) {
        ModelComic modelComic = imageSliderArrayList.get(position);
        String comicId = modelComic.getComicId();
        String imageComic = modelComic.getImageComic();

        Picasso.get()
                .load(imageComic)
                .fit()
                .into(viewHolder.imageSlider, new Callback() {
            @Override
            public void onSuccess() {
                viewHolder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(Exception e) {

            }
        });
        binding.imageSliderIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailComicActivity.class);
                intent.putExtra("comicId",comicId);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getCount() {
        return imageSliderArrayList.size();
    }

    public class HolderMySlider extends SliderViewAdapter.ViewHolder {
        ImageView imageSlider;
        ProgressBar progressBar;
        public HolderMySlider(View itemView) {
            super(itemView);
            imageSlider = binding.imageSliderIv;
            progressBar = binding.progressBar;
        }
    }
}
