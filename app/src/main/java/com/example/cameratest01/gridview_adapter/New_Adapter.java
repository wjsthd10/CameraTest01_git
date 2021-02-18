package com.example.cameratest01.gridview_adapter;

import android.content.Context;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.exifinterface.media.ExifInterface;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.cameratest01.Kids_gallery.GalleryActivity;
import com.example.cameratest01.Kids_gallery.GridViewGallery;
import com.example.cameratest01.MyInterface.PagerImageDelete;
import com.example.cameratest01.R;
import com.example.cameratest01.RC_Items.ImageData;
import com.example.cameratest01.RotateTransformation;
import com.example.cameratest01.gallery_clicked.Clicked_BigImage_FG;
import com.example.cameratest01.gallery_clicked.Clicked_adapter;
import com.squareup.picasso.Picasso;
//import com.example.cameratest01.fragments.GalleryClicked_Image_FG;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import de.hdodenhof.circleimageview.CircleImageView;

public class New_Adapter extends RecyclerView.Adapter implements PagerImageDelete {

    Context context;
    ArrayList<String> fileNames;

    File direct;
    String[] fileListArr;
    public int position=0;
    ArrayList<Integer> positions=new ArrayList<>();
//    ArrayList<ImageData> imageData=new ArrayList<>();
    ArrayList<ImageData> imageData;  //  전체 데이터
    GridViewGallery gridViewGallery;
    Fragment fragment;
    int countNum=0;

    ArrayList<Float> rotNumKidesImg=new ArrayList<>();


    public void setItem(ArrayList<ImageData> itemArr){
        this.imageData = itemArr;
        countNum=itemArr.size();
    }

    public New_Adapter(Context context, ArrayList<ImageData> imageData) {
        this.context = context;
        this.imageData = imageData;

        direct=new File(context.getFilesDir(), "CameraTest01"+File.separator+"kidsLove");
        Log.e("direct File : ", direct.listFiles().length+"");
        if (!direct.exists()) {
            if (!direct.mkdirs()) {
                Log.d("TAG", "failed to create directory");
            }
        }
    }

//        public New_Adapter(Context context, ArrayList<String> fileNames) {// 1
//        this.context = context;
//        this.fileNames = fileNames;
//        direct=new File(context.getFilesDir(), "CameraTest01"+File.separator+"kidsLove");
//        Log.e("direct File : ", direct.listFiles().length+"");
//        if (!direct.exists()) {
//            if (!direct.mkdirs()) {
//                Log.d("TAG", "failed to create directory");
//            }
//        }
//        fileListArr=direct.list();
//        File[] lists=direct.listFiles();
//        for (int i = 0; i < fileListArr.length; i++) {
//            imageData.add(new ImageData(lists[i].getName(), lists[i].getAbsolutePath(), 0, i));
//        }
//    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView= LayoutInflater.from(context).inflate(R.layout.grid_image_item, parent, false);
        VH holder=new VH(itemView);
//        Log.e("startViewNum : ", "onCreateViewHolder");
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VH vh= (VH) holder;
        positions.add(position);

        GridViewGallery activity=(GridViewGallery) context;

        fragment=new Clicked_BigImage_FG();
        File[] lists=direct.listFiles();
        Collections.reverse(Arrays.asList(lists));// 파일 내부에 있는 데이터 역순으로 돌림

        Matrix matrix=new Matrix();



        try {
            if (imageData.size()<=0 && imageData.get(position).rotateNum==0){
                imageData.get(position).ImageName=lists[position].getName();
                imageData.get(position).ImagePath=lists[position].getAbsolutePath();

            }

//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                rotNumKidesImg=gridViewGallery.getSharedPreferences();
//            }


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                ArrayList<Float> floats=new ArrayList<>();
                floats=activity.getSharedPreferences();
                Glide.with(context).load(lists[position]).transform(new RotateTransformation(floats.get(position))).into(vh.iv);
                Log.e("floats", floats.toString());
            }else {
                Glide.with(context).load(lists[position]).transform(new RotateTransformation(imageData.get(position).rotateNum)).into(vh.iv);
            }
//          // shared
//          Glide.with(context).load(lists[position]).transform(new RotateTransformation(imageData.get(position).rotateNum)).into(vh.iv);
//            Glide.with(context).load(lists[position]).transform(new RotateTransformation(0)).into(vh.iv);

//          Log.e("rotateNum_Test : ", " - "+imageData.get(position).rotateNum);
          Log.e("activity_data_show", " - degree : "+activity.degree);
          Log.e("activity_data_show", " - degree : "+activity.direct.toString());
          Log.e("activity_data_show", " - degree : "+activity.direct.list()[position]);

      }catch (Exception e){
          e.printStackTrace();
      }
