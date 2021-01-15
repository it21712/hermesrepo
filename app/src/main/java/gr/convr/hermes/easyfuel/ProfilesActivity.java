package gr.convr.hermes.easyfuel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;

import gr.convr.hermes.R;
import gr.convr.hermes.core.resources.Profile;
import gr.convr.hermes.core.resources.ProfileGroup;
import gr.convr.hermes.core.resources.StringExtras;
import gr.convr.hermes.storage.Storage;

public class ProfilesActivity extends AppCompatActivity {


    private static ArrayList<String> profNames = new ArrayList<>();

    private final String userMail = StringExtras.getEmailValue();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiles);


        Button newBtn = findViewById(R.id.add_btn);
        newBtn.setOnClickListener(v -> {
            Intent i = new Intent(getBaseContext(), NewProfileActivity.class);
            i.putExtra("id", getIntent().getStringExtra("USER_ID"));
            i.putExtra("USER_MAIL", getIntent().getStringExtra("USER_MAIL"));
            startActivity(i);
            finish();
        });

        Button quickAddBtn = findViewById(R.id.quick_add_btn);
        quickAddBtn.setOnClickListener(v -> {

            Intent i = new Intent(getBaseContext(), QuickSelection.class);
            i.putExtra("USER_MAIL", getIntent().getStringExtra("USER_MAIL"));
            startActivity(i);
            finish();

        });


    }


    @Override
    protected void onResume(){
        super.onResume();
        profNames.clear();

        LinearLayout nameLayout = findViewById(R.id.profName_layout);
        LinearLayout editLayout = findViewById(R.id.editProf_layout);
        LinearLayout deleteLayout = findViewById(R.id.deleteProf_layout);

        nameLayout.removeAllViews();
        editLayout.removeAllViews();
        deleteLayout.removeAllViews();
        LoadProfiles(nameLayout, editLayout, deleteLayout);
    }

    @Override
    public void onBackPressed(){

        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("USER_MAIL", getIntent().getStringExtra("USER_MAIL"));
        startActivity(i);
        finish();

    }

    @Override
    protected void onDestroy(){
        profNames.clear();
        super.onDestroy();
    }

    private void LoadProfiles(final LinearLayout nameLayout, final LinearLayout editLayout, final LinearLayout deleteLayout){

        int profs = Storage.profileCount(this, StringExtras.getEmailValue());
        int cc = nameLayout.getChildCount();

        if(profs > 0){
            File[] files = Storage.listProfiles(this, StringExtras.getEmailValue());

            for(int c = 0; c < profs; c++) {

                if(files[c].isFile()){

                    final String fName = files[c].getName();
                    if(!profNames.contains(fName)){
                        profNames.add(fName);
                        Log.d("NAME for time: "+"2", fName);
                        final ProfileGroup pg = new ProfileGroup(this);
                        pg.getNameBtn().setText(fName);
                        pg.getNameBtn().setOnClickListener(v -> {
                            Intent i = new Intent(getBaseContext(), BarcodeActivity.class);

                            i.putExtra("fType", pg.getProfile().getFuelType());
                            i.putExtra("fAmount", pg.getProfile().getFuelAmount());
                            i.putExtra("USER_ID", getIntent().getStringExtra("USER_ID"));
                            i.putExtra("USER_MAIL", getIntent().getStringExtra("USER_MAIL"));
                            i.putExtra("PAYMENT_METHOD", "INVOICE");
                            startActivity(i);
                        });
                        pg.setProfile(Profile.GetProfile(fName, this, StringExtras.getEmailValue()));

                        pg.getEditBtn().setOnClickListener(v ->{
                            Intent i = new Intent(ProfilesActivity.this, NewProfileActivity.class);
                            i.putExtra(StringExtras.PROFILE_NAME, pg.getProfile().getName());
                            i.putExtra(StringExtras.PROFILE_FUEL_TYPE, pg.getProfile().getFuelType());
                            i.putExtra(StringExtras.PROFILE_FUEL_AMOUNT, pg.getProfile().getFuelAmount());
                            i.putExtra("LOAD", 1);
                            startActivity(i);
                            Log.d("EDIT BTN CLICKEDS", "");

                        });


                        pg.getDeleteBtn().setOnClickListener(v -> {
                            pg.DeleteGroup();
                            Storage.deleteProfile(ProfilesActivity.this, StringExtras.getEmailValue(),
                                    pg.getProfile().getName());
                            //ProfilesActivity.this.getApplicationContext().deleteFile(pg.getProfile().getName());
                            nameLayout.removeView(nameLayout.findViewWithTag(pg.getProfile().getName()));
                            editLayout.removeView(editLayout.findViewWithTag(pg.getProfile().getName()));
                            deleteLayout.removeView(deleteLayout.findViewWithTag(pg.getProfile().getName()));


                        });
                        Place(pg, nameLayout, editLayout, deleteLayout);
                    }
                }


            }


        }

    }


    private void Place(ProfileGroup pg,LinearLayout nameLayout, LinearLayout editLayout, LinearLayout deleteLayout){

        Button nameBtn = pg.getNameBtn();
        ImageButton editBtn = pg.getEditBtn();
        ImageButton deleteBtn = pg.getDeleteBtn();
        CheckForParent(nameBtn);
        CheckForParent(editBtn);
        CheckForParent(deleteBtn);
        nameLayout.addView(nameBtn);
        editLayout.addView(editBtn);
        deleteLayout.addView(deleteBtn);

    }

    public void CheckForParent(View v){
        if(v.getParent() != null)
            ((ViewGroup)v.getParent()).removeView(v);
    }
}
