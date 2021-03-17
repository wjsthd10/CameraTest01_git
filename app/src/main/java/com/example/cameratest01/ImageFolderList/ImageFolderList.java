package com.example.cameratest01.ImageFolderList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.cameratest01.R;

import java.util.ArrayList;

public class ImageFolderList extends AppCompatActivity {

    ActionBar actionBar;
    Toolbar toolbar;
    RecyclerView recyclerView;
    ImageFolderAdapter adapter;

    ArrayList<String> folderLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        Bundle bundle=intent.getExtras();
        folderLists=bundle.getStringArrayList("imageFolders");// 폴더리스트 경로를 받아온다.
        // folderLists 널포인트 에러나옴....
    }

    private void setAdapter() {// 어뎁터 설정
        adapter=new ImageFolderAdapter(folderLists, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:onBackPressed();break;
        }
        return super.onOptionsItemSelected(item);
    }
}