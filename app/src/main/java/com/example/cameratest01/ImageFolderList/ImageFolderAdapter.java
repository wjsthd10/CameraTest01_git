package com.example.cameratest01.ImageFolderList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cameratest01.R;

import java.util.ArrayList;

public class ImageFolderAdapter extends RecyclerView.Adapter {

    ArrayList<String> folderLists=new ArrayList<>();
    Context context;

    public ImageFolderAdapter(ArrayList<String> folderLists, Context context) {
        this.folderLists = folderLists;
        this.context = context;
    }// 생성자

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

        Glide.with(context).load(folderLists.get(position)).into(vh.folderImage);
    }

    @Override
    public int getItemCount() {
        return folderLists.size();
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
