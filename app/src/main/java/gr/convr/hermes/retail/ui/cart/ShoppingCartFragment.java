package gr.convr.hermes.retail.ui.cart;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;


import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import gr.convr.hermes.R;
import gr.convr.hermes.core.AccountDetails;


public class ShoppingCartFragment extends Fragment {

    private ShoppingCartViewModel homeViewModel;

    private BottomSheetDialog product_dets_window;

    private Button yesBtn, noBtn;
    private TextView productNameTxt, productPriceTxt;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(ShoppingCartViewModel.class);
        View root = inflater.inflate(R.layout.fragment_shopping_cart, container, false);


        product_dets_window = new BottomSheetDialog(getActivity());

        loadPopup(product_dets_window);

        return root;
    }

    private void loadPopup(BottomSheetDialog popup){

        product_dets_window.setContentView(R.layout.scanned_product_popup);
        product_dets_window.setCancelable(false);


        yesBtn = popup.findViewById(R.id.yes_btn);
        yesBtn.setOnClickListener(v -> {

        });

        noBtn = popup.findViewById(R.id.no_btn);
        noBtn.setOnClickListener(v -> {

        });

        productNameTxt = popup.findViewById(R.id.product_name_txt);
        productPriceTxt = popup.findViewById(R.id.product_price_txt);


        product_dets_window.show();

    }


}
