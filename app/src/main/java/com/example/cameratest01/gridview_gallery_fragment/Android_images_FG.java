package com.example.cameratest01.gridview_gallery_fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cameratest01.Kids_gallery.GridViewGallery;
import com.example.cameratest01.R;
import com.example.cameratest01.RC_Items.ImageData;
import com.example.cameratest01.RC_Items.ImageFolder;
import com.example.cameratest01.gridview_adapter.Android_Adapter;

import java.util.ArrayList;

public class Android_images_FG extends Fragment {

    RecyclerView recyclerView;
    Android_Adapter adapter;
    GridViewGallery gridViewGallery;
    ArrayList<String> imageStr;
    ArrayList<ImageData> imageDataAC;


    public Android_images_FG(ArrayList<String> imageStr, ArrayList<ImageData> imageDataAC) {
        this.imageStr = imageStr;
        this.imageDataAC = imageDataAC;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gridViewGallery=(GridViewGallery)getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.gridview_fg_android, container, false);
        recyclerView=view.findViewById(R.id.android_rc_image_grid);
        adapter=new Android_Adapter(gridViewGallery, imageDataAC, imageStr);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
