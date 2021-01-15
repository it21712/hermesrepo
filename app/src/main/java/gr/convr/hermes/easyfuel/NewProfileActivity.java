package gr.convr.hermes.easyfuel;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import gr.convr.hermes.R;
import gr.convr.hermes.core.resources.StringExtras;
import gr.convr.hermes.easyfuel.tests.NavBar;
import gr.convr.hermes.storage.Storage;


public class NewProfileActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_profile);

        int load = getIntent().getIntExtra("LOAD", 0);
        String prevName = getIntent().getStringExtra(StringExtras.PROFILE_NAME);

        final EditText profName = findViewById(R.id.prof_name);
        final RadioGroup fuelTypes = findViewById(R.id.fuelType_group);
        final RadioGroup fuelAmounts = findViewById(R.id.fuelAmount_group);

        if(load==1){
            profName.setText(getIntent().getStringExtra(StringExtras.PROFILE_NAME));
            fuelTypes.check(getGroupChildByName(fuelTypes, getIntent().getStringExtra(StringExtras.PROFILE_FUEL_TYPE)));
            fuelAmounts.check(getGroupChildByName(fuelAmounts, getIntent().getStringExtra(StringExtras.PROFILE_FUEL_AMOUNT)));
        }

        Button saveBtn = findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(v -> {

            RadioButton fuelType = findViewById(fuelTypes.getCheckedRadioButtonId());
            RadioButton fuelAmount = findViewById(fuelAmounts.getCheckedRadioButtonId());

            if(load == 1){


                Storage.deleteProfile(NewProfileActivity.this, StringExtras.EMAIL_VALUE, prevName);
                Storage.saveProfile(NewProfileActivity.this, StringExtras.EMAIL_VALUE, profName.getText().toString(),
                        fuelType.getText().toString(), fuelAmount.getText().toString());

            }
            else{

                Storage.saveProfile(NewProfileActivity.this, StringExtras.EMAIL_VALUE,
                        profName.getText().toString(), fuelType.getText().toString(), fuelAmount.getText().toString());
            }

            Intent i = new Intent(getBaseContext(), NavBar.class);
            startActivity(i);
            finish();
        });




    }

    @Override
    public void onBackPressed(){

        Intent i = new Intent(this, ProfilesActivity.class);

        startActivity(i);
        finish();

    }

    @Override
    protected void onResume(){
        super.onResume();

        if(getIntent().hasExtra("PROF_NAME")){
            EditText pName = findViewById(R.id.prof_name);
            RadioGroup fType = findViewById(R.id.fuelType_group);
            RadioGroup fAmount = findViewById(R.id.fuelAmount_group);

            pName.setText(getIntent().getStringExtra("PROF_NAME"));
            fType.check(fType.findViewWithTag(getIntent().getStringExtra("PROF_FUEL_TYPE")).getId());
            fAmount.check(fAmount.findViewWithTag(getIntent().getStringExtra("PROF_FUEL_AMOUNT")).getId());

        }


    }

    private int getGroupChildByName(RadioGroup group, String name){
        for(int i = 0; i < group.getChildCount(); i++){
            Button b = (Button)group.getChildAt(i);
            if(b.getText().toString().equals(name))
                return b.getId();
        }
        return -1;
    }

}
