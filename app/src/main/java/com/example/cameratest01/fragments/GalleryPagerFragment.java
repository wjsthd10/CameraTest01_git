package com.example.cameratest01.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.cameratest01.R;

import java.io.File;
import java.util.ArrayList;

public class GalleryPagerFragment extends Fragment {

    ImageView imageView;
    ArrayList<String> strArr;
    File direct;
    File[] lists;
    String[] fileListArr;
    int position;

    public GalleryPagerFragment() {
        setRetainInstance(true);
    }

    public GalleryPagerFragment(ArrayList<String> strArr, int position) {
        this.strArr = strArr;
        this.position=position;

        direct=new File(getContext().getFilesDir(), "CameraTest01"+File.separator+"kidsLove");
        if (!direct.exists()) {
            if (!direct.mkdirs()) {
                Log.e("dirctErr", "failed to create directory");
            }
        }
        fileListArr=direct.list();

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.rc_gallery_bigimg, container, false);
        imageView=view.findViewById(R.id.rc_gallery_bigimg);

        // file 데이터 받기
        File[] lists=direct.listFiles();


        // 이미지 출력.
        Glide.with(getContext()).load(lists[position]).into(imageView);


        return view;
    }


}
