package com.example.cameratest01.RC_Items;

import android.graphics.Bitmap;
import java.io.Serializable;

public class RC_ImageItems  implements Serializable {// implements Serializable
    public byte[] imageBM;
    public String imageName;

    public RC_ImageItems(byte[] imageBM, String imageName) {
        this.imageBM = imageBM;
        this.imageName = imageName;
    }

    public byte[] getImageBM() {
        return imageBM;
    }

    public void setImageBM(byte[] imageBM) {
        this.imageBM = imageBM;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
