package com.example.cameratest01;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.List;

public class MySurfaceCameraView extends SurfaceView implements SurfaceHolder.Callback {

    SurfaceHolder holder;
    public Camera camera;
    public CountDownTimer mCountDownTimer;
    public List<Camera.Size> listPreviewSizes;
    public Camera.Size previewSize;
    Context context;

    public MySurfaceCameraView(Context context) {
        super(context);
    }

    public MySurfaceCameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        holder=getHolder();
        holder.addCallback(this);
//        camera = null;
        Log.e("OpenCamera : ", "OpenCamera");
        if (camera == null) {
            Log.e("OpenCamera : ", "if In");
            camera=Camera.open();
        }
        listPreviewSizes=camera.getParameters().getSupportedPreviewSizes();
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        camera=null;
        try {
            if (camera ==null){
                Log.e("OpenCamera : ", "In SurfaceCreated");
                camera=Camera.open();
            }
            Camera.Parameters parameters=camera.getParameters();
            if (getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
                parameters.set("orientation", "portrait");
                camera.setDisplayOrientation(90);
                parameters.setRotation(90);
            }else {
                parameters.set("orientation","landscape");
                camera.setDisplayOrientation(0);
                parameters.setRotation(0);
            }
            camera.setParameters(parameters);
            camera.setPreviewDisplay(holder);
            camera.startPreview();

            camera.autoFocus(new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean success, Camera camera) {
                    if (success) {

                    }
                }
            });

        } catch (IOException e) {
//            surfaceDestroyed(holder);
            e.printStackTrace();
        }
//        Toast.makeText(getContext(), "뷰생성", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {// 카메라 회전할때 처리
        if (holder.getSurface() == null) {
            return;// 프리뷰 없음
        }
        try {
            camera.stopPreview();
            Camera.Parameters parameters=camera.getParameters();
            int rotation=CameraActivity_test.getInstance.getWindowManager().getDefaultDisplay().getRotation();

            if (rotation == Surface.ROTATION_0){
                camera.setDisplayOrientation(0);
                Toast.makeText(getContext(), "0", Toast.LENGTH_SHORT).show();
            }else if (rotation==Surface.ROTATION_90){
                camera.setDisplayOrientation(90);
                Toast.makeText(getContext(), "90", Toast.LENGTH_SHORT).show();
            }else if (rotation==Surface.ROTATION_180){
                camera.setDisplayOrientation(180);
                Toast.makeText(getContext(), "180", Toast.LENGTH_SHORT).show();
            }else if (rotation==Surface.ROTATION_270){
                camera.setDisplayOrientation(270);
                Toast.makeText(getContext(), "270", Toast.LENGTH_SHORT).show();
            }

            parameters.setPreviewSize(previewSize.width, previewSize.height);
            camera.setParameters(parameters);
            camera.setPreviewDisplay(holder);
            camera.startPreview();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

//    hardware에서 직접 받아오는 view를 처리할때는 종료처리를 확실하게 하지 않으면 Activity를 종료하거나 app을 삭제해도 기능이 동작하지 않는 현상이 발생한다.

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        if (camera!=null){
            camera.stopPreview();
            camera.release();
            camera = null;
        }
        if (mCountDownTimer!=null){// 원본에 작성된 코드임 왜사용하는지 확인
            mCountDownTimer.cancel();
            mCountDownTimer=null;
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {// 화면회전시 사이즈 구함
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int width=resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int heigt=resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        setMeasuredDimension(width, heigt);

        if (listPreviewSizes !=null){

        }
    }

    public Camera.Size getPreviewSize(List<Camera.Size> sizes, int w, int h){
        final double ASPECT_TOLERANCE=0.1;
        double targetRatio=(double)h/w;
        if (sizes==null){
            return null;
        }
        Camera.Size optimalSize=null;
        double minDiff=Double.MAX_VALUE;
        int targetHeigt=h;
        for(Camera.Size size:sizes){
            double ratio=(double)size.width/size.height;
            if (Math.abs(ratio-targetRatio)>ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height-targetHeigt) <minDiff){
                optimalSize=size;
                minDiff=Math.abs(size.height-targetHeigt);
            }
        }
        if (optimalSize == null) {
            minDiff=Double.MAX_VALUE;
            for (Camera.Size size:sizes){
                if (Math.abs(size.height=targetHeigt)<minDiff){
                    optimalSize=size;
                    minDiff=Math.abs(size.height-targetHeigt);
                }
            }
        }
        return optimalSize;
    }

    public boolean capture(Camera.PictureCallback callback){
        if (camera!=null){
            camera.takePicture(null, null, callback);
            return true;
        }else {
            return false;
        }

    }

    @Override
    public View focusSearch(int direction) {
        return super.focusSearch(direction);
    }

    public void cameraTake(int delayMillis){
        if (mCountDownTimer !=null){
            mCountDownTimer.cancel();
            mCountDownTimer=null;
            camera.cancelAutoFocus();
        }
        if (delayMillis<=0){

        }

    }



}