package com.example.cameratest01.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.example.cameratest01.CameraActivity_test;
import com.example.cameratest01.MyInterface.RemoveFG_OnClick;
import com.example.cameratest01.R;
import com.example.cameratest01.RC_Items.ImageArrayItems;
import com.example.cameratest01.RC_Items.Image_Parcelable_Item;
import com.example.cameratest01.RC_Items.RC_ImageItems;

import java.util.ArrayList;
import java.util.Collection;

public class BigImageFragment extends Fragment implements View.OnClickListener{

    int position;
    ArrayList<byte[]> imgData;
    ImageView iv_fg, left, right;
    byte[] data;
    View top,bottom;
    public LinearLayout fragment_lay;
    ArrayList<RC_ImageItems> items;


    public BigImageFragment() {
    }

    public BigImageFragment(LinearLayout fragment_lay, int position,ArrayList<RC_ImageItems> items, byte[] data) {
        this.fragment_lay = fragment_lay;
        this.position = position;
        this.items=items;
        this.data=data;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.bigimage_fg, container, false);

        iv_fg=view.findViewById(R.id.bigImage_fg);

        left=view.findViewById(R.id.clickBigimg_left);
        left.setOnClickListener(this::onClick);
        right=view.findViewById(R.id.clickBigimg_right);
        right.setOnClickListener(this::onClick);
        top=view.findViewById(R.id.emptyView_bigimg_top);
        top.setOnClickListener(this::onClick);
        bottom=view.findViewById(R.id.emptyView_bigimg_bottom);
        bottom.setOnClickListener(this::onClick);


        // todo Parcelable확인해보기
        // todo 복잡함 다시 해봐야함 https://everyshare.tistory.com/23


        Glide.with(getContext()).load(data).into(iv_fg);
        iv_fg.setRotation(90);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.clickBigimg_left:{
                if (position-1 >= 0){
                    Glide.with(getContext()).load(items.get(position-1).imageBM).into(iv_fg);
                    position--;
                }else {
                    viv();
                }

                break;
            }
            case R.id.clickBigimg_right:{
                if (position+1 < items.size()){
                    Glide.with(getContext()).load(items.get(position+1).imageBM).into(iv_fg);
                    position++;
                }else {
                    viv();
                }
                break;
            }
            case R.id.emptyView_bigimg_top:{
                FragmentManager fm=getFragmentManager();
                fm.beginTransaction().setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).hide(this).commit();
                fragment_lay.setVisibility(View.GONE);
                break;
            }
            case R.id.emptyView_bigimg_bottom:{
                FragmentManager fm=getFragmentManager();
                fm.beginTransaction().setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).hide(this).commit();
                fragment_lay.setVisibility(View.GONE);
                break;
            }

        }
    }

    public void viv(){
        Vibrator vibrator;
        vibrator= (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(100);
        Toast.makeText(getContext(), "사진이 없습니다.", Toast.LENGTH_SHORT).show();
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
