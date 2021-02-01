package gr.convr.hermes.core.resources;

import android.widget.ImageView;

public class RetailProduct {


    private String name;
    private double price;


    public RetailProduct(String id, String name, double price, String imgTxt) {

        this.name = name;
        this.price = price;

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


}
