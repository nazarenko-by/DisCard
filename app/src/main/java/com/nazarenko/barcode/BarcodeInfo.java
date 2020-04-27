package com.nazarenko.barcode;

public class BarcodeInfo {

    private int ID;
    private String Image;
    private String NameCard;
    private int Type;

    public BarcodeInfo(int ID, String Image, String NameCard, int Type) {
        this.ID = ID;
        this.Image = Image;
        this.NameCard = NameCard;
        this.Type = Type;
    }

    public int getID() {
        return ID;
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