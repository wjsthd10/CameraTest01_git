package com.example.cameratest01.ImageFolderList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.cameratest01.R;
import com.example.cameratest01.RC_Items.ImageData;

import java.util.ArrayList;

public class ImageFolderList extends AppCompatActivity {

    ActionBar actionBar;
    Toolbar toolbar;
    RecyclerView recyclerView;
    ImageFolderAdapter adapter;

    ArrayList<String> folderLists;
    ArrayList<ImageData> imageData;
    String folderName=null;

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Log.w("ImageFolderListLog", "onCreate");
        setContentView(R.layout.activity_image_folder_list);
        setLayout();// 폴더리스트 레이아웃 설정.
        setData();
        setAdapter();
    }

    private void setLayout(){// 화면 설정.
        toolbar=findViewById(R.id.toolbar_folderList);
        setSupportActionBar(toolbar);
        actionBar=getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("갤러리 선택");
        recyclerView=findViewById(R.id.rc_folderList);

    }

    private void setData() {// 데이터 받기
        Intent intent=getIntent();
        folderLists=intent.getStringArrayListExtra("imageFolders");// 폴더리스트 경로를 받아온다.
//        imageData=intent.getParcelableArrayListExtra("imageData");
        imageData=(ArrayList<ImageData>) intent.getSerializableExtra("imageData");

    }

    private void setAdapter() {// 어뎁터 설정
        for (int i = 0; i < imageData.size(); i++) {
            Log.w("ImageFolderList", "Adapter : "+imageData.get(i).getImageType());
        }
        adapter=new ImageFolderAdapter(folderLists,this, imageData);
        adapter.notifyDataSetChanged();
//        adapter=new ImageFolderAdapter(imageData, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        Log.w("ImageFolderListLog", "onResume : ");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                finish();
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        Log.w("ImageFolderListLog", "onDestroy");
        super.onDestroy();
    }
}