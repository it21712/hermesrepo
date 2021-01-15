package gr.convr.hermes.easyfuel.enterprice.eu.vatchecker;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import gr.convr.hermes.R;
import gr.convr.hermes.easyfuel.server.StripeServerAPIS;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class TestActivity extends AppCompatActivity {

    private static Gson gson = new Gson();

    private int minVat = 10000000;
    private int maxVat = 49999999;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        File output = new File(getFilesDir(), "vat-nums.txt");
        if(!output.exists()) {
            try {
                output.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        advanceVatNumber(output, minVat);

    }

    private void advanceVatNumber(File file, int vatNumber){

        if(vatNumber <= maxVat){

            try {

                BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));

                String vat = "0"+vatNumber;
                String req = "{countryCode: "+"EL"+", vatNumber: "+vat+"}";
                StripeServerAPIS.sendRequest2(StripeServerAPIS.VAT_CHECK_URL, req, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.d("Vat request", e.getMessage());
                            advanceVatNumber(file, vatNumber);
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if(response.isSuccessful()){
                                EUVatCheckResponse vatCheckResponse = gson.fromJson(response.body().string(), EUVatCheckResponse.class);

                                if(vatCheckResponse.isValid){
                                    String companyInfo = "----------------\nVAT number: " + vat +"\ncompany name: "+vatCheckResponse.getName()
                                            +"\ncompany address: "+vatCheckResponse.getAddress()+"\n----------------\n";
                                    writer.write(companyInfo);
                                    writer.newLine();
                                    writer.flush();
                                }else{
                                    Log.d("company validity", "error(not valid)");
                                }
                                int next = vatNumber+1;
                                advanceVatNumber(file, next);

                            }else{
                                Log.d("vat check", response.message());
                                advanceVatNumber(file, vatNumber);
                            }
                        }
                    });

            } catch (IOException e) {
                e.printStackTrace();
            }

        }



    }

    private class EUVatCheckResponse {

        private final boolean isValid;
        private final String name;
        private final String address;


        EUVatCheckResponse(boolean isValid, String name, String address) {
            this.isValid = isValid;
            this.name = name;
            this.address = address;
        }

        public boolean isValid() {
            return isValid;
        }

        public String getName() {
            return name;
        }

        public String getAddress() {
            return address;
        }
    }


}
