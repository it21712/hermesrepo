package gr.convr.hermes.easyfuel.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import gr.convr.hermes.core.resources.StringExtras;
import gr.convr.hermes.R;
import gr.convr.hermes.easyfuel.braintreegateway.BraintreeSetupIntentActivity;
import gr.convr.hermes.easyfuel.enterprice.eu.vatchecker.VATCheckActivity;
import gr.convr.hermes.easyfuel.tests.NavBar;
import gr.convr.hermes.firebase.FirebaseAPIS;
import gr.convr.hermes.storage.Storage;

import gr.convr.hermes.wickets.WicketCodeActivity;

public class HermesUsageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hermes_usage);

        int sect = getIntent().getIntExtra(StringExtras.SECTOR_INTENT, 0);

        Button citizenBtn = findViewById(R.id.citizen_btn);
        citizenBtn.setOnClickListener(v -> {
            FirebaseAPIS.updateField(StringExtras.getEmailValue(), "employee", false, task -> {
                if(task.isSuccessful()){
                    StringExtras.setIsEmployee(false);
                }else{
                    StringExtras.showToast(HermesUsageActivity.this, "Something went wrong", 0);

                }
            });

            if(Storage.checkExists(HermesUsageActivity.this, StringExtras.IS_EMPLOYEE, StringExtras.getEmailValue())){
                if(Storage.getProcessingInfo(HermesUsageActivity.this, StringExtras.IS_EMPLOYEE, StringExtras.getEmailValue(), StringExtras.CUSTOMER_ID) == null){
                    Intent i = new Intent(HermesUsageActivity.this, BraintreeSetupIntentActivity.class);
                    i.putExtra(StringExtras.SECTOR_INTENT, sect);
                    i.putExtra(StringExtras.PAYMENT_METHOD_OP, StringExtras.NEW_CUSTOMER);
                    Log.d("SECT", sect+"");
                    startActivity(i);
                }else{
                    Intent i = new Intent(HermesUsageActivity.this, NavBar.class);//MainActivity
                    startActivity(i);
                }
            }else{
                Intent i = new Intent(HermesUsageActivity.this, BraintreeSetupIntentActivity.class);
                i.putExtra(StringExtras.SECTOR_INTENT, sect);
                i.putExtra(StringExtras.PAYMENT_METHOD_OP, StringExtras.NEW_CUSTOMER);
                Log.d("SECT", sect+"");
                startActivity(i);

            }


        });

        ImageButton citizenInfoBtn = findViewById(R.id.citizen_info_btn);
        citizenInfoBtn.setOnClickListener(v -> showInfoWindow("INFO", "For purchases with receipt issuance"));

        Button employeeBtn = findViewById(R.id.employee_btn);
        employeeBtn.setOnClickListener(v -> {
            FirebaseAPIS.updateField(StringExtras.getEmailValue(), "employee", true, task -> {
                if(task.isSuccessful()){
                    StringExtras.setIsEmployee(true);
                }else{
                    StringExtras.showToast(HermesUsageActivity.this, "Something went wrong", 0);

                }
            });

            if(!Storage.checkExists(HermesUsageActivity.this, StringExtras.IS_EMPLOYEE, StringExtras.getEmailValue())){

                Intent i = new Intent(HermesUsageActivity.this, VATCheckActivity.class);
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

            }

            if(Storage.getProcessingInfo(getBaseContext(), StringExtras.IS_EMPLOYEE, StringExtras.EMAIL_VALUE, StringExtras.CUSTOMER_ID) == null){
                Intent i = new Intent(HermesUsageActivity.this, VATCheckActivity.class);
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

            }else{
                Intent i;
                switch (sect){
                    case 0:
                        i = new Intent(HermesUsageActivity.this, NavBar.class);
                        startActivity(i);
                        break;
                    /*case 1:
                        i = new Intent(HermesUsageActivity.this, BarcodeMain.class);//TODO go to shopping cart
                        startActivity(i);
                        break;*/
                    case 2:
                        i = new Intent(HermesUsageActivity.this, WicketCodeActivity.class);
                        startActivity(i);
                        break;
                    default:
                        break;

                }


            }

            finish();
        });

        ImageButton employeeInfoBtn = findViewById(R.id.employee_info_btn);
        employeeInfoBtn.setOnClickListener(v -> showInfoWindow("INFO", "For purchases with invoice issuance"));
    }

    private void showInfoWindow(String title, String message){

        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)

                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_menu_info_details)
                .show();
    }
}
