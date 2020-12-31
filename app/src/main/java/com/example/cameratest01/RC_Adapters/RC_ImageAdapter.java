package com.example.cameratest01.RC_Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
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
import com.example.cameratest01.R;
import com.example.cameratest01.RC_Items.RC_ImageItems;
import com.example.cameratest01.customDialog.CustomDialog;
import com.example.cameratest01.fragments.BigImageFragment;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class RC_ImageAdapter extends RecyclerView.Adapter{


    ArrayList<RC_ImageItems> items;
    Bundle bundle=new Bundle();
//    DialogFragment bigImageFragment=new BigImageFragment();
    CameraActivity_test mActivity;
    Context context;

    CustomDialog customDialog;


    public RC_ImageAdapter(Context context, ArrayList<RC_ImageItems> items) {
        this.context=context;
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView= LayoutInflater.from(context).inflate(R.layout.rc_image_item, parent, false);
        VH_images holder=new VH_images(itemView);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VH_images vh= (VH_images) holder;


        Glide.with(context).load(items.get(position).imageBM).into(vh.iv);
        vh.tv.setText(items.get(position).imageName);

        vh.lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "이미지 셈플 클릭", Toast.LENGTH_SHORT).show();
                // todo 이미지 셈플 클릭시 프레그먼트 다이얼로그형식으로 이미지 확대하여 보여주기. 상단 구석에 x모양으로 삭제 여부 간단하게 표시
//                ByteArrayOutputStream stream =new ByteArrayOutputStream();
//                // 스트림 열고 변환하는 과정에서 느림. 스레드 만들어서 따로 돌려야할듯
//                // 프레그먼트 열기
//                items.get(position).imageBM.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                byte[] byteArray=stream.toByteArray();
//                bundle.putByteArray("image", byteArray);
//                bundle.putInt("position",position);
//                bigImageFragment.setArguments(bundle);
//
//                mActivity= (CameraActivity_test) context;
//                FragmentTransaction transaction=mActivity.getSupportFragmentManager().beginTransaction();
//                bigImageFragment.onCreateDialog(bundle).show();
//                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                transaction.addToBackStack(null);
////                transaction.add(bigImageFragment, "tag");
//                transaction.commit();

//                ImageView imageView=mActivity.findViewById(R.id.bigImage_D);
//                AlertDialog.Builder builder=new AlertDialog.Builder(context);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    builder.setView(R.layout.bigimage_fg);
//                }
//                builder.create().show();
                Dialog dialog=new Dialog(context);
                dialog.setContentView(R.layout.custom_dialog_lay);
                customDialog.iv.findViewById(R.id.custom_image);
                Glide.with(context).load(items.get(position).imageBM).into(customDialog.iv);
                dialog.show();

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
