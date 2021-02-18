package com.example.cameratest01.Kids_gallery;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.exifinterface.media.ExifInterface;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.RecoverableSecurityException;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.PermissionRequest;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cameratest01.R;
import com.example.cameratest01.RC_Items.ImageData;
import com.example.cameratest01.RC_Items.ImageFolder;
import com.example.cameratest01.customViewPager.StopPagingViewPager;
import com.example.cameratest01.gallery_clicked.Clicked_BigImage_FG;
import com.example.cameratest01.gallery_clicked.ImagePageFragment;
import com.example.cameratest01.gridview_adapter.Android_Adapter;
import com.example.cameratest01.gridview_adapter.Date_Adapter;
import com.example.cameratest01.gridview_adapter.New_Adapter;
import com.example.cameratest01.gridview_gallery_fragment.Android_images_FG;
import com.example.cameratest01.gridview_gallery_fragment.Date_images_FG;
import com.example.cameratest01.gridview_gallery_fragment.New_images_FG;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Permission;
import java.security.Permissions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import okhttp3.internal.Util;

public class GridViewGallery extends AppCompatActivity {

    public Toolbar toolbar;
    public ActionBar actionBar;
    ArrayList<String> strArr;
    public Fragment[] fragments=new Fragment[3];
    FragmentManager manager;
    public BottomNavigationView bnv;
//    public ViewPager viewPager;// todo 화면 전환 중지를 위해서 커스텀 뷰페이저로 바꿔보기.
    public StopPagingViewPager viewPager;
    public int pagePosition;

    New_Adapter new_adapter;
    Android_Adapter android_adapter;
    Date_Adapter date_adapter;
    public ConstraintLayout constraintLayout;

    public File direct;

    File[] lists;
    public LinearLayout bottomNav;
    public ImageView checkImgView;

    int checkedNum=0;
    public int rotClickedNum=0;
    public boolean returnVal=false;
    public ImageView lockImg;
    public int degree=0;
    boolean rotSaveOut=false;
    int outNum=0;
    public int zoomOut=0;
    public int topNavClickNum=0;

    public ArrayList<Float> flatRotate=new ArrayList<>();
    public ArrayList<Float> flatRotateKide=new ArrayList<>();

    ArrayList<ImageData> imageData=new ArrayList<>();
    public ArrayList<ImageData> AC_imageData=new ArrayList<>();

    public ArrayList<String> images=new ArrayList<>();

    public int clickZoomAndroid=0;
    public int switchNum;
//    public ArrayList<Float> floats=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        direct=new File(getFilesDir(), "CameraTest01"+File.separator+"kidsLove");// 키즈사랑 갤러리 이미지 받아오는곳

//        Log.e("direct File : ", getFilesDir().getName());
//        Log.e("direct File : ", getFilesDir().toString());
        if (!direct.exists()) {
            if (!direct.mkdirs()) {
                Log.d("TAG", "failed to create directory");
            }
        }
//        imageFolders=getImageFolders();
        try {
            images=getAllShownImagesPath();// 기본갤러리 이미지 받아오는 곳 todo 9버전 사진 못받아옴..
        }catch (Exception e){
            e.printStackTrace();
        }
        lists=direct.listFiles();
        Collections.reverse(Arrays.asList(lists));// 파일 내부에 있는 데이터 역순으로 돌림
        for (int i = 0; i < lists.length; i++) {
            imageData.add(new ImageData(lists[i].getName(),lists[i].getPath(),0,0));
        }
        // sharedPreferences


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            flatRotate=getAC_SharedPreferences();
            flatRotateKide=getSharedPreferences();
//            flatRotate=getSharedPreferences();

