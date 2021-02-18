package com.example.cameratest01.Kids_gallery;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.cameratest01.R;
import com.example.cameratest01.RC_Items.RC_ImageItems;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;

public class GalleryActivity_Pager extends AppCompatActivity  {

    ArrayList<RC_ImageItems> items;
    ArrayList<String> strArr;


    RecyclerView rc_gallery_bottomImg;

    GalleryAdapter_bottomImg bottomImgAdapter;
    Toolbar toolbar;
    ActionBar actionBar;

    ViewPager viewPager;
    File direct;
    String[] fileListArr;

    Context context=this;

    public GalleryActivity_Pager() {
        direct=new File(getFilesDir(), "CameraTest01"+File.separator+"kidsLove");
        Log.e("direct File : ", direct.listFiles().length+"");
        if (!direct.exists()) {
            if (!direct.mkdirs()) {
                Log.d("TAG", "failed to create directory");
            }
        }
        fileListArr=direct.list();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Toast.makeText(this, "resume", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        toolbar=findViewById(R.id.toolbar_gallery);
        setSupportActionBar(toolbar);
        actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);


        viewPager=findViewById(R.id.pager_gallery_test);
        PagerAdapter pagerAdapter=new PagerAdapter();
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(3);
//        viewPager.setCurrentItem();
//        Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show();

        rc_gallery_bottomImg=findViewById(R.id.gallery_rc_image);
        Bundle bundle=getIntent().getExtras();
        items= (ArrayList<RC_ImageItems>) bundle.getSerializable("items");
        strArr=bundle.getStringArrayList("fileName");
        if (strArr.size()>0){
            actionBar.setTitle(strArr.get(strArr.size()-1));
        }else actionBar.setTitle("키즈사랑 갤러리");// 마지막에 촬영한 사진이름
//        todo 페이지 넘어갈때 사진 이름으로 타이틀 전환




        LinearLayoutManager lm2=new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        lm2.setReverseLayout(true);
        lm2.setStackFromEnd(true);
        rc_gallery_bottomImg.setLayoutManager(lm2);
        bottomImgAdapter=new GalleryAdapter_bottomImg(this, strArr);
        rc_gallery_bottomImg.setAdapter(bottomImgAdapter);
        rc_gallery_bottomImg.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                View child=rv.findChildViewUnder(e.getX(),e.getY());
                int position=rv.getChildAdapterPosition(child);
                try {
                    ImageView iv=rc_gallery_bottomImg.getChildViewHolder(child).itemView.findViewById(R.id.rc_gallery_bottomImg);
                    iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // pager이동

                        }
                    });
                    return false;
                }catch (Exception e2){// 하단 슬라이드 넘길때 이미지간 간격 클릭하면 에러발생함. 강제종료만 막은 상태
                    Log.e("Error In Gallery : ", e2.toString());
                    return false;
                }
            }
            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) { }
            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) { }
        });// 바텀 이미지 슬라이드 리스너




    }//onCreate

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:finish();return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void goToAlbum(View view) {
//        앨범작성페이지로 이동

    }

    public void addGallery(View view) {
//        갤러리에 저장버튼(사용자가 지정한 위치의 갤러리)
//        todo 이미 저장이 되어있으니까 갤러리로 이동버튼으로 변경하는 것은 어떨까?
//        아니면 공유하기 버튼?
    }

    public void deleteImage(View view) {

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("사진 삭제").setMessage("삭제하시면 갤러리에서 확인하거나 앨범에 등록할 수 없습니다.");
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.e("사진 삭제 취소", "취소 성공");
            }
        });
        builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Log.d("listsSize_main", "delete1_"+mainImgAdapter.position+"");
//
//                mainImgAdapter.onClick(mainImgAdapter.position);// 사진 삭제 코드
//
////        recyclerview에 어뎁터 다시 연결 시켜주어야 했음.
//                mainImgAdapter=new GalleryAdapter(GalleryActivity_Pager.this, strArr);// 큰이미지 어뎁터
//                rc_gallery_mainImg.setAdapter(mainImgAdapter);// 성공!!  // 뷰페이저 사용하기로함.


                bottomImgAdapter=new GalleryAdapter_bottomImg(GalleryActivity_Pager.this, strArr);// 작은 스크롤 이미지 어뎁터
                rc_gallery_bottomImg.setAdapter(bottomImgAdapter);
                Snackbar.make(findViewById(R.id.gallery_main), "사진이 삭제 되었습니다.",Snackbar.LENGTH_LONG).show();
                // 하단 리사이클러뷰도 삭제+재실행 해야함.....
            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
        Log.e("다이얼로그 종료_", "다이얼로그 종료");

    }


    public void goToGallery(View view) {// 갤러리로 이동하는 버튼
        Intent intent=new Intent(this, GridViewGallery.class);
        Bundle bundle=new Bundle();
        bundle.putStringArrayList("fileName", strArr);
        intent.putExtras(bundle);
//        이동할때 데이터 전달.
        startActivity(intent);
    }

    private class PagerAdapter extends androidx.viewpager.widget.PagerAdapter{

        @Override
        public int getCount() {
            return fileListArr.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view==((View)object);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {

            LayoutInflater layoutInflater= (LayoutInflater) container.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view=layoutInflater.inflate(R.layout.viewpager_bigimage, null);
            ImageView imageView=view.findViewById(R.id.image_pager);

            File[] lists=direct.listFiles();

            Glide.with(context).load(lists[position]).into(imageView);

            ((ViewPager)container).addView(view);

            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            ((ViewPager)container).removeView((View)object);
        }
    }

}