package com.maxus376.kostromafastfood;

public class ListData {
    String id, name,img, price, numItem;

    public ListData(String id, String name, String img, String price, String numItem) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.price = price;
        this.numItem = numItem;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImg() {
        return img;
    }

    public String getPrice() {
        return price;
    }

    public String getNum() {
        return numItem;
    }
}