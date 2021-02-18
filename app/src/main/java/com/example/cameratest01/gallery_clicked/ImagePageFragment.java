package com.example.cameratest01.gallery_clicked;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.exifinterface.media.ExifInterface;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.cameratest01.Kids_gallery.GridViewGallery;
import com.example.cameratest01.R;
import com.example.cameratest01.RC_Items.ImageData;
import com.example.cameratest01.RotateTransformation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ImagePageFragment extends Fragment {// 뷰페이저에 보여지는 이미지

    File direct;
    Context context;
    File[] lists;
    ArrayList<ImageData> imageData;
    ArrayList<ImageData> AC_imageData;
    int position;
    int degree;
    GridViewGallery gridViewGallery;
    ImageView iv;
    ArrayList<String> images;

    public ImagePageFragment() {
    }

    public ImagePageFragment(Context context, ArrayList<ImageData> imageData, int position) {
        this.context = context;
        this.imageData = imageData;
        this.position = position;
    }

    public ImagePageFragment(Context context, ArrayList<ImageData> imageData, int position, ArrayList<String> images) {
        this.context = context;
        this.AC_imageData = imageData;
        this.position = position;
        this.images = images;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        direct=new File(context.getFilesDir(), "CameraTest01"+File.separator+"kidsLove");
//        Log.e("direct File : ", direct.listFiles().length+"");
//        if (!direct.exists()) {
//            if (!direct.mkdirs()) {
//                Log.d("TAG", "failed to create directory");
//            }
//        }
//        lists=direct.listFiles();  // 파일 출력못함.
        return inflater.inflate(R.layout.viewpager_bigimage, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.e("showPositions", " : "+position);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        gridViewGallery=(GridViewGallery)view.getContext();
        context=gridViewGallery;
        iv=view.findViewById(R.id.image_pager);
        ArrayList<Float> floats=new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            floats=gridViewGallery.getSharedPreferences();
//            Glide.with(context).load(lists[position]).transform(new RotateTransformation(floats.get(position))).into(iv);
//            Log.e("floats", floats.toString());
        }

        if (gridViewGallery.returnVal){// 회전눌렀을때 이미지 표시...
//            Toast.makeText(context, "test", Toast.LENGTH_SHORT).show();
//            Log.e("rotChac", "returnVal in");
            Bundle bundle=getArguments();
            imageData=bundle.getParcelableArrayList("imageData");
            AC_imageData=bundle.getParcelableArrayList("AC_imageData");
            position=bundle.getInt("position");
            degree=bundle.getInt("degree");
            images=bundle.getStringArrayList("images");
//            iv.setRotation(degree);

            // 여기에 회전값 넣어서 이미지 보여주기...
            if (AC_imageData!=null && images !=null){
                Log.e("ImgFGpager_Test", ", i : "+AC_imageData.get(position).rotateNum);
                if (AC_imageData.size()<=position) position=position-1;
//                Glide.with(context).load(images.get(position)).transform(new RotateTransformation(AC_imageData.get(position).rotateNum)).into(iv);
                Glide.with(context).load(images.get(position)).transform(new RotateTransformation(gridViewGallery.degree)).into(iv);
//                Glide.with(context).load(images.get(position)).transform(new RotateTransformation(270)).into(iv);
            }else if (imageData!=null){
                Log.e("ImgFGpager_Test", ", i : "+imageData.get(position).rotateNum);
                if (imageData.size()<=position) position=position-1;
//                Glide.with(context).load(imageData.get(position).ImagePath).transform(new RotateTransformation(imageData.get(position).rotateNum)).into(iv);// 270으로 회전시킨 이미지만 보여지지 않음....
                Glide.with(context).load(imageData.get(position).ImagePath).transform(new RotateTransformation(degree)).into(iv);
            }

        }else {// 회전안눌렀을때 이미지 표시...
//            Log.e("rotChac", "returnVal else");
            if (AC_imageData!=null && images!=null){// 기본갤러리 데이터있을때
                if (AC_imageData.size()<=position) position=position-1;
                Glide.with(context).load(images.get(position)).transform(new RotateTransformation(0)).into(iv);
//                try {
//                    ExifInterface exif=new ExifInterface(images.get(position));
//                    int orientation=exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
//                    Log.e("ExifInterface_Int", "name : "+images.get(position)+" + "+position+"");
//                    Log.e("ExifInterface_Int", "name : "+AC_imageData.get(position).rotateNum);
//                    switch (orientation){
//                        case ExifInterface.ORIENTATION_ROTATE_90:{//==6
//                            AC_imageData.get(position).rotateNum=90;
//                            Glide.with(context).load(images.get(position)).transform(new RotateTransformation(AC_imageData.get(position).rotateNum)).into(iv);
//                        }break;
//                        case ExifInterface.ORIENTATION_ROTATE_180:{//==3
//                            AC_imageData.get(position).rotateNum=180;
//                            Glide.with(context).load(images.get(position)).transform(new RotateTransformation(AC_imageData.get(position).rotateNum)).into(iv);
//                        }break;
//                        case ExifInterface.ORIENTATION_ROTATE_270:{//==8
//                            AC_imageData.get(position).rotateNum=270;
//                            Glide.with(context).load(images.get(position)).transform(new RotateTransformation(AC_imageData.get(position).rotateNum)).into(iv);
//                        }break;
//                        default:{
//                            AC_imageData.get(position).rotateNum=0;
//                            Glide.with(context).load(images.get(position)).transform(new RotateTransformation(AC_imageData.get(position).rotateNum)).into(iv);
//                        }break;
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }else if (imageData!=null){
                Log.e("flatRotateKideShow", " pagerin = "+gridViewGallery.switchNum);
                if (imageData.size()<=position) position=position-1;// 페이저에서 이미지 삭제할때 마지막 이미지 삭제하면 index error나옴
//                Glide.with(context).load(imageData.get(position).ImagePath).transform(new RotateTransformation(imageData.get(position).rotateNum)).into(iv);
//                try {
//                    ExifInterface exif=new ExifInterface(imageData.get(position).ImagePath);
//                    int orientation=exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
//                    switch (orientation){
//                        case ExifInterface.ORIENTATION_ROTATE_90:{
//                            imageData.get(position).rotateNum=90;
//                            Glide.with(context).load(imageData.get(position).ImagePath).transform(new RotateTransformation(imageData.get(position).rotateNum)).into(iv);
//                        }break;
//                        case ExifInterface.ORIENTATION_ROTATE_180:{
//                            imageData.get(position).rotateNum=180;
//                            Glide.with(context).load(imageData.get(position).ImagePath).transform(new RotateTransformation(imageData.get(position).rotateNum)).into(iv);
//                        }break;
//                        case ExifInterface.ORIENTATION_ROTATE_270:{
//                            imageData.get(position).rotateNum=270;
//                            Glide.with(context).load(imageData.get(position).ImagePath).transform(new RotateTransformation(imageData.get(position).rotateNum)).into(iv);
//                        }break;
//                        default:{
//                            imageData.get(position).rotateNum=0;
//                            Glide.with(context).load(imageData.get(position).ImagePath).transform(new RotateTransformation(imageData.get(position).rotateNum)).into(iv);
//                        }break;
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                Glide.with(context).load(imageData.get(position).ImagePath).transform(new RotateTransformation(floats.get(position))).into(iv);// sharedPreferences에서 받아온 회전값 사용.
            }

//        iv.setImageResource(R.drawable.ic_check_circle_b_24);
//        Log.e("image_path_test : ", "Path - "+ imageData.get(position).ImagePath);
//        if (imageData.size()<=position) position=position-1;// 페이저에서 이미지 삭제할때 마지막 이미지 삭제하면 index error나옴
        // NullPointerException bundle에서 다른 arrlist받아와야함

//            Glide.with(context).load(imageData.get(position).ImagePath).transform(new RotateTransformation(imageData.get(position).rotateNum)).into(iv);
        }

//        // 순서 설정....
//        gridViewGallery=(GridViewGallery)view.getContext();
    }





}
