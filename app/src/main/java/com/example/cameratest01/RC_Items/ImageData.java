package com.example.cameratest01.RC_Items;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class ImageData implements Serializable {

    public String ImageName="";// 파일 이름
    public String ImagePath="";// 파일 경로
    // 원본경로 비교위해서 추가
    public String basicPath="";
    public int Check;// 0 : 선택안함, 1 : 선택함
    public int Position;
    public int Dposition;
    public int Rposition;
    public int CheckNum=0;
    public int rotNum=0;
    public float rotateNum=0;// 변경된 회전값.
    public float orirotateNum=0;// 이미지가 갖고있는 회전값
    public String imageType="";    // K:키즈사랑 갤러리, D:기본 갤러리
    public String imagesPath="";
    public int sizeK=0;
    public int sizeD=0;

    // folderLost추가하기
    public String folderName="";// 폴더이름
    public String folderPath="";// 폴더경로
    public int folderCount=0;// 폴더가 갖고있는 파일 개수

    public String getBasicPath() {
        return basicPath;
    }

    public void setBasicPath(String basicPath) {
        this.basicPath = basicPath;
    }

    public String getImagesPath() {
        return imagesPath;
    }

    public void setImagesPath(String imagesPath) {
        this.imagesPath = imagesPath;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    public int getFolderCount() {
        return folderCount;
    }

    public void setFolderCount(int folderCount) {
        this.folderCount = folderCount;
    }

//    public static Creator<ImageData> getCREATOR() {
//        return CREATOR;
//    }

    public int getSizeK() {
        return sizeK;
    }

    public void setSizeK(int sizeK) {
        this.sizeK = sizeK;
    }

    public int getSizeD() {
        return sizeD;
    }

    public void setSizeD(int sizeD) {
        this.sizeD = sizeD;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public ImageData() {
    }

    public ImageData(String imageName, String imagePath, int check, int position, String imageType) {
        this.ImageName = imageName;
        this.ImagePath = imagePath;
        this.Check = check;
        this.Position = position;
        this.imageType=imageType;
    }

    public ImageData(String imageName, String imagePath, int check, int position, String imageType, String folderName, String folderPath) {
        this.ImageName = imageName;
        this.ImagePath = imagePath;
        this.Check = check;
        this.Position = position;
        this.imageType=imageType;
        this.folderName=folderName;
        this.folderPath=folderPath;
    }

    public ImageData(String imageName, String imagePath, int check, int position, int rotateNum, float orirotateNum, String imageType) {
        this.ImageName = imageName;
        this.ImagePath = imagePath;
        this.Check = check;
        this.Position = position;
        this.rotateNum = rotateNum;
        this.orirotateNum = orirotateNum;
        this.imageType=imageType;
    }

    protected ImageData(Parcel in) {
        ImageName=in.readString();
        ImagePath=in.readString();
        Check=in.readInt();
        Position=in.readInt();
        Dposition=in.readInt();
        Rposition=in.readInt();
        orirotateNum=in.readFloat();
    }

//    public static final Creator<ImageData> CREATOR = new Creator<ImageData>() {
//        @Override
//        public ImageData createFromParcel(Parcel in) {
//            return new ImageData(in);
//        }
//
//        @Override
//        public ImageData[] newArray(int size) {
//            return new ImageData[size];
//        }
//    };

//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(ImageName);
//        dest.writeString(ImagePath);
//        dest.writeInt(Check);
//        dest.writeInt(Position);
//        dest.writeInt(Dposition);
//        dest.writeInt(Rposition);
//    }

    public String getImageName() {
        return ImageName;
    }

    public void setImageName(String imageName) {
        ImageName = imageName;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public int getCheck() {
        return Check;
    }

    public void setCheck(int check) {
        Check = check;
    }

    public int getPosition() {
        return Position;
    }

    public void setPosition(int position) {
        Position = position;
    }

    public int getDposition() {
        return Dposition;
    }

    public void setDposition(int dposition) {
        Dposition = dposition;
    }

    public int getRposition() {
        return Rposition;
    }

    public void setRposition(int rposition) {
        Rposition = rposition;
    }

    public int getCheckNum() {
        return CheckNum;
    }

    public void setCheckNum(int checkNum) {
        CheckNum = checkNum;
    }

    public int getRotNum() {
        return rotNum;
    }

    public void setRotNum(int rotNum) {
        this.rotNum = rotNum;
    }

    public float getRotateNum() {
        return rotateNum;
    }

    public void setRotateNum(float rotateNum) {
        this.rotateNum = rotateNum;
    }

    public float getOrirotateNum() {
        return orirotateNum;
    }

    public void setOrirotateNum(float orirotateNum) {
        this.orirotateNum = orirotateNum;
    }
}
