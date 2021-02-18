package com.example.cameratest01.MyCameraApi2;

import android.content.Context;
import android.util.AttributeSet;
import android.view.TextureView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TextureView_Camera2 extends TextureView {

    private int mRatioWidth=0;
    private int mRatioHeight=0;

    public TextureView_Camera2(@NonNull Context context) {
        super(context);
    }

    public TextureView_Camera2(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TextureView_Camera2(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setAspectRatio(int width, int height){
        if (width<0 || height<0){
            throw new IllegalArgumentException("Size cannot e negative");
        }
        mRatioWidth=width;
        mRatioHeight=height;
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width=MeasureSpec.getSize(widthMeasureSpec);
        int height=MeasureSpec.getSize(heightMeasureSpec);
        if (0==mRatioHeight||0==mRatioWidth){
            setMeasuredDimension(width, height);
        }else {
            if (width < height * mRatioWidth / mRatioHeight) {
                setMeasuredDimension(width, width* mRatioHeight/mRatioWidth);
            }else {
                setMeasuredDimension(height * mRatioWidth/mRatioHeight, height);
            }
        }
    }
}
