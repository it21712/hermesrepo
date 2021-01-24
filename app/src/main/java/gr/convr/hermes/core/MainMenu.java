package gr.convr.hermes.core;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import gr.convr.hermes.core.resources.StringExtras;
import gr.convr.hermes.core.statistics.PaymentsBreakdownActivity;
import gr.convr.hermes.R;
import gr.convr.hermes.easyfuel.login.HermesUsageActivity;
import gr.convr.hermes.easyfuel.tests.NavBar;
import gr.convr.hermes.retail.StoresNavBar;
import gr.convr.hermes.storage.Storage;

import gr.convr.hermes.wickets.WicketCodeActivity;

public class MainMenu extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);



        ImageButton accountsBtn = findViewById(R.id.account_btn);
        accountsBtn.setOnClickListener(v -> {
            startActivity(new Intent(MainMenu.this, AccountDetails.class));

        });

        ImageView retailImg = findViewById(R.id.retail_img);

        Button easyfuelBtn = findViewById(R.id.easyfuel_btn);
        Button storesBtn = findViewById(R.id.stores_btn);
        Button wicketBtn = findViewById(R.id.wicket_btn);
        wicketBtn.setVisibility(View.GONE);
        Intent i = new Intent(MainMenu.this, HermesUsageActivity.class);

        easyfuelBtn.setOnClickListener(v -> {

            if(Storage.getProcessingInfo(MainMenu.this, StringExtras.IS_EMPLOYEE, StringExtras.EMAIL_VALUE, StringExtras.CUSTOMER_ID) == null){
                i.putExtra(StringExtras.SECTOR_INTENT, StringExtras.FUEL_INTENT);
                startActivity(i);
            }
            else {
                Intent n = new Intent(MainMenu.this, NavBar.class);
                n.putExtra(StringExtras.SECTOR_INTENT, StringExtras.FUEL_INTENT);
                startActivity(n);
            }
        });

        storesBtn.setOnClickListener(v -> {
            if(Storage.getProcessingInfo(MainMenu.this, StringExtras.IS_EMPLOYEE, StringExtras.EMAIL_VALUE, StringExtras.CUSTOMER_ID) == null){
                i.putExtra(StringExtras.SECTOR_INTENT, StringExtras.STORES_INTENT);
                startActivity(i);
            }
            else {
                Intent n = new Intent(MainMenu.this, StoresNavBar.class);
                n.putExtra(StringExtras.SECTOR_INTENT, StringExtras.STORES_INTENT);
                startActivity(n);
            }
        });

        wicketBtn.setOnClickListener(v -> {
            startActivity(new Intent(MainMenu.this, WicketCodeActivity.class));
        });



    }

    @Override
    public void onBackPressed(){
        return;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hermes_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_analytics) {
            startActivity(new Intent(MainMenu.this, PaymentsBreakdownActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