            if (flatRotate.size()==0){// 기본갤러리의 회전값 없을때...
                setAC_Preference();
                flatRotate=getAC_SharedPreferences();
            }else if (flatRotate.size()!=images.size() && flatRotate.size()!=0){// 기본갤러리
                setAC_Preference();
                flatRotate=getAC_SharedPreferences();
            }
            if (flatRotateKide.size()!=imageData.size() && flatRotateKide.size()!=0){
                Log.e("sharedPref_re", flatRotateKide.size()+" : "+imageData.get(imageData.size()-1).ImageName);
                setPreference();
                flatRotateKide=getSharedPreferences();
            }else if (flatRotateKide.size()==0){
                setPreference();
                flatRotateKide=getSharedPreferences();
            }
            Log.e("sharedTest : ", flatRotate.toString());
//            for (int i = 0; i < imageData.size(); i++) {
//                imageData.get(i).rotateNum=flatRotate.get(i);
//            }
//            switchNum=(int)flatRotateKide.get(pagePosition).floatValue();

        }


        constraintLayout=findViewById(R.id.setPageVisivility);
        setContentView(R.layout.activity_grid_view_gallery);
        toolbar=findViewById(R.id.toolbar_grid_gallery);
        setSupportActionBar(toolbar);
        actionBar=getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("키즈사랑앨범");

        Bundle bundle=getIntent().getExtras();
        strArr=bundle.getStringArrayList("fileName");
        Log.e("SizeTest : ", strArr.size()+"" );
        setFragments();// fragment배치
        new_adapter=new New_Adapter(GridViewGallery.this, imageData);
        android_adapter=new Android_Adapter(GridViewGallery.this,imageData, images);
        date_adapter=new Date_Adapter(GridViewGallery.this, imageData);
        manager=getSupportFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();
        transaction.add(R.id.pager_gridview, fragments[0]);
        transaction.commit();// 최근 페이지 처음화면으로 지정
        lockImg=findViewById(R.id.lockImage);
        checkImgView=findViewById(R.id.image_check_icon);
        viewPager=findViewById(R.id.viewpager_grid);
        viewPager.setPagingEnabled(true);
        bottomNav=findViewById(R.id.bottomLay_visivle);
        bnv=findViewById(R.id.navBar);
        bnv.setOnNavigationItemSelectedListener(navItemSelected);
        toolbar.setOnMenuItemClickListener(menuItemClickListener);


    }// onCreate...



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.grid_gallery_toolbar_icons, menu);
        return true;
    }

    @Override
    public void onBackPressed() {// 뒤로가기 클릭

        if (zoomOut==0 && outNum==0){// 그냥 나올때
            returnVal=false;
            degree=0;
            outNum=0;
            zoomOut=0;
            rotSaveOut=false;
            rotClickedNum=0;
            clickZoomAndroid=0;
            super.onBackPressed();
            finishBigImage();//종료
            // 종료 후 리스트 재실행
//            FragmentTransaction tran = manager.beginTransaction();
//            tran.replace(R.id.pager_gridview, fragments[0]);
//            tran.commit();
            setTransactionFragments();
            Log.e("fragmentShow", " - backPressed outNum=0");// 이거까지 나오고 에러남.
        }else if (outNum==1){// 수정하고 나올때
            returnVal=false;
            degree=0;
            outNum=0;
            zoomOut=0;
            rotClickedNum=0;
            rotSaveOut=false;
            clickZoomAndroid=0;
            GridViewGallery.super.onBackPressed();
            finishBigImage();
//            FragmentTransaction tran=manager.beginTransaction();
//            tran.replace(R.id.pager_gridview, fragments[0]);
//            tran.commit();
            setTransactionFragments();
            Log.e("fragmentShow", " - backPressed outNum=1");
        }else if (rotSaveOut && outNum!=1 || rotClickedNum==1){
            dialogSet("변경된 정보를 저장하지 않고 나가시겠습니까?", "", "예", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    returnVal=false;
                    degree=0;
                    outNum=0;
                    zoomOut=0;
                    rotClickedNum=0;
                    rotSaveOut=false;
                    clickZoomAndroid=0;
                    switchNum=(int)flatRotateKide.get(pagePosition).floatValue();
                    Log.e("switchNumSetOut", flatRotateKide.get(pagePosition).floatValue()+"");
                    GridViewGallery.super.onBackPressed();
                    finishBigImage();
//                    FragmentTransaction tran=manager.beginTransaction();
//                    tran.replace(R.id.pager_gridview, fragments[0]);
//                    tran.commit();
                    setTransactionFragments();
                }
            });
        }else if (rotClickedNum==0){
            super.onBackPressed();
//            finishBigImage();
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: {
                onBackPressed();// 백버튼과 동일한 동작 수행하도록 설정하니까 에러 없어짐...
                degree=0;
            }break;
        }
        return super.onOptionsItemSelected(item);
    }

    
    Toolbar.OnMenuItemClickListener menuItemClickListener=new Toolbar.OnMenuItemClickListener() {// 전송, 삭제 버튼 처리
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()){
// =================== 갤러리 메뉴====================
                case R.id.send:// 선택한 사진 전송
                    Toast.makeText(GridViewGallery.this, "전송", Toast.LENGTH_SHORT).show();
                    // todo 전송기능
                    break;
                case R.id.delete:// 선택한 사진 삭제
                    try {
                        for (int i = 0; i < imageData.size(); i++) {
                            if (imageData.get(i).Check==1){
                                checkedNum=1;
                                break;
                            }
                        }
                        Log.e("lists_size : ", "-imageData end-"+imageData.size());
                        if (checkedNum==1){
                            clickedImagesD(checkedNum);// 선택파일 삭제문
                            checkedNum=0;
                        }else {
                            clickedImagesD(checkedNum);
                        }
                        break;
                        // 다이얼로그 표시
                    }catch (Exception e){
                        Log.e("Delete_Error : ", " - Delete_Error - "+e.toString());
                    }
                    break;
// =================== 큰사진 메뉴====================  하단으로 위치변경 및 텍스트 표시
                case R.id.rotate:// 저  장 // todo 기본갤러리에서 저장할때 키즈사랑 카메라에서 촬영한 사진들 만큼의 길이에 있는 이미지만 저장됨...pagePosition확인해보기...
                    // 회전시킨 이미지 정보 저장
                    dialogSet("변경된 정보를 저장하시겠습니까?", "저 장", "저 장", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            outNum=1;
                            zoomOut=0;

                            File file = null;
                            ArrayList<Float> floats=new ArrayList<>();
                            if (clickZoomAndroid==0){
                                imageData.get(pagePosition).rotateNum=degree;
                                new_adapter.setItem(imageData);
                                file=new File(direct, imageData.get(pagePosition).ImageName);// 키즈사랑 갤러리에서 회전시켰을때 저장경로.
                            }else if (clickZoomAndroid==1){
                                AC_imageData.get(pagePosition).rotateNum=degree;
                                android_adapter.setItem(images,AC_imageData);
//                                file은 사진 가져오는 경로
//                                Log.e("image_path", images.get(pagePosition));
                                file=new File(images.get(pagePosition));
                            }


                            // 이미지 회전되서 저장된거 확인함 회전된 이미지 보여주지 않음 => Glide에서 이미지 회전 고정시켜서 보여줌.
                            degree=0;
                            // get, set
//                            setPreference();
//                            editRot(imageData);

                            if (clickZoomAndroid==0){// 키즈사랑갤러리에서 회전값 저장할때
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                                    floats=getSharedPreferences();
                                    floats=getSharedPreferences();// 여기서 이미지 회전값 저장하고 받아와야함.
                                    Log.e("floatsTest", " 0 = "+floats.toString());
                                    if (!floats.get(pagePosition).equals(imageData.get(pagePosition).rotateNum)){
                                        floats.set(pagePosition, imageData.get(pagePosition).rotateNum);// 쉐어드에 저장하기..
                                        SharedPreferences pref=getSharedPreferences("ImageData", MODE_PRIVATE);

                                        SharedPreferences.Editor editor=pref.edit();

                                        JSONArray jsonArray=new JSONArray();

                                        for (int i = 0; i < imageData.size(); i++) {
                                            jsonArray.put(floats.get(i));
                                        }
                                        if (!floats.isEmpty()) {
                                            editor.putString("rotData", jsonArray.toString());
                                        }else {
                                            editor.putString("rotData", null);
                                        }
                                        editor.apply();
                                        Toast.makeText(GridViewGallery.this, "", Toast.LENGTH_SHORT).show();
                                    }
                                }
//                                matrix.postRotate(imageData.get(pagePosition).rotateNum);// 인덱스 에러 회전값 저장//

                            }else if (clickZoomAndroid==1){// 기본 갤러리에서 회전값 저장하여 키즈사랑 갤러리로 보낼때.
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                                    floats=getSharedPreferences();
//                                    floats=getAC_SharedPreferences();// 다시 확인해보기
                                    floats=flatRotateKide;

                                    Log.e("floatsTest", " 1 = "+floats.toString());

                                    if (!imageData.contains(AC_imageData.get(pagePosition).ImageName)){
                                        floats.add(AC_imageData.get(pagePosition).rotateNum);// 쉐어드에 저장하기.. 값이 0으로 저장됨...
                                        SharedPreferences pref=getSharedPreferences("ImageData", MODE_PRIVATE);

                                        SharedPreferences.Editor editor=pref.edit();

                                        JSONArray jsonArray=new JSONArray();

                                        for (int i = 0; i < imageData.size(); i++) {
                                            jsonArray.put(floats.get(i));
                                        }

                                        if (!floats.isEmpty()) {
                                            editor.putString("rotData", jsonArray.toString());// 리스트 다시작성함
                                        }else {
                                            editor.putString("rotData", null);
                                        }
                                        editor.apply();
                                        Toast.makeText(GridViewGallery.this, AC_imageData.get(pagePosition).rotateNum+"", Toast.LENGTH_SHORT).show();
                                    }

                                }
//                                matrix.postRotate(AC_imageData.get(pagePosition).rotateNum);// 인덱스 에러 회전값 저장//
                            }// clickZoomAndroid == 1

                            Bitmap bm=BitmapFactory.decodeFile(file.getAbsolutePath());
                            Matrix matrix=new Matrix();
                            matrix.postRotate(AC_imageData.get(pagePosition).rotateNum);// 인덱스 에러 회전값 저장//
