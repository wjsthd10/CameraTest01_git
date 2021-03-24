package com.example.cameratest01.ImageFolderList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cameratest01.Kids_gallery.GridViewGallery;
import com.example.cameratest01.R;
import com.example.cameratest01.RC_Items.ImageData;

import java.util.ArrayList;
import java.util.Collections;

public class ImageFolderAdapter extends RecyclerView.Adapter {

    ArrayList<String> folderLists;
    ArrayList<ImageData> imageData;
    ArrayList<ImageData> folderData=new ArrayList<>();
    Context context;
    int folderCount=0;

    public ImageFolderAdapter(ArrayList<String> folderLists, Context context, ArrayList<ImageData> imageData) {

        this.folderLists = folderLists;
        this.context = context;
        this.imageData=imageData;
        Log.w("imageDataSize", imageData.size()+"");

        ArrayList<String> folderNames=new ArrayList<>();

        for (int i = 0; i < imageData.size(); i++) {
            Log.w("ImageFolderAdapter", i+") AllimgaeType : "+imageData.get(i).getImageType()+" , size : "+imageData.size());

            if (imageData.get(i).getImageType().equals("D")){// 기본갤러리의 데이터만 받아오는 데이터 D로 변경해주기.
//                Log.w("ImageFolderAdapter", i+")  imagePath : "+imageData.get(i).getImagePath());
                if (!folderNames.contains(imageData.get(i).getFolderName())){// 중복되는 폴더 없이 저장.
                    folderNames.add(imageData.get(i).getFolderName());
                    folderData.add(imageData.get(i));
                    Log.w("ImageFolderAdapter", i+") if imageType : "+imageData.get(i).getImageType()+" / imagePath : "+imageData.get(i).getImagePath());
                    // 정상적으로 출력될 리스트 정보가 들어옴.
                }
            }else {// 이미지가 갖고있는 타입이 K일때 여기로 빠짐
                // 여기로만 빠지면 데이터 없는것임.
                Log.w("ImageFolderAdapter", i+") else imageType : "+imageData.get(i).getImageType()+" / imagePath : "+imageData.get(i).getImagePath());
            }

        }

        folderData.add(new ImageData(
                imageData.get(0).getImageName(),
                imageData.get(0).getImagePath(),
                imageData.get(0).getCheck(),
                imageData.get(0).getPosition(),
                "D",
                "기본갤러리",
                null
        ));
    }// 생성자


//    public ImageFolderAdapter(ArrayList<ImageData> imageData, Context context) {
//        this.imageData = imageData;
//        this.context = context;
//        Log.w("showImageDataSize", ""+imageData.size());
//    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView= LayoutInflater.from(context).inflate(R.layout.folder_list_item, parent, false);
        VH holder=new VH(itemView);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VH vh= (VH) holder;
//        if (imageData.contains(folderLists)){
//            Log.w("showImageDataFolder", imageData.get(position).getImagePath());
//        }
        Log.w("ShowImageDataLists", "imagePath : "+folderData.get(position).getImagePath());
        Log.w("ShowImageDataLists", "folderName : "+folderData.get(position).getFolderName());

        Glide.with(context).load(folderData.get(position).getImagePath()).into(vh.folderImage);
        vh.folderName.setText(folderData.get(position).getFolderName());
        vh.folderItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context, ""+folderData.get(position).getFolderName(), Toast.LENGTH_SHORT).show();
                Log.w("finishClickFolder", folderData.get(position).getFolderName());
                ImageFolderList activity=(ImageFolderList)context;
                Intent intent=new Intent();
                intent.putExtra("folderName", folderData.get(position).getFolderPath());
                activity.setResult(Activity.RESULT_OK, intent);
                activity.finish();
            }
        });
    }

    @Override
    public int getItemCount() {
//        Log.w("folderlistsSize", ""+folderLists.size());
        return folderData.size();
    }

    class VH extends RecyclerView.ViewHolder{

        LinearLayout folderItem;
        ImageView folderImage;
        TextView folderName;

        public VH(@NonNull View itemView) {
            super(itemView);

            folderItem=itemView.findViewById(R.id.folderItem);
            folderImage=itemView.findViewById(R.id.folderImageView);
            folderName=itemView.findViewById(R.id.folderName);


        }
    }

}
