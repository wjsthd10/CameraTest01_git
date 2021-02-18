package com.example.cameratest01.RC_Items;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class Image_Parcelable_Item implements Parcelable {

    byte[] bytes;

    public Image_Parcelable_Item() {
    }

    public Image_Parcelable_Item(Parcel in) {
        this.bytes=new byte[in.readInt()];
    }

    public static final Image_Parcelable_Item.Creator<Image_Parcelable_Item> CREATOR = new Creator<Image_Parcelable_Item>() {
        @Override
        public Image_Parcelable_Item createFromParcel(Parcel in) {
            return new Image_Parcelable_Item(in);
        }

        @Override
        public Image_Parcelable_Item[] newArray(int size) {
            return new Image_Parcelable_Item[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByteArray(this.bytes);
    }

    public byte[] getBytes(){
        return bytes;
    }
    public void setBytes(byte[] bytes){
        this.bytes=bytes;
    }

}
