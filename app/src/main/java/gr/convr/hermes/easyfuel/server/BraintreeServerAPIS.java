package gr.convr.hermes.easyfuel.server;

import android.media.Image;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.Calendar;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class BraintreeServerAPIS {

    private static final String BACKEND_URL = "http://hermes0server.ddns.net:4567/";

    public static final String CLIENT_TOKEN_URL = "client_token";
    public static final String CHECKOUT_URL = "checkout";
    public static final String CREATE_CUSTOMER_URL = "create_customer";
    public static final String SAFE_CARD_DETAILS_URL = "safe_card_details";
    public static final String PAYPAL_DETAILS_URL = "paypal_accounts";
    public static final String ADD_PAYMENT_METHOD_URL = "add_payment_method";
    public static final String DELETE_PAYMENT_METHOD_URL = "delete_payment_method";
    public static final String CART = "cart";

    private static Gson gson = new Gson();

    private static OkHttpClient httpClient = new OkHttpClient();


    public static void sendRequest2(String url, String JSONRequest, Callback handler){

        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

        RequestBody reqBody = RequestBody.create(mediaType,  JSONRequest);
        Log.d("about to connect", BACKEND_URL+url);
        Request req =  new Request.Builder()
                .url(BACKEND_URL + url)
                .post(reqBody)
                .build();

        httpClient.newCall(req).enqueue(handler);



    }

    public static void sendRequest2(String url, Object object, Callback handler){

        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

        RequestBody reqBody = RequestBody.create(mediaType,  gson.toJson(object));

        Request req =  new Request.Builder() //TODO send authentication header!!
                .url(BACKEND_URL + url)
                .post(reqBody)
                .build();

        httpClient.newCall(req).enqueue(handler);

    }

    public static class RetailProduct{

        private String name;
        private float price;
        private Image img;


        public RetailProduct(String name, float price){
            this.name = name;
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public float getPrice() {
            return price;
        }

        public void setPrice(float price) {
            this.price = price;
        }

        public Image getImg() {
            return img;
        }

        public void setImg(Image img) {
            this.img = img;
        }
    }


    public static class Customer{

        private String firstName;
        private String lastName;
        private String email;
        private String paymentMethod;
        private String customerId;
        private String tokenId;

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPaymentMethod() {
            return paymentMethod;
        }

        public void setPaymentMethod(String paymentMethodNonce) {
            this.paymentMethod = paymentMethodNonce;
        }

        public String getTokenId() {
            return tokenId;
        }

        public void setTokenId(String tokenId) {
            this.tokenId = tokenId;
        }

        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }

        public Customer(String firstName, String lastName, String email) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
        }

        public Customer(String firstName, String lastName, String email, String paymentMethod) {
            this(firstName, lastName, email);
            this.paymentMethod = paymentMethod;
        }

        public Customer(String customerId){this.customerId = customerId;}

        public Customer(String customerId, String tokenId){
            this.customerId = customerId;
            this.tokenId = tokenId;
        }
    }

    public static final class ClientToken {

        @SerializedName("clientSecret")
        String clientSecret;


        public String getClientSecret() {
            return clientSecret;
        }


    }

    public static class CustomerCharge{

        private String customerId;
        private String productId;
        private String tokenId;

        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getTokenId() {
            return tokenId;
        }

        public void setTokenId(String tokenId) {
            this.tokenId = tokenId;
        }

        public CustomerCharge(String customerId, String productId){
            this.customerId=customerId;
            this.productId=productId;
        }

        public CustomerCharge(String customerId, String productId, String tokenId){
            this(customerId, productId);
            this.tokenId = tokenId;
        }
    }

    public static final class CardInfo{

        @SerializedName("id")
        String id;

        @SerializedName("brand")
        String brand;

        @SerializedName("last4")
        String last4;

        @SerializedName("expDate")
        String expDate;

        @SerializedName("paymentMethodId")
        String paymentMethodId;

        public String getId(){return id;}
        public String getBrand(){return brand;}
        public String getLast4(){return last4;}
        public String getExpDate(){return expDate;}
        public String getPaymentMethodId(){return paymentMethodId;}

    }

    public static final class PaypalInfo{

        private String id;

        private String email;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getId() { return id; }

        public void setId(String id) { this.id = id; }
    }

    public static final class StatusInfo{

        private boolean status;

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }
    }

    public static final class TransactionInfo{
        private Calendar createdAt;

        public Calendar getCreatedAt(){return createdAt;}

        public TransactionInfo(Calendar createdAt){this.createdAt=createdAt;}
    }

}
