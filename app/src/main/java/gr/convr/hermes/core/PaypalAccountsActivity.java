package gr.convr.hermes.core;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

public class PaypalAccountsActivity extends AppCompatActivity {


    private LinearLayout groupsLayout;

    private ArrayList<BraintreeServerAPIS.PaypalInfo> accounts = new ArrayList<>();

    private boolean isOnCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paypal_accounts);

        isOnCreate = true;

        groupsLayout = findViewById(R.id.groups_layout);

        getAccounts(StringExtras.EMAIL_VALUE);

        int defaultAccountIndex = getAccountIndex(Storage.getProcessingInfo(PaypalAccountsActivity.this, StringExtras.IS_EMPLOYEE, StringExtras.EMAIL_VALUE, StringExtras.PAYMENT_TOKEN_ID));
        checkBox(defaultAccountIndex, true);

        FloatingActionButton addPmFAB = findViewById(R.id.add_pm_fab);
        addPmFAB.setOnClickListener(v -> {

            if(StringExtras.IS_EMPLOYEE){


            }else{
                Intent i = new Intent(PaypalAccountsActivity.this, BraintreeSetupIntentActivity.class);
                i.putExtra(StringExtras.PAYMENT_METHOD_OP, StringExtras.ADD_PAYMENT_METHOD);
                i.putExtra(StringExtras.SECTOR_INTENT, StringExtras.PAYPAL_ACCOUNTS_ACTIVITY_INTENT);
                startActivity(i);
                finish();
            }


        });
    }

    @Override
    public void onResume(){
        super.onResume();
        refreshAccounts();
    }

    private void getAccounts(String mail){

        ArrayList<String> accountDets = Storage.readPaymentMethods(PaypalAccountsActivity.this, StringExtras.IS_EMPLOYEE, StringExtras.EMAIL_VALUE, Storage.PAYPAL_DETAILS_FILE);
        if(accountDets == null) return;

        for(int i = 0; i < accountDets.size(); i++){

            BraintreeServerAPIS.PaypalInfo paypalInfo = new Gson().fromJson(accountDets.get(i), BraintreeServerAPIS.PaypalInfo.class);
            accounts.add(paypalInfo);

            String content = "\t\t" + paypalInfo.getEmail() + "\t\t\t\t";
            PaymentMethodGroup pmGroup = new PaymentMethodGroup(PaypalAccountsActivity.this, i, PaymentMethodGroup.PAYPAL_NAME, content);

            LinearLayout group = new LinearLayout(PaypalAccountsActivity.this);
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

                if(pmGroup.getSelectorBtn().isChecked()){
                    PaypalAccountsActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new AlertDialog.Builder(PaypalAccountsActivity.this)
                                    .setTitle("Warning")
                                    .setMessage("Cannot delete this account, because its set as your preferred payment method.")

                                    .setPositiveButton(android.R.string.ok, null)
                                    .setIcon(android.R.drawable.ic_menu_info_details)
                                    .show();
                        }
                    });
                }else{
                    BraintreeServerAPIS.Customer customer = new BraintreeServerAPIS.Customer(Storage.getProcessingInfo(PaypalAccountsActivity.this, StringExtras.IS_EMPLOYEE, StringExtras.EMAIL_VALUE, StringExtras.CUSTOMER_ID), paypalInfo.getId());
                    BraintreeServerAPIS.sendRequest2(BraintreeServerAPIS.DELETE_PAYMENT_METHOD_URL, customer, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if(response.isSuccessful()){
                                String resp = response.body().string();

                                BraintreeServerAPIS.StatusInfo statusInfo = new Gson().fromJson(resp, BraintreeServerAPIS.StatusInfo.class);
                                Log.d("status", statusInfo.isStatus()+"");
                                if(statusInfo.isStatus()) {

                                    int s = Storage.deletePaypalDetails(PaypalAccountsActivity.this, StringExtras.EMAIL_VALUE, paypalInfo.getId());
                                    if(s==0){
                                        PaypalAccountsActivity.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {

                                                refreshAccounts();
                                                Snackbar snackbar = Snackbar.make(groupsLayout, "Successfuly deleted payment method", Snackbar.LENGTH_LONG);
                                                snackbar.setTextColor(Color.WHITE);
                                                snackbar.show();
                                            }
                                        });

                                    }

                                }else{
                                    Snackbar snackbar = Snackbar.make(groupsLayout, "Oops! Something went wrong. Please try again", Snackbar.LENGTH_LONG);
                                    snackbar.setTextColor(Color.WHITE);
                                    snackbar.show();
                                }
                            }
                        }
                    });


                }


            });

            group.addView(pmGroup.getSelectorBtn());
            group.addView(pmGroup.getPmIcon());
            group.addView(pmGroup.getPmDetails());
            group.setGravity(Gravity.CENTER_VERTICAL);
            group.addView(pmGroup.getDeleteBtn());

            groupsLayout.addView(group);
        }

    }

    private void clearAccounts(){
        accounts.clear();
        groupsLayout.removeAllViews();
    }

    private void refreshAccounts(){
        isOnCreate = true;
        clearAccounts();
        getAccounts(StringExtras.EMAIL_VALUE);
        int defaultCardIndex = getAccountIndex(Storage.getProcessingInfo(PaypalAccountsActivity.this, StringExtras.IS_EMPLOYEE, StringExtras.EMAIL_VALUE, StringExtras.PAYMENT_TOKEN_ID));
        checkBox(defaultCardIndex, true);
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
        Storage.replaceProcessingInfo(PaypalAccountsActivity.this, StringExtras.IS_EMPLOYEE, StringExtras.EMAIL_VALUE, StringExtras.PAYMENT_TOKEN_ID, tokenId);

        if(isOnCreate) return;
        Snackbar snackbar = Snackbar.make(view, "Selected account is set as your default payment method", Snackbar.LENGTH_LONG);
        snackbar.setTextColor(Color.WHITE);
        snackbar.show();

    }
}
