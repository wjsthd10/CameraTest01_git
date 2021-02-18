package com.example.cameratest01.gridview_adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cameratest01.Kids_gallery.GridViewGallery;
import com.example.cameratest01.MyInterface.PagerImageDelete;
import com.example.cameratest01.R;
import com.example.cameratest01.RC_Items.ImageData;
import com.example.cameratest01.RC_Items.ImageFolder;
import com.example.cameratest01.RotateTransformation;
import com.example.cameratest01.gallery_clicked.Clicked_BigImage_FG;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Android_Adapter extends RecyclerView.Adapter implements PagerImageDelete {

    Context context;
    ArrayList<ImageData> imageData;
    File direct;//
    GridViewGallery gridViewGallery;
    ArrayList<String> images;
    ArrayList<Float> rotNumAllImg;

    String pathUri;


    public Android_Adapter(Context context, ArrayList<ImageData> imageData, ArrayList<String> images) {
        this.context = context;
        this.imageData = imageData;
        this.images = images;
    }

    public void setItem(ArrayList<String> images, ArrayList<ImageData> imageData){
        this.images=images;
        this.imageData=imageData;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        gridViewGallery=(GridViewGallery)parent.getContext();
        View itemView= LayoutInflater.from(context).inflate(R.layout.grid_image_item, parent,false);
        VH holder=new VH(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VH vh=(VH)holder;
        Log.d("fileDataShow", "lists - "+images.get(position));

//        Collections.reverse(images);

        Glide.with(context).load(images.get(position)).transform(new RotateTransformation(0)).placeholder(R.drawable.ic_baseline_no_image_24).into(vh.iv);// 사진보여줌.
//        Glide.with(context).load(images.get(position)).transform(new RotateTransformation(imageData.get(position).rotateNum)).placeholder(R.drawable.ic_baseline_no_image_24).into(vh.iv);// 사진이 갖고있는 회전각도 적용
        if (imageData.get(position).Check == 1) {
            Glide.with(context).load(R.drawable.ic_check_circle_w_24).into(vh.check);
        }else if (imageData.get(position).Check==0){
            Glide.with(context).load(R.drawable.ic_check_circle_b_24).into(vh.check);
        }

        Log.e("ShowSize : ","imageData - "+imageData.size());// 6
        Log.e("ShowSize : ","images - "+images.size());

        vh.iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageData.get(position).Check == 0) {
                    Glide.with(context).load(R.drawable.ic_check_circle_w_24).into(vh.check);
                    imageData.get(position).Check=1;
                } else if (imageData.get(position).Check==1) {
                    Glide.with(context).load(R.drawable.ic_check_circle_b_24).into(vh.check);
                    imageData.get(position).Check=0;
                }
            }
        });

        vh.zoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gridViewGallery.clickZoomAndroid=1;
                Bundle bundle=new Bundle();
                bundle.putInt("position", position);
                bundle.putParcelableArrayList("AC_imageData", imageData);// AC_imageData
                bundle.putStringArrayList("images", images);

                Fragment fragment=new Clicked_BigImage_FG();
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
            }
        });

    }

    @Override
    public int getItemCount() {
        return images.size();
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
//
//    public ArrayList<String> getAllShownImagesPath(){
//        Uri uri;
//        Cursor cursor;
//        int column_index_data, column_index_folder_name;
//        ArrayList<String> listOfAllImages=new ArrayList<>();
//        String absolutePathOfImage=null;
//        uri=MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//
//        String[] projection={MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
//        cursor=gridViewGallery.getContentResolver().query(uri, projection, null, null, null);
//        column_index_data=cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
//        column_index_folder_name=cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
//
//        while (cursor.moveToNext()){
//            absolutePathOfImage=cursor.getString(column_index_data);
//            listOfAllImages.add(absolutePathOfImage);
//        }
//        return listOfAllImages;
//    }

//    public ArrayList<ImageFolder> getImageFolders(){
//        ArrayList<ImageFolder> picFolders=new ArrayList<>();
//        ArrayList<String> picPaths=new ArrayList<>();
//        Uri allImagesUri=MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//        String[] projection={MediaStore.Images.ImageColumns.DATA, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.BUCKET_ID};
//        Cursor cursor=gridViewGallery.getContentResolver().query(allImagesUri, projection, null, null, null);
//
//        try {
//            while (cursor.moveToNext()){
//                ImageFolder folder=new ImageFolder();
//                String name=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME));
//                String folderName=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
//                String dataPath=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
//
//                String folderPaths=dataPath.substring(0, dataPath.lastIndexOf(folderName+"/"));
//                folderPaths=folderPaths+folderName+"/";
//                if (!picPaths.contains(folderPaths)){
//                    picPaths.add(folderPaths);
//                    folder.setPath(folderPaths);
//                    folder.setFolderName(folderName);
//                    folder.setFirstPic(dataPath);
//                    folder.addPics();
//                    picFolders.add(folder);
//                }else {
//                    for (int i = 0; i < picFolders.size(); i++) {
//                        if (picFolders.get(i).getPath().equals(folderPaths)){
//                            picFolders.get(i).setFirstPic(dataPath);
//                            picFolders.get(i).addPics();
//                        }
//                    }
//                }
//            }
//            cursor.close();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//        for (int i = 0; i < picFolders.size(); i++) {
//            Log.d("picture_folders", " - name / "+picFolders.get(i).getFolderName()+" / path / "+picFolders.get(i).getPath()+" / "+picFolders.get(i).getNumberOfPics());
//        }
//
//        return picFolders;
//    }

}