//                            matrix.postRotate(floats.get(pagePosition));// 인덱스 에러 회전값 저장//


                            bm=Bitmap.createBitmap(bm, 0,0,bm.getWidth(),bm.getHeight(),matrix,true);
//                            Log.e("degreeNum : ", " - "+floats.get(pagePosition));// 저장될때 shared에 저장하기
//                            file.delete();
//                            imageData.remove(pagePosition);
                            FileOutputStream fOut=null;
                            try {
                                if (clickZoomAndroid==0){
                                    fOut=new FileOutputStream(file);
                                    bm.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                                    fOut.flush();//덮어씌우기
                                    fOut.close();
//                                    switchNum=getOrientateionOfImage(imageData.get(pagePosition).ImagePath);
                                }else if (clickZoomAndroid==1){
                                    File file2=new File(direct, AC_imageData.get(pagePosition).ImageName);
                                    fOut=new FileOutputStream(file2);
                                    bm.compress(Bitmap.CompressFormat.JPEG, 100, fOut);

                                    ByteArrayOutputStream stream=new ByteArrayOutputStream();
                                    bm.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                                    byte[] bytes=stream.toByteArray();

                                    fOut.write(bytes);//새로쓰기
                                    fOut.close();
                                    Collections.reverse(imageData);
                                    imageData.add(new ImageData(
                                            AC_imageData.get(pagePosition).ImageName,
                                            AC_imageData.get(pagePosition).ImagePath,
                                            0, 0
                                    ));
                                    Collections.reverse(imageData);
                                    new_adapter.setItem(imageData);
                                    new_adapter.notifyDataSetChanged();// newimage를 다시 실행해야할듯
//                                    switchNum=getOrientateionOfImage(AC_imageData.get(pagePosition).ImagePath);
                                }
//                                fOut.write(bytes);
//                                stream.write(bytes);
//                                stream.close();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            FragmentTransaction tran = manager.beginTransaction();
                            tran.remove(fragments[0]);
                            fragments[0]=new New_images_FG(imageData);
                            tran.replace(R.id.pager_gridview, fragments[0]);
                            tran.commit();// new fragment다시실행
                            // 저장 메소드 추가
//                            switchNum=(int)floats.get(pagePosition).floatValue();
//                            Log.e("flatRotateKideShow", " in saved = "+switchNum+" / "+floats.get(pagePosition));
                            bigImageToolbarItem_clear();// 회전버튼 눌렀을때 나오는 아이콘 안보이게 변경
                            commitClickFG();// 큰이미지 다시 보여주기
                            // 저장하고 다시 회전시켜서 저장하면 바뀐 데이터로 저장되지 않음...

                        }
                    });
                    break;
                case R.id.deleteImg:// 되돌리기
                    // 회전시킨 이미지 정보 되돌리기
                    dialogSet( "변경된 정보를 저장하지 않고 되돌리겠습니까?","되돌리기", "되돌리기", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            bigImageToolbarItem_clear();
                            commitClickFG();
                            outNum=1;
                            zoomOut=0;
                            degree=0;
                        }
                    });
                    break;
            }
            return false;
        }
    };

    public void rotateImage(){// 이미지 회전시키기.
        rotSaveOut=true;
        zoomOut=2;

        if (rotClickedNum==0){
            lockImg.setVisibility(View.VISIBLE);// 잠금키 보이게 하기
            toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.custom_toolbar_c));
            toolbar.setTitleTextColor(ContextCompat.getColor(this,R.color.custom_toolbar_c));
            Menu menu=toolbar.getMenu();
            menu.removeGroup(R.id.grid_toolbar_menu);
            toolbar.inflateMenu(R.menu.fg_bigimg_clicked_menu);
        }
        Bundle bundle=new Bundle();

        if (clickZoomAndroid==0){// 키즈사랑 갤러리에서 회전버튼클릭
            if (returnVal==false){
                returnVal=true;
            }

            if (flatRotateKide.size()>0){
//                Log.e("flatRotateKideShow", " = "+flatRotateKide.get(pagePosition));
//                if (flatRotateKide.get(pagePosition) == 90) {
//                    degree = 180;
//                    imageData.get(pagePosition).rotateNum = 180;
//                } else if (flatRotateKide.get(pagePosition) == 180) {
//                    degree = 270;
//                    imageData.get(pagePosition).rotateNum = 270;
//                } else if (flatRotateKide.get(pagePosition) == 270) {
//                    degree = 0;
//                    imageData.get(pagePosition).rotateNum = 0;
//                } else if (flatRotateKide.get(pagePosition) == 0) {
//                    degree = 90;
//                    imageData.get(pagePosition).rotateNum = 90;
//                }


                Log.e("flatRotateKideShow", " rot in 1= "+switchNum);
                switch (switchNum){
                    case 90:degree+=90; imageData.get(pagePosition).rotateNum=180; switchNum=180;break;
                    case 180:degree+=90; imageData.get(pagePosition).rotateNum=270; switchNum=270;break;
                    case 270:degree=0; imageData.get(pagePosition).rotateNum=0; switchNum=0;break;
                    case 0:degree+=90; imageData.get(pagePosition).rotateNum=90;switchNum=90;break;
                }
                Log.e("flatRotateKideShow", " rot in 2= "+switchNum);
            }else {
                switch ((int) imageData.get(pagePosition).rotateNum){
                    case 90:degree=180; imageData.get(pagePosition).rotateNum=180;break;
                    case 180:degree=270; imageData.get(pagePosition).rotateNum=270;break;
                    case 270:degree=0; imageData.get(pagePosition).rotateNum=0;break;
                    case 0:degree=90; imageData.get(pagePosition).rotateNum=90;break;
                }
            }

            Log.e("degreeNum : "," degree - "+degree);
            Log.e("degreeNum : "," rotateNum - "+imageData.get(pagePosition).rotateNum);
            // 회전시킨 이미지 저장
            // 메뉴 버튼 변경

            bundle.putInt("position", pagePosition);
            bundle.putParcelableArrayList("imageData", imageData);
            bundle.putInt("degree", switchNum);

        }else {// 기본갤러리에서 회전버튼 클릭
            if (returnVal==false){
                returnVal=true;
                Collections.reverse(AC_imageData);
            }

            switch ((int) AC_imageData.get(pagePosition).rotateNum){
                case 90:degree=180; AC_imageData.get(pagePosition).rotateNum=180;break;
                case 180:degree=270; AC_imageData.get(pagePosition).rotateNum=270;break;
                case 270:degree=0; AC_imageData.get(pagePosition).rotateNum=0;break;
                case 0:degree=90; AC_imageData.get(pagePosition).rotateNum=90;break;
            }

            bundle.putInt("position", pagePosition);
            bundle.putParcelableArrayList("AC_imageData", AC_imageData);
            bundle.putInt("degree", degree);
            bundle.putStringArrayList("images", images);
        }
        Fragment fragment=new ImagePageFragment();
        fragment.setArguments(bundle);
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
//        transaction.addToBackStack(null);// 백스텍에 남기지 않으니까 에러나지 않음.
        transaction.replace(R.id.pager_gridview, fragment);
        transaction.commit();
        bindAdapter();
    }


    public void click_Delete(){// 큰이미지 화면 사진 삭제
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("사진 삭제").setMessage("삭제하시면 갤러리에서 확인하거나 앨범에 등록할 수 없습니다.");
        builder.setNegativeButton("취소", null);
        builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                returnVal=false;
                outNum=1;
                zoomOut=1;
                if (clickZoomAndroid==0){
                    new_adapter.imgDelete(pagePosition);
                    imageData.remove(pagePosition);
                    lists[pagePosition].delete();
                    commitClickFG();// 데이터 넘기고 페이저 commit하는 코드
                } else if (clickZoomAndroid == 1) {// 기본갤러리 사진 지우는 코드
//                    Log.e("getAbsolutePath", " - "+file.getAbsolutePath());
                    Log.e("getAbsolutePath", " - "+images.get(pagePosition));
                    // 기본갤러리에서 사진삭제
                    deleteUri();

                }
                Snackbar.make(viewPager, "사진이 삭제 되었습니다.", Snackbar.LENGTH_LONG).show();
            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode){
//            case 42:{
//                Toast.makeText(this, "test", Toast.LENGTH_SHORT).show();
//                if (resultCode==RESULT_OK){
//                    Uri uri=data.getData();
//                    InputStream inputStream=null;
//                    try {
//                        inputStream=getBaseContext().getContentResolver().openInputStream(uri);
//                        Bitmap bm=BitmapFactory.decodeStream(inputStream);
//                        int maxHeight=1920;
//                        int maxWidth=1920;
//                        float scale=Math.min(((float)maxHeight/bm.getWidth()),((float)maxWidth/bm.getHeight()));
//                        Matrix matrix=new Matrix();
//                        matrix.postScale(scale, scale);
//                        Bitmap bitmap=Bitmap.createBitmap(bm, 0,0, bm.getWidth(), bm.getHeight(), matrix, true);
//
//                        bm.recycle();
//                        bitmap.recycle();
//                        finishActivity(42);
//
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//        }
//    }

    private void bindAdapter(){
        if(new_adapter == null){
            new_adapter=new New_Adapter(GridViewGallery.this, imageData);
        }else{
            new_adapter.setItem(imageData);
            new_adapter.notifyDataSetChanged();// 데이터 변경 알림.
        }
        if (android_adapter==null){
            android_adapter=new Android_Adapter(GridViewGallery.this,imageData, images);
        }else {
            android_adapter.setItem(images, imageData);
            android_adapter.notifyDataSetChanged();
        }
        if (date_adapter==null){
            date_adapter=new Date_Adapter(GridViewGallery.this,imageData);
        }else {
            date_adapter.notifyDataSetChanged();
        }
    }

    private void commitClickFG(){
        Bundle bundle=new Bundle();
        bundle.putInt("position", pagePosition);
        bundle.putParcelableArrayList("imageData", imageData);
        if (clickZoomAndroid==1){
            bundle.putStringArrayList("images", images);
            bundle.putInt("position", pagePosition);// todo nullpoint Error
            bundle.putParcelableArrayList("AC_imageData", AC_imageData);
        }
        Fragment fragment=new Clicked_BigImage_FG();
        fragment.setArguments(bundle);
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
//        transaction.addToBackStack(null);     // 백스택 문제일듯... file.size확인
        Log.e("showBackStack", " - backstack : "+transaction.isAddToBackStackAllowed());
        transaction.replace(R.id.pager_gridview, fragment);
        transaction.commit();
        bindAdapter();
    }

    public void clickedImagesD(int checked){//선택한 이미지 삭제
        if (checked==0){
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setMessage("삭제할 사진을 선택해 주세요.");
            builder.setPositiveButton("확인", null);
            AlertDialog alertDialog=builder.create();
            alertDialog.show();
        }else if (checked==1){
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("사진 삭제").setMessage("삭제하시면 갤러리에서 확인하거나 앨범에 등록할 수 없습니다.");
            builder.setNegativeButton("취소",null);
            builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    File[] lists1=direct.listFiles();
                    Collections.reverse(Arrays.asList(lists1));// 파일 내부에 있는 데이터 역순으로 돌림 //

                    Log.e("lists_size : ", "-lists1-"+lists1.length);
                    Log.e("lists_size : ", "-imageData-"+imageData.size());
                    //new_adapter.notifyDataSetChanged();
//        int forNum=imageData.size();
                    Log.e("fori_01 : ", "fileList_size... - "+lists1.length+" - start");//
                    for (int i = 0; i < lists1.length; i++) {
                        if (lists1[i].exists()) {
                            if (imageData.get(i).Check == 1) {
                                lists1[i].delete();
                            }
                        }
                        Log.e("fori_01 : ", i + "- end");// 9
                    }
                    File[] lists2=direct.listFiles();
                    Collections.reverse(Arrays.asList(lists2));
                    Log.e("fori_01 : ", "fileList_size... - "+lists1.length+" - end");
                    Log.e("fori _0 : ", "end_for");
                    imageData.clear();
                    for (int i = 0; i < lists2.length; i++) {
                        imageData.add(new ImageData(lists2[i].getName(),lists2[i].getPath(),0,0));
                    }
                    new_adapter.setItem(imageData);
                    bindAdapter();
                    setTransactionFragments();
                    Snackbar.make(viewPager, "사진이 삭제 되었습니다.", Snackbar.LENGTH_LONG).show();// 안옴.
                }
            });
            AlertDialog dialog=builder.create();
            dialog.show();
        }
    }

