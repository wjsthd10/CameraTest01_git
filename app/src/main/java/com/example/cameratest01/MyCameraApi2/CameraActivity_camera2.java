package com.example.cameratest01.MyCameraApi2;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.os.Build;
import android.os.Bundle;
import android.util.Size;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.TextureView;
import android.widget.Toast;

import com.example.cameratest01.R;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class CameraActivity_camera2 extends AppCompatActivity {

    private static final int REQUEST_CAMERA_PERMISSION=1234;
    private TextureView mTextureView;

    private CameraDevice mCamera;
    private Size mPreviewSize;
    private CameraCaptureSession mCaptureSession;
    private CaptureRequest.Builder mCaptureRequestBuilder;
    Fragment camer2FG=new Camera2Framgnet();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_camera_camera2);

        if (null == savedInstanceState) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, Camera2Framgnet.newInstance()).commit();
            }
        }
    }

    // fragment에서 보여질 툴바에 옵션버튼생성
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.camera2_toolbar_setting, menu);
        return true;
    }

    // fragment에서 보여지는 버튼 제어
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {// activity에서 호출해야함
        switch (item.getItemId()){
            case android.R.id.home: {
                onBackPressed();// 뒤로가기 버튼 누른효과...
                return true;
            }
            case R.id.setting:{
                Toast.makeText(this, "setting", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

}