package gr.convr.hermes.stripe;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.stripe.android.model.ConfirmSetupIntentParams;
import com.stripe.android.model.PaymentMethod;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.view.CardInputWidget;

import java.io.IOException;

import gr.convr.hermes.easyfuel.server.StripeServerAPIS;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RecurringPayment {


    private static StripeServerAPIS.ClientKeys keys;
    private static Gson gson = new Gson();
    public static void loadPage(Activity activity, String mail, Button btn, CardInputWidget cardInputWidget){

        StripeServerAPIS.sendRequest2(StripeServerAPIS.CREATE_SETUP_INTENT_URL, "", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(" REQUEST STATUS", call.toString());

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String res = response.body().string();
                Log.d("CARD SAVE", res);
                keys = gson.fromJson(res, StripeServerAPIS.ClientKeys.class);

                StripeServerAPIS.initStripe(activity, keys.getPublishableKey());

            }
        });


        btn.setOnClickListener((View view) -> {

            PaymentMethodCreateParams.Card card = cardInputWidget.getPaymentMethodCard();


            PaymentMethod.BillingDetails billingDetails = (new PaymentMethod.BillingDetails.Builder())
                    .setEmail(mail)
                    .build();
            if (card != null) {
                // Create SetupIntent confirm parameters with the above
                PaymentMethodCreateParams paymentMethodParams = PaymentMethodCreateParams
                        .create(card, billingDetails);
                ConfirmSetupIntentParams confirmParams = ConfirmSetupIntentParams
                        .create(paymentMethodParams, keys.getClientSecret());

                StripeServerAPIS.confirmSetupIntent(activity, confirmParams);
            }
        });

    }


}

