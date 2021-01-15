package gr.convr.hermes.easyfuel;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;

import gr.convr.hermes.R;
import gr.convr.hermes.easyfuel.server.BraintreeServerAPIS;

public class TransactionStatusMessageActivity extends AppCompatActivity {

    private TextView transactionStatusTextView, dateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_status_message);

        transactionStatusTextView = findViewById(R.id.transtat_txt);
        dateTextView = findViewById(R.id.date_txt);
        String status = getIntent().getStringExtra("status");

        String info = getIntent().getStringExtra("date");


        if(status.equals("_SUCCESS_")){
            transactionStatusTextView.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.green));
            transactionStatusTextView.setText("Transaction Completed");

            dateTextView.setText("05/12/2020 18:23:17");

        }else if(status.equals("")){
            transactionStatusTextView.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.darkRed));
            transactionStatusTextView.setText("Transaction Failed");
            dateTextView.setText("Insufficient balance");
            BraintreeServerAPIS.TransactionInfo tInfo = new Gson().fromJson(info, BraintreeServerAPIS.TransactionInfo.class);


           /* Date date = tInfo.getCreatedAt().getTime();
            dateTextView.setText(date.toString());*/
           /* SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String dateInString =""+tInfo.getCreatedAt().get(Calendar.DAY_OF_MONTH)+"-"+
                    tInfo.getCreatedAt().get(Calendar.MONTH)+"-"+tInfo.getCreatedAt().get(Calendar.YEAR)+
                    " "+tInfo.getCreatedAt().get(Calendar.HOUR_OF_DAY)+":"+tInfo.getCreatedAt().get(Calendar.MINUTE)+
                    ":"+tInfo.getCreatedAt().get(Calendar.SECOND);
            try {
                Date date = sdf.parse(dateInString);
                dateTextView.setText(date.toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }*/
        }
    }
}
