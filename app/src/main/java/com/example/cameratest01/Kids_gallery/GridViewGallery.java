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
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Visibility;

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
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
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
import com.example.cameratest01.gridview_adapter.New_Adapter;
import com.example.cameratest01.gridview_gallery_fragment.Android_images_FG;
//import com.example.cameratest01.gridview_gallery_fragment.Date_images_FG;
import com.example.cameratest01.gridview_gallery_fragment.New_images_FG;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Permission;
import java.security.Permissions;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import okhttp3.internal.Util;

// todo 툴바 색상 변경, ImageData클래스명 변경, 이미지 저장시에 기존경로 추가로 데이터 저장하기, SCALABLELAYOUT사용해서 layout구성하기,
//  같은 이미지 회전하여 저장할때 다른이름으로 저장되게 하기, 이미지 로드할때 스레드 완료되지 않으면 기본갤러리 클릭되지 않게 하기..
//  이미지 크게보기 후 나가서 선택버튼 클릭시 취소와 삭제버튼 나오지 않는 현상.
//  회전버튼 클릭하면 기본갤러리 이미지로 변경되는 현상..

public class GridViewGallery extends AppCompatActivity {

    //  layout, fragment
    public Toolbar toolbar;
    public ActionBar actionBar;
    public LinearLayout bottomNav;
    public ImageView checkImgView;
    public BottomNavigationView bnv;// 상단의 갤러리 네비게이션
    public StopPagingViewPager viewPager;
    public ConstraintLayout constraintLayout;
    public ImageView lockImg;
    public Fragment[] fragments=new Fragment[3];// 이미지 리스트 보여줄 프레그먼트 => 날짜 프레그먼터 사용하지 않음..
    FragmentManager manager;
    New_Adapter new_adapter;// 이미지 보여주는 어뎁터
    MenuItem deletMenu, selectMenu, cancelMeun, sendMenu;// 툴바의 삭제버튼, 툴바의 선택버튼

    // fileData
    public File direct;     // 키즈사랑 갤러리 경로
    File[] lists;           // 키즈사랑 갤러리에 보여질 파일 리스트
    ArrayList<ImageData> imageData=new ArrayList<>();// 키즈사랑 갤러리 데이터
    public ArrayList<ImageData> AC_imageData=new ArrayList<>();// 기본갤러리 데이터
    public ArrayList<String> imagesArr =new ArrayList<>();// 기본갤러리 이미지 경로 및 데이터

