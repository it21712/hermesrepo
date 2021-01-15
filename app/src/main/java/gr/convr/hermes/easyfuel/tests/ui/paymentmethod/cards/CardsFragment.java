package gr.convr.hermes.easyfuel.tests.ui.paymentmethod.cards;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import gr.convr.hermes.core.resources.PaymentMethodGroup;
import gr.convr.hermes.core.resources.StringExtras;
import gr.convr.hermes.R;
import gr.convr.hermes.easyfuel.braintreegateway.BraintreeSetupIntentActivity;
import gr.convr.hermes.easyfuel.server.BraintreeServerAPIS;
import gr.convr.hermes.storage.Storage;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CardsFragment extends Fragment {

    private CardsViewModel cardsViewModel;


    private LinearLayout groupsLayout;

    private FloatingActionButton addPmFAB;

    private boolean isOnCreate; //workaround for snackbar unsuitable parent error


    private ArrayList<BraintreeServerAPIS.CardInfo> cards = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        cardsViewModel =
                ViewModelProviders.of(this).get(CardsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_cards, container, false);

        isOnCreate = true;

        groupsLayout = root.findViewById(R.id.groups_layout);


        getCards(StringExtras.EMAIL_VALUE, root);

        int defaultCardIndex = getCardIndex(Storage.getProcessingInfo(getContext(), StringExtras.IS_EMPLOYEE, StringExtras.EMAIL_VALUE, StringExtras.PAYMENT_TOKEN_ID));
        checkBox(defaultCardIndex, true);

        addPmFAB = root.findViewById(R.id.add_pm_fab);
        addPmFAB.setOnClickListener(v -> {
            /*Snackbar.make(v, "About to add payment method", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();*/


            if(StringExtras.IS_EMPLOYEE){

            }else{
                Intent i = new Intent(getContext(), BraintreeSetupIntentActivity.class);
                i.putExtra(StringExtras.PAYMENT_METHOD_OP, StringExtras.ADD_PAYMENT_METHOD);
                i.putExtra(StringExtras.SECTOR_INTENT, StringExtras.FUEL_INTENT);
                startActivity(i);
            }


        });




        return root;
    }

    private void getCards(String mail, View root){

        ArrayList<String> cardDets = Storage.readPaymentMethods(getContext(), StringExtras.IS_EMPLOYEE, StringExtras.EMAIL_VALUE, Storage.CARD_DETAILS_FILE);
        if(cardDets == null) return;

        for(int i = 0; i < cardDets.size(); i++){

            BraintreeServerAPIS.CardInfo cardInfo = new Gson().fromJson(cardDets.get(i), BraintreeServerAPIS.CardInfo.class);
            cards.add(cardInfo);

            String content = "\t\t" + cardInfo.getBrand() + "\t\t" + cardInfo.getExpDate() + "\t\t\t\t";
            PaymentMethodGroup pmGroup = new PaymentMethodGroup(getContext(), i, cardInfo.getLast4(), content);

            LinearLayout group = new LinearLayout(getContext());
            group.setOrientation(LinearLayout.HORIZONTAL);

            CheckForParent(pmGroup.getSelectorBtn());
            CheckForParent(pmGroup.getPmIcon());
            CheckForParent(pmGroup.getPmDetails());
            CheckForParent(pmGroup.getDeleteBtn());

            pmGroup.getSelectorBtn().setOnCheckedChangeListener((buttonView, isChecked) -> {

                int count = groupsLayout.getChildCount();
                int checked = 0;
                for(int g = 0; g < count; g++){
                    LinearLayout groupLayout = (LinearLayout)groupsLayout.getChildAt(g);
                    CheckBox checkBox = (CheckBox)groupLayout.getChildAt(0);
                    if(checkBox.isChecked()) checked += 1;
                }

                if(checked == 0)buttonView.setChecked(true);

                if(isChecked){
                    setPaymentMethod(buttonView.getId(), buttonView);
                    int id = buttonView.getId();
                    for(int g = 0; g < count; g++){
                        LinearLayout groupLayout = (LinearLayout)groupsLayout.getChildAt(g);
                        CheckBox checkBox = (CheckBox)groupLayout.getChildAt(0);
                        if(g != id)
                            checkBox.setChecked(false);

                    }
                }
                isOnCreate = false;

            });

            pmGroup.getDeleteBtn().setOnClickListener(v -> {
                BraintreeServerAPIS.Customer customer = new BraintreeServerAPIS.Customer(StringExtras.CUSTOMER_ID, cardInfo.getId());
                BraintreeServerAPIS.sendRequest2(BraintreeServerAPIS.DELETE_PAYMENT_METHOD_URL, customer, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if(response.isSuccessful()){
                            String resp = response.body().string();

                            BraintreeServerAPIS.StatusInfo statusInfo = new Gson().fromJson(resp, BraintreeServerAPIS.StatusInfo.class);

                            if(statusInfo.isStatus()) {

                                Snackbar.make(v, "Successfuly deleted payment method", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();

                                //TODO remove card from cardDetails and from activity
                            }else Snackbar.make(v, "Oops! Something went wrong. Please try again", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    }
                });


            });

            group.addView(pmGroup.getSelectorBtn());
            group.addView(pmGroup.getPmIcon());
            group.addView(pmGroup.getPmDetails());
            group.setGravity(Gravity.CENTER_VERTICAL);
            group.addView(pmGroup.getDeleteBtn());

            groupsLayout.addView(group);
        }

    }

    private int getCardIndex(String id){

        for(int i = 0; i < cards.size(); i++){
            if(cards.get(i).getId().equals(id)){
                Log.d("CARD INDEX", i+"");
                return i;
            }

        }
        return -1;
    }

    private void CheckForParent(View v){
        if(v.getParent() != null)
            ((ViewGroup)v.getParent()).removeView(v);
    }

    private void checkBox(int index, boolean isChecked){
        if(index == -1) {
            isOnCreate = false;
            return;
        }
        LinearLayout groupLayout = (LinearLayout)groupsLayout.getChildAt(index);
        CheckBox checkBox = (CheckBox)groupLayout.getChildAt(0);
        checkBox.setChecked(isChecked);

    }

    private void setPaymentMethod(int id, View view){
        String tokenId = cards.get(id).getId();
        StringExtras.SELECTED_PAYMENT_TOKEN_ID = tokenId;
        Storage.replaceProcessingInfo(getContext(), StringExtras.IS_EMPLOYEE, StringExtras.EMAIL_VALUE, StringExtras.PAYMENT_TOKEN_ID, tokenId);

        if(isOnCreate) return;
        Snackbar snackbar = Snackbar.make(view, "Selected card is set as your default payment method", Snackbar.LENGTH_LONG);
        snackbar.setTextColor(Color.WHITE);
        snackbar.show();
        //TODO change snackbar color on paypal too


    }

}
