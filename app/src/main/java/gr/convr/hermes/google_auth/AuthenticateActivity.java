package gr.convr.hermes.google_auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.List;

import gr.convr.hermes.R;
import gr.convr.hermes.firebase.FirebaseAPIS;

public class AuthenticateActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticate);


        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build());


        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                1);


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Log.d("SIGN_IN_STATUS", "User sign in successfull");
                ValidateUser(user.getEmail(), user.getUid());
            } else {
                Log.d("SIGN_IN_STATUS", "User sign in failed");
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }

    @Override
    public void onBackPressed(){
        return;
    }

    private void ValidateUser(final String mail, final String id){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("users").document(mail);

        docRef.get().addOnCompleteListener(task -> {

            if(task.isSuccessful()){
                DocumentSnapshot doc = task.getResult();
                if(doc.exists()){
                    Log.d("USER_VALIDATION", "User already exists, loging him in...");
                    FirebaseAPIS.fieldQuery(mail, task1 -> {
                        DocumentSnapshot document = task1.getResult();
                        String cId = String.format("%s", document.get("customerId"));
                        Intent loginIntent = new Intent(getBaseContext(), LoginActivity.class);
                        loginIntent.putExtra("USER_MAIL", mail);
                        loginIntent.putExtra("USER_ID", cId);
                        startActivity(loginIntent);
                        finish();
                    });

                }else{
                    Log.d("USER_VALIDATION", "User is being registered...");

                    Intent regIntent = new Intent(getBaseContext(), RegisterActivity.class);
                    regIntent.putExtra("USER_MAIL", mail);
                    regIntent.putExtra("USER_ID", id);
                    startActivity(regIntent);
                    finish();

                }
            }else{
                Log.d("USER_VALIDATION", "Failed with: ", task.getException());
            }

        });

    }


}
