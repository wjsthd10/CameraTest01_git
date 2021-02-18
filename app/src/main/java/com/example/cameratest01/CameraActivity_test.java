package com.example.cameratest01;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.media.ExifInterface;
import android.media.ImageReader;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Vibrator;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cameratest01.Kids_gallery.GalleryActivity;
import com.example.cameratest01.MyInterface.OnItemClick;
import com.example.cameratest01.MyInterface.RemoveFG_OnClick;
import com.example.cameratest01.RC_Adapters.RC_CameraButton_Adapter;
import com.example.cameratest01.RC_Adapters.RC_ImageAdapter;
import com.example.cameratest01.RC_Items.Image_Parcelable_Item;
import com.example.cameratest01.RC_Items.RC_CameraButton_items;
import com.example.cameratest01.RC_Items.RC_ImageItems;
import com.example.cameratest01.customDialog.CustomDialog;
import com.example.cameratest01.fragments.BigImageFragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CameraActivity_test extends AppCompatActivity implements OnItemClick{



    public static MySurfaceCameraView mCameraView;
    SurfaceHolder holder;
    static Camera camera;
    int RESULT_PERMISSIONS=100;

    public static CameraActivity_test getInstance;

    Vibrator vibrator;
//    리사이클러뷰
    RecyclerView rc_imageList, rc_chileList, rc_buttonList;
//    어뎁터

    ArrayList<String> strArr=new ArrayList<>();

    RC_CameraButton_Adapter cameraButtonAdapter;
//    아이템리스트
    public ArrayList<RC_ImageItems> imageItems=new ArrayList<>();
    ArrayList<RC_CameraButton_items> buttonItems=new ArrayList<>();
    public CircleImageView camera_act_image, img_gallery;
    ImageView ivList_show, ivButton_show;
    public byte[] itemArr;
    ArrayList<Bitmap> bitMapData=new ArrayList<>() ;


    public LinearLayout fragment_lay;



    ArrayList<Image_Parcelable_Item> mParcelableItems=new ArrayList<>();

    File direct;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getInstance=this;
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.camera_act_v2_test);
        fragment_lay=findViewById(R.id.fragment_lay);

        camera_act_image=findViewById(R.id.camera_act_image);
        img_gallery=findViewById(R.id.camera_act_image_showImg);
        mCameraView=findViewById(R.id.cv_myCamera);

        rc_imageList=findViewById(R.id.camera_act_rc_imageList);
        rc_chileList=findViewById(R.id.camera_act_rc_childList);
        rc_buttonList=findViewById(R.id.camera_act_rc_button_lay);
        ivList_show=findViewById(R.id.rc_imaeglist_showButton);
        ivButton_show=findViewById(R.id.rc_buttonlist_showButton);

        cameraButtonListSet();
        cameraButtonAdapter=new RC_CameraButton_Adapter(CameraActivity_test.this, buttonItems);
        rc_buttonList.setAdapter(cameraButtonAdapter);
        ivList_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageList(v);
            }
        });
        direct=new File(this.getFilesDir(), "CameraTest01"+File.separator+"kidsLove");
        if (!direct.exists()) {
            direct.mkdirs();
        }
        File[] files=direct.listFiles();
        if (files.length>0){
            Glide.with(this).load(files[files.length-1]).into(img_gallery);
        }

        img_gallery.setRotation(90);

        holder=mCameraView.getHolder();
        holder.addCallback(mCameraView);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);


    }

    public static Camera getCamera(){
        return camera;
    }

    private void setInit(){
        getInstance=this;

    }

    @Override
    protected void onResume() {
        super.onResume();
        File[] files=direct.listFiles();
        if (files.length>0){
            Glide.with(this).load(files[files.length-1]).into(img_gallery);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        mCameraView.camera.startPreview();
        Log.e("OpenCamera : ", "InTouchEvent");
        mCameraView.camera.autoFocus(new Camera.AutoFocusCallback() {// todo 오토포커스에서 에러남 확인
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                Log.e("OpenCamera : ", "InAutoFocus");
                try {
                    if (success){
                        Log.e("OpenCamera : ", "InSuccess");
                        mAutoFocus.onAutoFocus(success, camera);
                        camera.startPreview();// 카메라 미리보기 화면 갱신
                    }
                }catch (Exception e){
                    Log.e("OpenCamera : ", "open Exception");
                }
//                camera.startPreview();// 카메라 미리보기 화면 갱신
            }
        });// 터치시작하면 바로 포커스 맞춤

        return super.onTouchEvent(event);
    }// 터치했을때 실행되는 메소드

    Camera.AutoFocusCallback mAutoFocus =new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, Camera camera) {
            if (success){
                camera.startPreview();
            }
        }
    };



    public void takePicture(View view) {// todo 사진 촬영 버튼
        mCameraView.camera.startPreview();
        mCameraView.camera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                if (success){
                    camera.takePicture(null, null, new Camera.PictureCallback() {
                        @Override
                        public void onPictureTaken(byte[] data, Camera camera) {
                            Bitmap bm= BitmapFactory.decodeByteArray(data,0,data.length);// 사진을 바이트로 변환
//                            camera_act_image.setImageBitmap(bm);
//                            img_gallery.setImageBitmap(bm);// 받은 바이트데이터로 이미지뷰에 그리기.
//                            img_gallery.setRotation(90);

                            itemArr=data;// byte[] 데이터 저장하여 bundle로 보내기 중
                            bitMapData.add(bm);
                            img_gallery.setImageBitmap(bm);// 촬영한 이미지 왼쪽 동그란 이미지뷰에 보여줌
                            imageItems.add(new RC_ImageItems(data, "childName"));

                            RC_ImageAdapter imageAdapter=new RC_ImageAdapter(CameraActivity_test.this, imageItems, CameraActivity_test.this::onClick, data);
                            rc_imageList.setAdapter(imageAdapter);

                            SimpleDateFormat dateFormat=new SimpleDateFormat("yyMMddHHmmss_");// _쓴이유는 다음에 원생코드 번호 들어가야하기 때문.
                            Log.e("format", dateFormat.format(new Date()));
                            String fileName=dateFormat.format(new Date())+"ski"+"001_"+"002_";// 파일 이름에 원생코드 추가하기

                            strArr.add(fileName);

                            // todo 저장 위치 설정한 곳에 따라서 분기 지정.
                            // 저장경로 : data/data/com.example.cameratest01/Images/사진저장됨.
                            createCustomFile(fileName,bm);// 앱 내부에 저장 코드 호출

                            camera.startPreview();//서페이서 프리뷰 다시 시작
                        }
                    });
                }
            }
        });// 흐린화면에서 포커스 잡고 사진 촬영.
    }

    public void createCustomFile(String fileName, Bitmap image){
//      기본 갤러리에 저장되지 않기 때문에 기본 갤러리에서 보이지 않음
//      앱 내부에 저장.
        direct=new File(this.getFilesDir(), "CameraTest01"+File.separator+"kidsLove");
        if (!direct.exists()) {
            direct.mkdirs();
        }
        File outFile=new File(direct, fileName+".jpg");
        try {
            FileOutputStream outputStream=new FileOutputStream(outFile);
            image.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void showImageList(View view){// 이미지 리스트 보여주는 버튼
        if (rc_imageList.getVisibility()!=View.VISIBLE && imageItems.size()>0){
            rc_imageList.setVisibility(View.VISIBLE);
        }else {
            if (imageItems.size()==0){
                vibrator= (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(100);
                Toast.makeText(this, "촬영한 사진이 없습니다.", Toast.LENGTH_SHORT).show();
                Thread thread=new Thread(new Runnable() {// 진동 간격 두고 2번울리는 구간.
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(400);
                            vibrator.vibrate(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.setDaemon(true);
                thread.start();
            }// 사진없을때 동작 부분 끝
            rc_imageList.setVisibility(View.GONE);
        }
    }

    public void cameraButtonListSet(){
        buttonItems.add(new RC_CameraButton_items(R.drawable.ic_baseline_child_care_24, "반 / 부"));// 제거
        buttonItems.add(new RC_CameraButton_items(R.drawable.ic_baseline_select_all_24, "전  체"));
        buttonItems.add(new RC_CameraButton_items(R.drawable.ic_baseline_person_add_24, "인원지정"));
        buttonItems.add(new RC_CameraButton_items(R.drawable.ic_baseline_person_search_24, "미촬영자"));
//        우측 기능 버튼들 리스트( 반/부[리스트 보여줌], 전체[버튼 보여줌/데이터가져가서], 인원지정[리스트 보여줌], 미촬영자[리스트 보여줌] )
    }

    public void showImageAll(View view) {
        // todo 촬영한 사진의 임시저장정보를 갤러리에 보여주고 선택하여 작성페이지로 이동할 수 있도록 해야함.
        Toast.makeText(this, "갤러리 모양의 엑티비티 보여주기", Toast.LENGTH_SHORT).show();



    }

    public void showButtonList(View view) {
        if (rc_buttonList.getVisibility()!=View.VISIBLE){
            Glide.with(this).load(R.drawable.ic_top_arrow_24).into(ivButton_show);
            rc_buttonList.setVisibility(View.VISIBLE);
        }else {
            rc_buttonList.setVisibility(View.GONE);
            Glide.with(this).load(R.drawable.ic_bottom_arrow_24).into(ivButton_show);
        }
    }


    @Override
    public void onClick(ArrayList<RC_ImageItems> items, int position) {//

        BigImageFragment bigImageFragment=new BigImageFragment(fragment_lay, position, imageItems, itemArr);
//        Toast.makeText(this, items.get(position).imageName, Toast.LENGTH_SHORT).show();

        //        프레그먼트 보여주는 코드
        if (fragment_lay.getVisibility()!=View.VISIBLE){
            fragment_lay.setVisibility(View.VISIBLE);
            FragmentManager fragmentManager=getSupportFragmentManager();
            FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.bigimage_fg_lay, bigImageFragment);
            fragmentTransaction.commit();
            fragmentTransaction.show(bigImageFragment);
        }

    }//onclick

    public void showGallery(View view) {
//        클릭시 갤러리 보여줌
//        intent로 데이터 전달
        Intent intent=new Intent(this, GalleryActivity.class);
        Bundle bundle=new Bundle();
        bundle.putStringArrayList("fileName", strArr);
        intent.putExtras(bundle);
        startActivity(intent);



    }
}//main