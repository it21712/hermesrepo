package gr.convr.hermes.easyfuel;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import gr.convr.hermes.core.resources.StringExtras;
import gr.convr.hermes.stripe.CardDetailsActivity;
import gr.convr.hermes.stripe.SetupIntentActivity;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*setContentView(R.layout.activity_main);
        String pId = Storage.getProcessingInfo(MainActivity.this, StringExtras.IS_EMPLOYEE,StringExtras.getEmailValue(), StringExtras.PAYMENT_METHOD_ID);

        Button cardBtn = findViewById(R.id.pm_btn);
        cardBtn.setOnClickListener(v -> {

            if(pId == null){
                saveCard();
            }else{

                loadCard();
            }
        });

        Button profilesBtn = findViewById(R.id.prof_btn);
        profilesBtn.setOnClickListener(v -> {

            if(pId == null){

                MainActivity.this.runOnUiThread(() -> Toast.makeText(getApplicationContext(), "No valid card was found. Please add a valid card first", Toast.LENGTH_LONG).show());
                return;
            }

            Intent i = new Intent(this, ProfilesActivity.class);
            startActivity(i);

        });


*/
    }

    @Override
    public void onBackPressed(){

        return;

    }

    private void saveCard(){

        Intent i = new Intent(MainActivity.this, SetupIntentActivity.class);

        startActivity(i);
    }

    private void loadCard(){

        Intent i = new Intent(this, CardDetailsActivity.class);
        i.putExtra("USER_MAIL", StringExtras.getEmailValue());
        startActivity(i);

    }


}
