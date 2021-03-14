package com.example.cameratest01.gallery_clicked;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.cameratest01.Kids_gallery.GridViewGallery;
import com.example.cameratest01.MyInterface.PagerImageDelete;
import com.example.cameratest01.RC_Items.ImageData;

import java.util.ArrayList;

public class Clicked_adapter extends FragmentStatePagerAdapter implements PagerImageDelete {// 뷰페이저 어뎁터

    Context context;
    ArrayList<ImageData> imageData;
    int position;
    ArrayList<String> images;
    GridViewGallery activity;

    public Clicked_adapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public Clicked_adapter(@NonNull FragmentManager fm, Context context, ArrayList<ImageData> imageData, int position) {
        super(fm);
        this.context = context;
        activity=(GridViewGallery) context;
        this.imageData = imageData;
        this.position = position;
    }

    public Clicked_adapter(@NonNull FragmentManager fm, Context context, ArrayList<ImageData> imageData, int position, ArrayList<String> images) {// cuntNumK따로 보내줘야함.
        super(fm);
        this.context = context;
        activity=(GridViewGallery) context;
        this.imageData = imageData;
        this.position = position;
        this.images = images;
    }

    //    public Clicked_adapter(@NonNull FragmentManager fm, Context context) {
//        super(fm);
//        this.context = context;
//    }


    @NonNull
    @Override
    public Fragment getItem(int position) {// 새로 생성되는 포지션
        Log.e("show_position_now : ", "   _ca_"+this.position);// 클릭했던 포지션 유지
        Log.e("show_position_item : ", "   _itemP_"+position);// 새로 생성되는 포지션
        if (images!=null){
            Fragment fragment=new ImagePageFragment(context, imageData, position, images);
            return fragment;
        }else {
            Fragment fragment=new ImagePageFragment(context, imageData, position);
            return fragment;
        }
    }

    @Override
    public int getCount() {
        if (activity.GALLERY_TYPE.equals("D")){// 기본갤러리일때
            Log.e("GALLERY_TYPE_SHOW", " - "+activity.GALLERY_TYPE);
            try {
                return images.size();
            }catch (NullPointerException e) {
                return imageData.size();
            }
        }else {// 키즈사랑 갤러리일때
            Log.e("GALLERY_TYPE_SHOW", " - "+activity.GALLERY_TYPE);
            try {
                return imageData.size();// 여기에 새로받은 키즈사랑갤러리 이미지 리스트 사이즈 입력해야함.
            }catch (NullPointerException e){
                return imageData.size();
            }

        }
    }

    @Override
    public void imgDelete(int position) {
        notifyDataSetChanged();
    }
}