    // values
    public int pagePosition;    // 큰 이미지의 포지션
    public int checkedNum=0;    // 삭제할 사진이 있는지 확인
    public int rotClickedNum=0; // 회전버튼 클릭 횟수
    public int outNum=0;        // 큰 이미지뷰 나올때 구분 0 : 이미지정보 수정되지 않았을때, 1 : 삭제, 저장, 되돌리기 등의 작업 완료 시,
    public int zoomOut=0;       // 0 : 그냥 나옴, 1 : 삭제, 2: 회전버튼클릭
    public int imageloading=0;  // 0: 이미지 로드되지 않은 상태, 1 : 이미지 로드된 상태
    public boolean returnVal=false;
    public boolean rotSaveOut=false;
    public static String GALLERY_TYPE="K";// K : 키즈사랑 갤러리, D : 기본갤러리
    public String SELECT_IMAGES="C";// C : 선택으로 택스트 보여질때, S : 전송으로 택스트 보여질때


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setData();// 이미지 데이터 set
        //getDatta();
        setFragments();// fragment배치
        setLayout();// 레이아웃 배치
        setEvent();

    }// onCreate...

    private void setEvent() {
        bnv.setOnNavigationItemSelectedListener(navItemSelected);
        toolbar.setOnMenuItemClickListener(menuItemClickListener);

    }



    private void setData() {
        direct=new File(getFilesDir(), "CameraTest01"+File.separator+"kidsLove");  // 키즈사랑 갤러리 이미지 받아오는곳

        Log.e("showDirect", Uri.fromFile(direct).getPath());   // file:///data/user/0/com.example.cameratest01/files/CameraTest01/kidsLove

        if (!direct.exists()) {
            direct.mkdirs();
        }
        lists=direct.listFiles();
        Collections.reverse(Arrays.asList(lists));  // 파일 내부에 있는 데이터 역순으로 돌림


//        Log.e("showUris", " - "+getUriArr());

        for (int i = 0; i < lists.length; i++) {    // 키즈사랑 갤러리
            imageData.add(new ImageData(
                    lists[i].getName(),
                    lists[i].getPath(),
                    0,0,
                    getOrientateionOfImage(lists[i].getAbsolutePath()),
                    getOrientateionOfImage(lists[i].getAbsolutePath()),
                    "K")
            );
            try {
                Log.e("showImageOri", "imageDataOri - "+getOrientationOfKidsLoveImage(lists[i].getCanonicalPath()));// 테그에서 받아온 값을 정수로 변환하지 않음...
                Log.e("showImageOri", "imageDataOri - "+getOrientateionOfImage(lists[i].getAbsolutePath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.e("showImageOri", "imageDataOri - "+lists[i].getAbsoluteFile().toString());

            imageData.get(i).setSizeK(lists.length);

//            imageData.get(i).setImageType(KIDS_LOVE_TYPE);
        }

//        ArrayList<Integer> integers=new ArrayList<>();
//        integers=getKidsLoveImageOri();
//        for (int i = 0; i < integers.size(); i++) {
//            Log.e("getImageOri", " : "+integers.get(i));
//        }
        try {
            imagesArr=getAllShownImagesPath();// 기본갤러리 이미지 받아오는 곳 9버전 사진 못받아옴..
        }catch (Exception e){
            e.printStackTrace();
        }
        // 스레드사용해보기
        ImageLoadThread loadThread=new ImageLoadThread();
        Thread thread=new Thread(loadThread);
        thread.start();
        Log.e("getState", "- state"+thread.getState());

        // 스레드 작업이 끝나기 전에 기본 갤러리로 이동하면 보여주는 리스트 회전 값 이상하게 나옴
//        for (int i = 0; i < imagesArr.size(); i++) {
//            AC_imageData.get(i).setRotateNum(getOrientateionOfImage(imagesArr.get(i)));
//            AC_imageData.get(i).setOrirotateNum(getOrientateionOfImage(imagesArr.get(i)));
////            AC_imageData.get(i).setImageType(DEFAULT_TYPE);
//        }

    }// setData....

    private void setLayout() {// layout 설정
        new_adapter=new New_Adapter(GridViewGallery.this, imageData);// 처음 실행은 키즈사랑 갤러리로 실행
        new_adapter.setType(GALLERY_TYPE);
        Log.d("setLayoutTest", " - inLay : ");
        if (GALLERY_TYPE.equals("K")){

            new_adapter.setCountNumK(lists.length);// 지우기
            Log.e("inNewAdapterCountNum", " : "+new_adapter.countNumK);
            new_adapter.notifyDataSetChanged();
            Log.e("inNewAdapterCountNum", " : "+new_adapter.countNumK);
        }else {
            new_adapter.setCountNumD(imagesArr.size());
            new_adapter.notifyDataSetChanged();
        }
        manager=getSupportFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();
        transaction.add(R.id.pager_gridview, fragments[0]);
        transaction.commit();// 키즈사랑 갤러리 처음화면으로 지정

        constraintLayout=findViewById(R.id.setPageVisivility);
        setContentView(R.layout.activity_grid_view_gallery);
        toolbar=findViewById(R.id.toolbar_grid_gallery);
        toolbar.inflateMenu(R.menu.grid_gallery_toolbar_icons);
        setSupportActionBar(toolbar);
        actionBar=getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("키즈사랑앨범");

        lockImg=findViewById(R.id.lockImage);
        checkImgView=findViewById(R.id.image_check_icon);
        viewPager=findViewById(R.id.viewpager_grid);
        viewPager.setPagingEnabled(true);
        bottomNav=findViewById(R.id.bottomLay_visivle);
        bnv=findViewById(R.id.navBar);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (SELECT_IMAGES.equals(""))
        Log.d("ResumeTest", "re");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.grid_gallery_toolbar_icons, menu);
        deletMenu=menu.findItem(R.id.delete);
        selectMenu=menu.findItem(R.id.send);
        cancelMeun=menu.findItem(R.id.cancel);
        sendMenu=menu.findItem(R.id.send);
        return true;
    }

    @Override
    public void onBackPressed() {// 뒤로가기 클릭

        if (zoomOut==0 && outNum==0){// 그냥 나올때 : 큰이미지에서 나올때 툴바 메뉴 안보여지는 아이템이 있음.
            returnVal=false;
            // 기본갤러리에서 저장 후 키즈사랑앨범에서 확인하고 뒤로가기 버튼 클릭시 에러 기본갤러리에서 바로 나가는것은 상관없음
            if (GALLERY_TYPE.equals("K")){
                imageData.get(pagePosition).setRotateNum(imageData.get(pagePosition).getOrirotateNum());
                Log.e("pagePositionShow", " - K"+pagePosition);
            }else {
                AC_imageData.get(pagePosition).setRotateNum(AC_imageData.get(pagePosition).getOrirotateNum());
                Log.e("pagePositionShow", " - D"+pagePosition);
            }
            outNum=0;
            zoomOut=0;
            rotSaveOut=false;
            rotClickedNum=0;
//            clickZoomAndroid=0;
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
            if (GALLERY_TYPE.equals("K")){
//                imageData.get(pagePosition).setRotateNum(imageData.get(pagePosition).getOrirotateNum());
                imageData.get(pagePosition).setOrirotateNum(imageData.get(pagePosition).getRotateNum());
            }else {
                AC_imageData.get(pagePosition).setOrirotateNum(AC_imageData.get(pagePosition).getRotateNum());
            }
            outNum=0;
            zoomOut=0;
            rotClickedNum=0;
            rotSaveOut=false;
//            clickZoomAndroid=0;
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
                    if (GALLERY_TYPE.equals("K")){
                        imageData.get(pagePosition).setRotateNum(imageData.get(pagePosition).getOrirotateNum());
                    }else {
                        AC_imageData.get(pagePosition).setRotateNum(AC_imageData.get(pagePosition).getOrirotateNum());
                    }
                    outNum=0;
                    zoomOut=0;
                    rotClickedNum=0;
                    rotSaveOut=false;
//                    clickZoomAndroid=0;
//                    switchNum=(int)flatRotateKide.get(pagePosition).floatValue();
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
            }break;
        }
        return super.onOptionsItemSelected(item);
    }

    
    Toolbar.OnMenuItemClickListener menuItemClickListener=new Toolbar.OnMenuItemClickListener() {// 전송, 삭제 버튼 처리
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()){
// =================== 갤러리 메뉴====================
                case R.id.send:// 사진 리스트 선택으로 UI전환 하기
                    if (SELECT_IMAGES.equals("C")){// 선택일때

                        Log.e("SELECT_IMAGES", " : "+SELECT_IMAGES);
                        item.setTitle("전송");// 선택 누르면 전송으로 변경 구분값 추가해야함.
                        SELECT_IMAGES="S";
                        Log.e("SELECT_IMAGES", " : "+SELECT_IMAGES);
                        bindAdapter();
                        Log.e("testNumSendIn", " : "+1);
                        new_adapter.setSelectType(SELECT_IMAGES);
                        Log.e("testNumSendIn", " : "+2);
                        deletMenu.setVisible(true);// 툴바에 삭제 매뉴 표시
                        cancelMeun.setVisible(true);

                        Log.e("testNumSendIn", " : "+3);
                        Log.e("testNumSendIn", " : "+4);
                        setTransactionFragments();

                        // 기본갤러리로 전환할때 툴바, 리스트의 세팅값을 초기화 해야함.

                    }else if (SELECT_IMAGES.equals("S")){// 전송버튼 눌렀을때.
                        ArrayList<String> selectArr=new ArrayList<>();
                        Log.d("sendDataList", selectArr.size()+"");// 선택파일정보 지워지는지 확인
                        for (int i = 0; i < imageData.size(); i++) {
                            if (imageData.get(i).getCheck()==1){
                                selectArr.add(imageData.get(i).getImagePath());
                            }
                        }// fori끝
                        
                        if (selectArr.size()==0){// 전송할 사진 없을때 토스트 표시하기.
                            Toast toast= Toast.makeText(GridViewGallery.this, "전송할 사진을 선택해 주세요.", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0,0);
                            toast.setGravity(Gravity.TOP, 0, 400);// 아래로 400
                            toast.show();
                        }else {//  전송할 데이터 있을때 이쪽으로 들어옴
                            Log.d("sendDataList", selectArr.toString());// 선택한 파일들
                            dialogSet("선택한 사진을 전송 하시겠습니까?", "선택사진 전송", "전송", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {// 전송버튼 누를때 동작할 다이얼로그
                                    selectMenu.setTitle("선택");
            //                        cancelMeun.setTitle("취소");
                                    SELECT_IMAGES="C";
            //                        new_adapter.setSelectType(SELECT_IMAGES);
                                    Log.e("SELECT_IMAGES", " : "+SELECT_IMAGES);
                                    //findViewById(R.id.delete);
                                    for (int i = 0; i < imageData.size(); i++) {
                                        if (imageData.get(i).getCheck()==1){
                                            imageData.get(i).setCheck(0);
                                        }
                                    }
                                    bindAdapter();
                                    new_adapter.setSelectType(SELECT_IMAGES);
                                    setTransactionFragments();
                                    deletMenu.setVisible(false);// 툴바에 삭제 매뉴 제거
                                    cancelMeun.setVisible(false);
                                }// 전송확인 버튼 클릭시.
                            });

                        }
                    }// 전송버튼 클릭시 동작 내용
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
                case R.id.cancel:// 선택내역 취소하기. 선택화면 취소하기.
                    item.setTitle("취소");
                    sendMenu.setTitle("선택");
                    SELECT_IMAGES="C";
//                        new_adapter.setSelectType(SELECT_IMAGES);
                    Log.e("SELECT_IMAGES", " : "+SELECT_IMAGES);
                    //findViewById(R.id.delete);
                    for (int i = 0; i < imageData.size(); i++) {
                        if (imageData.get(i).getCheck()==1){
                            imageData.get(i).setCheck(0);
                        }
                    }
                    bindAdapter();
                    new_adapter.setSelectType(SELECT_IMAGES);
                    setTransactionFragments();
                    deletMenu.setVisible(false);// 툴바에 삭제 매뉴 제거
                    cancelMeun.setVisible(false);
                    break;
// =================== 큰사진 메뉴====================  하단으로 위치변경 및 텍스트 표시
                case R.id.rotate:// 저  장
                    // 회전시킨 이미지 정보 저장
                    dialogSet("변경된 정보를 저장하시겠습니까?", "저 장", "저 장", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            outNum=1;
                            zoomOut=0;
                            File file = null;
                            Collections.reverse(AC_imageData);
                            float rotateNumSet=0f;
//                            ArrayList<Float> floats=new ArrayList<>();
                            if (GALLERY_TYPE.equals("K")){// 키즈사랑 갤러리 파일
                                setOrientateionOfKidsLoveImage(imageData.get(pagePosition).ImagePath, imageData.get(pagePosition).rotNum);// 이미지에 회전값 테그 set하는방법.
                                rotateNumSet=imageData.get(pagePosition).getOrirotateNum();// 실제이미지는 한칸씩회전되어 저장됨 하지만 앱에서 보여지는 회전값이 오락가락함...
                                imageData.get(pagePosition).setOrirotateNum(imageData.get(pagePosition).getRotateNum());
                                imageData.get(pagePosition).setRotateNum(rotateNumSet);

                                new_adapter.setItem(imageData);


                                new_adapter.setType(GALLERY_TYPE);
                                file=new File(direct, imageData.get(pagePosition).ImageName);// 키즈사랑 갤러리에서 회전시켰을때 저장경로.

                            }else if (GALLERY_TYPE.equals("D")){// 기본 갤러리 파일
                                rotateNumSet=AC_imageData.get(pagePosition).getOrirotateNum();//
                                AC_imageData.get(pagePosition).setOrirotateNum(AC_imageData.get(pagePosition).getRotateNum());
                                AC_imageData.get(pagePosition).setRotateNum(rotateNumSet);
                                new_adapter.setItem(imagesArr,AC_imageData);
                                new_adapter.setType(GALLERY_TYPE);
//                                file은 사진 가져오는 경로
//                                Log.e("image_path", imagesArr.get(pagePosition));
                                file=new File(imagesArr.get(pagePosition));// 기본갤러리 파일
                            }


                            Bitmap bm=BitmapFactory.decodeFile(file.getAbsolutePath());
                            RectF viewRect=new RectF(0,0,bm.getWidth(), bm.getHeight());
//                            float centerX=viewRect.centerX();
//                            float centerY=viewRect.centerY(); , centerX, centerY
                            Matrix matrix=new Matrix();
                            if (GALLERY_TYPE.equals("K")){// 키즈사랑 갤러리 회전값
//                                matrix.postRotate(imageData.get(pagePosition).rotateNum);
                                if (matrix.postRotate(imageData.get(pagePosition).getRotateNum())){
//                                    matrix.setRotate(imageData.get(pagePosition).getRotateNum());
                                }else {
                                    matrix.setRotate(imageData.get(pagePosition).getRotateNum());
                                }
                            }else {// 기본갤러리 회전값
//                                matrix.postRotate(AC_imageData.get(pagePosition).rotateNum);// 인덱스 에러 회전값 저장//
                                if (matrix.postRotate(AC_imageData.get(pagePosition).getRotateNum())){
//                                    Toast.makeText(GridViewGallery.this, ""+AC_imageData.get(pagePosition).getRotateNum(), Toast.LENGTH_SHORT).show();
                                }else {
                                    matrix.setRotate(AC_imageData.get(pagePosition).getRotateNum());
                                }
                            }
//                            matrix.postRotate(floats.get(pagePosition));// 인덱스 에러 회전값 저장//
                            try {
                                Log.e("matrixShow", " - width : "+bm.getWidth());
                                bm=Bitmap.createBitmap(bm, 0,0,bm.getWidth(),bm.getHeight(),matrix,true);
                            }catch (NullPointerException e){
                                e.printStackTrace();
                            }

//                            file.delete();
//                            imageData.remove(pagePosition);



                            FileOutputStream fOut=null;
                            try {
                                if (GALLERY_TYPE.equals("K")){// 키즈사랑 갤러리에서 저장 누를시 저장 경로
                                    fOut=new FileOutputStream(file);
                                    bm.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                                    fOut.flush();//덮어씌우기
                                    fOut.close();
//                                    switchNum=getOrientateionOfImage(imageData.get(pagePosition).ImagePath);
                                }else if (GALLERY_TYPE.equals("D")){// 기본 갤러리에서 저장 누를시 저장경로
//                                    File file2=new File(direct, AC_imageData.get(pagePosition).ImageName);
                                    File file2=new File(direct, "cp_"+new SimpleDateFormat("yyyy_MM_dd_HHmmss").format(new Date())+".jpg");
                                    fOut=new FileOutputStream(file2);
                                    bm.compress(Bitmap.CompressFormat.JPEG, 100, fOut);

                                    ByteArrayOutputStream stream=new ByteArrayOutputStream();
                                    bm.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                                    byte[] bytes=stream.toByteArray();

                                    fOut.write(bytes);//새로쓰기
                                    fOut.close();

                                    Collections.reverse(imageData);// 저장하는 데이터 뒤집기
                                    imageData.add(new ImageData(
                                            AC_imageData.get(pagePosition).ImageName,
//                                            AC_imageData.get(pagePosition).ImagePath,
                                                    imagesArr.get(pagePosition),
                                            0, 0, 0, AC_imageData.get(pagePosition).getRotateNum(),
                                            "K"
                                    ));

                                    Collections.reverse(imageData);// 저장완료 후 다시 뒤집기
                                    new_adapter.setItem(imageData);// 기본갤러리에서 받아온 사진을 키즈사랑 갤러리에 저장하기때문에 키즈사랑 갤러리 데이터만 갱신함
                                    new_adapter.setType(GALLERY_TYPE);
//                                    new_adapter.notifyDataSetChanged();// newimage를 다시 실행해야할듯
                                }
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Collections.reverse(AC_imageData);

                            FragmentTransaction tran = manager.beginTransaction();
                            tran.remove(fragments[0]);
                            fragments[0]=new New_images_FG(imageData);
                            new_adapter.notifyDataSetChanged();// newimage를 다시 실행해야할듯
                            tran.replace(R.id.pager_gridview, fragments[0]);
                            tran.commit();// new fragment다시실행
                            // 저장 메소드 추가
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
                            rotClickedNum=1;//
                            if (GALLERY_TYPE.equals("K")){
                                imageData.get(pagePosition).setRotateNum(imageData.get(pagePosition).getOrirotateNum());
                            }else {
                                AC_imageData.get(pagePosition).setRotateNum(AC_imageData.get(pagePosition).getOrirotateNum());
                            }//}else if (rotSaveOut && outNum!=1 || rotClickedNum==1){
                        }
                    });
                    break;
            }
            return false;
