package com.example.cameratest01.RC_Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionProvider;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cameratest01.CameraActivity_test;
import com.example.cameratest01.MyInterface.OnItemClick;
import com.example.cameratest01.MyInterface.RemoveFG_OnClick;
import com.example.cameratest01.R;
import com.example.cameratest01.RC_Items.RC_ImageItems;
import com.example.cameratest01.customDialog.CustomDialog;
import com.example.cameratest01.fragments.BigImageFragment;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class RC_ImageAdapter extends RecyclerView.Adapter {


    ArrayList<RC_ImageItems> items;
//    Fragment bigImageFragment=new BigImageFragment();
    Bundle bundle=new Bundle();
    Context context;
    public OnItemClick mCallback;
    public RemoveFG_OnClick mRemove;
    byte[] data;

    public RC_ImageAdapter(Context context, ArrayList<RC_ImageItems> items, OnItemClick onItemClick, byte[] data) {
        this.context=context;
        this.items = items;//이미지 정보 받아옴
        this.mCallback = onItemClick;
        this.data=data;//이미지 정보 받아옴
    }// 생성자에서 온클릭

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView= LayoutInflater.from(context).inflate(R.layout.rc_image_item, parent, false);
        VH_images holder=new VH_images(itemView);

//        bundle.putByteArray("items",data);
//        bigImageFragment.setArguments(bundle);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VH_images vh= (VH_images) holder;

//        서페이서 가져와서 이미지 회전

//        Glide.with(context).load(items.get(position).imageBM).into(vh.iv);
        Glide.with(context).load(items.get(position).imageBM).into(vh.iv);
        vh.tv.setText(items.get(position).imageName);
        vh.iv.setRotation(90);// 리스트에 보여지는 이미지 회전



        vh.lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onClick(items, position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return items.size();
    }



    class VH_images extends RecyclerView.ViewHolder{

        LinearLayout lay;
        ImageView iv;
        TextView tv;

        public VH_images(@NonNull View itemView) {
            super(itemView);

            lay=itemView.findViewById(R.id.rc_imageList_item);
            iv=itemView.findViewById(R.id.rc_imageList_imageView);
            tv=itemView.findViewById(R.id.rc_imageList_textView);


        }
    }

}
