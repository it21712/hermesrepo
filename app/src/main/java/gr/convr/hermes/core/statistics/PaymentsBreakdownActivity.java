package gr.convr.hermes.core.statistics;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import gr.convr.hermes.R;

public class PaymentsBreakdownActivity extends AppCompatActivity {


    private ImageView fuelBar1, fuelBar2, fuelBar3, fuelBar4;
    private ImageView retailBar1, retailBar2, retailBar3, retailBar4;

    private TextView fuelTxt1, fuelTxt2, fuelTxt3, fuelTxt4;
    private TextView retailTxt1, retailTxt2, retailTxt3, retailTxt4;

    private TextView monthly_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments_breakdown);


        fuelBar1 = findViewById(R.id.fuel_bar_1);
        fuelBar2 = findViewById(R.id.fuel_bar_2);
        fuelBar3 = findViewById(R.id.fuel_bar_3);
        fuelBar4 = findViewById(R.id.fuel_bar_4);

        retailBar1 = findViewById(R.id.retail_bar_1);
        retailBar2 = findViewById(R.id.retail_bar_2);
        retailBar3 = findViewById(R.id.retail_bar_3);
        retailBar4 = findViewById(R.id.retail_bar_4);

        fuelTxt1 = findViewById(R.id.f_tv_1);
        fuelTxt2 = findViewById(R.id.f_tv_2);
        fuelTxt3 = findViewById(R.id.f_tv_3);
        fuelTxt4 = findViewById(R.id.f_tv_4);

        retailTxt1 = findViewById(R.id.r_tv_1);
        retailTxt2 = findViewById(R.id.r_tv_2);
        retailTxt3 = findViewById(R.id.r_tv_3);
        retailTxt4 = findViewById(R.id.r_tv_4);

        monthly_txt = findViewById(R.id.expences_text);

        drawBars(fuelBar1, retailBar1, fuelTxt1, retailTxt1, 50f, 0);
        drawBars(fuelBar2, retailBar2, fuelTxt2, retailTxt2, 0, 157.34f);
        drawBars(fuelBar3, retailBar3, fuelTxt3, retailTxt3, 20f, 19.80f);
        drawBars(fuelBar4, retailBar4, fuelTxt4, retailTxt4, 40f, 73.20f);
        String total = 360.34f+"";
        monthly_txt.setText(total);

    }

    private void drawBars(ImageView fuelBar, ImageView retailBar, TextView fuelTxt, TextView retailTxt, float fuelAmount, float retailAmount){


        if(fuelAmount == 0){
            fuelBar.setVisibility(View.INVISIBLE);
            //fuelBar.setMaxHeight(0);
            fuelTxt.setVisibility(View.GONE);
        }else{
            fuelBar.getLayoutParams().height = (int)fuelAmount*2;

            String txt = fuelAmount+"";
            fuelTxt.setText(txt);
            fuelTxt.setVisibility(View.VISIBLE);
        }

        if(retailAmount == 0){
            retailBar.setMaxHeight(0);
            retailTxt.setVisibility(View.GONE);
        }else{
            retailBar.getLayoutParams().height = (int)retailAmount*2;
            String txt = retailAmount+"";
            retailTxt.setText(txt);
            retailTxt.setVisibility(View.VISIBLE);
        }


    }
}
