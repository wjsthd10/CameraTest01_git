package com.example.cameratest01.RC_Items;

public class ImageFolder {

    public String path;
    public String FolderName;
    public int numberOfPics=0;
    public String firstPic;

    public ImageFolder() {
    }

    public ImageFolder(String path, String folderName, int numberOfPics, String firstPic) {
        this.path = path;
        FolderName = folderName;
    }

    public String getPath(){return path;}
    public void setPath(String path){this.path=path;}
    public String getFolderName(){return FolderName;}
    public void setFolderName(String folderName){FolderName=folderName;}
    public int getNumberOfPics(){return numberOfPics;}
    public void setNumberOfPics(int numberOfPics){this.numberOfPics=numberOfPics;}
    public void addPics(){this.numberOfPics++;}
    public String getFirstPic(){return firstPic;}
    public void setFirstPic(String firstPic){this.firstPic=firstPic;}


}
