package com.example.cameratest01.gridview_gallery_fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cameratest01.Kids_gallery.GridViewGallery;
import com.example.cameratest01.R;
import com.example.cameratest01.RC_Items.ImageData;
import com.example.cameratest01.RC_Items.ImageFolder;
import com.example.cameratest01.gridview_adapter.New_Adapter;
//import com.example.cameratest01.gridview_adapter.Android_Adapter;

import java.util.ArrayList;

public class Android_images_FG extends Fragment {

    RecyclerView recyclerView;
//    Android_Adapter adapter;
    New_Adapter new_adapter;
    GridViewGallery gridViewGallery;
    ArrayList<String> imageStr;
    ArrayList<ImageData> imageDataAC;


    public Android_images_FG(ArrayList<String> imageStr, ArrayList<ImageData> imageDataAC) {
        this.imageStr = imageStr;
        this.imageDataAC = imageDataAC;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gridViewGallery=(GridViewGallery)getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.gridview_fg_android, container, false);
        recyclerView=view.findViewById(R.id.android_rc_image_grid);
        new_adapter=new New_Adapter(gridViewGallery, imageDataAC, imageStr);
        Bundle bundle=getArguments();// 선택매뉴 눌러서 받아오는 번들 확인
        int countNun=0;
        if (bundle!=null){// 번들의 값이 있으면 타입 지정..
            String type=bundle.getString("Type");
            countNun=bundle.getInt("CountNum");
            Log.w("showSelectType", "inFG : "+type);
            new_adapter.setSelectType(type);
        }

        new_adapter.setType("D");
        new_adapter.setCountNumD(countNun);
        new_adapter.setFlag("D");
        recyclerView.setAdapter(new_adapter);

        return view;
    }

    // 아래 내용은 기본 갤러리에서 이미지 전송코드 완료시에 삭제하기.

