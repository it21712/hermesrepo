package gr.convr.hermes.easyfuel.tests.ui.profiles;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import java.io.File;
import java.util.ArrayList;

import gr.convr.hermes.core.resources.Profile;
import gr.convr.hermes.core.resources.ProfileGroup;
import gr.convr.hermes.core.resources.StringExtras;
import gr.convr.hermes.easyfuel.BarcodeActivity;
import gr.convr.hermes.easyfuel.NewProfileActivity;
import gr.convr.hermes.easyfuel.QuickSelection;
import gr.convr.hermes.R;
import gr.convr.hermes.storage.Storage;

public class ProfilesFragment extends Fragment {

    private ProfilesViewModel profilesViewModel;
    private static ArrayList<String> profNames = new ArrayList<>();



    LinearLayout nameLayout;
    LinearLayout editLayout;
    LinearLayout deleteLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profilesViewModel =
                ViewModelProviders.of(this).get(ProfilesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profiles, container, false);

        nameLayout = root.findViewById(R.id.profName_layout);
        editLayout = root.findViewById(R.id.editProf_layout);
        deleteLayout = root.findViewById(R.id.deleteProf_layout);

        Button newBtn = root.findViewById(R.id.add_btn);
        newBtn.setOnClickListener(v -> {
            Intent i = new Intent(getContext(), NewProfileActivity.class);

            startActivity(i);

        });

        Button quickBtn = root.findViewById(R.id.quick_add_btn);
        quickBtn.setOnClickListener(v -> {
            Intent i = new Intent(getContext(), QuickSelection.class);
            startActivity(i);
        });



        return root;
    }

    @Override
    public void onResume() {

        super.onResume();
        profNames.clear();

        nameLayout.removeAllViews();
        editLayout.removeAllViews();
        deleteLayout.removeAllViews();
        LoadProfiles(nameLayout, editLayout, deleteLayout);

    }

    private void LoadProfiles(final LinearLayout nameLayout, final LinearLayout editLayout, final LinearLayout deleteLayout){

        int profs = Storage.profileCount(getContext(), StringExtras.EMAIL_VALUE);
        int cc = nameLayout.getChildCount();

        if(profs > 0){
            File[] files = Storage.listProfiles(getContext(), StringExtras.getEmailValue());

            for(int c = 0; c < profs; c++) {

                if(files[c].isFile()){

                    final String fName = files[c].getName();
                    if(!profNames.contains(fName)){
                        profNames.add(fName);
                        Log.d("NAME for time: "+"2", fName);
                        final ProfileGroup pg = new ProfileGroup(getContext());
                        pg.getNameBtn().setText(fName);
                        pg.getNameBtn().setOnClickListener(v -> {
                            Intent i = new Intent(getContext(), BarcodeActivity.class);

                            i.putExtra("fType", pg.getProfile().getFuelType());
                            i.putExtra("fAmount", pg.getProfile().getFuelAmount());
                            i.putExtra("PAYMENT_METHOD", "INVOICE");
                            startActivity(i);
                        });
                        pg.setProfile(Profile.GetProfile(fName, getContext(), StringExtras.getEmailValue()));
                        //pg.getEditBtn().setText("#");
                        pg.getEditBtn().setOnClickListener(v ->{
                            Intent i = new Intent(getContext(), NewProfileActivity.class);
                            i.putExtra(StringExtras.PROFILE_NAME, pg.getProfile().getName());
                            i.putExtra(StringExtras.PROFILE_FUEL_TYPE, pg.getProfile().getFuelType());
                            i.putExtra(StringExtras.PROFILE_FUEL_AMOUNT, pg.getProfile().getFuelAmount());
                            i.putExtra("LOAD", 1);
                            startActivity(i);
                            Log.d("EDIT BTN CLICKEDS", "");

                        });


                        pg.getDeleteBtn().setOnClickListener(v -> {
                            pg.DeleteGroup();
                            Storage.deleteProfile(getContext(), StringExtras.getEmailValue(),
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