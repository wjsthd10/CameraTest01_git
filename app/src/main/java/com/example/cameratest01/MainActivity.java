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
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.cameratest01.MyCameraApi2.CameraActivity_camera2;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Camera mCamera;
    String[] permissions={Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    static final int PERMISSIONS_REQUEST_CONDE=1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSaveFolder();
    }

    public File getSaveFolder(){
        String folderName="kidsLove";
        File dir=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+File.pathSeparator+folderName);
        if (!dir.mkdirs()){
            dir.mkdirs();

        }
        return dir;
    }

    public void click_camera(View view) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (!hasPermissions(permissions)){
                requestPermissions(permissions,PERMISSIONS_REQUEST_CONDE);

            }else {
                Toast.makeText(this, "onClick", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(this, CameraActivity_test.class);
                startActivity(intent);
            }
        }



    }


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
                    boolean readPermissionAccepted=grantResults[2]==PackageManager.PERMISSION_GRANTED;
                    if (!cameraPermissionAccepted||!diskPermissionAccepted || readPermissionAccepted){
//                        showDialogForPermission("카메라 기능을 사용하려면 동의해 주세요.");
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
                            Toast.makeText(this, "camera2", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(this, CameraActivity_camera2.class);
                            startActivity(intent);
                        }else {
                            Toast.makeText(this, "onRequestPermissions_if", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(this, CameraActivity_test.class);
                            startActivity(intent);
                        }
                    }else {
                        Toast.makeText(this, "onRequestPermissions_else", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(this, CameraActivity_test.class);
                        startActivity(intent);
                    }
                }
        }
    }
//    @TargetApi(Build.VERSION_CODES.M)
    private void showDialogForPermission(String msg){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("알림");
        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                requestPermissions(permissions, PERMISSIONS_REQUEST_CONDE);
                Intent intent=new Intent(MainActivity.this, CameraActivity_test.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.create().show();
    }

}//main