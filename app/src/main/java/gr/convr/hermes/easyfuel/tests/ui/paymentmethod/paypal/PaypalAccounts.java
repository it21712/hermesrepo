package gr.convr.hermes.easyfuel.tests.ui.paymentmethod.paypal;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.util.ArrayList;

import gr.convr.hermes.core.resources.PaymentMethodGroup;
import gr.convr.hermes.core.resources.StringExtras;
import gr.convr.hermes.R;
import gr.convr.hermes.easyfuel.braintreegateway.BraintreeSetupIntentActivity;
import gr.convr.hermes.easyfuel.server.BraintreeServerAPIS;
import gr.convr.hermes.storage.Storage;

public class PaypalAccounts extends Fragment {

    private PaypalAccountsViewModel mViewModel;

    private LinearLayout groupsLayout;

    private ArrayList<BraintreeServerAPIS.PaypalInfo> accounts = new ArrayList<>();

    public static PaypalAccounts newInstance() {
        return new PaypalAccounts();
    }

    private boolean isOnCreate;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_paypal_accounts, container, false);

        isOnCreate = true;

        groupsLayout = root.findViewById(R.id.groups_layout);

        getAccounts(StringExtras.EMAIL_VALUE, root);

        int defaultAccountIndex = getAccountIndex(Storage.getProcessingInfo(getContext(), StringExtras.IS_EMPLOYEE, StringExtras.EMAIL_VALUE, StringExtras.PAYMENT_TOKEN_ID));
        checkBox(defaultAccountIndex, true);

        FloatingActionButton addPmFAB = root.findViewById(R.id.add_pm_fab);
        addPmFAB.setOnClickListener(v -> {

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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(PaypalAccountsViewModel.class);

    }


    private void getAccounts(String mail, View root){

        ArrayList<String> accountDets = Storage.readPaymentMethods(getContext(), StringExtras.IS_EMPLOYEE, StringExtras.EMAIL_VALUE, Storage.PAYPAL_DETAILS_FILE);
        if(accountDets == null) return;

        for(int i = 0; i < accountDets.size(); i++){

            BraintreeServerAPIS.PaypalInfo paypalInfo = new Gson().fromJson(accountDets.get(i), BraintreeServerAPIS.PaypalInfo.class);
            accounts.add(paypalInfo);

            String content = "\t\t" + paypalInfo.getEmail() + "\t\t\t\t";
            PaymentMethodGroup pmGroup = new PaymentMethodGroup(getContext(), i, PaymentMethodGroup.PAYPAL_NAME, content);

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
                Snackbar.make(v, "About to delete payment method", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            });

            group.addView(pmGroup.getSelectorBtn());
            group.addView(pmGroup.getPmIcon());
            group.addView(pmGroup.getPmDetails());
            group.setGravity(Gravity.CENTER_VERTICAL);
            group.addView(pmGroup.getDeleteBtn());

            groupsLayout.addView(group);
        }

    }

    private int getAccountIndex(String id){

        for(int i = 0; i < accounts.size(); i++){
            if(accounts.get(i).getId().equals(id)){
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
        String tokenId = accounts.get(id).getId();
        StringExtras.SELECTED_PAYMENT_TOKEN_ID = tokenId;
        Storage.replaceProcessingInfo(getContext(), StringExtras.IS_EMPLOYEE, StringExtras.EMAIL_VALUE, StringExtras.PAYMENT_TOKEN_ID, tokenId);

        if(isOnCreate) return;
        Snackbar snackbar = Snackbar.make(view, "Selected account is set as your default payment method", Snackbar.LENGTH_LONG);
        snackbar.setTextColor(Color.WHITE);
        snackbar.show();

    }

}
