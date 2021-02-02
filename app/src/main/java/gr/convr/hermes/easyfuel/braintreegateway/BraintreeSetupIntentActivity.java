package gr.convr.hermes.easyfuel.braintreegateway;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.braintreepayments.api.BraintreeFragment;
import com.braintreepayments.api.GooglePayment;
import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.dropin.utils.PaymentMethodType;
import com.braintreepayments.api.exceptions.InvalidArgumentException;
import com.google.gson.Gson;

import java.io.IOException;

import gr.convr.hermes.core.CardsActivity;
import gr.convr.hermes.core.PaypalAccountsActivity;
import gr.convr.hermes.core.resources.StringExtras;
import gr.convr.hermes.R;
import gr.convr.hermes.easyfuel.server.BraintreeServerAPIS;
import gr.convr.hermes.easyfuel.tests.NavBar;
import gr.convr.hermes.retail.ui.cart.ShoppingCartFragment;
import gr.convr.hermes.storage.Storage;

import gr.convr.hermes.wickets.WicketCodeActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class BraintreeSetupIntentActivity extends AppCompatActivity {

    String clientToken;
    BraintreeFragment mBraintreeFragment;

    String op;

    private static Gson gson = new Gson();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_braintree_setup_intent);

        op = getIntent().getStringExtra(StringExtras.PAYMENT_METHOD_OP);
        //Log.d("SETUP", "ON BRaINTREE");
        BraintreeServerAPIS.sendRequest2(BraintreeServerAPIS.CLIENT_TOKEN_URL, "", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                final String error = e.getMessage();
                runOnUiThread(() -> Toast.makeText(BraintreeSetupIntentActivity.this, error, Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String res = response.body().string();
                    Log.d("response", res);
                    BraintreeServerAPIS.ClientToken clientKeys = gson.fromJson(res, BraintreeServerAPIS.ClientToken.class);
                    clientToken = clientKeys.getClientSecret();
                    try {
                        mBraintreeFragment = BraintreeFragment.newInstance(BraintreeSetupIntentActivity.this, clientToken);

                        GooglePayment.isReadyToPay(mBraintreeFragment, isReadyToPay -> {
                            if (isReadyToPay)
                                showDropinGUI(clientToken, 5, true);
                            else
                                showDropinGUI(clientToken, 5, false);
                        });

                    } catch (InvalidArgumentException e) {
                        e.printStackTrace();
                    }


                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 5) {
            if (resultCode == RESULT_OK) {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                PaymentMethodType pmt = result.getPaymentMethodType();


                final String nonce = result.getPaymentMethodNonce().getNonce();
                Log.d("nonce", nonce);

                if(op.equals(StringExtras.NEW_CUSTOMER))
                    saveCustomer("John", "Doe", StringExtras.EMAIL_VALUE, nonce, pmt.getCanonicalName());
                else if(op.equals(StringExtras.ADD_PAYMENT_METHOD))
                    addPaymentMethodToCustomer(Storage.getProcessingInfo(getBaseContext(), false, StringExtras.EMAIL_VALUE, StringExtras.CUSTOMER_ID), nonce, pmt.getCanonicalName());



                // use the result to update your UI and send the payment method nonce to your server
            } else if (resultCode == RESULT_CANCELED) {
                // the user canceled
            } else {
                // handle errors here, an exception may be available in
                Exception error = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
            }
        }
    }


    private void showDropinGUI(String clientToken, int requestCode, boolean googleEnabled) {


       /* ThreeDSecureRequest threeDSecureRequest = new ThreeDSecureRequest()
                .amount("1.00")
                .email(StringExtras.EMAIL_VALUE)
                .versionRequested(ThreeDSecureRequest.VERSION_2);*/

        DropInRequest dropInRequest = new DropInRequest().clientToken(clientToken)

                /*.requestThreeDSecureVerification(true)
                .threeDSecureRequest(threeDSecureRequest)*/;
        if (!googleEnabled)
            dropInRequest.disableGooglePayment();

        startActivityForResult(dropInRequest.getIntent(BraintreeSetupIntentActivity.this), requestCode);

    }

    private void saveCustomer(String fname, String lname, String mail, String paymentNonce, String paymentType) {

        BraintreeServerAPIS.Customer c = new BraintreeServerAPIS.Customer(fname, lname, mail, paymentNonce);

        BraintreeServerAPIS.sendRequest2(BraintreeServerAPIS.CREATE_CUSTOMER_URL, gson.toJson(c), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (response.isSuccessful()) {
                    String res = response.body().string();
                    BraintreeServerAPIS.Customer customer = gson.fromJson(res, BraintreeServerAPIS.Customer.class);

                    Storage.saveProcessingInfo(getBaseContext(), StringExtras.PROCCESSOR_BRAINTREE, StringExtras.getEmailValue(), StringExtras.PAYMENT_TOKEN_ID, customer.getTokenId());
                    StringExtras.SELECTED_PAYMENT_TOKEN_ID = customer.getTokenId();
                    if(paymentType.equals("PayPal")){
                        StringExtras.PAYMENT_METHOD_TYPE = StringExtras.PAYPAL_METHOD;

                        BraintreeServerAPIS.sendRequest2(BraintreeServerAPIS.PAYPAL_DETAILS_URL, customer, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                if(response.isSuccessful()){
                                    String res = response.body().string();

                                    Storage.savePaypalDetails(getBaseContext(), StringExtras.EMAIL_VALUE, res);
                                    Log.d("Paypal Info", res);
                                }
                            }
                        });
                    }
                    else {
                        Log.d("PAYMENT METHOD", "CARD");

                        StringExtras.PAYMENT_METHOD_TYPE = StringExtras.CARD_METHOD;

                        BraintreeServerAPIS.sendRequest2(BraintreeServerAPIS.SAFE_CARD_DETAILS_URL, customer, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                if(response.isSuccessful()){
                                    String res = response.body().string();

                                    Storage.saveCardDetails(getBaseContext(), StringExtras.IS_EMPLOYEE, StringExtras.EMAIL_VALUE, res);
                                    Log.d("CARD DETAILS", res);

                                }
                            }
                        });

                    }


                    Storage.saveProcessingInfo(BraintreeSetupIntentActivity.this, StringExtras.PROCCESSOR_BRAINTREE, StringExtras.getEmailValue(), StringExtras.CUSTOMER_ID, customer.getCustomerId());
                    //TODO removed Storage.saveProcessingInfo(BraintreeSetupIntentActivity.this, StringExtras.PROCCESSOR_BRAINTREE, StringExtras.getEmailValue(), StringExtras.PAYMENT_METHOD_ID, customer.getPaymentMethod());

                    Intent i;
                    int sect = getIntent().getIntExtra(StringExtras.SECTOR_INTENT, 0);

                    switch (sect){
                        case 0:
                            i = new Intent(BraintreeSetupIntentActivity.this, NavBar.class);
                            i.putExtra(StringExtras.SECTOR_INTENT, 0);
                            break;
                        case 1:
                            i = new Intent(BraintreeSetupIntentActivity.this, ShoppingCartFragment.class);//barcodemain //TODO go to shopping cart
                            i.putExtra(StringExtras.SECTOR_INTENT, 1);
                            break;
                        case 2:
                            i = new Intent(BraintreeSetupIntentActivity.this, WicketCodeActivity.class);
                            break;
                        default:
                            i=null;
                            break;

                    }

                    startActivity(i);
                    finish();


                }

            }
        });
    }

    private void addPaymentMethodToCustomer(String customerId, String paymentNonce, String paymentType){

        BraintreeServerAPIS.Customer customer = new BraintreeServerAPIS.Customer(customerId, paymentNonce);

        BraintreeServerAPIS.sendRequest2(BraintreeServerAPIS.ADD_PAYMENT_METHOD_URL, gson.toJson(customer), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("ADD METHOD", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    String res = response.body().string();

                    BraintreeServerAPIS.Customer customer_token = gson.fromJson(res, BraintreeServerAPIS.Customer.class);


                    if(paymentType.equals("PayPal")){


                        BraintreeServerAPIS.sendRequest2(BraintreeServerAPIS.PAYPAL_DETAILS_URL, customer_token, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                if(response.isSuccessful()){
                                    String res = response.body().string();

                                    Storage.savePaypalDetails(getBaseContext(), StringExtras.EMAIL_VALUE, res);
                                    Log.d("Paypal Info", res);
                                    returnBack();
                                }
                            }
                        });
                    }
                    else {
                        Log.d("PAYMENT METHOD", "CARD");


                        BraintreeServerAPIS.sendRequest2(BraintreeServerAPIS.SAFE_CARD_DETAILS_URL, customer_token, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                if(response.isSuccessful()){
                                    String res = response.body().string();

                                    Storage.saveCardDetails(getBaseContext(), StringExtras.IS_EMPLOYEE, StringExtras.EMAIL_VALUE, res);
                                    Log.d("CARD DETAILS", res);
                                    returnBack();

                                }
                            }
                        });

                    }



                }
            }
        });
    }

    private void returnBack(){
        Intent i;
        int sect = getIntent().getIntExtra(StringExtras.SECTOR_INTENT, 0);

        switch (sect){
            case 0:
                i = new Intent(BraintreeSetupIntentActivity.this, NavBar.class);
                i.putExtra(StringExtras.SECTOR_INTENT, 0);
                break;
           /* case 1:
                i = new Intent(BraintreeSetupIntentActivity.this, BarcodeMain.class);//barcodemain //TODO go to shopping cart
                i.putExtra(StringExtras.SECTOR_INTENT, 1);
                break;*/
            case 2:
                i = new Intent(BraintreeSetupIntentActivity.this, WicketCodeActivity.class);
                break;
            case 3:
                i = new Intent(BraintreeSetupIntentActivity.this, CardsActivity.class);
                break;
            case 4:
                i = new Intent(BraintreeSetupIntentActivity.this, PaypalAccountsActivity.class);
                break;
            default:
                i=null;
                break;

        }

        startActivity(i);
        finish();
    }

}
