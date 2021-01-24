package gr.convr.hermes.core.resources;

import android.widget.ImageView;

public class RetailProduct {

    private String id;
    private String name;
    private double price;
    private String imgTxt;

    public RetailProduct(String id, String name, double price, String imgTxt) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imgTxt = imgTxt;
    }

    public String getId(){
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getimgTxt() {
        return imgTxt;
    }

    public void setImgTxt(String imgTxt) {
        this.imgTxt = imgTxt;
    }
}
