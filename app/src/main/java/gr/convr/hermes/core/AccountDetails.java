package gr.convr.hermes.core;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import gr.convr.hermes.R;

public class AccountDetails extends AppCompatActivity {


    private BottomSheetDialog pmsDialog;
    private TextView usernameText;
    private TextView pmsText;

    private Button cardsBtn, paypalBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);


        usernameText = findViewById(R.id.username_text);
        usernameText.setText("Haris Kyr");//StringExtras.EMAIL_VALUE

        pmsDialog = new BottomSheetDialog(AccountDetails.this);
        pmsDialog.setContentView(R.layout.bottom_sheet_popup_payment_methods);
        pmsDialog.setCancelable(true);



        cardsBtn = (Button) pmsDialog.findViewById(R.id.cards_btn);
        cardsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountDetails.this, CardsActivity.class));
                finish();
            }
        });

        paypalBtn = (Button)pmsDialog.findViewById(R.id.paypal_btn);
        paypalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountDetails.this, PaypalAccountsActivity.class));
                finish();
            }
        });

        TextView pmsText = findViewById(R.id.pms_text);
        pmsText.setOnClickListener(v -> {
            pmsDialog.show();
        });

    }
}
