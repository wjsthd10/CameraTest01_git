package com.example.cameratest01.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.cameratest01.CameraActivity_test;
import com.example.cameratest01.R;

public class BigImageFragment extends DialogFragment {

    byte[] bytes;
    Bitmap bm;
    ImageView iv_fg;

    public BigImageFragment() {
    }

    public static BigImageFragment getInstance() {
        BigImageFragment e=new BigImageFragment();
        return e;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bytes=getArguments().getByteArray("image");
        bm= BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.bigimage_fg, container);
        bytes=getArguments().getByteArray("image");
        bm= BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        iv_fg=view.findViewById(R.id.bigImage_D);
        Glide.with(getContext()).load(bm).into(iv_fg);

        return view;
    }



}
