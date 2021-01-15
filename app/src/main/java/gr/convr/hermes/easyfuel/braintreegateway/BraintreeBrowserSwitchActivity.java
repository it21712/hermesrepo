package gr.convr.hermes.easyfuel.braintreegateway;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.braintreepayments.api.BraintreeFragment;
import com.braintreepayments.api.exceptions.InvalidArgumentException;

import gr.convr.hermes.R;

public class BraintreeBrowserSwitchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_braintree_browser_switch);

        try {
            BraintreeFragment mBraintreeFragment = BraintreeFragment.newInstance(this, "");
        }catch (InvalidArgumentException e){

        }
    }
}
