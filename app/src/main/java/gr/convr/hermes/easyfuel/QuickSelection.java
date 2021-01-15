package gr.convr.hermes.easyfuel;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import gr.convr.hermes.R;

public class QuickSelection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_selection);


        final RadioGroup fTypeGroup = findViewById(R.id.fuelType_group);
        final RadioGroup fAmountGroup = findViewById(R.id.fuelAmount_group);

        Button okBtn = findViewById(R.id.ok_btn);
        okBtn.setOnClickListener(v -> {

            RadioButton fTypeBtn = findViewById(fTypeGroup.getCheckedRadioButtonId());
            RadioButton fAmountBtn = findViewById(fAmountGroup.getCheckedRadioButtonId());

            Intent i = new Intent(getBaseContext(), BarcodeActivity.class);

            i.putExtra("fType", fTypeBtn.getText().toString());
            i.putExtra("fAmount", fAmountBtn.getText().toString());

            startActivity(i);
            finish();

        });

        Button backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(v -> {
            Intent i = new Intent(getBaseContext(), ProfilesActivity.class);
            startActivity(i);
            finish();
        });
    }

    @Override
    public void onBackPressed(){
        return;
    }
}
