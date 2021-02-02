package gr.convr.hermes.google_auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;

import gr.convr.hermes.core.MainMenu;
import gr.convr.hermes.core.resources.StringExtras;
import gr.convr.hermes.R;
import gr.convr.hermes.storage.Storage;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText usrName = findViewById(R.id.nameInputLogin);
        final EditText password = findViewById(R.id.passInputLogin);
        TextView logoutTxt = findViewById(R.id.logout_text);
        logoutTxt.setOnClickListener(v -> AuthUI.getInstance()
                .signOut(getApplicationContext())
                .addOnCompleteListener(task -> startActivity(new Intent(getBaseContext(), AuthenticateActivity.class))));


        Button loginButton = findViewById(R.id.login_btn);

        loginButton.setOnClickListener(v -> ValidateUser(getIntent().getStringExtra("USER_MAIL"),
                usrName.getText().toString(),
                password.getText().toString()));
    }


    @Override
    public void onBackPressed(){
        return;
    }

    private void ValidateUser(String mail, final String name, final String password){

        FirebaseFirestore db = FirebaseFirestore.getInstance();


        DocumentReference docRef = db.collection("users").document(mail);

        docRef.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                DocumentSnapshot document = task.getResult();

                if(document.get("name").equals(name) &&
                        document.get("password").equals(password)){

                    Log.d("USER_LOGIN_STATS", "Login success");
                    boolean isEmployee = (boolean)document.get("employee");
                    StringExtras.setIsEmployee(isEmployee);//TODO GET INFO FROM FIREBASE IF CONNECTED TO INTERNET ELSE FROM STORAGE
                    StringExtras.setEmailValue(mail);
                    StringExtras.COMPANY_VAT = (String)document.get("companyVat");

                    if(!isEmployee){

                    if(Storage.checkExists(getBaseContext(), false,StringExtras.getEmailValue())){
                        String defaultTokenId = Storage.getProcessingInfo(getBaseContext(), false, StringExtras.getEmailValue(), StringExtras.PAYMENT_TOKEN_ID);
                        if(defaultTokenId != null)
                            StringExtras.SELECTED_PAYMENT_TOKEN_ID = defaultTokenId;
                    }


                    }
                    startActivity(new Intent(LoginActivity.this, MainMenu.class));
                    finish();
                    /*if(Storage.getProcessingInfo(LoginActivity.this, StringExtras.IS_EMPLOYEE, StringExtras.EMAIL_VALUE, StringExtras.CUSTOMER_ID) == null)
                        startActivity(new Intent(LoginActivity.this, EasyFuelLoginActivity.class));//EasyFuelLoginActivity
                    else startActivity(new Intent(LoginActivity.this, NavBar.class));//MainActivity*/

                    finish();

                }else
                    Log.d("USER_LOGIN_STATS", "Login failed");

            }else {
                Log.d("USER_LOGIN_STATS", "get failed with ", task.getException());
            }

        });

    }
}
