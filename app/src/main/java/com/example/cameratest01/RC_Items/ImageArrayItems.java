package com.example.cameratest01.RC_Items;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class ImageArrayItems implements Parcelable {

    ArrayList<Image_Parcelable_Item> img_arrItems;

    protected ImageArrayItems(Parcel in) {
        img_arrItems=new ArrayList<>();
        in.readTypedList(img_arrItems, Image_Parcelable_Item.CREATOR);
    }

    public static final Creator<ImageArrayItems> CREATOR = new Creator<ImageArrayItems>() {
        @Override
        public ImageArrayItems createFromParcel(Parcel in) {
            return new ImageArrayItems(in);
        }

        @Override
        public ImageArrayItems[] newArray(int size) {
            return new ImageArrayItems[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(img_arrItems);
    }

    public ArrayList<Image_Parcelable_Item> getImg_arrItems(){
        return img_arrItems;
    }
    public void setImg_arrItems(ArrayList<Image_Parcelable_Item> img_arrItems){
        this.img_arrItems=img_arrItems;
    }

}
