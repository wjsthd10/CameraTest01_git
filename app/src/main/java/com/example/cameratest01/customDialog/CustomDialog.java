package com.example.cameratest01.customDialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.cameratest01.R;
import com.example.cameratest01.RC_Items.RC_ImageItems;

import java.util.ArrayList;

public class CustomDialog extends AlertDialog {

    public int postion;
    public ArrayList<RC_ImageItems> items;
    public ImageView iv;


    public CustomDialog(Context context, int postion, ArrayList<RC_ImageItems> items) {
        super(context);
        this.postion = postion;
        this.items = items;
    }

    public CustomDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog_lay);
        iv.findViewById(R.id.custom_image);
        Glide.with(getContext()).load(items.get(postion).imageBM).into(iv);




    }
}
