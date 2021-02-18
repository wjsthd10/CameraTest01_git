package com.example.cameratest01.MyCameraApi2;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ExifInterface;
import android.media.Image;
import android.media.ImageReader;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.renderscript.Sampler;
import android.util.Log;
import android.util.Printer;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cameratest01.Kids_gallery.GalleryActivity;
import com.example.cameratest01.R;
import com.example.cameratest01.RC_Adapters.RC_CameraButton_Adapter;
import com.example.cameratest01.RC_Items.RC_CameraButton_items;
import com.google.android.material.transition.platform.MaterialSharedAxis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class Camera2Framgnet extends Fragment implements View.OnClickListener, ActivityCompat.OnRequestPermissionsResultCallback {

    private static final SparseIntArray ORIENTATIONS=new SparseIntArray();
    private static final int REQUEST_CAMERA_PERMISSION=1;
    private static final String FRAGMENT_DIALOG= "dialog";

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    private static final String TAG="Camera2Fragment";
    private static final int STATE_PREVIEW=0;
    private static final int STATE_WAITING_LOCK=1;
    private static final int STATE_WAITING_PRECAPTURE=2;
    private static final int STATE_WAITING_NON_PRECAPTURE=3;
    private static final int STATE_PICTURE_TAKEN=4;
    private static final int MAX_PREVIEW_WIDTH=1929;
    private static final int MAX_PREVIEW_HEIGHT=1080;

    File direct;


    CircleImageView picture;
    CircleImageView gallery;
    ImageView btnListShow;
    RecyclerView btnList;
    SimpleDateFormat dateFormat=new SimpleDateFormat("yyMMddHHmmss_");
    String fileName;
    ArrayList<String> strArr=new ArrayList<>();
    ArrayList<RC_CameraButton_items> buttonItems=new ArrayList<>();
    RC_CameraButton_Adapter cameraButtonAdapter;
    LinearLayout btnListBtnLay;
    LinearLayout btnItemLay;

    private final TextureView.SurfaceTextureListener surfaceTextureListener=new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
            openCamera(width, height);
        }

        @Override
        public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {
            configureTransform(width, height);
        }

        @Override
        public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) {
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) {

        }
    };

    private String mCameraId;
    private TextureView_Camera2 mTextureView;
    private CameraCaptureSession mCaptureSession;
    private CameraDevice mCameraDevice;
    private Size mPreviewSize;


    private HandlerThread mBackgroundThread;
    private Handler mBackgroundHandler;
    private ImageReader mImageReader;
    private File mFile;
    ActionBar actionBar;

    private final CameraDevice.StateCallback mStateCallback =new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {// 카메라 연결 시작
            mCameraOpenCloseLock.release();
            mCameraDevice=camera;
            createCameraPreviewSession();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {// 카메라 연결 해제
            mCameraOpenCloseLock.release();
            camera.close();
            mCameraDevice=null;
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {// 카메라 연결 에러
            mCameraOpenCloseLock.release();
            camera.close();
            mCameraDevice=null;
            Activity activity=getActivity();
            if (null != activity) {
                activity.finish();
            }
        }
    };

    private void createCameraPreviewSession() {
        try {
            SurfaceTexture texture=mTextureView.getSurfaceTexture();
            assert texture!=null;
            texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
            Surface surface=new Surface(texture);

            mPreviewRequestBuilder=mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            mPreviewRequestBuilder.addTarget(surface);

            mCameraDevice.createCaptureSession(Arrays.asList(surface, mImageReader.getSurface()), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    if (null == mCameraDevice) {
                        return;
                    }
                    mCaptureSession=session;
                    try {
                        mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                        setAutoFlash(mPreviewRequestBuilder);

                        mPreviewRequest=mPreviewRequestBuilder.build();
                        mCaptureSession.setRepeatingRequest(mPreviewRequest, mCaptureCallback, mBackgroundHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                    Log.e("TAG : ", "onConfigureFailed");
                }
            }, null);

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }


    private final ImageReader.OnImageAvailableListener mOnImageAvailableListener=new ImageReader.OnImageAvailableListener() {// 이미지 읽어오는 메소드
        @Override
        public void onImageAvailable(ImageReader reader) {

            mBackgroundHandler.post(new ImageSaver(reader.acquireNextImage(), mFile));
            // 사진 찍을때 여기서 왼쪽 이미지뷰에 임시 사진 보여주기
            getActivity().runOnUiThread(new Runnable() {// 이미지 전환 작업을 위해서 스레드 사용이 필요함.
                @Override
                public void run() {
                    gallery.setImageBitmap(mTextureView.getBitmap());// 갤러리로 이동하는 이미지뷰에 마지막에 촬영한 이미지 출력
                }// run...
            });
        }
    };

    private CaptureRequest.Builder mPreviewRequestBuilder;
    private CaptureRequest mPreviewRequest;
    private int mState=STATE_PREVIEW;
    private Semaphore mCameraOpenCloseLock = new Semaphore(1);
    private boolean mFlashSupported;
    private int mSensorOrientation;
    private OrientationEventListener orientationEventListener;

    private CameraCaptureSession.CaptureCallback mCaptureCallback=new CameraCaptureSession.CaptureCallback() {

        private void process(CaptureResult result){
            switch (mState){
                case STATE_PREVIEW:break;
                case STATE_WAITING_LOCK:{
                    Integer afState=result.get(CaptureResult.CONTROL_AF_STATE);
                    if (afState==null){
                        captureStillPicture();
                    }else if (CaptureResult.CONTROL_AF_STATE_FOCUSED_LOCKED==afState || CaptureResult.CONTROL_AF_STATE_NOT_FOCUSED_LOCKED==afState){
                        Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                        if (aeState == null || aeState == CaptureResult.CONTROL_AE_STATE_CONVERGED) {
                            mState=STATE_PICTURE_TAKEN;
                            captureStillPicture();
                        }else {
                            runPrecaptureSequence();
                        }
                    }
                    break;
                }
                case STATE_WAITING_PRECAPTURE:{
                    Integer aeState=result.get(CaptureResult.CONTROL_AE_STATE);
                    if (aeState ==null || aeState == CaptureResult.CONTROL_AE_STATE_PRECAPTURE || aeState== CaptureRequest.CONTROL_AE_STATE_FLASH_REQUIRED){
                        mState=STATE_WAITING_NON_PRECAPTURE;
                    }
                    break;
                }
                case STATE_WAITING_NON_PRECAPTURE:{
                    Integer aeState=result.get(CaptureResult.CONTROL_AE_STATE);
                    if (aeState == null || aeState != CaptureResult.CONTROL_AE_STATE_PRECAPTURE) {
                        mState=STATE_PICTURE_TAKEN;
                        captureStillPicture();
                    }
                    break;
                }
            }
        }

        @Override
        public void onCaptureProgressed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureResult partialResult) {
            process(partialResult);
        }

        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
            process(result);
        }
    };

    private void runPrecaptureSequence(){
        try {
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER, CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER_START);
            mState=STATE_WAITING_PRECAPTURE;
            mCaptureSession.capture(mPreviewRequestBuilder.build(), mCaptureCallback, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private static Size chooseOptimalSize(Size[] choices, int textureViewWidth, int textureViewHeight, int maxWidth, int maxHeight, Size aspectRatio){
        List<Size> bigEnough=new ArrayList<>();
        List<Size> notBigEnough=new ArrayList<>();
        int w=aspectRatio.getWidth();
        int h=aspectRatio.getHeight();
        for (Size option : choices){
            if (option.getWidth() <= maxWidth && option.getHeight() <= maxHeight && option.getHeight() == option.getWidth() * h/w){
                if (option.getWidth() >= textureViewWidth && option.getHeight() >= textureViewHeight){
                    bigEnough.add(option);
                }else {
                    notBigEnough.add(option);
                }
            }
        }

        if (bigEnough.size() > 0) {
            return Collections.min(bigEnough,new CompareSizesByArea());
        } else if (notBigEnough.size() > 0) {
            return Collections.max(notBigEnough, new CompareSizesByArea());
        }else {
            Log.e("TAG", "Couldn't find any suitable preview size");
            return choices[0];
        }

    }

    public static Camera2Framgnet newInstance(){
        return new  Camera2Framgnet();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 상태바 제거..
        super.onCreate(savedInstanceState);

        orientationEventListener=new OrientationEventListener(getContext()) {
            @Override
            public void onOrientationChanged(int orientation) {// 회전될때 저장되는 파일 회전 시켜야함.

                if (orientation >=45 && orientation <135){
                    gallery.setRotation(270);
                }else if (orientation>= 135 && orientation<225){
                    gallery.setRotation(180);
                }else if (orientation>=225 && orientation<315){
                    gallery.setRotation(90);
                }else {
                    gallery.setRotation(0);
                }
            }
//            f로그 결과 - 0 ~ 3의 값 반시계방향으로
        };
        orientationEventListener.enable();

    }

    public void cameraButtonListSet(){
        buttonItems.add(new RC_CameraButton_items(R.drawable.ic_baseline_child_care_24, "반 / 부"));
        buttonItems.add(new RC_CameraButton_items(R.drawable.ic_baseline_select_all_24, "전  체"));
        buttonItems.add(new RC_CameraButton_items(R.drawable.ic_baseline_person_add_24, "인원선택"));
        buttonItems.add(new RC_CameraButton_items(R.drawable.ic_baseline_person_search_24, "미촬영자"));
//        우측 기능 버튼들 리스트( 반/부[리스트 보여줌], 전체[버튼 보여줌/데이터가져가서], 인원지정[리스트 보여줌], 미촬영자[리스트 보여줌] )
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.camera2_fragment, container, false);
//        return inflater.inflate(R.layout.camera2_view_test01, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {// ActivityCreated보다 먼저 실행됨
        picture=view.findViewById(R.id.camera_act_image);
        picture.setOnClickListener(this);
        gallery=view.findViewById(R.id.camera_act_image_showImg);
        gallery.setOnClickListener(this);
        mTextureView=view.findViewById(R.id.textureView);
        btnListShow=view.findViewById(R.id.btnListShow);
        btnList=view.findViewById(R.id.camera2_btnList);
        btnListBtnLay=view.findViewById(R.id.btnListBtnLay);
        btnListBtnLay.setOnClickListener(this);
        btnItemLay=view.findViewById(R.id.rc_cameraButton_lay);

        Toolbar toolbar=view.findViewById(R.id.camera2_Toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        actionBar=((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        actionBar.setTitle("전체사진");

        cameraButtonListSet();
        
        cameraButtonAdapter=new RC_CameraButton_Adapter(getContext(), buttonItems);
        btnList.setAdapter(cameraButtonAdapter);

//        Toast.makeText(getActivity(), "camera2", Toast.LENGTH_SHORT).show();
//        Log.e("startTest : ", "onView");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {// 뷰들의 id찾아진 상태
        super.onActivityCreated(savedInstanceState);
        direct=new File(getActivity().getFilesDir(), "CameraTest01"+File.separator+"kidsLove");
        if (!direct.exists()){
            if (!direct.mkdirs()){
                Log.d("TAG", "failed to create directory");
            }
        }
//        Log.e("startTest : ", "onAc");
//        fileListArr=mFile.list(); 파일 리스트

        if (direct.listFiles().length>0){
            int position=direct.listFiles().length;
            CircleImageView iv=getActivity().findViewById(R.id.camera_act_image_showImg);


            Glide.with(getActivity()).load(direct.listFiles()[position-1]).into(iv);
        }
    }

    @Override
    public void onResume() {// 텍스쳐뷰 다시 생성될때 화면 멈춘 상태였다가 다시 포커스 잡는 부분 개선 필요
        super.onResume();
        startBackgroundThread();
        if (mTextureView.isAvailable()) {
            openCamera(mTextureView.getWidth(), mTextureView.getHeight());
        }else {
            mTextureView.setSurfaceTextureListener(surfaceTextureListener);
        }

        if (direct.listFiles().length>0){
            int position=direct.listFiles().length;
            CircleImageView iv=getActivity().findViewById(R.id.camera_act_image_showImg);
            Glide.with(getActivity()).load(direct.listFiles()[position-1]).into(iv);
        }

    }

    @Override
    public void onPause() {
        closeCamera();
        stopBackgroundThread();
        super.onPause();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.camera_act_image:{
                takePicture();// 사진촬영 및 저장
                break;
            }
            case R.id.camera_act_image_showImg:{
                Intent intent=new Intent(getContext(), GalleryActivity.class);
                Bundle bundle=new Bundle();
                bundle.putStringArrayList("fileName", strArr);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            }
            case R.id.btnListBtnLay:{// 리스너 달아줘야함
                if (btnList.getVisibility()==View.GONE){
                    btnList.setVisibility(View.VISIBLE);
                    Glide.with(getContext()).load(R.drawable.ic_top_arrow_24).into(btnListShow);
                    break;
                }else {
                    btnList.setVisibility(View.GONE);
                    Glide.with(getContext()).load(R.drawable.ic_bottom_arrow_24).into(btnListShow);
                    break;
                }
            }
        }
    }



    private void startBackgroundThread(){
        mBackgroundThread =new HandlerThread("CameraBackground");
        mBackgroundThread.start();
        mBackgroundHandler=new Handler(mBackgroundThread.getLooper());
    }

    private void stopBackgroundThread(){
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread=null;
            mBackgroundHandler=null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void closeCamera(){// 카메라 종료
        try {
            mCameraOpenCloseLock.acquire();
            if (null != mCaptureSession) {
                mCaptureSession.close();
                mCaptureSession=null;
            }
            if (null != mCameraDevice) {
                mCameraDevice.close();
                mCameraDevice=null;
            }
            if (null != mImageReader) {
                mImageReader.close();
                mImageReader =null;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            mCameraOpenCloseLock.release();
        }
    }

    private void openCamera(int width, int height){
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestCameraPermission();
            return;
        }
        setUpCameraOutputs(width, height);
        configureTransform(width, height);
        Activity activity=getActivity();
        CameraManager manager= (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);

        try {
            if (!mCameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)){
                throw new RuntimeException("Time out waiting to lock camera opening.");
            }
            manager.openCamera(mCameraId, mStateCallback, mBackgroundHandler);
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera opening.", e);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }

    private void requestCameraPermission(){
        if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
            new ConfirmationDialog().show(getChildFragmentManager(), FRAGMENT_DIALOG);
        }else {
            requestPermissions(new String[]{Manifest.permission.CAMERA},REQUEST_CAMERA_PERMISSION);
        }
    }

    public static class ConfirmationDialog extends DialogFragment{
        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            final Fragment parent=getParentFragment();
            return new AlertDialog.Builder(getActivity()).setMessage("메시지")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            parent.requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Activity activity=parent.getActivity();
                            if (activity != null) {
                                activity.finish();
                            }
                        }
                    }).create();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressWarnings("SuspiciousNameCombination")
    public void setUpCameraOutputs(int width, int height){
        Activity activity=getActivity();
        CameraManager manager= (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
        try {
            for (String cameraId : manager.getCameraIdList()){
                CameraCharacteristics characteristics=manager.getCameraCharacteristics(cameraId);
                Integer facing=characteristics.get(CameraCharacteristics.LENS_FACING);
                if (facing != null && facing == CameraCharacteristics.LENS_FACING_FRONT){
                    continue;
                }

                StreamConfigurationMap map=characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

                if (map ==null){
                    continue;
                }

                Size largest= Collections.max(Arrays.asList(map.getOutputSizes(ImageFormat.JPEG)), new CompareSizesByArea());
                mImageReader= ImageReader.newInstance(largest.getWidth(), largest.getHeight(), ImageFormat.JPEG, 2);
                mImageReader.setOnImageAvailableListener(mOnImageAvailableListener, mBackgroundHandler);

                int displayRotation =activity.getWindowManager().getDefaultDisplay().getRotation();
                mSensorOrientation =characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
                boolean swappedDimensions = false;
                switch (displayRotation){
                    case Surface.ROTATION_0:
                    case Surface.ROTATION_180:
                        if (mSensorOrientation == 90 || mSensorOrientation == 270) {
//                            Toast.makeText(activity, "90, 270", Toast.LENGTH_SHORT).show();
                            swappedDimensions=true;
                        }break;
                    case Surface.ROTATION_90:
                    case Surface.ROTATION_270:
                        if (mSensorOrientation==0||mSensorOrientation==180){
//                            Toast.makeText(activity, "0, 180", Toast.LENGTH_SHORT).show();
                            swappedDimensions=true;
                        }
                        break;
                    default:Log.e("TAG : ", "Display rotation is invalid: "+displayRotation);
                }

                Point displaySize =new Point();
                activity.getWindowManager().getDefaultDisplay().getSize(displaySize);
                int rotatedPreviewWidth=width;
                int rotatedPreviewHeight=height;
                int maxPreviewWidth=displaySize.x;
                int maxPreviewHeight=displaySize.y;

                if (swappedDimensions) {
                    rotatedPreviewWidth=height;
                    rotatedPreviewHeight=width;
                    maxPreviewWidth=displaySize.y;
                    maxPreviewHeight=displaySize.x;
                }

                if (maxPreviewWidth > MAX_PREVIEW_WIDTH){
                    maxPreviewWidth=MAX_PREVIEW_WIDTH;
                }

                if (maxPreviewHeight > MAX_PREVIEW_HEIGHT) {
                    maxPreviewHeight = MAX_PREVIEW_HEIGHT;
                }

                mPreviewSize=chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class), rotatedPreviewWidth, rotatedPreviewHeight, maxPreviewWidth, maxPreviewHeight, largest);
                int orientation=getResources().getConfiguration().orientation;
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    mTextureView.setAspectRatio(mPreviewSize.getWidth(), mPreviewSize.getHeight());
                }else {
                    mTextureView.setAspectRatio(mPreviewSize.getHeight(), mPreviewSize.getWidth());
                }

                Boolean available=characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
                mFlashSupported=available== null ? false:available;

                mCameraId=cameraId;
                return;

            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }catch (NullPointerException e){
            Log.e("TAG", e.toString());
        }
    }



    private void configureTransform(int viewWidth, int viewHeight){
        Activity activity=getActivity();
        if (null==mTextureView || null == mPreviewSize || null == activity){
            return;
        }
        int rotation=activity.getWindowManager().getDefaultDisplay().getRotation();// 디바이스의 회전각도 가져오기.
        Log.e("getRotation", " - rotation : "+rotation);
        Matrix matrix=new Matrix();
        RectF viewRect=new RectF(0,0,viewWidth, viewHeight);
        RectF bufferRect=new RectF(0,0, mPreviewSize.getHeight(), mPreviewSize.getWidth());
        float centerX=viewRect.centerX();
        float centerY=viewRect.centerY();
        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {// 디바이스 회전각도 수직일때...
            Log.e("화면 90 : ", "90");
            bufferRect.offset(centerX-bufferRect.centerX(), centerY-bufferRect.centerX());
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
            float scale=Math.max(
                    (float) viewHeight/mPreviewSize.getHeight(),
                    (float) viewWidth/mPreviewSize.getWidth()
            );
            matrix.postScale(scale, scale, centerX,centerY);

            matrix.postRotate(90*(rotation-2), centerX, centerY);// 회전각도 저장.원본
//            matrix.postRotate(0, centerX, centerY);// 회전각 테스트1
//            matrix.postRotate(180, centerX,centerY);// 회전각 테스트2
        } else if (Surface.ROTATION_180 == rotation) {// 디바이스 수평으로 사진 촬영할때
            Log.e("화면 180 : ", "180");
            matrix.postRotate(180, centerX,centerY);// 위치 변경해서 다시 저장해보기
//            matrix.postRotate(90*(rotation-2), centerX, centerY);// 회전각도 저장.
//            matrix.postRotate(90, centerX, centerY);
        }
        mTextureView.setTransform(matrix);
    }

    private void takePicture(){// 사진 촬영시에 포커스 고정, 저장될 파일 이름 지정
        lockFocus();
        fileName=dateFormat.format(new Date())+"test_"+"001_"+"002_";// 원생 이름을 앞으로 이동시켜서 저장
        strArr.add(fileName);
        mFile=new File(direct, fileName+".jpg");
    }

    private void lockFocus(){
        try {
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CameraMetadata.CONTROL_AF_TRIGGER_START);
            mState=STATE_WAITING_LOCK;
            mCaptureSession.capture(mPreviewRequestBuilder.build(), mCaptureCallback, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    static class CompareSizesByArea implements Comparator<Size>{

        @Override
        public int compare(Size o1, Size o2) {
            return Long.signum((long)o1.getWidth() * o1.getHeight() - (long)o2.getWidth() * o2.getHeight());
        }
    }

    private static class ImageSaver implements Runnable{// 사진 저장 메소드

        private final Image mImage;
        private final File  mFile;


        public ImageSaver(Image mImage, File mFile) {
            this.mImage = mImage;
            this.mFile = mFile;

        }


        @Override
        public void run() {
            ByteBuffer buffer=mImage.getPlanes()[0].getBuffer();
            byte[] bytes=new byte[buffer.remaining()];
            buffer.get(bytes);
            FileOutputStream output=null;

            try {
                output=new FileOutputStream(mFile);
                output.write(bytes);
                output.close();// 추가함.
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                mImage.close();
                if (null != output) {
                    try {
                        output.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void captureStillPicture(){// 저장한 파일관련
        try {
            final Activity activity=getActivity();
            if (null == activity || null == mCameraDevice){
                return;
            }
            final CaptureRequest.Builder captureBuilder=mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(mImageReader.getSurface());
            captureBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
            setAutoFlash(captureBuilder);// 자동 플래쉬

            int rotation =activity.getWindowManager().getDefaultDisplay().getRotation();
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, getOrientation(rotation));
            CameraCaptureSession.CaptureCallback captureCallback=new CameraCaptureSession.CaptureCallback() {// 사진 촬영후 콜백
                @Override
                public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                    Log.d("TAG", mFile.toString());// 저장된 파일 경로
                    unlockFocus();
                }
            };
            mCaptureSession.stopRepeating();
            mCaptureSession.abortCaptures();
            mCaptureSession.capture(captureBuilder.build(), captureCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void setAutoFlash(CaptureRequest.Builder requestBuilder){
        if (mFlashSupported){
            requestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
        }
    }

    private int getOrientation(int rotation){
        return ( ORIENTATIONS.get(rotation) + mSensorOrientation + 270 ) % 360;
    }
    private void unlockFocus(){
        try {
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CameraMetadata.CONTROL_AF_TRIGGER_CANCEL);
            setAutoFlash(mPreviewRequestBuilder);
            mCaptureSession.capture(mPreviewRequestBuilder.build(), mCaptureCallback, mBackgroundHandler);
            mState=STATE_PREVIEW;
            mCaptureSession.setRepeatingRequest(mPreviewRequest, mCaptureCallback, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

}
