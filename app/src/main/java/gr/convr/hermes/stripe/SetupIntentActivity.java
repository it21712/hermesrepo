package gr.convr.hermes.stripe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.SetupIntentResult;
import com.stripe.android.model.SetupIntent;

import java.io.IOException;

import gr.convr.hermes.core.resources.StringExtras;
import gr.convr.hermes.R;
import gr.convr.hermes.easyfuel.server.StripeServerAPIS;
import gr.convr.hermes.easyfuel.tests.NavBar;
import gr.convr.hermes.storage.Storage;

import gr.convr.hermes.wickets.WicketCodeActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class SetupIntentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_intent);

        RecurringPayment.loadPage(this, StringExtras.getEmailValue(), findViewById(R.id.save_btn),
                findViewById(R.id.cardInputWidget));

        StripeServerAPIS.initConfigurePayment(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Handle the result of stripe.confirmSetupIntent

        StripeServerAPIS.onSetupResult(requestCode, data, new ApiResultCallback<SetupIntentResult>() {
            @Override
            public void onSuccess(@NonNull SetupIntentResult result) {
                SetupIntent setupIntent = result.getIntent();
                SetupIntent.Status status = setupIntent.getStatus();
                if (status == SetupIntent.Status.Succeeded) {
                    // Setup completed successfully
                    String pId = setupIntent.getPaymentMethodId();
                    StripeServerAPIS.createCustomer("test", StringExtras.getEmailValue(), pId, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.d("CREATE_CUST", e.getMessage());
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if(response.isSuccessful()){
                                String res = response.body().string();
                                StripeServerAPIS.Customer c = new Gson().fromJson(res, StripeServerAPIS.Customer.class);
                                Log.d("CREATE_CUST", "success");

                                //Storage.saveStripeIds2(SetupIntentActivity.this, StringExtras.getEmailValue(), StringExtras.CUSTOMER_ID, c.getCustomerId());
                                Storage.saveProcessingInfo(getBaseContext(), StringExtras.PROCCESSOR_STRIPE, StringExtras.EMAIL_VALUE, StringExtras.CUSTOMER_ID, c.getCustomerId());
                                StripeServerAPIS.sendRequest2(StripeServerAPIS.SAFE_CARD_DETAILS_URL, new Gson().toJson(c), new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {

                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        if(response.isSuccessful()){
                                            String res = response.body().string();
                                            Storage.saveCardDetails(SetupIntentActivity.this, StringExtras.IS_EMPLOYEE, StringExtras.EMAIL_VALUE, res);
                                            StripeServerAPIS.CardInfo cardInfo = new Gson().fromJson(res, StripeServerAPIS.CardInfo.class);
                                            SetupIntentActivity.this.runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Successfuly saved card", Toast.LENGTH_SHORT).show());
                                            int sect = getIntent().getIntExtra(StringExtras.SECTOR_INTENT, 0);
                                            Intent i;
                                            switch (sect){
                                                case 0:
                                                    i = new Intent(SetupIntentActivity.this, NavBar.class);
                                                    i.putExtra(StringExtras.SECTOR_INTENT, 0);
                                                    startActivity(i);
                                                    break;
                                                /*case 1:
                                                    i = new Intent(SetupIntentActivity.this, BarcodeMain.class); //TODO go to shopping cart
                                                    i.putExtra(StringExtras.SECTOR_INTENT, 1);
                                                    startActivity(i);
                                                    break;*/
                                                case 2:
                                                    startActivity(new Intent(SetupIntentActivity.this, WicketCodeActivity.class));

                                                    break;
                                                default:
                                                    break;

                                            }

                                            finish();
                                        }
                                    }
                                });


                            }else{
                                Log.d("CREATE_CUST", response.message());
                            }
                        }
                    });

                    Storage.saveStripeIds2(SetupIntentActivity.this, StringExtras.getEmailValue(), StringExtras.PAYMENT_TOKEN_ID, setupIntent.getPaymentMethodId());

                } else if (status == SetupIntent.Status.RequiresPaymentMethod) {
                    // Setup failed – allow retrying using a different payment method

                }
            }

            @Override
            public void onError(@NonNull Exception e) {
                // Setup request failed – allow retrying using the same payment method

            }
        });

    }

    @Override
    public void onBackPressed() {

        return;
        /*Intent i = new Intent(this, MainActivity.class);

        i.putExtra("USER_MAIL", getIntent().getStringExtra("USER_MAIL"));
        i.putExtra("USER_ID", getIntent().getStringExtra("CUSTOMER_ID"));
        i.putExtra("PAYMENT_METHOD", getIntent().getStringExtra("PAYMENT_METHOD"));

        startActivity(i);*/

    }

}
