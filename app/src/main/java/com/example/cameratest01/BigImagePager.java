package com.example.cameratest01;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cameratest01.RC_Items.RC_ImageItems;

import java.util.ArrayList;

public class BigImagePager extends AppCompatActivity implements View.OnClickListener {

    View top,bottom;
    ImageView iv_fg, left, right;
    ArrayList<RC_ImageItems> items;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_image_pager);

        iv_fg=findViewById(R.id.bigImage_fg);

        left=findViewById(R.id.clickBigimg_left);
        left.setOnClickListener(this::onClick);
        right=findViewById(R.id.clickBigimg_right);
        right.setOnClickListener(this::onClick);
        top=findViewById(R.id.emptyView_bigimg_top);
        top.setOnClickListener(this::onClick);
        bottom=findViewById(R.id.emptyView_bigimg_bottom);
        bottom.setOnClickListener(this::onClick);
        Intent intent=getIntent();
        items= (ArrayList<RC_ImageItems>) intent.getExtras().getSerializable("items");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.clickBigimg_left:{
                if (position-1 >= 0){
                    Glide.with(this).load(items.get(position-1).imageBM).into(iv_fg);

                    position--;
                }else {
                    viv();
                }

                break;
            }
            case R.id.clickBigimg_right:{
                if (position+1 < items.size()){
                    Glide.with(this).load(items.get(position+1).imageBM).into(iv_fg);
                    position++;
                }else {
                    viv();
                }
                break;
            }
            case R.id.emptyView_bigimg_top:{
                finish();
                break;
            }
            case R.id.emptyView_bigimg_bottom:{
                finish();
                break;
            }

        }
    }

    public void viv(){
        Vibrator vibrator;
        vibrator= (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(100);
        Toast.makeText(this, "사진이 없습니다.", Toast.LENGTH_SHORT).show();
        Thread thread=new Thread(new Runnable() {// 진동 간격 두고 2번울리는 구간.
            @Override
            public void run() {
                try {
                    Thread.sleep(400);
                    vibrator.vibrate(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

}