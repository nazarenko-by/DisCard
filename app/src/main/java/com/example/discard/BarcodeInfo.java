package com.example.discard;

import android.os.Parcelable;

public class BarcodeInfo {

    private String Image;
    private String NameCard;
    private int Type;

    public BarcodeInfo(String Image, String NameCard, int Type) {
        this.Image = Image;
        this.NameCard = NameCard;
        this.Type = Type;
    }

    public String getImage() {
        return Image;
    }
    public int getType() {
        return Type;
    }
    public String getNameCard() {
        return NameCard;
    }
    public  void setImage(String Image) {
        this.Image = Image;
    }
    public  void setType(int Type) {
        this.Type = Type;
    }
    public  void setNameCard(String NameCard) {
        this.NameCard = NameCard;
    }
}