package gr.convr.hermes.easyfuel.enterprice.eu.vatchecker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.gson.Gson;

import java.io.IOException;

import gr.convr.hermes.core.resources.StringExtras;
import gr.convr.hermes.R;
import gr.convr.hermes.easyfuel.server.StripeServerAPIS;
import gr.convr.hermes.firebase.FirebaseAPIS;
import gr.convr.hermes.storage.Storage;
import gr.convr.hermes.stripe.SetupIntentActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class VATCheckActivity extends AppCompatActivity {

    private static Gson gson = new Gson();

    ConstraintLayout compInfoLayout;

    TextView companyNameText;
    TextView companyAddressText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vatcheck);

        compInfoLayout = findViewById(R.id.companyInfo_layout);
        compInfoLayout.setVisibility(View.GONE);

        EditText vatInput = findViewById(R.id.vat_input_text);

        companyNameText = findViewById(R.id.companyName_text);
        companyAddressText = findViewById(R.id.companyAddress_text);

        Button checkBtn = findViewById(R.id.vatCheck_btn);
        checkBtn.setOnClickListener(v -> {

            String req = "{countryCode: " + "EL" + ",vatNumber: " + vatInput.getText().toString()+"}";

            StripeServerAPIS.sendRequest2(StripeServerAPIS.VAT_CHECK_URL, req, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("VAT CHECK", "failure");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(response.isSuccessful()){
                        EUVatCheckResponse vatCheckResponse = gson.fromJson(response.body().string(), EUVatCheckResponse.class);
                        if(vatCheckResponse.isValid()){
                            showCompanyInfo(true, vatCheckResponse.getName(), vatCheckResponse.getAddress());
                        }else{

                            showCompanyInfo(false, null, null);
                        }
                    }else{
                        Log.d("VAT CHECK", "response error");
                    }
                }
            });

        });

        Button continueBtn = findViewById(R.id.continue_btn);
        continueBtn.setOnClickListener(v -> {

            StringExtras.COMPANY_VAT = vatInput.getText().toString();
            Storage.saveProcessingInfo(getApplicationContext(), StringExtras.PROCCESSOR_STRIPE, StringExtras.EMAIL_VALUE, StringExtras.COMPANY_VAT_ID, StringExtras.COMPANY_VAT);
            FirebaseAPIS.updateField(StringExtras.EMAIL_VALUE, "companyVat", StringExtras.COMPANY_VAT, null);
            Intent i = new Intent(VATCheckActivity.this, SetupIntentActivity.class);
            int sect = getIntent().getIntExtra(StringExtras.SECTOR_INTENT, 0);
            switch (sect){
                case 0:
                    i.putExtra(StringExtras.SECTOR_INTENT, StringExtras.FUEL_INTENT);
                    startActivity(i);
                    break;
                case 1:
                    i.putExtra(StringExtras.SECTOR_INTENT, StringExtras.STORES_INTENT);
                    startActivity(i);
                    break;
                case 2:
                    i.putExtra(StringExtras.SECTOR_INTENT, StringExtras.LOCKERS_INTENT);
                    startActivity(i);
                    break;
                default:
                    break;

            }

            finish();

        });



    }
    private void showCompanyInfo(boolean valid, String compName, String compAddress){

        if(valid){
            VATCheckActivity.this.runOnUiThread(() -> {

                companyNameText.setText(compName);
                companyAddressText.setText(compAddress);
                compInfoLayout.setVisibility(View.VISIBLE);

            });

        }else{
            VATCheckActivity.this.runOnUiThread(() -> {
                compInfoLayout.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Invalid VAT number", Toast.LENGTH_LONG).show();
            });

        }

    }

}
