package com.example.cameratest01.Kids_gallery;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.cameratest01.R;
import com.example.cameratest01.fragments.GalleryPagerFragment;

import java.io.File;
import java.util.ArrayList;

public class PagerGalleryAdapter extends FragmentStatePagerAdapter {

    ArrayList<String> strArr;
    LayoutInflater mInflater;
    Context context;
    File direct;
    File[] lists;
    String[] fileListArr;

    public PagerGalleryAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return new GalleryPagerFragment();
    }

    public PagerGalleryAdapter(@NonNull FragmentManager fm, int behavior, ArrayList<String> strArr, Context context) {
        super(fm, behavior);
        this.strArr = strArr;
        this.context = context;
        mInflater=LayoutInflater.from(context);

        direct=new File(context.getFilesDir(), "CameraTest01"+File.separator+"kidsLove");
        if (!direct.exists()){
            if (!direct.mkdirs()) {
                Log.d("TAG", "failed to create directory");
            }
        }
        fileListArr=direct.list();
    }


    @Override
    public int getCount() {
        return fileListArr.length;
    }
}
