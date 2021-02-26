//package com.example.cameratest01.gridview_adapter;
//
//import android.content.Context;
//import android.util.Log;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.cameratest01.Kids_gallery.GalleryActivity;
//import com.example.cameratest01.Kids_gallery.GridViewGallery;
//import com.example.cameratest01.RC_Items.ImageData;
//
//import java.io.File;
//import java.util.ArrayList;
//
//public class Date_Adapter extends RecyclerView.Adapter {
//
//    Context context;
//    ArrayList<ImageData> imageData;
//    File direct;//
//    GridViewGallery gridViewGallery;
//
//    public Date_Adapter(Context context, ArrayList<ImageData> imageData) {
//        this.context = context;
//        this.imageData = imageData;
//
//        direct=new File(context.getFilesDir(), "CameraTest01"+File.separator+"kidsLove");
//        Log.e("direct File : ", direct.listFiles().length+"");
//        if (!direct.exists()) {
//            if (!direct.mkdirs()) {
//                Log.d("TAG", "failed to create directory");
//            }
//        }
//    }
//
//    @NonNull
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return null;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return imageData.size();
//    }
//}
