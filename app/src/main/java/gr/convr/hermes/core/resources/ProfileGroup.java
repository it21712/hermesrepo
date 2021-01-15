package gr.convr.hermes.core.resources;


import android.content.Context;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;

import gr.convr.hermes.R;

public class ProfileGroup {

    private Button nameBtn;
    private ImageButton editBtn;
    private ImageButton deleteBtn;

    private boolean isSelected = false;

    private Profile prof;

    public static ArrayList<ProfileGroup> profs = new ArrayList<>();


    public Button getNameBtn(){
        return nameBtn;
    }
    public ImageButton getEditBtn(){
        return editBtn;
    }
    public ImageButton getDeleteBtn(){
        return deleteBtn;
    }


    public Profile getProfile(){
        return prof;
    }


    public void setSelected(boolean selected){
        isSelected = selected;
    }

    public void setProfile(Profile prof){
        this.prof = prof;
        nameBtn.setTag(prof.getName());
        editBtn.setTag(prof.getName());
        deleteBtn.setTag(prof.getName());

    }


    public ProfileGroup(final Context context){
        prof = null;
        nameBtn = new Button(context);
        editBtn = new ImageButton(context);
        editBtn.setImageResource(R.drawable.baseline_create_black_24);
        deleteBtn = new ImageButton(context);
        deleteBtn.setImageResource(R.drawable.baseline_delete_black_24);


        /*nameBtn.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
        editBtn.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
        deleteBtn.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
        nameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0; i < profs.size(); i++){
                    profs.get(i).setSelected(false);
                    profs.get(i).getNameBtn().getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
                    profs.get(i).getEditBtn().getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
                    profs.get(i).getDeleteBtn().getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
                }

                isSelected = true;
                nameBtn.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                editBtn.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                deleteBtn.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);

            }
        });*/


        profs.add(this);
    }

    public ProfileGroup(Context context, String profName){
        this(context);
        nameBtn.setText(profName);

    }

    public void DeleteGroup(){

        profs.remove(this);

    }



}
