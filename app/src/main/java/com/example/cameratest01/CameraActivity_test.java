package com.example.cameratest01;


import android.content.Context;
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
import android.os.Handler;
import android.os.Vibrator;
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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cameratest01.RC_Adapters.RC_CameraButton_Adapter;
import com.example.cameratest01.RC_Adapters.RC_ImageAdapter;
import com.example.cameratest01.RC_Items.RC_CameraButton_items;
import com.example.cameratest01.RC_Items.RC_ImageItems;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CameraActivity_test extends AppCompatActivity {

    SurfaceView mSurfaceView;
    SurfaceHolder mSurfaceHolder;
    Handler mHandler;
    ImageReader mImageReader;
    CameraDevice cameraDevice;
    CaptureRequest.Builder mPreviewBuilder;
    CameraCaptureSession mSession;
    int mDeviceRotation;
    Sensor mAccelerometer;
    Sensor mMagnetometer;
    SensorManager mSensorManager;
    DeviceOrientation mDeviceOrientation;
    int mDSI_height, mDSI_width;
    MySurfaceCameraView mCameraView;
    Vibrator vibrator;
//    리사이클러뷰
    RecyclerView rc_imageList, rc_chileList, rc_buttonList;
//    어뎁터
    RC_ImageAdapter imageAdapter;
    RC_CameraButton_Adapter cameraButtonAdapter;
//    아이템리스트
    ArrayList<RC_ImageItems> imageItems=new ArrayList<>();
    ArrayList<RC_CameraButton_items> buttonItems=new ArrayList<>();

    CircleImageView camera_act_image;
    ImageView ivList_show, ivButton_show;


    private static final SparseIntArray ORIENTATIONS=new SparseIntArray();
    static {
        ORIENTATIONS.append(ExifInterface.ORIENTATION_NORMAL,0);
        ORIENTATIONS.append(ExifInterface.ORIENTATION_ROTATE_90, 90);
        ORIENTATIONS.append(ExifInterface.ORIENTATION_ROTATE_180, 180);
        ORIENTATIONS.append(ExifInterface.ORIENTATION_ROTATE_270, 270);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.camera_act_v2_test);
        camera_act_image=findViewById(R.id.camera_act_image);
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



    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mCameraView.camera.startPreview();
        mCameraView.camera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                camera.startPreview();// 카메라 미리보기 화면 갱신
            }
        });// 터치시작하면 바로 포커스 맞춤




        return super.onTouchEvent(event);
    }// 터치했을때 실행되는 메소드

    public void takePicture(View view) {// 사진 촬영 버튼
        mCameraView.camera.startPreview();
        mCameraView.camera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                if (success){
                    camera.takePicture(null, null, new Camera.PictureCallback() {
                        @Override
                        public void onPictureTaken(byte[] data, Camera camera) {
                            Bitmap bm= BitmapFactory.decodeByteArray(data,0,data.length);// 사진을 바이트로 변환
                            camera_act_image.setImageBitmap(bm);// 받은 바이트데이터로 이미지뷰에 그리기.
//                ==> 리사이클러뷰에 이미지 정보 add하기
                            camera_act_image.setRotation(90);
                            imageItems.add(new RC_ImageItems(bm, "childName"));
                            imageAdapter=new RC_ImageAdapter(CameraActivity_test.this, imageItems);
                            rc_imageList.setAdapter(imageAdapter);

//                오토 포커스 안됌.
//                todo 사진 저장소에 저장하는 코드 추가, 이미지 리스트 역순배치
//                저장시에 SimpleDateFormat로 폴더 신규 생성.


                            camera.startPreview();
                        }
                    });
                }
            }
        });// 흐린화면에서 포커스 잡고 사진 촬영.
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
        buttonItems.add(new RC_CameraButton_items(R.drawable.ic_baseline_child_care_24, "반 / 부"));
        buttonItems.add(new RC_CameraButton_items(R.drawable.ic_baseline_select_all_24, "전  체"));
        buttonItems.add(new RC_CameraButton_items(R.drawable.ic_baseline_person_add_24, "인원지정"));
        buttonItems.add(new RC_CameraButton_items(R.drawable.ic_baseline_person_search_24, "미촬영자"));
//        우측 기능 버튼들 리스트( 반/부[리스트 보여줌], 전체[버튼 보여줌/데이터가져가서], 인원지정[리스트 보여줌], 미촬영자[리스트 보여줌] )
    }

    public void showImageAll(View view) {
        // 촬영한 이미지 데이터 전달하면서 프레그먼트 보여주기.
        Toast.makeText(this, "갤러리 모양의 프레그먼트 보여주기", Toast.LENGTH_SHORT).show();
    }

    public void showButtonList(View view) {
        if (rc_buttonList.getVisibility()!=View.VISIBLE){
            Glide.with(this).load(R.drawable.ic_top_arrow_24).into(ivButton_show);
            rc_buttonList.setVisibility(View.VISIBLE);
        }else {
            rc_buttonList.setVisibility(View.GONE);
            Glide.with(this).load(R.drawable.ic_bottom_arrow_24).into(ivButton_show);
            // gone상태일때 마진주려고 했는데 강제 종료됨.
//            LinearLayout.LayoutParams mLayoutParams=(LinearLayout.LayoutParams)ivButton_show.getLayoutParams();
//            mLayoutParams.rightMargin=16;
//            ivButton_show.setLayoutParams(mLayoutParams);


        }
    }

//    public void takePicture(){
//        try {
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//                CaptureRequest.Builder captureRequestBuilder=cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
//                captureRequestBuilder.addTarget(mImageReader.getSurface());
//                captureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
//                captureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
//
//                mDeviceRotation=ORIENTATIONS.get(mDeviceOrientation.getOrientation());
//
//                captureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, mDeviceRotation);
//                CaptureRequest mCaptureRequest=captureRequestBuilder.build();
//
//            }
//
//        } catch (Exception e) {
//
//        }
//    }


//    public void takePicture(View view) {
//        mCameraView.camera.takePicture(null, null, new Camera.PictureCallback() {
//            @Override
//            public void onPictureTaken(byte[] data, Camera camera) {
//                Bitmap bm= BitmapFactory.decodeByteArray(data,0,data.length);
//                camera_act_image.setImageBitmap(bm);
//                camera_act_image.setRotation(90);
//            }
//        });
//    }


}