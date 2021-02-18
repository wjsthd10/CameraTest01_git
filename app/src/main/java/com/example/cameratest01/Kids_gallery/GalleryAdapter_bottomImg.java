package com.example.cameratest01.Kids_gallery;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cameratest01.MyInterface.OnClickDelete_Image;
import com.example.cameratest01.MyInterface.OnClick_ImageSet;
import com.example.cameratest01.R;
import com.example.cameratest01.RC_Items.RC_ImageItems;

import java.io.File;
import java.util.ArrayList;

public class GalleryAdapter_bottomImg extends RecyclerView.Adapter implements OnClickDelete_Image {

    Context context;
    ArrayList<String> fileName;

    File direct;
    String[] fileListArr;
    int position=0;
    File[] lists;

    public GalleryAdapter_bottomImg() {
    }

    public GalleryAdapter_bottomImg(Context context, ArrayList<String> fileName) {
        this.context = context;
        this.fileName = fileName;

        direct=new File(context.getFilesDir(), "CameraTest01"+File.separator+"kidsLove");
        if (!direct.exists()) {
            if (!direct.mkdirs()) {
                Log.e("TAG", "failed to create directiory");
            }
        }
        fileListArr=direct.list();
    }


    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        VH vh= (VH) holder;
        position=vh.getLayoutPosition();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView= LayoutInflater.from(context).inflate(R.layout.rc_gallery_bottom_img, parent, false);
        VH holder=new VH(itemView);

        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VH vh= (VH) holder;
//        Glide.with(context).load(bytes.get(position)).into(vh.imageView);

        File[] list=direct.listFiles();
        try{
            Glide.with(context).load(list[position]).into(vh.imageView);
        }catch (Exception e){
            Toast.makeText(context, e+"", Toast.LENGTH_SHORT);
        }
        if (Build.VERSION.SDK_INT<= Build.VERSION_CODES.LOLLIPOP){
            vh.imageView.setRotation(90);
        }
//        vh.imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.e("position", position+"");
//                GalleryAdapter mAdapter=new GalleryAdapter();
//                GalleryAdapter.VH.class.getMethods();
//
//            }
//        });
    }

    @Override
    public int getItemCount() {
//        return items.size();
        return fileListArr.length;
    }

    @Override
    public void onClick(int position) {
//        lists=direct.listFiles();
//        lists[this.position].delete();
        notifyItemRemoved(this.position);
    }

    class VH extends RecyclerView.ViewHolder{

        ImageView imageView;

        public VH(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.rc_gallery_bottomImg);
        }
    }



}
