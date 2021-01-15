package gr.convr.hermes.easyfuel.server;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.PaymentIntentResult;
import com.stripe.android.SetupIntentResult;
import com.stripe.android.Stripe;
import com.stripe.android.model.ConfirmPaymentIntentParams;
import com.stripe.android.model.ConfirmSetupIntentParams;
import com.stripe.android.model.PaymentIntent;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.view.CardInputWidget;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class StripeServerAPIS {


    private static final String BACKEND_URL = "http://10.0.2.2:4567/";
    public static final String CREATE_CUSTOMER_URL = "create-customer";
    private static final String CREATE_PAYMENT_INTENT_URL = "create-payment-intent";
    private static final String UPDATE_CUSTOMER_URL = "update-customer";
    private static final String CHARGE_SAVED_CARD_URL = "charge-saved-card";
    private static final String CUSTOMER_CARD_SAFE_INFO = "safe-card-info";
    public static final String CREATE_SETUP_INTENT_URL = "create-setup-intent";
    public static final String ATTACH_METHOD_URL = "attach-method";
    public static final String CREATE_INVOICE_ITEM_URL = "create-invoice-item";
    public static final String CREATE_INVOICE_URL = "create-invoice";
    public static final String VAT_CHECK_URL = "vat-check";
    public static final String SAFE_CARD_DETAILS_URL = "default-card";
    public static final String COMPANY_MAP = "company-map";


    private static String paymentIntentClientSecret;

    private static Gson gson = new Gson();

    private static OkHttpClient httpClient = new OkHttpClient();

    private static Stripe stripe;

    private static Customer c;

    public static void createCustomer(String email, Callback handler){

        c = new Customer(email);

        Request req = sendRequest(CREATE_CUSTOMER_URL, gson.toJson(c));

        httpClient.newCall(req).enqueue(handler);

    }

    public static void createCustomer(String name, String email, String paymentId, Callback handler){
        c = new Customer(name, email, paymentId);

        Request req = sendRequest(CREATE_CUSTOMER_URL, gson.toJson(c));
        httpClient.newCall(req).enqueue(handler);
    }


    public static void startCheckOut(Activity checkOutActivity, Button payButton, CardInputWidget cardInputWidget){

        Request req = sendRequest(CREATE_PAYMENT_INTENT_URL, "");

        httpClient.newCall(req)
                .enqueue(new PayCallBack(checkOutActivity));




        payButton.setOnClickListener((View view) -> {

            PaymentMethodCreateParams params = cardInputWidget.getPaymentMethodCreateParams();
            if (params != null) {
                Map<String, String> extraParams = new HashMap<>();
                extraParams.put("setup_future_usage", "off_session");
                ConfirmPaymentIntentParams confirmParams = ConfirmPaymentIntentParams
                        .createWithPaymentMethodCreateParams(params, paymentIntentClientSecret, null, false, extraParams);
                stripe.confirmPayment(checkOutActivity, confirmParams);
            }
        });

    }

    public static void initConfigurePayment(Activity activity){
        PaymentConfiguration.init(activity, "pk_test_3UoIGeNH71sGfpG9JWaVjMj700MUPclwCm");
    }

    private static final class PayCallBack implements Callback {

        @NonNull
        private final WeakReference<Activity> activityRef;

        PayCallBack(@NonNull Activity activity) {
            activityRef = new WeakReference<>(activity);
        }



        @Override
        public void onFailure(@NonNull Call call, IOException e) {
            final Activity activity = activityRef.get();
            if (activity == null) {
                return;
            }

            activity.runOnUiThread(() ->
                    Toast.makeText(
                            activity, "Error: " + e.toString(), Toast.LENGTH_LONG
                    ).show()
            );
        }

        @Override
        public void onResponse(@NonNull Call call, Response response) throws IOException {

            final Activity activity = activityRef.get();
            if (activity == null) {
                return;
            }

            if (!response.isSuccessful()) {
                activity.runOnUiThread(() ->
                        Toast.makeText(
                                activity, "Error: " + response.toString(), Toast.LENGTH_LONG
                        ).show()
                );
            } else {
                onPaymentSuccess(activity, response);
            }

        }
    }

    public static void chargeCustomer(String customerId, String paymentMethodId, String productId, Callback handler){

        String JSONReq = "{customerId: "+customerId+", paymentMethodId: "+paymentMethodId+", productId: "+productId+"}";


        sendRequest2(CHARGE_SAVED_CARD_URL, JSONReq, handler);

    }


    private static void onPaymentSuccess(Activity activity, @NonNull final Response response) throws IOException {
        Gson gson = new Gson();

        ClientKeys ck = gson.fromJson(response.body().string(), ClientKeys.class);

        String stripePublishableKey = ck.getPublishableKey();
        paymentIntentClientSecret = ck.getClientSecret();


        // The response from the server includes the Stripe publishable key and
        // PaymentIntent details.
        // For added security, our sample app gets the publishable key from the server
        //String stripePublishableKey = responseMap.get("publishableKey");
        //paymentIntentClientSecret = responseMap.get("clientSecret");
        Log.d("CLIENT_SECRET", paymentIntentClientSecret);
        // Configure the SDK with your Stripe publishable key so that it can make requests to the Stripe API
        stripe = new Stripe(
                activity.getApplicationContext(),
                Objects.requireNonNull(stripePublishableKey)
        );
    }

    public static void onPaymentResult(Activity activity, int requestCode, Intent data){

        stripe.onPaymentResult(requestCode, data, new PaymentResultCallback(activity));

    }

    private static final class PaymentResultCallback
            implements ApiResultCallback<PaymentIntentResult> {
        @NonNull private final WeakReference<Activity> activityRef;

        PaymentResultCallback(@NonNull Activity activity) {
            activityRef = new WeakReference<>(activity);
        }

        @Override
        public void onSuccess(@NonNull PaymentIntentResult result) {
            final Activity activity = activityRef.get();
            if (activity == null) {
                return;
            }

            PaymentIntent paymentIntent = result.getIntent();
            PaymentIntent.Status status = paymentIntent.getStatus();
            if (status == PaymentIntent.Status.Succeeded) {
                Log.d("PAYMENT INTENT ID", paymentIntent.getId());
                //customerInfo.setPaymentMethod(paymentIntent.getPaymentMethodTypes().get(0));


                Request req = sendRequest(UPDATE_CUSTOMER_URL, "{customerId: "+c.getCustomerId()+
                        ",paymentId: " + paymentIntent.getId()+"}");

                try {
                    httpClient.newCall(req).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            } else if (status == PaymentIntent.Status.RequiresPaymentMethod) {
                // Payment failed – allow retrying using a different payment method

            }
        }

        @Override
        public void onError(@NonNull Exception e) {
            final Activity activity = activityRef.get();
            if (activity == null) {
                return;
            }

            // Payment request failed – allow retrying using the same payment method

        }
    }

    public static void sendRequest2(String url, String JSONRequest, Callback handler){

        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

        RequestBody reqBody = RequestBody.create(mediaType,  JSONRequest);

        Request req =  new Request.Builder()
                .url(BACKEND_URL + url)
                .post(reqBody)
                .build();

        httpClient.newCall(req).enqueue(handler);



    }

    public static void sendRequest2(String url, Object object, Callback handler){

        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

        RequestBody reqBody = RequestBody.create(mediaType,  gson.toJson(object));

        Request req =  new Request.Builder()
                .url(BACKEND_URL + url)
                .post(reqBody)
                .build();

        httpClient.newCall(req).enqueue(handler);

    }

    public static void sendRequest2(String url, String JSONRequest){
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

        RequestBody reqBody = RequestBody.create(mediaType,  JSONRequest);

        Request req =  new Request.Builder()
                .url(BACKEND_URL + url)
                .post(reqBody)
                .build();

        httpClient.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }

    public static Request sendRequest(String url, String JSONRequest){

        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

        RequestBody reqBody = RequestBody.create(mediaType,  JSONRequest);

        return new Request.Builder()
                .url(BACKEND_URL + url)
                .post(reqBody)
                .build();



    }

    public static void initStripe(Activity activity, String publicKey){

        stripe = new Stripe(activity, publicKey);

    }

    public static void confirmSetupIntent(Activity activity, ConfirmSetupIntentParams confirmParams){
        stripe.confirmSetupIntent(activity, confirmParams);
    }

    public static void onSetupResult(int requestCode, Intent data, ApiResultCallback<SetupIntentResult> handler){

        stripe.onSetupResult(requestCode, data, handler);

    }


    public static void getCardDetailsSafe(String paymentMethodId, Callback handler){

        String jsonReq = "{paymentMethodId: " + paymentMethodId+"}";

        sendRequest2(CUSTOMER_CARD_SAFE_INFO, jsonReq, handler);

    }


    public static class Customer{

        private String customerId;
        private String email;
        private String name;
        private String paymentId;

        public Customer(String email){
            this.email = email;
        }

        public Customer(String name, String email, String paymentId){
            this.name = name;
            this.email = email;
            this.paymentId = paymentId;
        }

        public Customer(String name, String email, String paymentId, String customerId){
            this(name, email, paymentId);
            this.customerId = customerId;
        }

        public String getCustomerId(){
            return customerId;
        }

        public String getEmail(){
            return email;
        }

        public String getPaymentId(){return paymentId;}

        public void setCustomerId(String customerId){
            this.customerId = customerId;
        }

        public void setEmail(String email){
            this.email = email;
        }

    }

    public static final class ClientKeys {

        @SerializedName("clientSecret")
        String clientSecret;

        @SerializedName("publishableKey")
        String publishableKey;

        public String getClientSecret() {
            return clientSecret;
        }

        public String getPublishableKey() {
            return publishableKey;
        }

    }

    public static final class CardInfo{

        @SerializedName("brand")
        String brand;

        @SerializedName("last4")
        String last4;

        @SerializedName("expDate")
        String expDate;

        @SerializedName("paymentMethodId")
        String paymentMethodId;

        public String getBrand(){return brand;}
        public String getLast4(){return last4;}
        public String getExpDate(){return expDate;}
        public String getPaymentMethodId(){return paymentMethodId;}

    }

    public static class PaymentMethod{

        @SerializedName("paymentMethodId")
        String paymentMethodId;

        public String getPaymentMethodId() { return paymentMethodId; }
    }

    public static class CompanyMap{

        private String companyVat;
        private String companyId;

        public CompanyMap(String companyId) {
            this.companyId = companyId;
        }

        public String getCompanyId() {
            return companyId;
        }

        public String getCompanyVat() {
            return companyVat;
        }

        public void setCompanyVat(String companyVat) {
            this.companyVat = companyVat;
        }

        public void setCompanyId(String companyId) {
            this.companyId = companyId;
        }
    }


}
