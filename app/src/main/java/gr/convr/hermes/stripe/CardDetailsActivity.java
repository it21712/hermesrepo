package gr.convr.hermes.stripe;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import gr.convr.hermes.core.resources.StringExtras;
import gr.convr.hermes.easyfuel.MainActivity;
import gr.convr.hermes.R;
import gr.convr.hermes.easyfuel.server.StripeServerAPIS;
import gr.convr.hermes.storage.Storage;

public class CardDetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_details);

       /* TextView brand = findViewById(R.id.text_brand);
        TextView last4 = findViewById(R.id.text_last);
        TextView expDate = findViewById(R.id.text_exp);
*/
        String cardJSON = Storage.readCardDetails(this, StringExtras.getEmailValue());

        StripeServerAPIS.CardInfo cardInfo = new Gson().fromJson(cardJSON, StripeServerAPIS.CardInfo.class);

        /*brand.setText(cardInfo.getBrand());
        last4.setText(cardInfo.getLast4());
        expDate.setText(cardInfo.getExpDate());*/

    }

    @Override
    public void onBackPressed(){

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
