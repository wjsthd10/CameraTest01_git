//package com.example.cameratest01.fragments;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.AttributeSet;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentActivity;
//import androidx.fragment.app.FragmentManager;
//import androidx.fragment.app.FragmentStatePagerAdapter;
//import androidx.viewpager.widget.ViewPager;
//
//import com.example.cameratest01.R;
//
//import java.io.File;
//
//public class GalleryClicked_Image_FG extends Fragment{
//
//    ViewPager viewPager;
//    int position;
//
//    File direct;
//    String[] fileListArr;
//    int pageNum;
//
//    public GalleryClicked_Image_FG() {
//        direct=new File(getActivity().getFilesDir(), "CameraTest01"+File.separator+"kidsLove");
//        Log.e("direct File : ", direct.listFiles().length+"");
//        if (!direct.exists()) {
//            if (!direct.mkdirs()) {
//                Log.d("TAG", "failed to create directory");
//            }
//        }
//        fileListArr=direct.list();
//        pageNum=fileListArr.length;
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//
//
//    }
//
//    public class GalleryClickAdatper extends FragmentStatePagerAdapter{
//
//        public GalleryClickAdatper(@NonNull FragmentManager fm) {
//            super(fm);
//        }
//
//        @NonNull
//        @Override
//        public Fragment getItem(int position) {
//            Fragment fragment=new M_Fragment();
//
//            return fragment;
//        }
//
//        @Override
//        public int getCount() {
//            return 0;
//        }
//    }
//
//    public class M_Fragment extends Fragment{
//        @Nullable
//        @Override
//        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//
//            return inflater.inflate(R.layout.viewpager_bigimage, container, false);
//        }
//
//        @Override
//        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//
//        }
//    }
//
//}
