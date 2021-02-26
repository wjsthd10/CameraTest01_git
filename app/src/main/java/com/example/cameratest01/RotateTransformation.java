package com.example.cameratest01;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

public class RotateTransformation extends BitmapTransformation {

    float rotateRotationAngle=0f;

    public RotateTransformation(float rotateRotationAngle) {
        this.rotateRotationAngle = rotateRotationAngle;
        Log.e("ImgFGpager_Test : "," rotateRotationAngle : "+rotateRotationAngle);
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {


        Log.e("ImgFGpager_Test : "," transform : "+rotateRotationAngle);
        Matrix matrix=new Matrix();
        matrix.postRotate(rotateRotationAngle);

        return Bitmap.createBitmap(toTransform, 0, 0, toTransform.getWidth(), toTransform.getHeight(), matrix, true);
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update(("rotate"+rotateRotationAngle).getBytes());
    }
}
