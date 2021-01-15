package gr.convr.hermes.core.resources;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

import gr.convr.hermes.storage.Storage;

public class Profile {

    private  String name;
    private String fuelType;
    private String fuelAmount;


    public Profile(){}


    public Profile(String name, String fuelType, String fuelAmount){
        this.name = name;
        this.fuelType = fuelType;
        this.fuelAmount = fuelAmount;
    }


    public String getName() {
        return name;
    }

    public String getFuelType() {
        return fuelType;
    }

    public String getFuelAmount() {
        return fuelAmount;
    }

    public static Profile GetProfile(String name, Context context, String mail){


        String[] s = Storage.readProfile(context, mail, name);

        /*FileInputStream fis = null;
        try {
            fis = context.openFileInput(name);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader bufferedReader = new BufferedReader(isr);

        String line = "";
        try {
            line = bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }*/


        //String[] s = line.split("@");
        Log.d("SPLIT STRING", s[1]);
        Profile profile = new Profile(name, s[0], s[1]);
        return profile;
    }



    public void save(Context context){

        File file = new File(context.getFilesDir(), name+".txt");

        String fileContents = fuelType+"@"+fuelAmount;

        FileOutputStream outputStream;
        try {
            outputStream = context.openFileOutput(name, context.getApplicationContext().MODE_PRIVATE);
            outputStream.write(fileContents.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
