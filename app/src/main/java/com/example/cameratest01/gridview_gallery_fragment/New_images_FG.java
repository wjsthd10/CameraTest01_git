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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.example.cameratest01.Kids_gallery.GridViewGallery;
import com.example.cameratest01.R;
import com.example.cameratest01.RC_Items.ImageData;
import com.example.cameratest01.gridview_adapter.New_Adapter;

import java.util.ArrayList;

public class New_images_FG extends Fragment {

    RecyclerView recyclerView;
    New_Adapter adapter;
    ArrayList<String> strArr;
    GridViewGallery gridViewGallery;
    ArrayList<ImageData> imageData;

    public New_images_FG(ArrayList<ImageData> imageData) {
        this.imageData = imageData;
    }

    @Override
    public void onAttachFragment(@NonNull Fragment childFragment) {
        super.onAttachFragment(childFragment);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {// 2.
        try {
            super.onCreate(savedInstanceState);
            gridViewGallery= (GridViewGallery) getActivity();
//            Toast.makeText(gridViewGallery, "create", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {// 3.
        View view=inflater.inflate(R.layout.gridview_fg_new, container, false);
//        Toast.makeText(gridViewGallery, "createView", Toast.LENGTH_SHORT).show();
        try {
            recyclerView=view.findViewById(R.id.new_rc_image_grid);
//        GridLayoutManager gm=new GridLayoutManager(gridViewGallery, 4, GridLayoutManager.VERTICAL, true);
//        gm.scrollToPosition(0);//
//        recyclerView.setLayoutManager(gm);
            // 메니저를 통해서 역순으로 변경하면 상단에 빈공간이 생기고 하단부터 스크롤 시작하는 현상
            // 따라서 어뎁터에서 아이템 역순으로 돌려야함.

            adapter=new New_Adapter(gridViewGallery, imageData);// fragemtn의 activity 전달해서 에러난거임 activity생성해서 activity의 context전달.
            int countNum=0;
            Bundle bundle=getArguments();
            if (bundle!=null){
                String SELECT_IMAGES=bundle.getString("Type");
                countNum=bundle.getInt("CountNum");
                Log.w("showSelectType", "inFG : "+SELECT_IMAGES);
                adapter.setSelectType(SELECT_IMAGES);// 번들로 받아온 데이터 넘겨보기.
            }
//            Log.d("ShowTypeInNewFG", SELECT_IMAGES);
            adapter.setType("K");// 타입 지정
            adapter.setCountNumK(countNum);
            adapter.setFlag("K");
            recyclerView.setAdapter(adapter);


            return view;
        }catch (Exception e){

        }
        return view;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {// 4.
        super.onActivityCreated(savedInstanceState);
    }
}
