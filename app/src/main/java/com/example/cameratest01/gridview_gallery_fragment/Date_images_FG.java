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

import java.util.ArrayList;

public class Date_images_FG extends Fragment {

    RecyclerView recyclerView;
    GridViewGallery gridViewGallery;
    ArrayList<ImageData> imageData;

    public Date_images_FG(ArrayList<ImageData> imageData) {
        this.imageData = imageData;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        try {
            super.onCreate(savedInstanceState);
            gridViewGallery=(GridViewGallery)getActivity();
        }catch (Exception e){

        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.gridview_fg_date, container, false);

        return view;
    }
}
