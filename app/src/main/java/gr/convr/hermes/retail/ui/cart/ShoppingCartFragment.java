package gr.convr.hermes.retail.ui.cart;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.net.*;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;

import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.stripe.android.model.Card;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


import gr.convr.hermes.R;
import gr.convr.hermes.core.CardsActivity;
import gr.convr.hermes.core.resources.RetailProduct;
import gr.convr.hermes.core.resources.RetailProductGroup;
import gr.convr.hermes.easyfuel.TransactionStatusMessageActivity;
import gr.convr.hermes.easyfuel.server.BraintreeServerAPIS;


public class ShoppingCartFragment extends Fragment {


    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String string = bundle.getString("json");
            BraintreeServerAPIS.RetailProduct product = new Gson().fromJson(string, BraintreeServerAPIS.RetailProduct.class);
            Log.d("details: ", product.getName()+"\t"+product.getPrice());
            loadPopup(product_dets_window, product.getName(), product.getPrice());

            int id = getContext().getResources().getIdentifier(product.getName(), "drawable", getContext().getPackageName());
            Log.d("img sourxce", id+"");
            AddItem(nameLayout, priceLayout, product);
            return;

        }
    };

    private final Runnable mMessageSender = () -> {

        String json = " ";

        try {

            Socket s=new Socket("hermes0server.ddns.net",4568);

            DataInputStream din = new DataInputStream(s.getInputStream());



            while(true){
                json=din.readUTF();
                if(!json.equals(" ")){
                    Log.d("server sent: ", json);
                    Message msg = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString("json", json);
                    msg.setData(bundle);
                    mHandler.sendMessage(msg);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();

        }


    };


    private ShoppingCartViewModel homeViewModel;

    private BottomSheetDialog product_dets_window;

    private Button yesBtn, noBtn;
    private TextView productNameTxt, productPriceTxt;
    private ImageView productImageView;

    private ArrayList<RetailProduct> products = new ArrayList<RetailProduct>();

    private LinearLayout /*imgLayout,*/ nameLayout, priceLayout;

    private TextView totalPriceView;
    private float totalPrice = 0;
    private String dollarSign = "€";

    private View rootView;

    private FloatingActionButton addPmFAB;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(ShoppingCartViewModel.class);
        View root = inflater.inflate(R.layout.fragment_shopping_cart, container, false);


        //imgLayout = root.findViewById(R.id.product_img_layout);
        nameLayout = root.findViewById(R.id.product_name_layout);
        priceLayout = root.findViewById(R.id.product_price_layout);

        totalPriceView = root.findViewById(R.id.total_var_txt);

        String pTxt = totalPrice+dollarSign;
        totalPriceView.setText(pTxt);

        addPmFAB = root.findViewById(R.id.pay_pm_fab);
        addPmFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), TransactionStatusMessageActivity.class);
                i.putExtra("status", "_SUCCESS_");
                i.putExtra("date", "");
                startActivity(i);
                getActivity().finish();
            }
        });


        product_dets_window = new BottomSheetDialog(getActivity());

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        new Thread(mMessageSender).start();




        rootView=root;
        return root;
    }

    @Override
    public void onResume(){
        super.onResume();

        /*products.clear();

        LinearLayout imgLayout = rootView.findViewById(R.id.profName_layout);
        LinearLayout nameLayout = rootView.findViewById(R.id.editProf_layout);
        LinearLayout priceLayout = rootView.findViewById(R.id.deleteProf_layout);

        imgLayout.removeAllViews();
        nameLayout.removeAllViews();
        priceLayout.removeAllViews();
        LoadProducts(imgLayout, nameLayout, priceLayout);*/
    }

    private void loadPopup(BottomSheetDialog popup, String productName, float productPrice){

        product_dets_window.setContentView(R.layout.scanned_product_popup);
        product_dets_window.setCancelable(false);


        yesBtn = popup.findViewById(R.id.yes_btn);
        yesBtn.setOnClickListener(v -> {
            product_dets_window.hide();

        });

        noBtn = popup.findViewById(R.id.no_btn);
        noBtn.setOnClickListener(v -> {
            product_dets_window.hide();
        });

        productNameTxt = popup.findViewById(R.id.product_name_txt);
        productPriceTxt = popup.findViewById(R.id.product_price_txt);

        productNameTxt.setText(generateName(productName));
        String price = productPrice+"€";
        productPriceTxt.setText(price);

        productImageView = popup.findViewById(R.id.product_img);

        Context context = productImageView.getContext();
        int id = context.getResources().getIdentifier(productName, "drawable", context.getPackageName());
        productImageView.setImageResource(id);

        product_dets_window.show();

    }

    //TODO save scanned products somewehre so that they can be retrived if the app crashes for some reason
    private void LoadProducts(final LinearLayout imgLayout, final LinearLayout nameLayout, final LinearLayout priceLayout){



    }


    private void Place(RetailProductGroup rg, LinearLayout imgLayout, LinearLayout nameLayout, LinearLayout priceLayout){

        ImageView img = rg.getImg();
        TextView nameTxt = rg.getNameTxt();
        TextView priceTxt = rg.getPricetxt();
        CheckForParent(img);
        CheckForParent(nameTxt);
        CheckForParent(priceTxt);
        imgLayout.addView(img);
        nameLayout.addView(nameTxt);
        priceLayout.addView(priceTxt);

    }

    public void CheckForParent(View v){
        if(v.getParent() != null)
            ((ViewGroup)v.getParent()).removeView(v);
    }

    private void AddItem(/*int imgId, LinearLayout imgLayout, */LinearLayout nameLayout, LinearLayout priceLayout, BraintreeServerAPIS.RetailProduct product){


       /* ImageView pimg = new ImageView(getContext());

        pimg.setImageResource(imgId);*/

        TextView pname = new TextView(getContext());
        pname.setTextSize(14);
        pname.setGravity(Gravity.CENTER_VERTICAL);
        pname.setPadding(0,0,0,100);
        pname.setText(generateName(product.getName()));

        TextView pprice = new TextView(getContext());
        String ppriceTxt = product.getPrice()+dollarSign;
        pprice.setText(ppriceTxt);
        pprice.setGravity(Gravity.CENTER_VERTICAL);
        pprice.setPadding(0,0,0,100);
        pprice.setTextSize(14);
        //imgLayout.addView(pimg);
        nameLayout.addView(pname);
        priceLayout.addView(pprice);

        totalPrice+=product.getPrice();
        String npt = totalPrice+dollarSign;
        totalPriceView.setText(npt);

    }

    private String generateName(String _name){

        switch (_name){

            case "delta_milk":
                return "Delta Almond Milk 500ml";
            case "monster_drink":
                return "Monster Zero 500ml";
            case "loux_orange":
                return "Loux Orange 330ml";
            case "nescafe_classic":
                return "Nescafe Classic 200gr";
            case "kyknos_tomato":
                return "KYKNOS CRUSHED TOMATOES 500gr";
            default:
                return "";
        }


    }
}