//            return true;
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


        if (GALLERY_TYPE.equals("K")){
            if (returnVal==false){
                returnVal=true;
            }

            switch ((int) imageData.get(pagePosition).getRotateNum()){
                case 90:
                    imageData.get(pagePosition).setRotateNum(180f);
                    Log.e("kidsLoveRotPath", imageData.get(pagePosition).ImagePath);
                    break;
                case 180:
                    imageData.get(pagePosition).setRotateNum(270f);
                    Log.e("kidsLoveRotPath", imageData.get(pagePosition).ImagePath);
                    break;
                case 270:
                    imageData.get(pagePosition).setRotateNum(0f);
                    Log.e("kidsLoveRotPath", imageData.get(pagePosition).ImagePath);
                    break;
                case 0:
                    imageData.get(pagePosition).setRotateNum(90f);
                    Log.e("kidsLoveRotPath", imageData.get(pagePosition).ImagePath);
                    break;
            }
            // 회전할때마다 저장되는 이미지 경로값이 같음..

            Log.e("ImgFGpager_Test : "," iD_G : "+imageData.get(pagePosition).getRotateNum());
            // 회전시킨 이미지 저장
            // 메뉴 버튼 변경

            bundle.putInt("position", pagePosition);
            bundle.putParcelableArrayList("imageData", imageData);
            bundle.putInt("degree", (int) imageData.get(pagePosition).getRotateNum());

        }else {// 기본갤러리에서 회전버튼 클릭
            if (returnVal==false){
                returnVal=true;
                if (rotClickedNum==1){// 처음 회전버튼 눌렀을때만 데이터 반전
                    Collections.reverse(AC_imageData);  //  회전시킬때 데이터 회전시킴..
                }
            }

            switch ((int) AC_imageData.get(pagePosition).getRotateNum()){
                case 90:AC_imageData.get(pagePosition).setRotateNum(180f);break;
                case 180:AC_imageData.get(pagePosition).setRotateNum(270f);break;
                case 270:AC_imageData.get(pagePosition).setRotateNum(0f);break;
                case 0:AC_imageData.get(pagePosition).setRotateNum(90f);break;
            }

            bundle.putInt("position", pagePosition);
            bundle.putParcelableArrayList("AC_imageData", AC_imageData);
            bundle.putInt("degree", (int) AC_imageData.get(pagePosition).getRotateNum());
            Log.e("ImgFGpager_Test", ", AC_G : "+AC_imageData.get(pagePosition).getRotateNum());
            bundle.putStringArrayList("images", imagesArr);
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
//                returnVal=false;
//                outNum=1;
//                zoomOut=1;
                if (GALLERY_TYPE.equals("K")){
                    new_adapter.imgDelete(pagePosition);
                    imageData.remove(pagePosition);
                    if (lists.length==pagePosition){
//                        길이확인 이거 왜 확인한건지 기억해보기
                    }else {
                        lists[pagePosition].delete();
                    }
                    commitClickFG();// 데이터 넘기고 페이저 commit하는 코드
                } else if (GALLERY_TYPE.equals("D")) {// 기본갤러리 사진 지우는 코드
//                    Log.e("getAbsolutePath", " - "+file.getAbsolutePath());
                    Log.e("getAbsolutePath", " - "+imagesArr.get(pagePosition));

                    deleteUri();// 기본갤러리에서 사진삭제
                    Collections.reverse(AC_imageData);// 이부분은 데이터 무조건 뒤집어야함
                }
                Snackbar.make(viewPager, "사진이 삭제 되었습니다.", Snackbar.LENGTH_LONG).show();
                returnVal=false;
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

    private void bindAdapter(){// 어뎁터 생성 및 어뎁터의 데이터 갱신
        Log.e("Click_S",  "GALLERY_TYPE["+GALLERY_TYPE+"], SELECT_IMAGES["+SELECT_IMAGES+"]");
//        if (GALLERY_TYPE.equals("K")){// 키즈사랑 갤러리
//            Log.e("Click_S",  "   in K: "+SELECT_IMAGES);
//            if(new_adapter == null){
//                Log.e("Click_S", "new_adapter == null");
//                new_adapter=new New_Adapter(GridViewGallery.this, imageData);// GALLERY_TYPE추가하여 데이터 전송하기.
//                new_adapter.setType("K");
//            }else{
//                Log.e("Click_S", "new_adapter != null, getItemType = [" + new_adapter.getSelectType()+"]");
//                new_adapter.setItem(imageData);
//                Log.e("Click_S", "111 getItemType = [" + new_adapter.getSelectType()+"]");
//                new_adapter.notifyDataSetChanged();// 데이터 변경 알림.
//                Log.e("Click_S", "222 getItemType = [" + new_adapter.getSelectType()+"]");
//            }
//        }else {// D : 기본갤러리 어뎁터 넣는 부분
////            if (android_adapter==null){
////                android_adapter=new Android_Adapter(GridViewGallery.this,imageData, imagesArr);
////            }else {
////                android_adapter.setItem(imagesArr, imageData);
////                android_adapter.notifyDataSetChanged();
////            }
//            if (new_adapter==null){
//                new_adapter=new New_Adapter(GridViewGallery.this, AC_imageData, imagesArr);
//                new_adapter.setType("D");
//            }else {
//                new_adapter.setItem(imagesArr ,AC_imageData);
//                new_adapter.setType("D");
//                new_adapter.notifyDataSetChanged();
//            }
//        }

        if(new_adapter == null){
            Log.e("Click_S", "new_adapter == null");
            new_adapter=new New_Adapter(GridViewGallery.this, imageData);// GALLERY_TYPE추가하여 데이터 전송하기.
            new_adapter.setType("K");
            new_adapter.setItem(imageData);
            new_adapter.notifyDataSetChanged();
        }else{
            Log.e("Click_S", "new_adapter != null, getItemType = [" + new_adapter.getSelectType()+"]");
            new_adapter.setItem(imageData);
            Log.e("Click_S", "111 getItemType = [" + new_adapter.getSelectType()+"]");
            new_adapter.notifyDataSetChanged();// 데이터 변경 알림.
            Log.e("Click_S", "222 getItemType = [" + new_adapter.getSelectType()+"]");
        }

        if (new_adapter==null){
            new_adapter=new New_Adapter(GridViewGallery.this, AC_imageData, imagesArr);
            new_adapter.setType("D");
            new_adapter.setItem(imagesArr ,AC_imageData);
            new_adapter.notifyDataSetChanged();
        }else {
            new_adapter.setItem(imagesArr ,AC_imageData);
            new_adapter.setType("D");
            new_adapter.notifyDataSetChanged();
        }

    }

    private void commitClickFG(){// 큰이미지 보여주기.
        Bundle bundle=new Bundle();
        bundle.putInt("position", pagePosition);
        bundle.putParcelableArrayList("imageData", imageData);
        if (GALLERY_TYPE.equals("D")){
            bundle.putStringArrayList("images", imagesArr);
            bundle.putInt("position", pagePosition);
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
        if (checked==0){// 학제할 사진이 없을때.
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
                            if (imageData.get(i).getCheck() == 1) {
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
                        imageData.add(new ImageData(
                                lists2[i].getName(),
                                lists2[i].getPath(),
                                0,
                                0,
                                0,
                                getOrientateionOfImage(lists2[i].getPath()),
                                "K"
                        ));
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
                    if (actionBar.getTitle().toString().equals("기본갤러리")){
                        actionBar.setTitle("키즈사랑앨범");
                        for (int i = 0; i < imageData.size(); i++) {
                            imageData.get(i).Check=0;// 클릭 내용 초기화
                        }
                        for (int i = 0; i < AC_imageData.size(); i++) {
                            AC_imageData.get(i).Check=0;
                        }
//                        new_adapter=new New_Adapter(GridViewGallery.this, imageData);
//                        new_adapter.notifyDataSetChanged();// 데이터 변경 알림.
//                        new_adapter.setType("K");
                        if (selectMenu.getTitle().toString().equals("전송")){//툴바버튼 이름이 전송일때
                            selectMenu.setTitle("선택");
                            deletMenu.setVisible(false);
                        }
                        GALLERY_TYPE="K";
                        SELECT_IMAGES="C";

                        Log.e("typeVal", "- inNavK : "+GALLERY_TYPE);
//                        bindAdapter();
//                        setTransactionFragments();

                        tran.remove(fragments[0]);
                        fragments[0]=new New_images_FG(imageData);
                        new_adapter.notifyDataSetChanged();// newimage를 다시 실행해야할듯
                        tran.replace(R.id.pager_gridview, fragments[0]);
                        tran.commit();// new fragment다시실행

                        pagePosition=0;
                        Log.d("clickedKidsAlbum", " - num1 : "+SELECT_IMAGES);
                        break;
//                    tran.replace(R.id.pager_gridview, fragments[0]);
                    }else {// 여기로 들어갈때만 초기화됨
                        setTransactionFragments();
                        pagePosition=0;
                        Log.d("clickedKidsAlbum", " - num2 : "+SELECT_IMAGES);
                        break;
                    }


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
                    for (int i = 0; i < imageData.size(); i++) {
                        imageData.get(i).Check=0;
                    }
                    for (int i = 0; i < AC_imageData.size(); i++) {
                        AC_imageData.get(i).Check=0;
                    }
                    // 데이터 변경 알림 추가하기 - 어뎁터에
//                    new_adapter.setType("D");
                    GALLERY_TYPE="D";
                    SELECT_IMAGES="C";
                    if (selectMenu.getTitle().toString().equals("전송")){//툴바버튼 이름이 전송일때
                        selectMenu.setTitle("선택");
                        deletMenu.setVisible(false);
                    }

                    Log.e("typeVal", "- inNavD : "+GALLERY_TYPE);
//                    bindAdapter();
                    tran.remove(fragments[2]);
                    fragments[2]=new Android_images_FG(imagesArr, imageData);
                    new_adapter.notifyDataSetChanged();// newimage를 다시 실행해야할듯
                    tran.replace(R.id.pager_gridview, fragments[2]);

                    pagePosition=0;
                    tran.commit();// 밖에있었음
                    break;
            }

            return true;
        }
    };

    public void click_rotate(View view) {// 큰 이미지 회전
        if (rotClickedNum==0) {
            rotateImage();
            rotClickedNum=1;// 회전버튼 1번 누르면 1로 변경
        }else if (returnVal){
            rotateImage();
        }
    }

    public void click_delete(View view) {// 이미지 삭제
        click_Delete();
    }// 큰 이미지 삭제

    public void click_check(View view) {// 큰 이미지 선택
        FragmentTransaction tran=manager.beginTransaction();
        //
        Log.d("SELECTIMAGES", " SELECT_TYPE : "+SELECT_IMAGES);


        if (GALLERY_TYPE.equals("D")){
            if (AC_imageData.get(pagePosition).Check==0){
                AC_imageData.get(pagePosition).Check=1;// 이 값이 남아있음.
                Glide.with(this).load(R.drawable.ic_check_circle_w_24).into(checkImgView);
            }else {
                AC_imageData.get(pagePosition).Check=0;
                Glide.with(this).load(R.drawable.ic_check_circle_b_24).into(checkImgView);
            }
//
//            if (SELECT_IMAGES.equals("C")){// 툴바메뉴가 선택으로 되어있을때
//                SELECT_IMAGES="S";
//                tran.remove(fragments[0]);// 키즈사랑 갤러리 기존 데이터 삭제
//                fragments[0]=new New_images_FG(imageData);
//                new_adapter.notifyDataSetChanged();// 키즈사랑 갤러리 데이터 변경알림
//                tran.replace(R.id.pager_gridview, fragments[0]);
//                tran.commit();// 키즈사랑 갤러리 다시실행
//            }
        }else {
            if (imageData.get(pagePosition).Check==0) {
                imageData.get(pagePosition).Check=1;// 선택한 이미지 체크
                Glide.with(this).load(R.drawable.ic_check_circle_w_24).into(checkImgView);
            }else {
                imageData.get(pagePosition).Check=0;
                Glide.with(this).load(R.drawable.ic_check_circle_b_24).into(checkImgView);
            }

//            if (SELECT_IMAGES.equals("C")){// 툴바메뉴가 선택으로 되어있을때
//                SELECT_IMAGES="S";
//                tran.remove(fragments[2]);// 기본갤러리 기존 데이터 삭제
//                fragments[2]=new Android_images_FG(imagesArr,imageData);
//                new_adapter.notifyDataSetChanged();// 기본갤러리 데이터 변경알림
//                tran.replace(R.id.pager_gridview, fragments[2]);
//                tran.commit();// 기본갤러리 다시실행
//            }
        }

    }

    public void bigImageToolbarItem_clear(){
        lockImg.setVisibility(View.GONE);
        Menu menu_re=toolbar.getMenu();
        menu_re.removeGroup(R.id.bigimg_click_menu);
        rotClickedNum=0;
        returnVal=false;
        rotSaveOut=true;
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
//        fragments[1]=new Date_images_FG(imageData);
        fragments[2]=new Android_images_FG(imagesArr, AC_imageData);
//        fragments[2]=new Android_images_FG(testPath.getPath());
    }


    public void finishBigImage(){// 이미지 크게보기 종료
        if (bnv.getVisibility()==View.GONE){// 이미지 크게 보기 상태일때 백버튼
            // 툴바의 버튼 설정
            lockImg.setVisibility(View.GONE);// 잠금표시 지움
            manager.beginTransaction().remove(new Clicked_BigImage_FG()).commit();// 큰이미지화면 지우기
            manager.popBackStack();
            toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.kidsLove_color));// 툴바색 변경 다르게 됨.
            toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
            Log.e("backstackcount", " - count : "+manager.getBackStackEntryCount());

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


    public void setTransactionFragments(){
        FragmentTransaction tran = manager.beginTransaction();
        switch (GALLERY_TYPE){
            case "K":{
                new_adapter.setType(GALLERY_TYPE);
                if (fragments[0].isAdded()){
                    tran.remove(fragments[0]);
                    fragments[0]=new New_images_FG(imageData);
                    Bundle bundle=new Bundle();
                    bundle.putString("Type", SELECT_IMAGES);
                    fragments[0].setArguments(bundle);
//                    Log.e("fragmentShow", " - fragments[0]");
                }
//                else {
//                    Log.e("fragmentShow", " - else fragments[0]");
//                }
                tran.replace(R.id.pager_gridview, fragments[0]);
                Log.e("fragmentShow", " - end fragments[0]");
            }break;
//            case 1:{
//                if (fragments[1].isAdded()){ tran.remove(fragments[1]);  fragments[1]=new Date_images_FG(imageData); }
//                tran.replace(R.id.pager_gridview, fragments[1]);
//            }break;
            case "D":{
                new_adapter.setType(GALLERY_TYPE);
                if (fragments[2].isAdded()){
                    tran.remove(fragments[2]);
                    fragments[2]=new Android_images_FG(imagesArr, AC_imageData);
                    Bundle bundle=new Bundle();
                    bundle.putString("Type", SELECT_IMAGES);
                    fragments[2].setArguments(bundle);//번들받기

//                    fragments[2]=new Android_images_FG(testPath.getPath());
                }
                tran.replace(R.id.pager_gridview, fragments[2]);
            }break;
        }
//        Log.e("fragmentShow", " - end fragments[0]");
        tran.commit();
    }


    public void click_locked(View view) {
            Toast.makeText(this, "저장되지 않은 정보가 있습니다.", Toast.LENGTH_SHORT).show();
    }

    public ArrayList<ImageFolder> getImageFolders(){// 기본갤러리 데이터값 받아오는 부분
        ArrayList<ImageFolder> picFolders=new ArrayList<>();
        ArrayList<String> picPaths=new ArrayList<>();
        Uri allImagesUri=MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection={
                MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.ORIENTATION
        };
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

    public Uri getUriArr(){
        String fileName=Uri.fromFile(direct).getPath()+lists[0].getName();
        Uri fileUri=Uri.parse(fileName);
        String filePath=fileUri.getPath();
        Cursor cursor=getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, "_data="+filePath+"",null, null);
        cursor.moveToNext();
        int id=cursor.getInt(cursor.getColumnIndex("_id"));
        Uri uri=ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

        return uri;
    }


    public ArrayList<String> getAllShownImagesPath(){// 기본갤러리 사진 받아옴
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name,column_index_path,column_index_ori;
        int testName;
        ArrayList<String> listOfAllImages=new ArrayList<>();
        String absolutePathOfImage=null;
        uri=MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        Log.e("URI", " - "+uri.toString());
        Log.e("showDirect", uri.toString());

        String[] projection={
                MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.ImageColumns.RELATIVE_PATH,
                MediaStore.Images.Media.RELATIVE_PATH,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.ORIENTATION
        };
        cursor=getContentResolver().query(uri, projection, null, null, null);
        column_index_data=cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);

//        29버전 (Q)에서 사진 로드되지 않음=> android 11버전사용하는 디바이스는 android:requestLegacyExternalStorage="true" 하지 않아도 정상적으로 로드됨...
        column_index_path=cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.RELATIVE_PATH);
        testName=cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DISPLAY_NAME);
        column_index_ori=cursor.getColumnIndexOrThrow(MediaStore.Images.Media.ORIENTATION);

        while (cursor.moveToNext()){
            absolutePathOfImage=cursor.getString(column_index_data);
//            absolutePathOfImage="sdcard/";
//            absolutePathOfImage=cursor.getString(column_index_path);
            Log.e("filePathError : ", " - 01 path :  "+cursor.getString(column_index_path));
            Log.e("filePathError : ", " - 02 name :  "+cursor.getString(testName));// 이거사용해서 삭제
            Log.e("filePathError : ", " - 03 data :  "+absolutePathOfImage);
            Log.e("getOri_cursor", ""+cursor.getString(column_index_ori));// 기본갤러리 회전값 가져오는 코드
//            AC_imageData.add(new ImageData(cursor.getString(testName), absolutePathOfImage, 0, cursor.getPosition(), getOrientateionOfImage(cursor.getString(column_index_path)), getOrientateionOfImage(cursor.getString(column_index_path))));
            AC_imageData.add(new ImageData(cursor.getString(testName), absolutePathOfImage, 0, cursor.getPosition(), "D"));
            imageData.add(new ImageData(cursor.getString(testName), absolutePathOfImage, 0, cursor.getPosition(),"D"));
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

//        Collections.reverse(AC_imageData);//    이미지 데이터 다시 뒤집어서 삭제할 Uri에 넣기.

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
                try {// 삭제 버튼 클릭시
                    startIntentSenderForResult(intentSender, DELETE_PERMISSION_REQUEST, null, 0, 0, 0, null);
                } catch (IntentSender.SendIntentException sendIntentException) {
                    sendIntentException.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode== Activity.RESULT_OK && requestCode==DELETE_PERMISSION_REQUEST){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                Toast.makeText(this, "in_request", Toast.LENGTH_SHORT).show();
                getContentResolver().delete(contentUri, null, null);
                if (rotClickedNum==1){
                    Collections.reverse(AC_imageData);
                }
                AC_imageData.remove(pagePosition);
                imagesArr.remove(pagePosition);
                new_adapter.imgDelete(pagePosition);
                commitClickFG();
                returnVal=false;
                outNum=1;
                zoomOut=1;
            }
        }else {// 삭제 거부시
            Collections.reverse(AC_imageData);
        }
    }

    public int getOrientateionOfImage(String filePath){
        ExifInterface exif=null;
        try {
            exif=new ExifInterface(filePath);
        } catch (IOException e) {// 경로 찾지 못할때 -1 리턴
            e.printStackTrace();
            return -1;
        }

        int orientation=exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
        Log.e("getorientationExif", " - "+orientation);
        if (orientation != -1){
            switch (orientation){
                case ExifInterface.ORIENTATION_ROTATE_90:return 90;
                case ExifInterface.ORIENTATION_ROTATE_180:return 180;
                case ExifInterface.ORIENTATION_ROTATE_270:return 270;
            }
        }
        return 0;
    }

    public void setOrientateionOfKidsLoveImage(String filePath, int rotVal){// 키즈사랑 갤러리에 이미지 저장시 회전값 저장하는법
        ExifInterface ei;
        try {
            ei=new ExifInterface(filePath);
            ei.setAttribute(ExifInterface.TAG_ORIENTATION, String.valueOf(rotVal));
            ei.saveAttributes();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public int getOrientationOfKidsLoveImage(String filePath){
        ExifInterface ei;

        String ori;
        int returnVal=0;
        try {
            ei=new ExifInterface(filePath);
            ori=ei.getAttribute(ExifInterface.TAG_ORIENTATION);
            returnVal=Integer.parseInt(ori);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnVal;
    }

    class ImageLoadThread implements Runnable{// 이미지 로드 스레드
        @Override
        public void run() {
            for (int i = 0; i < imagesArr.size(); i++) {
                float num=getOrientateionOfImage(imagesArr.get(i));
                AC_imageData.get(i).setRotateNum(num);
                AC_imageData.get(i).setOrirotateNum(num);
//            AC_imageData.get(i).setImageType(DEFAULT_TYPE);
                Log.e("showDImageOri", " - ori "+getOrientateionOfImage(imagesArr.get(i))+" / name : "+imagesArr.get(i));
            }
            // 핸들러로 메시지 줬을때 터치할 수 있게 하기
            imageloading=1;// 이미지 로드 왼료됨.
//            Thread thread=new Thread(this);
//            Log.e("getState", "- endRun : "+thread.getState()); => NEW
        }
    }




}// class