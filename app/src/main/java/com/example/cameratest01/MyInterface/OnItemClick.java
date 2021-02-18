package com.example.cameratest01.MyInterface;

import androidx.recyclerview.widget.RecyclerView;

import com.example.cameratest01.RC_Items.RC_ImageItems;

import java.util.ArrayList;

public interface OnItemClick {
    void onClick(ArrayList<RC_ImageItems> items, int position);
}
