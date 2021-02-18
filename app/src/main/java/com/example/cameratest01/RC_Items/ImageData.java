package com.example.cameratest01.RC_Items;

import android.os.Parcel;
import android.os.Parcelable;

public class ImageData implements Parcelable {

    public String ImageName;
    public String ImagePath;
    public int Check;
    public int Position;
    public int Dposition;
    public int Rposition;
    public int CheckNum=0;
    public int rotNum=0;
    public float rotateNum=0;

    public ImageData(String imageName, String imagePath, int check, int position, int dposition, int rposition) {
        ImageName = imageName;
        ImagePath = imagePath;
        Check = check;
        Position = position;
        Dposition = dposition;
        Rposition = rposition;
    }

    public ImageData(String imageName, String imagePath, int check, int position) {
        ImageName = imageName;
        ImagePath = imagePath;
        Check = check;
        Position = position;
    }

    protected ImageData(Parcel in) {
        ImageName=in.readString();
        ImagePath=in.readString();
        Check=in.readInt();
        Position=in.readInt();
        Dposition=in.readInt();
        Rposition=in.readInt();
    }

    public static final Creator<ImageData> CREATOR = new Creator<ImageData>() {
        @Override
        public ImageData createFromParcel(Parcel in) {
            return new ImageData(in);
        }

        @Override
        public ImageData[] newArray(int size) {
            return new ImageData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ImageName);
        dest.writeString(ImagePath);
        dest.writeInt(Check);
        dest.writeInt(Position);
        dest.writeInt(Dposition);
        dest.writeInt(Rposition);
    }
}
