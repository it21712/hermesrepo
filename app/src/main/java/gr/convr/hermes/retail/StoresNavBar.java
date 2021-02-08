package gr.convr.hermes.retail;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Menu;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.IOException;

import gr.convr.hermes.R;
import gr.convr.hermes.core.MainMenu;
import gr.convr.hermes.easyfuel.MainActivity;
import gr.convr.hermes.easyfuel.server.BraintreeServerAPIS;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class StoresNavBar extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stores_nav_bar);
        Toolbar toolbar = findViewById(R.id.stores_toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.stores_drawer_layout);
        NavigationView navigationView = findViewById(R.id.stores_nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_cart, R.id.nav_cards, R.id.nav_paypal)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.stores_nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.stores_nav_bar, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.stores_nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed(){
        Intent i = new Intent(StoresNavBar.this, MainMenu.class);

        BraintreeServerAPIS.sendRequest2(BraintreeServerAPIS.CART, "{\"string\": \"CLOSE_SOCKET\"}", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("CART ROUTE ERROR", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    Log.d("CART RESPONSE", response.body().string());
                }else{
                    Log.d("CART RESPONSE", response.message());
                }
            }
        });

        startActivity(i);
        finish();

    }
}
