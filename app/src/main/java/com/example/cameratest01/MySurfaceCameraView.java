package com.example.cameratest01;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.IOException;

public class MySurfaceCameraView extends SurfaceView implements SurfaceHolder.Callback {

    SurfaceHolder holder;
    Camera camera;

    public MySurfaceCameraView(Context context) {
        super(context);
    }

    public MySurfaceCameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        holder=getHolder();
        holder.addCallback(this);
    }


    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        camera=Camera.open(0);
        try {
            camera.setPreviewDisplay(holder);
            camera.setDisplayOrientation(90);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        Toast.makeText(getContext(), "뷰생성", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
        camera.startPreview();
        Toast.makeText(getContext(), "뷰변경 : "+width+" : "+height, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        camera.stopPreview();
        camera.release();
        camera=null;

//        Toast.makeText(getContext(), "뷰종료", Toast.LENGTH_SHORT).show();
    }

    public boolean capture(Camera.PictureCallback callback){
        if (camera!=null){
            camera.takePicture(null, null, callback);
            return true;
        }else {
            return false;
        }

    }

}
//Camera.AutoFocusCallback mAutoFocus = new Camera.AutoFocusCallback() {
//    public void onAutoFocus(boolean success, Camera camera) {
//        if (success) {
//
//            //FOCUS_MODE_CONTINUOUS_PICTURE 지원하지 않는 단말에서 촬영버튼을 눌렀을 경우
//            if(autofocusTakePicture == true){
//                autofocusTakePicture = false;
//
//                mSurface.mCamera.takePicture(shutterCallback, rawCallback,  jpegCallback);
//            }
//
//            //Log.d("myLog","success-->"+mSurface.mCamera.getParameters().getFocusMode());
//
//        }else{ //포커스 실패해도 사진을 촬영한다.
//            if(autofocusTakePicture == true){
//                autofocusTakePicture = false;
//
//                mSurface.mCamera.takePicture(shutterCallback, rawCallback,  jpegCallback);
//            }
//
//            //Log.d("myLog","fail-->"+mSurface.mCamera.getParameters().getFocusMode());
//
//        }
//
//    }
//}
