package com.example.cameratest01.RC_Adapters;

import android.content.Context;
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
import com.example.cameratest01.R;
import com.example.cameratest01.RC_Items.RC_CameraButton_items;

import java.util.ArrayList;

public class RC_CameraButton_Adapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<RC_CameraButton_items> items;

    public RC_CameraButton_Adapter(Context context, ArrayList<RC_CameraButton_items> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView= LayoutInflater.from(context).inflate(R.layout.rc_camera_button_item, parent, false);
        VH_camera holder=new VH_camera(itemView);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VH_camera vh= (VH_camera) holder;
        Glide.with(context).load(items.get(position).image).into(vh.iv);
        vh.tv.setText(items.get(position).name);
        vh.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "클릭시 해당하는 데이터 받아와서 촬영리스트에 추가하기", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class VH_camera extends RecyclerView.ViewHolder{

        LinearLayout layout;
        ImageView iv;
        TextView tv;

        public VH_camera(@NonNull View itemView) {
            super(itemView);
            layout=itemView.findViewById(R.id.rc_cameraButton_lay);
            iv=itemView.findViewById(R.id.rc_cameraButton_image);
            tv=itemView.findViewById(R.id.rc_cameraButton_text);
        }
    }
}