//    public void test(){
//        outNum=1;
//        zoomOut=0;
//        File file = null;
//        Collections.reverse(AC_imageData);
//        float rotateNumSet=0f;
////                            ArrayList<Float> floats=new ArrayList<>();
//        if (GALLERY_TYPE.equals("K")){// 키즈사랑 갤러리 파일
//            setOrientateionOfKidsLoveImage(imageData.get(pagePosition).ImagePath, imageData.get(pagePosition).rotNum);// 이미지에 회전값 테그 set하는방법.
//            rotateNumSet=imageData.get(pagePosition).getOrirotateNum();// 실제이미지는 한칸씩회전되어 저장됨 하지만 앱에서 보여지는 회전값이 오락가락함...
//            imageData.get(pagePosition).setOrirotateNum(imageData.get(pagePosition).getRotateNum());
//            imageData.get(pagePosition).setRotateNum(rotateNumSet);
//
//            new_adapter.setItem(imageData);
//
//
//            new_adapter.setType(GALLERY_TYPE);
//            file=new File(direct, imageData.get(pagePosition).ImageName);// 키즈사랑 갤러리에서 회전시켰을때 저장경로.
//
//        }else if (GALLERY_TYPE.equals("D")){// 기본 갤러리 파일
////            rotateNumSet=AC_imageData.get(pagePosition).getOrirotateNum();//
//            AC_imageData.get(pagePosition).setOrirotateNum(AC_imageData.get(pagePosition).getRotateNum());
//            AC_imageData.get(pagePosition).setRotateNum(rotateNumSet);
//            new_adapter.setItem(imagesArr,AC_imageData);
//            new_adapter.setType(GALLERY_TYPE);
////                                file은 사진 가져오는 경로
////                                Log.e("image_path", imagesArr.get(pagePosition));
//            file=new File(imagesArr.get(pagePosition));// 기본갤러리 파일
//        }
//        Bitmap bm=BitmapFactory.decodeFile(file.getAbsolutePath());
////                            RectF viewRect=new RectF(0,0,bm.getWidth(), bm.getHeight());
////                            float centerX=viewRect.centerX();
////                            float centerY=viewRect.centerY(); , centerX, centerY
//        Matrix matrix=new Matrix();
//        if (GALLERY_TYPE.equals("K")){// 키즈사랑 갤러리 회전값
////                                matrix.postRotate(imageData.get(pagePosition).rotateNum);
//            if (matrix.postRotate(imageData.get(pagePosition).getRotateNum())){
////                                    matrix.setRotate(imageData.get(pagePosition).getRotateNum());
//            }else {
//                matrix.setRotate(imageData.get(pagePosition).getRotateNum());
//            }
//        }else {// 기본갤러리 회전값
////                                matrix.postRotate(AC_imageData.get(pagePosition).rotateNum);// 인덱스 에러 회전값 저장//
//            if (matrix.postRotate(AC_imageData.get(pagePosition).getRotateNum())){
////                                    Toast.makeText(GridViewGallery.this, ""+AC_imageData.get(pagePosition).getRotateNum(), Toast.LENGTH_SHORT).show();
//            }else {
//                matrix.setRotate(AC_imageData.get(pagePosition).getRotateNum());
//            }
//        }
////                            matrix.postRotate(floats.get(pagePosition));// 인덱스 에러 회전값 저장//
//        try {
//            Log.e("matrixShow", " - width : "+bm.getWidth());
//            bm=Bitmap.createBitmap(bm, 0,0,bm.getWidth(),bm.getHeight(),matrix,true);
//        }catch (NullPointerException e){
//            e.printStackTrace();
//        }
//
////                            file.delete();
////                            imageData.remove(pagePosition);
//
//        FileOutputStream fOut=null;
//        try {
//            if (GALLERY_TYPE.equals("K")){// 키즈사랑 갤러리에서 저장 누를시 저장 경로
//                fOut=new FileOutputStream(file);
//                bm.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
//                fOut.flush();//덮어씌우기
//                fOut.close();
////                                    switchNum=getOrientateionOfImage(imageData.get(pagePosition).ImagePath);
//            }else if (GALLERY_TYPE.equals("D")){// 기본 갤러리에서 저장 누를시 저장경로
////                                    File file2=new File(direct, AC_imageData.get(pagePosition).ImageName);
//                File file2=new File(direct, "cp_"+fileNameDate.format(new Date())+"_.jpg");
//                fOut=new FileOutputStream(file2);
//                bm.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
//
//                ByteArrayOutputStream stream=new ByteArrayOutputStream();
//                bm.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//                byte[] bytes=stream.toByteArray();
//
//                fOut.write(bytes);//새로쓰기
//                fOut.close();
//
//                Collections.reverse(imageData);// 저장하는 데이터 뒤집기
//                imageData.add(new ImageData(
//                        AC_imageData.get(pagePosition).ImageName,
////                                            AC_imageData.get(pagePosition).ImagePath,
//                        imagesArr.get(pagePosition),
//                        0, 0, 0, AC_imageData.get(pagePosition).getRotateNum(),
//                        "K"
//                ));
//
//                Collections.reverse(imageData);// 저장완료 후 다시 뒤집기
//                new_adapter.setItem(imageData);// 기본갤러리에서 받아온 사진을 키즈사랑 갤러리에 저장하기때문에 키즈사랑 갤러리 데이터만 갱신함
//                new_adapter.setType(GALLERY_TYPE);
////                                    new_adapter.notifyDataSetChanged();// newimage를 다시 실행해야할듯
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        Collections.reverse(AC_imageData);
//
//        FragmentTransaction tran = manager.beginTransaction();
//        tran.remove(fragments[0]);
//        fragments[0]=new New_images_FG(imageData);
//        new_adapter.notifyDataSetChanged();// newimage를 다시 실행해야할듯
//        tran.replace(R.id.pager_gridview, fragments[0]);
//        tran.commit();// new fragment다시실행
//        // 저장 메소드 추가
//        bigImageToolbarItem_clear();// 회전버튼 눌렀을때 나오는 아이콘 안보이게 변경
//        commitClickFG();// 큰이미지 다시 보여주기
//        // 저장하고 다시 회전시켜서 저장하면 바뀐 데이터로 저장되지 않음...
//    }
//    }  SELECT_IMAGES="S";

}
