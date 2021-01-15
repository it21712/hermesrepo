package gr.convr.hermes.google_auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import gr.convr.hermes.core.resources.User;
import gr.convr.hermes.R;
import gr.convr.hermes.storage.Storage;

public class RegisterActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        final EditText userNameText = findViewById(R.id.usrNameInput);
        final EditText userPassText = findViewById(R.id.passwordInput);
        Button regButton = findViewById(R.id.regBtn);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);


        regButton.setOnClickListener(v -> {

            /*StripeServerAPIS.createCustomer(getIntent().getStringExtra("USER_MAIL"), new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("STRIPE_CUSTOMER_CREATE", e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    if(!response.isSuccessful())
                        Log.d("STRIPE_CUSTOMER_CREATE", response.message());
                    else{

                        StripeServerAPIS.Customer c = new Gson().fromJson(response.body().string(), StripeServerAPIS.Customer.class);

                        RegisterUser(
                                db,
                                getIntent().getStringExtra("USER_MAIL"),
                                userNameText.getText().toString(),
                                userPassText.getText().toString(),
                                getIntent().getStringExtra("USER_ID"));

                        Intent loginIntent = new Intent(getBaseContext(), LoginActivity.class);
                        loginIntent.putExtra("USER_MAIL", getIntent().getStringExtra("USER_MAIL"));
                        //loginIntent.putExtra("USER_ID", c.getCustomerId());
                        startActivity(loginIntent);
                        finish();

                    }


                }
            });*/

            RegisterUser(db, getIntent().getStringExtra("USER_MAIL"),
                    userNameText.getText().toString(),
                    userPassText.getText().toString(),
                    getIntent().getStringExtra("USER_ID"));


        });

    }

    @Override
    public void onBackPressed(){
        return;
    }

    private void RegisterUser(FirebaseFirestore db, String mail, String name,
                              String password, String id, String customerId){

        User user1 = new User(mail, name, password, id, customerId);

        db.collection("users").document(mail)
                .set(user1)
                .addOnSuccessListener(aVoid -> {
                    Log.d("USER_REG_STATUS", "DocumentSnapshot successfully written!");
                    String fMail = mail.replace(".", "_");
                    Storage.createUserDirs(RegisterActivity.this, fMail);
                })
                .addOnFailureListener(e -> Log.w("USER_REG_STATUS", "Error writing document", e));
    }



    private void RegisterUser(FirebaseFirestore db, String mail, String name,
                              String password, String id){

        User user1 = new User(mail, name, password, id);

        db.collection("users").document(mail)
                .set(user1)
                .addOnSuccessListener(aVoid -> {
                    Log.d("USER_REG_STATUS", "DocumentSnapshot successfully written!");
                    String fMail = mail.replace(".", "_");
                    Storage.createUserDirs(RegisterActivity.this, fMail);

                    Intent loginIntent = new Intent(getBaseContext(), LoginActivity.class);
                    loginIntent.putExtra("USER_MAIL", getIntent().getStringExtra("USER_MAIL"));
                    startActivity(loginIntent);
                    finish();

                })
                .addOnFailureListener(e -> Log.w("USER_REG_STATUS", "Error writing document", e));
    }

}
