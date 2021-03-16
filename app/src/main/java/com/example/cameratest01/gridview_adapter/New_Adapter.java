package com.example.cameratest01.gridview_adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
    File direct;
//    public int position=0;
    File[] lists;
    ArrayList<ImageData> imageData_test=new ArrayList<>();  //  키즈사랑 갤러리, 기본 갤러리 => 이거로 이미지 출력하기

    ArrayList<ImageData> imageData;  //  전체 데이터 => 이거로 이미지 출력하지 않고 받아오기만 하고 분류는 setFlag에서 하기.....K, D로 분류
    ArrayList<String> imagesArr;     // 기본갤러리 이미지 데이터

    GridViewGallery gridViewGallery;  // GridViewGallery의 Activity 가져옴
    Fragment fragment;// 클릭시 크게 보여주는 프레그먼트
    public int countNumK=0;// 리스트 사이즈
    public int countNumD=0;

    ArrayList<Float> rotNumKidesImg=new ArrayList<>();
    // 타입 추가하기
    public String TYPE="K";// K : 키즈사랑, D : 기본갤러리
    public String SELECT_TYPE="C";// C : 선택으로 택스트 보여질때, S : 전송으로 택스트 보여질때

    public void setType(String TYPE){
        this.TYPE=TYPE;
    }
    public void setCountNumK(int countNum){
        this.countNumK=countNum;
    }
    public void setCountNumD(int countNum){
        this.countNumD=countNum;
    }

    public void setSelectType(String TYPE){
        SELECT_TYPE=TYPE;
        Log.e("Click_Select = ","setSelectType : "+SELECT_TYPE);// 왜 정상적으로 나오는지 확인.
        notifyDataSetChanged();
    }

    public String getSelectType() {
        return SELECT_TYPE;
    }

    public void setItem(ArrayList<ImageData> itemArr){// 이거 하나만 사용하기...
        this.imageData = itemArr;
//        countNum=itemArr.size();// 이거도 변경해야함..
        for (int i = 0; i < imageData.size(); i++) {
            setFlag(imageData.get(i).getImageType());
        }
//        Log.e("inNewAdapterCountNum", " : "+countNum);
    }

    public void setItem(ArrayList<String> imagesArr, ArrayList<ImageData> imageData){// 삭제
        this.imagesArr=imagesArr;
        this.imageData=imageData;
    }

    public void setFlag(String flag){// 이거 하나로 데이터 구분하기...
        Log.e("inSetFlags", " : inFlages - "+flag);
        if (imageData_test!=null){
            imageData_test.clear();
        }
        if(flag.equals("K")){
            for (int i = 0; i < imageData.size(); i++) {
                if(imageData.get(i).imageType.equals("K")){
                    imageData_test.add(imageData.get(i));
//                    countNumK=imageData_test.size();
                }
            }
//            Log.e("inNewAdapterCountNum", " : "+countNum);
//            countNumK=imageData_test.size();
            Log.e("countNumShow", " countNum_K : "+imageData_test.size());
        }
        if (flag.equals("D")){
            for (int i = 0; i < imageData.size(); i++) {
                if(imageData.get(i).imageType.equals("D")){
                    imageData_test.add(imageData.get(i));
//                    countNumD=imageData_test.size();
                }
            }
//            Log.e("inNewAdapterCountNum", " : "+countNum);
//            countNumD=imageData_test.size();
            Log.e("countNumShow", " countNum_D : "+countNumD);
        }
    }

    public New_Adapter(Context context, ArrayList<ImageData> imageData) {// 키즈사랑 갤러리 어뎁터 생성자
        this.context = context;
        this.imageData = imageData;
//        Log.e("inNewAdapterCountNum", " : "+countNumK);

        for (int i = 0; i < imageData.size(); i++) {
            if(imageData.get(i).imageType.equals("K")){
                imageData_test.add(imageData.get(i));
//                    countNumK=imageData_test.size();
            }
        }

        Log.d("imageDataSize", imageData_test.size()+"");

        direct=new File(context.getFilesDir(), "CameraTest01"+File.separator+"kidsLove");
        Log.e("direct File : ", direct.listFiles().length+"");
        if (!direct.exists()) {
            direct.mkdirs();
        }
        lists=direct.listFiles();
        Collections.reverse(Arrays.asList(lists));// 파일 내부에 있는 데이터 역순으로 돌림
        countNumK=lists.length;
//        Collections.reverse(this.imageData);
    }

    public New_Adapter(Context context, ArrayList<ImageData> imageData, ArrayList<String> imagesArr) { // 기본갤러리 어뎁터 생성자
//        Collections.reverse(imageData);
        this.context = context;
        this.imageData = imageData;
        this.imagesArr = imagesArr;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView= LayoutInflater.from(context).inflate(R.layout.grid_image_item, parent, false);
        // 받아온 타입에 따라서 VH의 형태 변경 가능하다.
        VH holder=new VH(itemView);
//        Log.e("startViewNum : ", "onCreateViewHolder");
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.e("Click_S = ", "0>  Gallery : "+TYPE+" |select : "+this.SELECT_TYPE);
        Log.e("typeVal", "- inBind : "+TYPE);
        Log.e("Click_S", " : inBind : "+SELECT_TYPE);
        VH vh= (VH) holder;
        GridViewGallery activity=(GridViewGallery) context;

        fragment=new Clicked_BigImage_FG();
        Log.e("Click_S = ", "1>  Gallery : "+TYPE+" |select : "+this.SELECT_TYPE);

        if (TYPE.equals("K")){// 키즈사랑 갤러리 이미지 리스트 보여주는 부분
            try {
                    if (imageData.size()<=0 && imageData.get(position).getRotateNum()==0){
                        imageData.get(position).ImageName=lists[position].getName();
                        imageData.get(position).ImagePath=lists[position].getAbsolutePath();
                    }
                    Glide.with(context)
                            .load(lists[position])
                            .transform(new RotateTransformation(imageData.get(position).getRotateNum()))
                            .into(vh.iv);

//                    File file=new File(direct, imageData.get(position).ImageName);
//                    Bitmap bitmap= BitmapFactory.decodeFile(file.getAbsolutePath());
//                    Matrix matrix=new Matrix();
//                    matrix.postRotate(imageData.get(position).getOrirotateNum());
//                    bitmap=Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix,true);
//                    vh.iv.setImageBitmap(bitmap);
//                속도가 무지 느려짐...

//                    Log.e("activity_data_show", " - degree : "+activity.direct.toString());
//                    Log.e("activity_data_show", " - degree : "+activity.direct.list()[position]);

                }catch (Exception e){
                    e.printStackTrace();
                }


        }else if (TYPE.equals("D")){// 기본갤러리 이미지 리스트 보여주는 부분
//            Collections.reverse(imagesArr);
            try {
                Glide.with(context)
                        .load(imagesArr.get(position))
                        .transform(new RotateTransformation(imageData.get(position).getOrirotateNum()))// imageData대신 test출력함
                        .placeholder(R.drawable.ic_baseline_no_image_24).into(vh.iv);// 사진보여줌.
                Log.e("getOrirotateNum", " - "+imageData.get(position).getOrirotateNum()+" / name : "+imageData.get(position).getImageName());
            }catch (Exception e){
//                Glide.with(context).load()
            }

        }


        if (getSelectType().equals("C")){// 선택 일때 이미지 클릭 리스너들..
            Log.e("Click_S", " : inC : "+SELECT_TYPE);
            vh.check.setVisibility(View.GONE);
            vh.zoom.setVisibility(View.GONE);

            vh.iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 1. 클릭시 뷰페이저 프레그먼트 보여줌
//                Toast.makeText(context, "이미지 확대", Toast.LENGTH_SHORT).show();
                    Log.e("showPositionInAdapter", "  po : "+position);
                    // viewpager fragmet로 작업하기. + adapter
                    Bundle bundle=new Bundle();
                    if (TYPE.equals("D")){
                        bundle.putInt("position", position);
                        bundle.putParcelableArrayList("AC_imageData", imageData);// AC_imageData
                        bundle.putStringArrayList("images", imagesArr);
                    }else {
                        bundle.putInt("position", position);
                        bundle.putParcelableArrayList("imageData", imageData_test);
                    }

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
//                    Menu menu=gridViewGallery.toolbar.getMenu();
//                    menu.removeGroup(R.id.grid_toolbar_menu);// 메뉴버튼 지우지 않고 안보이게 변경
                    // 툴바 메뉴 안보이게 변경.
                    gridViewGallery.sendMenu.setVisible(false);
                    gridViewGallery.deletMenu.setVisible(false);
                    gridViewGallery.cancelMeun.setVisible(false);

//                menu.add(R.id.bigimg_click_menu, R.id.rotate, 0, "");
//                gridViewGallery.toolbar.inflateMenu(R.menu.fg_bigimg_clicked_menu);
                    gridViewGallery.zoomOut=0;
                    Log.e("show_position : _ ", "bundle"+position);
                }
            });

        }
        if (getSelectType().equals("S")){// 전송일때 이미지 클릭 이벤트들
            Log.e("Click_S", " : inS : "+SELECT_TYPE);
            vh.check.setVisibility(View.VISIBLE);
            vh.zoom.setVisibility(View.VISIBLE);

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
                    Log.e("showPositionInAdapter", "  po : "+position);
                    // viewpager fragmet로 작업하기. + adapter
                    Bundle bundle=new Bundle();
                    if (TYPE.equals("D")){// 기본갤러리일때 보낼 데이터
                        bundle.putInt("position", position);
                        bundle.putParcelableArrayList("AC_imageData", imageData);// AC_imageData
                        bundle.putStringArrayList("images", imagesArr);
                    }else {// 키즈사랑갤러리일때 보낼 데이터
                        bundle.putInt("position", position);
                        bundle.putParcelableArrayList("imageData", imageData_test);
                    }

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
//                    Menu menu=gridViewGallery.toolbar.getMenu();
//                    menu.removeGroup(R.id.grid_toolbar_menu);

                    gridViewGallery.sendMenu.setVisible(false);
                    gridViewGallery.deletMenu.setVisible(false);
                    gridViewGallery.cancelMeun.setVisible(false);

//                menu.add(R.id.bigimg_click_menu, R.id.rotate, 0, "");
//                gridViewGallery.toolbar.inflateMenu(R.menu.fg_bigimg_clicked_menu);
                    gridViewGallery.zoomOut=0;
                    Log.e("show_position : _ ", "bundle"+position);
                }// onclick_imageview
            });
        }


    }// onbindviewholder...


    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {// 화면에서 보여지지 않게 되는 아이템
        super.onViewDetachedFromWindow(holder);
    }


    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {// 리사이클러 재활용
//        VH vh= (VH) holder;
//        Log.e("startViewNum : ", "onViewRecycled");
//        File[] lists=direct.listFiles();
//        Collections.reverse(Arrays.asList(lists));
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

//        if (TYPE.equals("K")){
////            Log.e("typeVal", "- inCount_I : "+TYPE+" : "+countNumK);
////            return this.countNumK;
//            if (countNumK==0){
//                return imageData.size();
//            }else {
//                return countNumK;
//            }
//        }else {
//            if (countNumD!=0){
//                return countNumD;
//            }
//            Log.e("typeVal", "- inCount_E : "+TYPE+" : "+countNumD);
//            GridViewGallery ac=(GridViewGallery)context;
//            return ac.imagesArr.size();
//        }
        return imageData_test.size();
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

