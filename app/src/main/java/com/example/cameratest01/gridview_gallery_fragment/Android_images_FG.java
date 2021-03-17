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

public class Android_images_FG extends Fragment {

    public RecyclerView recyclerView;
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
            Log.w("showSelectType", "inFG : "+type);
            new_adapter.setSelectType(type);
        }

        new_adapter.setType("D");
        new_adapter.setCountNumD(countNun);
        new_adapter.setFlag("D");
        recyclerView.addOnScrollListener(scrollListener);
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
            gridViewGallery.setFirstCounNum(layoutManager.findFirstCompletelyVisibleItemPosition());
            gridViewGallery.setLastCounNum(lastVisible, totalItemCount);
            if (lastVisible>=totalItemCount-1 || newState==1){// 리스트의 개수보다 마지막에 보여지는 번호가 크거나 같으면 동작
                gridViewGallery.floatingButton.setVisibility(View.GONE);
            }else if (newState==0){//lastVisible<totalItemCount-1
                gridViewGallery.floatingButton.setVisibility(View.VISIBLE);
            }

        }
    };

    @Override
    public void onResume() {
        super.onResume();
        Log.w("showLayouts", "R");
        if (gridViewGallery.firstCounNum!=0){
            recyclerView.scrollToPosition(gridViewGallery.firstCounNum);
        }
    }


}
