package gr.convr.hermes.core.resources;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class ProfileStyle {

    private Button btn;
    private boolean isSelected = false;

    private Profile prof;

    private static ArrayList<ProfileStyle> profs = new ArrayList<>();


    public Button getButton(){
        return btn;
    }

    public Profile getProfile(){
        return prof;
    }


    public void setSelected(boolean selected){
        isSelected = selected;
    }

    public void setProfile(Profile prof){
        this.prof = prof;
    }

    public ProfileStyle(Context context){
        prof = null;
        btn = new Button(context);
        btn.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0; i < profs.size(); i++){
                    profs.get(i).setSelected(false);
                    profs.get(i).getButton().getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
                }


                btn.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
            }
        });

        profs.add(this);
    }

    public ProfileStyle(Context context, String profName){
        this(context);
        btn.setText(profName);

    }
}