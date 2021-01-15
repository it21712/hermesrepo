package gr.convr.hermes.easyfuel;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.zxing.WriterException;

import java.io.IOException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import gr.convr.hermes.R;
import gr.convr.hermes.core.resources.StringExtras;
import gr.convr.hermes.easyfuel.server.BraintreeServerAPIS;
import gr.convr.hermes.easyfuel.server.StripeServerAPIS;
import gr.convr.hermes.easyfuel.tests.NavBar;
import gr.convr.hermes.storage.Storage;
import gr.convr.hermes.stripe.InvoiceConfig;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class BarcodeActivity extends AppCompatActivity {


    private String customerId;
    private String tokenId;
    private String productId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);
        customerId = Storage.getProcessingInfo(this, StringExtras.IS_EMPLOYEE, StringExtras.EMAIL_VALUE, StringExtras.CUSTOMER_ID);
        //paymentMethodId = Storage.getProcessingInfo(this, StringExtras.IS_EMPLOYEE, StringExtras.EMAIL_VALUE, StringExtras.PAYMENT_METHOD_ID);
        setupCode();

        Button backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(v -> {

            Intent i = new Intent(getBaseContext(), NavBar.class);

            startActivity(i);
            finish();

        });

    }

    @Override
    public void onBackPressed(){

        return;

    }

    private void setupCode(){

        Intent i = getIntent();

        String del = "#";

        String fAmount = getIntent().getStringExtra("fAmount");
        productId = "sku_"+fAmount.substring(1);

        tokenId = StringExtras.SELECTED_PAYMENT_TOKEN_ID;
        String text = customerId+del+tokenId+del+productId;

        ImageView qrImage = findViewById(R.id.imageView);

        generateQR(text, 400, qrImage);
        if(!StringExtras.IS_EMPLOYEE) braintreePay(customerId, productId, tokenId);
        else createInvoiceItem(productId);


    }


    private void createInvoiceItem(String product){

        StripeServerAPIS.CompanyMap cm = new StripeServerAPIS.CompanyMap("");

        cm.setCompanyVat(StringExtras.COMPANY_VAT);//TODO METAFORIKH A.E for testing
        StripeServerAPIS.sendRequest2(StripeServerAPIS.COMPANY_MAP, cm, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("STATUS", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    String res = response.body().string();

                    StripeServerAPIS.CompanyMap cm = new Gson().fromJson(res, StripeServerAPIS.CompanyMap.class);
                    Log.d("COMPANY MAP", cm.getCompanyId());
                    InvoiceConfig.InvoiceItemParams ii = new InvoiceConfig.InvoiceItemParams(product, "eur", cm.getCompanyId());
                    StripeServerAPIS.sendRequest2(StripeServerAPIS.CREATE_INVOICE_ITEM_URL, ii, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.d("INVOICE_ITEM_CREATE", "FAILED");
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {

                            if(response.isSuccessful()){
                                Log.d("INVOICE_ITEM_CREATE", "SUCCESS");
                            }else{Log.d("INVOICE_ITEM_CREATE", response.message());}

                        }
                    });

                }
            }
        });
    }

    private void braintreePay(String customerId, String productId, String tokenId){

        BraintreeServerAPIS.sendRequest2(BraintreeServerAPIS.CLIENT_TOKEN_URL, "", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    BraintreeServerAPIS.CustomerCharge charge = new BraintreeServerAPIS.CustomerCharge(customerId, productId, tokenId);

                    BraintreeServerAPIS.sendRequest2(BraintreeServerAPIS.CHECKOUT_URL, charge, new Callback() {

                        Intent i = new Intent(BarcodeActivity.this, TransactionStatusMessageActivity.class);

                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.d("CHARGE_STATUS", "CHARGE FAILED");
                            i.putExtra("status" , "_FAIL_");
                            startActivity(i);
                            finish();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String res = response.body().string();
                            Log.d("CALENDAR RESPONSE", res);
                            Log.d("CHARGE_STATUS", "CHARGE SUCCEEDED");
                            i.putExtra("status" , "_SUCCESS_");
                            i.putExtra("date", res);
                            startActivity(i);
                            finish();
                        }
                    });
                }
            }
        });




    }

    private void generateQR(String text, int size, ImageView qrImage){

        QRGEncoder encoder = new QRGEncoder(text, null, QRGContents.Type.TEXT, size);
        try {
            Bitmap bitmap = encoder.encodeAsBitmap();
            qrImage.setImageBitmap(bitmap);
        } catch (WriterException e) {
            Log.v("QR WRITER STATUS", e.toString());
        }

    }
}
