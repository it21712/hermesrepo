package gr.convr.hermes.core.resources;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.util.Base64;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class RetailProductGroup {

    private LinearLayout groupLayout;
    private ImageView img;
    private TextView nameTxt;
    private TextView pricetxt;

    private RetailProduct product;

    private char euroSign='€';

    public static ArrayList<RetailProduct> products = new ArrayList<>();

    public ImageView getImg() {
        return img;
    }

    public TextView getNameTxt() {
        return nameTxt;
    }

    public TextView getPricetxt() {
        return pricetxt;
    }

    public RetailProduct getProduct(){return product;}




    public void setProduct(RetailProduct product){



        nameTxt.setText(product.getName());

        String priceStr = product.getPrice()+" "+euroSign;
        pricetxt.setText(priceStr);
    }


    private Bitmap decodeImage(String encodedString){

        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }

    }

}
