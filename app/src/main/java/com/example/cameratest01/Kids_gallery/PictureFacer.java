package com.example.cameratest01.Kids_gallery;

public class PictureFacer {
    private String pictureName;
    private String picturePath;
    private String pictureSize;
    private String imageUri;
    private Boolean selected=false;

    public PictureFacer() {
    }

    public PictureFacer(String pictureName, String picturePath, String pictureSize, String imageUri, Boolean selected) {
        this.pictureName = pictureName;
        this.picturePath = picturePath;
        this.pictureSize = pictureSize;
        this.imageUri = imageUri;
        this.selected = selected;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }
    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public void setPictureSize(String pictureSize) {
        this.pictureSize = pictureSize;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public Boolean getSelected() {
        return selected;
    }
}
