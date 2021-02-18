package com.example.cameratest01.gallery_clicked;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.cameratest01.Kids_gallery.GridViewGallery;
import com.example.cameratest01.MyInterface.PagerImageDelete;
import com.example.cameratest01.R;
import com.example.cameratest01.RC_Items.ImageData;

import java.util.ArrayList;

public class Clicked_BigImage_FG extends Fragment {// 2. 뷰페이저를 갖고있는 프레그먼트

    ViewPager viewPager;
    ArrayList<ImageData> imageData, AC_imageData;
    GridViewGallery gridViewGallery;
//    int pagePosition;
    Clicked_adapter adapter;
    int position;
    ArrayList<String> images;

    // AC_ImageData로 정보 받아서 지워야함.....

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.click_bigimage_pager, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        gridViewGallery=(GridViewGallery)view.getContext();

        viewPager=view.findViewById(R.id.bigimage_pager_clicked);

        Bundle bundle=getArguments();
        imageData=bundle.getParcelableArrayList("imageData");
        position=bundle.getInt("position");
        images=bundle.getStringArrayList("images");
        AC_imageData=bundle.getParcelableArrayList("AC_imageData");

        if (images!=null){// 개본 갤러리 데이터 있을때 실행
            adapter=new Clicked_adapter(getChildFragmentManager(), getActivity(), AC_imageData, position, images);
            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(position);

            viewPager.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    if (gridViewGallery.rotClickedNum==1){// 회전버튼 눌렀으면 동작할 코드
                        gridViewGallery.lockImg.setVisibility(View.VISIBLE);
                        return gridViewGallery.returnVal;// true 페이지 넘어가지 않음
                    }else return gridViewGallery.returnVal;// false
                }
            });

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {// 여기서 메뉴작성

                    gridViewGallery.pagePosition=position;// 변경된 페이지 번호 전달.
                    if (AC_imageData.get(position).Check==0){
                        Glide.with(gridViewGallery).load(R.drawable.ic_check_circle_b_24).into(gridViewGallery.checkImgView);
                    }else if (AC_imageData.get(position).Check==1){
                        Glide.with(gridViewGallery).load(R.drawable.ic_check_circle_w_24).into(gridViewGallery.checkImgView);
                    }
                    gridViewGallery.bigImageToolbarItem_clear();
                }
                @Override
                public void onPageSelected(int position) {}

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

        }else {// 기본갤러리 아닐때 실행.
            adapter=new Clicked_adapter(getChildFragmentManager(),getActivity(), imageData, position);
            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(position);

            viewPager.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    if (gridViewGallery.rotClickedNum==1){// 회전버튼 눌렀으면 동작할 코드
                        gridViewGallery.lockImg.setVisibility(View.VISIBLE);
                        return gridViewGallery.returnVal;// true 페이지 넘어가지 않음
                    }else return gridViewGallery.returnVal;// false
                }
            });

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {// 여기서 메뉴작성

                    gridViewGallery.pagePosition=position;// 변경된 페이지 번호 전달.
                    if (imageData.get(position).Check==0){
                        Glide.with(gridViewGallery).load(R.drawable.ic_check_circle_b_24).into(gridViewGallery.checkImgView);
                    }else if (imageData.get(position).Check==1){
                        Glide.with(gridViewGallery).load(R.drawable.ic_check_circle_w_24).into(gridViewGallery.checkImgView);
                    }
                    gridViewGallery.bigImageToolbarItem_clear();
                }
                @Override
                public void onPageSelected(int position) {}

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