//        imageData.get(position).ImageName=lists[position].getName();
//        imageData.get(position).ImagePath=lists[position].getAbsolutePath();
//        Glide.with(context).load(lists[position]).into(vh.iv);
        // todo 받아올때도 데이터 역순으로 돌려야함.
        if (imageData.get(position).Check==1){// 어차피 뷰를 묶어서 보여주려면 지나야 하는 구간임 재활용 되고 다시 실행됨....
            Glide.with(context).load(R.drawable.ic_check_circle_w_24).into(vh.check);
        }else if (imageData.get(position).Check==0){
            Glide.with(context).load(R.drawable.ic_check_circle_b_24).into(vh.check);
        }

        vh.iv.setOnClickListener(new View.OnClickListener() {// 이미지 클릭하여 선택 버튼
            @Override
            public void onClick(View v) {

                if (imageData.get(position).Check==0){
                    Glide.with(context).load(R.drawable.ic_check_circle_w_24).into(vh.check);
                    imageData.get(position).Check=1;
//                    Log.e("Click_", "imageData_check : "+imageData.get(position).Check+" : "+position);
                    imageData.get(position).Position=position;
                }else if (imageData.get(position).Check==1){
                    Glide.with(context).load(R.drawable.ic_check_circle_b_24).into(vh.check);
                    imageData.get(position).Check=0;
//                    Log.e("Click_", "imageData_check : "+imageData.get(position).Check);
                }
            }
        });

        vh.zoom.setOnClickListener(new View.OnClickListener() {// 사진 확대 버튼 클릭
            @Override
            public void onClick(View v) {// 1. 클릭시 뷰페이저 프레그먼트 보여줌
//                Toast.makeText(context, "이미지 확대", Toast.LENGTH_SHORT).show();
                // viewpager fragmet로 작업하기. + adapter
                Bundle bundle=new Bundle();
                bundle.putInt("position", position);
                bundle.putParcelableArrayList("imageData", imageData);

                gridViewGallery=(GridViewGallery)v.getContext();
                fragment.setArguments(bundle);
                FragmentManager manager=gridViewGallery.getSupportFragmentManager();
                FragmentTransaction transaction=manager.beginTransaction();
                transaction.addToBackStack(null);
                transaction.replace(R.id.pager_gridview, fragment);
                transaction.commit();
                // 툴바 설정 변경해야함.

                gridViewGallery.bnv.setVisibility(View.GONE);
                gridViewGallery.bottomNav.setVisibility(View.VISIBLE);
                gridViewGallery.toolbar.setBackgroundColor(ContextCompat.getColor(gridViewGallery, R.color.custom_toolbar_c));
                gridViewGallery.toolbar.setTitleTextColor(ContextCompat.getColor(gridViewGallery,R.color.custom_toolbar_c));
                // 메뉴 버튼 변경
                Menu menu=gridViewGallery.toolbar.getMenu();
                menu.removeGroup(R.id.grid_toolbar_menu);
//                menu.add(R.id.bigimg_click_menu, R.id.rotate, 0, "");
//                gridViewGallery.toolbar.inflateMenu(R.menu.fg_bigimg_clicked_menu);
                gridViewGallery.zoomOut=0;
                Log.e("show_position : _ ", "bundle"+position);
            }// onclick_imageview
        });
    }// onbindviewholder...


    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {// 화면에서 보여지지 않게 되는 아이템
        super.onViewDetachedFromWindow(holder);
//        Log.e("startViewNum : ", "onViewDetachedFrom");
//        Log.e("Detached : ", holder.getAdapterPosition()+"");
//
////        Log.e("re_test : ", "D_adapterPos_D  "+holder.getAdapterPosition());
//        Log.e("re_test : ", "D_layoutPos_D   "+holder.getLayoutPosition());
    }


    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {// 리사이클러 재활용
        VH vh= (VH) holder;
//        Log.e("startViewNum : ", "onViewRecycled");
        File[] lists=direct.listFiles();
        Collections.reverse(Arrays.asList(lists));
//        Log.e("view_recycled_test", holder.getAdapterPosition()+"번째 재활용");
//        if (clickListStr.contains(lists[holder.getAdapterPosition()].getName())){
//            Glide.with(context).load(R.drawable.ic_check_circle_w_24).into(vh.check);
//        }else if (!clickListStr.contains(lists[holder.getAdapterPosition()].getName())){
//            Glide.with(context).load(R.drawable.ic_check_circle_b_24).into(vh.check);
//        }
//        if (imageData.get(holder.getLayoutPosition()).Check==0){
//            Glide.with(context).load(R.drawable.ic_check_circle_b_24).into(vh.check);
//        }else if (imageData.get(holder.getLayoutPosition()).Check==1){
//            Glide.with(context).load(R.drawable.ic_check_circle_w_24).into(vh.check);
//        }
//        Log.e("re_test : ", "_adapterPos_   "+holder.getAdapterPosition());
//        Log.e("re_test : ", "_layoutPos_    "+holder.getLayoutPosition());
//        Log.e("lists.length : ", lists.length+"");
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        if (countNum==0){
            return imageData.size();
        }else {
            return countNum;
        }

    }

    @Override
    public void imgDelete(int position) {
        notifyItemRemoved(position);
    }

    class VH extends RecyclerView.ViewHolder{

        ImageView iv;
        LinearLayout zoom;
        CircleImageView check;

        public VH(@NonNull View itemView) {
            super(itemView);

            iv=itemView.findViewById(R.id.rc_grid_img);
            zoom=itemView.findViewById(R.id.rc_grid_img_zoom);
            check=itemView.findViewById(R.id.rc_check_img);
        }
    }

}

