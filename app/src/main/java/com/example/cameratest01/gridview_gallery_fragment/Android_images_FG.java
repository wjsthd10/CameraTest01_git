package com.example.cameratest01.gridview_gallery_fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cameratest01.Kids_gallery.GridViewGallery;
import com.example.cameratest01.R;
import com.example.cameratest01.RC_Items.ImageData;
import com.example.cameratest01.RC_Items.ImageFolder;
import com.example.cameratest01.gridview_adapter.New_Adapter;
//import com.example.cameratest01.gridview_adapter.Android_Adapter;

import java.util.ArrayList;
import java.util.Collections;

public class Android_images_FG extends Fragment {

    public RecyclerView recyclerView;
//    Android_Adapter adapter;
    New_Adapter new_adapter;
    GridViewGallery gridViewGallery;
    ArrayList<String> imageStr;
    ArrayList<ImageData> imageDataAC;
    ArrayList<ImageData> images;

    public Android_images_FG(ArrayList<String> imageStr, ArrayList<ImageData> imageDataAC) {
        this.imageStr = imageStr;
        this.imageDataAC = imageDataAC;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gridViewGallery=(GridViewGallery)getActivity();
        Log.w("showLayouts", "C");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.gridview_fg_android, container, false);
        recyclerView=view.findViewById(R.id.android_rc_image_grid);
        Log.w("showLayouts", "CV");

        new_adapter=new New_Adapter(gridViewGallery, imageDataAC, imageStr);
        Bundle bundle=getArguments();// 선택매뉴 눌러서 받아오는 번들 확인
        int countNun=0;
        if (bundle!=null){// 번들의 값이 있으면 타입 지정..
            String type=bundle.getString("Type");
            countNun=bundle.getInt("CountNum");
            images=(ArrayList<ImageData>) bundle.getSerializable("images");// 폴더 선택했을때 이미지 데이터값
            new_adapter.totalImageData= (ArrayList<ImageData>) bundle.getSerializable("AC_imageData");// 기본갤러리 전체 이미지 개수
//            imageDataAC= (ArrayList<ImageData>) bundle.getSerializable("AC_imageData");// 이코드 사용하면 imageData -> null됨.
            if (images!=null){
//                Collections.reverse(images);
                Log.w("AndroidImageFG", "bundleImagesSize"+images.size()+" / "+new_adapter.totalImageData.size());
                new_adapter.setItem(images);
            }
//            if (imageLists!=null){
//                Log.w("AndroidImageFG", "bundleImageDataSize"+imageLists.size());
//                new_adapter.setItem(imageLists);
//            }

            Log.w("showSelectType", "inFG : "+type);
            new_adapter.setSelectType(type);
        }
        new_adapter.setType("D");
        new_adapter.setCountNumD(countNun);
        new_adapter.setFlag("D");
        String type=gridViewGallery.GALLERY_TYPE;
        if (type.equals("D")){
            recyclerView.addOnScrollListener(scrollListener);
        }else {
            recyclerView.addOnScrollListener(null);
        }
        recyclerView.setAdapter(new_adapter);

        return view;
    }


    RecyclerView.OnScrollListener scrollListener=new RecyclerView.OnScrollListener() {// 스크롤 내릴때 플로팅버튼 안보이게 설정.
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {// newState 1: 이동시작, 2: 이동중, 0: 이동종료
            super.onScrollStateChanged(recyclerView, newState);
            Log.w("showLayouts", "L");
            LinearLayoutManager layoutManager=LinearLayoutManager.class.cast(recyclerView.getLayoutManager());
            int totalItemCount=layoutManager.getItemCount();//리스트의 개수 확인
            int lastVisible=layoutManager.findLastCompletelyVisibleItemPosition();// 리스트뷰에 마지막으로 보여지는 아이템 번호...
            Log.w("lastVisibleShow", "lastVisible : "+lastVisible+" - totalItemCount : "+totalItemCount+" - newState : "+newState);
            Log.w("firstCompletelyNum", layoutManager.findFirstCompletelyVisibleItemPosition()+"");

            gridViewGallery.setFirstCounNum(layoutManager.findFirstCompletelyVisibleItemPosition());
            gridViewGallery.setLastCounNum(lastVisible, totalItemCount);
            if (layoutManager.findFirstCompletelyVisibleItemPosition()==0 ){ // && newState==0
                gridViewGallery.floatingButton.setVisibility(View.VISIBLE);
            } else if (lastVisible>=totalItemCount-1 || newState==2){// 리스트의 개수보다 마지막에 보여지는 번호가 크거나 같으면 동작
                gridViewGallery.floatingButton.setVisibility(View.GONE);
            }else if (newState==0 ){//lastVisible<totalItemCount-1
                gridViewGallery.floatingButton.setVisibility(View.VISIBLE);
            }

//            String type=gridViewGallery.GALLERY_TYPE;
//            if (type.equals("D")){
//                super.onScrollStateChanged(recyclerView, newState);
//                Log.w("showLayouts", "L");
//                LinearLayoutManager layoutManager=LinearLayoutManager.class.cast(recyclerView.getLayoutManager());
//                int totalItemCount=layoutManager.getItemCount();//리스트의 개수 확인
//                int lastVisible=layoutManager.findLastCompletelyVisibleItemPosition();// 리스트뷰에 마지막으로 보여지는 아이템 번호...
//                Log.w("lastVisibleShow", "lastVisible : "+lastVisible+" - totalItemCount : "+totalItemCount+" - newState : "+newState);
//                Log.w("firstCompletelyNum", layoutManager.findFirstCompletelyVisibleItemPosition()+"");
//
//                if (type.equals("D")){
//                    gridViewGallery.setFirstCounNum(layoutManager.findFirstCompletelyVisibleItemPosition());
//                    gridViewGallery.setLastCounNum(lastVisible, totalItemCount);
//                    if (layoutManager.findFirstCompletelyVisibleItemPosition()==0 && newState==0 && type.equals("D")){
//                        gridViewGallery.floatingButton.setVisibility(View.VISIBLE);
//                    } else if (lastVisible>=totalItemCount-1 || newState==1 && type.equals("D")){// 리스트의 개수보다 마지막에 보여지는 번호가 크거나 같으면 동작
//                        gridViewGallery.floatingButton.setVisibility(View.GONE);
//                    }else if (newState==0 && type.equals("D") ){//lastVisible<totalItemCount-1
//                        gridViewGallery.floatingButton.setVisibility(View.VISIBLE);
//                    }
//                }else {
//                    gridViewGallery.floatingButton.setVisibility(View.GONE);
//                }
//            }else {
//                super.onScrollStateChanged(recyclerView, newState);
//            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.w("showLayouts", "R");
        if (gridViewGallery.firstCounNum!=0){
            recyclerView.scrollToPosition(gridViewGallery.firstCounNum);
        }
    }


}