//    네비게이션바 설정
    BottomNavigationView.OnNavigationItemSelectedListener navItemSelected=new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction tran=manager.beginTransaction();
            switch (item.getItemId()){
                case R.id.new_img:
                    actionBar.setTitle("키즈사랑앨범");
                    if (actionBar.getTitle().equals("키즈사랑앨범")){
                        for (int i = 0; i < imageData.size(); i++) {
                            imageData.get(i).Check=0;// 클릭 내용 초기화
                        }
                        for (int i = 0; i < AC_imageData.size(); i++) {
                            AC_imageData.get(i).Check=0;
                        }
//                        new_adapter=new New_Adapter(GridViewGallery.this, imageData);
//                        new_adapter.notifyDataSetChanged();// 데이터 변경 알림.
                        bindAdapter();
                    }
                    tran.replace(R.id.pager_gridview, fragments[0]);
                    topNavClickNum=0;
                    break;
//                case R.id.date_img:
//                    actionBar.setTitle("날짜");
//                    if (actionBar.getTitle().equals("날짜")){
//                        for (int i = 0; i < imageData.size(); i++) {
//                            imageData.get(i).Check=0;
//                        }
//                        for (int i = 0; i < AC_imageData.size(); i++) {
//                            AC_imageData.get(i).Check=0;
//                        }
//                        // 데이터 변경 알림 추가하기 - 어뎁터에
//                        bindAdapter();
//                    }
//                    tran.replace(R.id.pager_gridview, fragments[1]);
//                    topNavClickNum=1;
//                    break;
                case R.id.android_img:

                    actionBar.setTitle("기본갤러리");
                    if (actionBar.getTitle().equals("기본갤러리")){
                        for (int i = 0; i < imageData.size(); i++) {
                            imageData.get(i).Check=0;
                        }
                        for (int i = 0; i < AC_imageData.size(); i++) {
                            AC_imageData.get(i).Check=0;
                        }
                        // 데이터 변경 알림 추가하기 - 어뎁터에
                        bindAdapter();
                    }
                    tran.replace(R.id.pager_gridview, fragments[2]);
                    topNavClickNum=2;
                    break;
            }
            tran.commit();
            return true;
        }
    };

    public void click_rotate(View view) {// 이미지 회전
        if (rotClickedNum==0) {
            rotateImage();
            rotClickedNum=1;
        }else if (returnVal){
            rotateImage();
        }
    }

    public void click_delete(View view) {// 이미지 삭제
        click_Delete();
    }// 이미지 삭제

    public void click_check(View view) {// 이미지 선택

        if (clickZoomAndroid==1){
            if (AC_imageData.get(pagePosition).Check==0){
                AC_imageData.get(pagePosition).Check=1;// 이 값이 남아있음.
                Glide.with(this).load(R.drawable.ic_check_circle_w_24).into(checkImgView);
            }else {
                AC_imageData.get(pagePosition).Check=0;
                Glide.with(this).load(R.drawable.ic_check_circle_b_24).into(checkImgView);
            }
        }else {
            if (imageData.get(pagePosition).Check==0) {
                imageData.get(pagePosition).Check=1;// 선택한 이미지 체크
                Glide.with(this).load(R.drawable.ic_check_circle_w_24).into(checkImgView);
            }else {
                imageData.get(pagePosition).Check=0;
                Glide.with(this).load(R.drawable.ic_check_circle_b_24).into(checkImgView);
            }
        }

    }

    public void bigImageToolbarItem_clear(){
        lockImg.setVisibility(View.GONE);
        Menu menu_re=toolbar.getMenu();
        menu_re.removeGroup(R.id.bigimg_click_menu);
        rotClickedNum=0;
        returnVal=false;
        rotSaveOut=true;
//        clickZoomAndroid=0;
//        zoomOut=0;
    }

    public void dialogSet(String msg, String title, String positive, DialogInterface.OnClickListener listener){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle(title).setMessage(msg);
        builder.setNegativeButton("취소", null);
        builder.setPositiveButton(positive,listener);
        AlertDialog dialog=builder.create();
        dialog.show();
    }
    public void dialogSet(String msg, String title, String positive, DialogInterface.OnClickListener listener, DialogInterface.OnClickListener negativeBtn){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle(title).setMessage(msg);
        builder.setNegativeButton("취소", negativeBtn);
        builder.setPositiveButton(positive,listener);
        AlertDialog dialog=builder.create();
        dialog.show();
    }

    void setFragments(){
        fragments[0]=new New_images_FG(imageData);
        fragments[1]=new Date_images_FG(imageData);
        fragments[2]=new Android_images_FG(images, AC_imageData);
//        fragments[2]=new Android_images_FG(testPath.getPath());
    }


    public void finishBigImage(){// 이미지 크게보기 종료
        if (bnv.getVisibility()==View.GONE){// 이미지 크게 보기 상태일때 백버튼
            lockImg.setVisibility(View.GONE);
            manager.beginTransaction().remove(new Clicked_BigImage_FG()).commit();
            manager.popBackStack();
            toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.kidsLove_color));
            toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));

            Menu menu=toolbar.getMenu();
            menu.removeGroup(R.id.bigimg_click_menu);
            toolbar.inflateMenu(R.menu.grid_gallery_toolbar_icons);

            bnv.setVisibility(View.VISIBLE);
            bottomNav.setVisibility(View.GONE);
            rotClickedNum=0;
        }else {
            finish();
        }
    }

    public void setPreference(){
        ArrayList<Float> rotData=new ArrayList<Float>();
        for (int i = 0; i < imageData.size(); i++) {
            try {
                ExifInterface exif=new ExifInterface(lists[i].getAbsolutePath());// 이거 사용해서 회전 방법 다시 작성해보기
                int orientation=exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
                switch (orientation){
                    case ExifInterface.ORIENTATION_ROTATE_90:{
                        imageData.get(i).rotateNum=90;
                        rotData.add(90f);
                    }break;
                    case ExifInterface.ORIENTATION_ROTATE_180:{
                        imageData.get(i).rotateNum=180;
                        rotData.add(180f);
                    }break;
                    case ExifInterface.ORIENTATION_ROTATE_270:{
                        imageData.get(i).rotateNum=270;
                        rotData.add(270f);
                    }break;
                    default:{
                        imageData.get(i).rotateNum=0;
                        rotData.add(0f);
                    }break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        SharedPreferences sharedPreferences=getSharedPreferences("ImageData", MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();

        JSONArray jsonArray=new JSONArray();

        for (int i = 0; i < imageData.size(); i++) {
            jsonArray.put(rotData.get(i));
        }
        if (!rotData.isEmpty()) {
            editor.putString("rotData", jsonArray.toString());
        }else {
            editor.putString("rotData", null);
        }
        editor.apply();
    }

    public void setAC_Preference(){// 기본갤러리의 사진 회전값 기기에 저장. => 회전값 받아올 필요없이 모두 0으로 변경.
        ArrayList<Float> AC_rotData=new ArrayList<Float>();
        if (AC_imageData!=null){
            for (int i = 0; i < AC_imageData.size(); i++) {
                AC_rotData.add(0f);
//                try {
//                    ExifInterface exif=new ExifInterface(images.get(i));
//                    int orientation=exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
//                    switch (orientation){
//                        case ExifInterface.ORIENTATION_ROTATE_90:{
//                            AC_imageData.get(i).rotateNum=90;
//                            AC_rotData.add(90f);
//                        }break;
//                        case ExifInterface.ORIENTATION_ROTATE_180:{
//                            AC_imageData.get(i).rotateNum=180;
//                            AC_rotData.add(180f);
//                        }break;
//                        case ExifInterface.ORIENTATION_ROTATE_270:{
//                            AC_imageData.get(i).rotateNum=270;
//                            AC_rotData.add(270f);
//                        }break;
//                        default:{
//                            AC_imageData.get(i).rotateNum=0;
//                            AC_rotData.add(0f);
//                        }break;
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }
        }// if문 끝...

        SharedPreferences sharedPreferences=getSharedPreferences("AC_ImageData", MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();

        JSONArray jsonArray=new JSONArray();

        for (int i = 0; i < AC_imageData.size(); i++) {
            jsonArray.put(AC_rotData.get(i));
        }
        if (!AC_rotData.isEmpty()) {
            editor.putString("rotData", jsonArray.toString());
        }else {
            editor.putString("rotData", null);
        }
        editor.apply();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public ArrayList<Float> getSharedPreferences(){

        SharedPreferences pref=getSharedPreferences("ImageData", MODE_PRIVATE);
        String json=pref.getString("rotData", null);
        ArrayList<Float> urls=new ArrayList<>();
        if (json!=null){
            try {
                JSONArray a=new JSONArray(json);
                for (int i = 0; i < a.length(); i++) {
                    float url=a.optInt(i);
                    urls.add(url);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return urls;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public ArrayList<Float> getAC_SharedPreferences(){

        SharedPreferences pref=getSharedPreferences("AC_ImageData", MODE_PRIVATE);
        String json=pref.getString("rotData", null);
        ArrayList<Float> urls=new ArrayList<>();
        if (json!=null){
            try {
                JSONArray a=new JSONArray(json);
                for (int i = 0; i < a.length(); i++) {
                    float url=a.optInt(i);
                    urls.add(url);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return urls;
    }

    public void editRot(ArrayList<ImageData> imageData){

        SharedPreferences sharedPreferences=getSharedPreferences("ImageData", MODE_PRIVATE);
//        SharedPreferences.Editor editor=sharedPreferences.edit();

        ArrayList<Float> rotData=new ArrayList<>();

        JSONArray jsonArray=new JSONArray();

        for (int i = 0; i < imageData.size(); i++) {
            rotData.add(imageData.get(i).rotateNum);
        }

        for (int i = 0; i < imageData.size(); i++) {
            jsonArray.put(rotData.get(i));
        }

        if (!rotData.isEmpty()) {
//            editor.putString("rotData", jsonArray.toString());
            sharedPreferences.edit().putString("rotData", jsonArray.toString()).apply();
        }else {
//            editor.putString("rotData", null);
            sharedPreferences.edit().putString("rotData", null).apply();
        }

    }

//    private boolean isExternalStorageWritable(){
//        return Environment.getExternalStorageState()==Environment.MEDIA_MOUNTED;
//    }
//    private boolean isExternalStorageReadable(){
//        return Environment.getExternalStorageState()==Environment.MEDIA_MOUNTED || Environment.getExternalStorageState()==Environment.MEDIA_MOUNTED_READ_ONLY;
//    }

    public void setTransactionFragments(){
        FragmentTransaction tran = manager.beginTransaction();
        switch (topNavClickNum){
            case 0:{
                if (fragments[0].isAdded()){
                    tran.remove(fragments[0]);
                    fragments[0]=new New_images_FG(imageData);

//                    Log.e("fragmentShow", " - fragments[0]");
                }else {
                    Log.e("fragmentShow", " - else fragments[0]");
                }
                tran.replace(R.id.pager_gridview, fragments[0]);

            }break;
            case 1:{
                if (fragments[1].isAdded()){
                    tran.remove(fragments[1]);
                    fragments[1]=new Date_images_FG(imageData);
                }
                tran.replace(R.id.pager_gridview, fragments[1]);
            }break;
            case 2:{
                if (fragments[2].isAdded()){
                    tran.remove(fragments[2]);
                    fragments[2]=new Android_images_FG(images, AC_imageData);
//                    fragments[2]=new Android_images_FG(testPath.getPath());

                }
                tran.replace(R.id.pager_gridview, fragments[2]);
            }break;
        }
        tran.commit();
    }


    public void click_locked(View view) {
            Toast.makeText(this, "저장되지 않은 정보가 있습니다.", Toast.LENGTH_SHORT).show();
    }
//    public void selectGallery(){
//        Intent intent=new Intent(Intent.ACTION_PICK);
//        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        intent.setType("image/*");
//        startActivityForResult(intent, 200);
//    }

    public ArrayList<ImageFolder> getImageFolders(){// 기본갤러리 데이터값 받아오는 부분
        ArrayList<ImageFolder> picFolders=new ArrayList<>();
        ArrayList<String> picPaths=new ArrayList<>();
        Uri allImagesUri=MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection={MediaStore.Images.ImageColumns.DATA, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.BUCKET_ID};
        Cursor cursor=getContentResolver().query(allImagesUri, projection, null, null, null);

        try {
            while (cursor.moveToNext()){
                ImageFolder folder=new ImageFolder();
                String name=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME));
                String folderName=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                String dataPath=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));

                String folderPaths=dataPath.substring(0, dataPath.lastIndexOf(folderName+"/"));
                folderPaths=folderPaths+folderName+"/";
                if (!picPaths.contains(folderPaths)){
                    picPaths.add(folderPaths);
                    folder.setPath(folderPaths);
                    folder.setFolderName(folderName);
                    folder.setFirstPic(dataPath);
                    folder.addPics();
                    picFolders.add(folder);
                }else {
                    for (int i = 0; i < picFolders.size(); i++) {
                        if (picFolders.get(i).getPath().equals(folderPaths)){
                            picFolders.get(i).setFirstPic(dataPath);
                            picFolders.get(i).addPics();
                        }
                    }
                }
            }
            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        for (int i = 0; i < picFolders.size(); i++) {
            Log.d("picture_folders", " - name / "+picFolders.get(i).getFolderName()+" / path / "+picFolders.get(i).getPath()+" / "+picFolders.get(i).getNumberOfPics());
        }

        return picFolders;
    }


    public ArrayList<String> getAllShownImagesPath(){// 기본갤러리 사진 받아옴
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name,column_index_path;
        int testName;
        ArrayList<String> listOfAllImages=new ArrayList<>();
        String absolutePathOfImage=null;
        uri=MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Log.e("URI", " - "+uri.toString());

        String[] projection={
                MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.ImageColumns.RELATIVE_PATH,
                MediaStore.Images.Media.RELATIVE_PATH,
                MediaStore.Images.Media.DISPLAY_NAME,
        };
        cursor=getContentResolver().query(uri, projection, null, null, null);
        column_index_data=cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);

//        29버전 (Q)에서 사진 로드되지 않음=> android 11버전사용하는 디바이스는 android:requestLegacyExternalStorage="true" 하지 않아도 정상적으로 로드됨...
        column_index_path=cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.RELATIVE_PATH);
        testName=cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DISPLAY_NAME);

        while (cursor.moveToNext()){
            absolutePathOfImage=cursor.getString(column_index_data);
//            absolutePathOfImage="sdcard/";
//            absolutePathOfImage=cursor.getString(column_index_path);
            Log.e("filePathError : ", " - 01 path :  "+cursor.getString(column_index_path));
            Log.e("filePathError : ", " - 02 name :  "+cursor.getString(testName));// 이거사용해서 삭제
            Log.e("filePathError : ", " - 03 data :  "+absolutePathOfImage);
            AC_imageData.add(new ImageData(cursor.getString(testName), absolutePathOfImage, 0, cursor.getPosition()));
//            Log.e("AC_imageData_Show", AC_imageData.get(cursor.getPosition()).ImageName);
//            Log.e("AC_imageData_Show", AC_imageData.get(cursor.getPosition()).ImagePath);
//            Log.e("AC_imageData_Show", AC_imageData.get(cursor.getPosition()).Position+"");
//            absolutePathOfImage+=cursor.getString(testName);
            listOfAllImages.add(absolutePathOfImage);
        }

        Collections.reverse(listOfAllImages);

        return listOfAllImages;
    }

    int DELETE_PERMISSION_REQUEST=1033;
    Uri contentUri=null;

    public void deleteUri(){// 기본 갤러리 이미지 삭제

        Cursor cursor;
        String[] projection={
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME
        };
        String displayName=null;
        cursor=getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
        int idColumn=cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
        int displayNameColumn=cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);

        Collections.reverse(AC_imageData);//    이미지 데이터 다시 뒤집어서 삭제할 Uri에 넣기.

        while (cursor.moveToNext()) {
            long id=cursor.getLong(idColumn);
            displayName=cursor.getString(displayNameColumn);
            if (displayName.equals(AC_imageData.get(pagePosition).ImageName)){
                Log.e("show_contentUri", displayName+" / "+AC_imageData.get(pagePosition).ImageName);
                contentUri=Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id+"");
            }
        }

        Log.e("show_contentUri_out", contentUri+"");
        try {
            getContentResolver().delete(contentUri, null, null);
        }catch (RecoverableSecurityException e){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                IntentSender intentSender=e.getUserAction().getActionIntent().getIntentSender();
                try {
                    startIntentSenderForResult(intentSender, DELETE_PERMISSION_REQUEST, null, 0, 0, 0, null);
                } catch (IntentSender.SendIntentException sendIntentException) {
                    sendIntentException.printStackTrace();
                }
            }

        }
        //
//        catch ( SecurityException se){
//            boolean permission=true;
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                permission= Settings.System.canWrite(this);
//                if (permission){
//                    Log.e("permission_se", "허용");
//                }else {
//                    Intent intent=new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
//                    intent.setData(Uri.parse("package"+getPackageName()));
//                    startActivityForResult(intent, 2003);
//                    permission=false;
//                }
//            }
//        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode== Activity.RESULT_OK && requestCode==DELETE_PERMISSION_REQUEST){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                Toast.makeText(this, "in_request", Toast.LENGTH_SHORT).show();
                getContentResolver().delete(contentUri, null, null);
                // 삭제는 성공하지만 뒤집기 이전에 파일이 삭제됨.
                Collections.reverse(AC_imageData);
                AC_imageData.remove(pagePosition);
                images.remove(pagePosition);
                android_adapter.imgDelete(pagePosition);
                commitClickFG();
            }
        }
    }

    public int getOrientateionOfImage(String filePath){
        ExifInterface exif=null;
        try {
            exif=new ExifInterface(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }

        int orientation=exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);

        if (orientation != -1){
            switch (orientation){
                case ExifInterface.ORIENTATION_ROTATE_90:return 90;
                case ExifInterface.ORIENTATION_ROTATE_180:return 180;
                case ExifInterface.ORIENTATION_ROTATE_270:return 270;
            }
        }
        return 0;

    }

}// class