package com.example.cameratest01;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    Camera mCamera;
    String[] permissions={Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    static final int PERMISSIONS_REQUEST_CONDE=1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void click_camera(View view) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (!hasPermissions(permissions)){
                requestPermissions(permissions,PERMISSIONS_REQUEST_CONDE);

            }else {
                Intent intent=new Intent(this, CameraActivity_test.class);
                startActivity(intent);
            }

        }



    }
//    private boolean checkCameraHardware(Context context){
//        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
//            return true;
//        }else {
//            return false;
//        }
//    }
//
//    public static Camera getCameraInstance(){
//        Camera camera=null;
//        try {
//            camera=Camera.open();
//        }catch (Exception e){}
//        return camera;
//    }


    private boolean hasPermissions(String[] permissions){

        int result;
        for (String perms:permissions){
            result= ContextCompat.checkSelfPermission(this,perms);
            if (result==PackageManager.PERMISSION_DENIED){
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSIONS_REQUEST_CONDE:
                if (grantResults.length>0){
                    boolean cameraPermissionAccepted=grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    boolean diskPermissionAccepted=grantResults[1]==PackageManager.PERMISSION_GRANTED;
                    if (!cameraPermissionAccepted||!diskPermissionAccepted){
                        showDialogForPermission("카메라 기능을 사용하려면 동의해 주세요.");
                    }else {
                        Intent intent=new Intent(this, CameraActivity_test.class);
                        startActivity(intent);
                    }
                }
        }
    }
    @TargetApi(Build.VERSION_CODES.M)
    private void showDialogForPermission(String msg){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("알림");
        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requestPermissions(permissions, PERMISSIONS_REQUEST_CONDE);
            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        builder.create().show();
    }

}//main