package com.example.cameratest01.Kids_gallery;

import android.content.Context;
import android.graphics.Matrix;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cameratest01.MyInterface.OnClickDelete_Image;
import com.example.cameratest01.R;
import com.example.cameratest01.RC_Items.RC_ImageItems;

import java.io.File;
import java.util.ArrayList;

public class GalleryAdapter extends RecyclerView.Adapter implements OnClickDelete_Image {

    ArrayList<RC_ImageItems> items;
    public Context context;
    ArrayList<String> fileNames;

    File direct;
    String[] fileListArr;
    int position=0;
    File[] lists;

    int nums=0;

    public GalleryAdapter() {
    }

    public GalleryAdapter(Context context, ArrayList<String> fileNames) {
        this.context = context;
        this.fileNames =fileNames;

        direct=new File(context.getFilesDir(), "CameraTest01"+File.separator+"kidsLove");
        if (!direct.exists()){
            if (!direct.mkdirs()){
                Log.d("TAG", "failed to create directory");
            }
        }
        fileListArr=direct.list();
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView= LayoutInflater.from(context).inflate(R.layout.rc_gallery_bigimg, parent, false);
        VH holder=new VH(itemView);

        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VH vh= (VH) holder;
//        this.position=position;// 제거, 업로드에 필요한 사진 번째
        File[] lists=direct.listFiles();// 저장한 경로의 파일들을 파일 리스트로 저장
//        Log.d("bindviewposition", position+"");
//        if (position-1 == -1){ Log.d("bindposition", "-1-1-1"); }
//        Glide.with(context).load(lists[position]).into(vh.imageView);



        try {
            Glide.with(context).load(lists[position]).into(vh.imageView);
            if (nums!=0){
                Glide.with(context).load(lists[nums]).into(vh.imageView);
                nums=0;
            }
        }catch (Exception e){
            Toast.makeText(context, e+"", Toast.LENGTH_SHORT).show();
        }
//        Log.d("listsSize", lists.length+"");
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP){

            vh.imageView.setRotation(90);
        }else

        vh.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {// 화면에 이미지 보여질때
        super.onViewAttachedToWindow(holder);
        VH vh= (VH) holder;
        position=vh.getLayoutPosition();
        Log.d("layPosition", vh.getLayoutPosition()+"");
        // 여기서 GalleryAdapter_bottomImg로 position 전달해주고 어답터 다시 실행해야하는지?
    }

    @Override
    public int getItemCount() {
        return fileListArr.length;
    }


    @Override
    public void onClick(int position) {// 사진 삭제
        this.lists=direct.listFiles();
        Log.e("listsSize", "1_"+lists.length+"");
        Log.e("listsSize", "2_"+this.lists.length+"");
//        Toast.makeText(context, "delete1", Toast.LENGTH_SHORT).show();
        lists[this.position].delete();// 폴더에서 지워지는거 확인함.
        notifyItemRemoved(this.position);// 화면 갱신이 되지 않음.
//        Toast.makeText(context, "delete2", Toast.LENGTH_SHORT).show();// 재활용 뷰 생성될때 이미지 리스트에 없어서 에러남.

//        보여지는 뷰에서 좌우의 뷰를 미리 생성하는데 하나 삭제하고 나서 재활용 뷰생성에서 에러남
//        GalleryAdapter.this.lists[position].delete();// 폴더에서 지워지는거 확인함.
//        해결 : GalleryActivity에서 어뎁터 연결해서 다시 recycler실행하면 정상 동작함.

        Log.d("listsSize_main", "delete2_"+this.position+"");
//        Log.d("확인", lists[position].exists()+"");

    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {// 재활용 홀더 가져오기
        super.onViewRecycled(holder);
//        Log.e("recycledLayoutPosition", holder.getLayoutPosition()+"-----");
//        Log.e("adapterPosition", holder.getAdapterPosition()+"");
//        Log.e("recycled_itemviewtype", holder.getItemViewType()+"");
    }



    static class VH extends RecyclerView.ViewHolder{
        public ImageView imageView;
        public VH(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.rc_gallery_bigimg);

        }
    }


}
