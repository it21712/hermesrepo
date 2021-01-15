package gr.convr.hermes.storage;


import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import gr.convr.hermes.core.resources.StringExtras;


public class Storage {

    private static final String STRIPE_FILE = "stripe";
    private static final String BRAINTREE_FILE = "braintree";
    private static final String IDS_FILE = "ids.txt";
    public static final String CARD_DETAILS_FILE = "card-details.json";
    public static final String PAYPAL_DETAILS_FILE = "paypal-details.json";
    private static final String STRIPE_IDS_FILE_PATH = "/stripe/ids.txt";
    private static final String STRIPE_CARD_DETAILS_FILE_PATH = "/stripe/card-details.json";
    private static final String STRIPE_PATH = "/stripe/";
    private static final String BRAINTREE_PATH = "/braintree/";
    private static final String EASYFUEL_PATH = "/easyfuel/";
    private static final String EASYFUEL_PROFILES_PATH = "/easyfuel/profiles/";



    private static File openFile(Context context, String folder, String name){

        File f = new File(context.getFilesDir(), folder);
        if(!f.exists())
            f.mkdir();

        File file = new File(f.getPath()+"/"+name);
        if(!file.exists()) {
            try {
                file.createNewFile();
                File f2 = new File(f.getPath()+"/"+name);
                return f2;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static void writeToFile(File file, boolean append, String fileContents){

        BufferedWriter bw = null;
        try {

            bw = new BufferedWriter(new FileWriter(file, append));
            bw.write(fileContents);
            bw.newLine();
            bw.flush();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (bw != null) try {
                bw.close();
            } catch (IOException ioe2) {

            }
        }

    }



    private static BufferedReader setupReader(Context context, String pathName){

        File file = new File(context.getFilesDir()+pathName);

        FileInputStream inputStream;

        try {
            inputStream = new FileInputStream(file);

            StringBuffer fileContent = new StringBuffer();

            InputStreamReader isr = new InputStreamReader(inputStream);
            BufferedReader buffreader = new BufferedReader(isr);

            return buffreader;

        }catch(FileNotFoundException e) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
        return null;
    }


    public static void saveCardDetails2(Context context, String mail, String json){

        String userName = mail.replace(".","_");

        File userStripe = new File(context.getFilesDir(), STRIPE_FILE+"/"+userName);
        if(!userStripe.exists()) userStripe.mkdirs();

        File cardDets = new File(userStripe.getPath()+"/"+ CARD_DETAILS_FILE);
        if(!cardDets.exists()){
            try {
                cardDets.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        BufferedWriter bw = null;
        try {

            bw = new BufferedWriter(new FileWriter(cardDets, true));
            bw.write(json);
            bw.newLine();
            bw.flush();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (bw != null) try {
                bw.close();
            } catch (IOException ioe2) {

            }
        }
    }
    public static String readCardDetails(Context context, String mail){

        String m = mail.replace(".", "_");

        File userCardDets = new File(context.getFilesDir()+STRIPE_PATH+m+"/"+ CARD_DETAILS_FILE);

        FileInputStream inputStream;

        try {
            inputStream = new FileInputStream(userCardDets);

            StringBuffer fileContent = new StringBuffer();

            InputStreamReader isr = new InputStreamReader (inputStream);
            BufferedReader buffreader = new BufferedReader (isr);

            String readString;
            String result="";

            try{
                while ((readString = buffreader.readLine()) != null){
                    result+=readString;
                }
                return result;

            }catch (IOException ioe){
                ioe.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;

    }

    public static void saveCardDetails(Context context, boolean isEmployee, String mail, String json){

        String userName = mail.replace(".","_");

        String prov;

        if(isEmployee)
            prov = STRIPE_FILE;
        else prov = BRAINTREE_FILE;

        File userFile = new File(context.getFilesDir(), prov+"/"+userName);
        if(!userFile.exists()) userFile.mkdirs();

        File cardDets = new File(userFile.getPath()+"/"+ CARD_DETAILS_FILE);
        if(!cardDets.exists()){
            try {
                cardDets.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        BufferedWriter bw = null;
        try {

            bw = new BufferedWriter(new FileWriter(cardDets, true));
            bw.write(json);
            bw.newLine();
            bw.flush();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (bw != null) try {
                bw.close();
            } catch (IOException ioe2) {

            }
        }
    }
    public static String readCardDetails(Context context, boolean isEmployee, String mail){

        String prov;
        String m = mail.replace(".", "_");
        if(isEmployee)
            prov = STRIPE_PATH;
        else
            prov = BRAINTREE_PATH;
        File userFile = new File(context.getFilesDir()+prov+m+"/"+CARD_DETAILS_FILE);

        FileInputStream inputStream;

        try {
            inputStream = new FileInputStream(userFile);

            StringBuffer fileContent = new StringBuffer();

            InputStreamReader isr = new InputStreamReader (inputStream);
            BufferedReader buffreader = new BufferedReader (isr);

            String readString;
            String result="";

            try{
                while ((readString = buffreader.readLine()) != null){
                    result+=readString;
                }
                return result;

            }catch (IOException ioe){
                ioe.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
    public static ArrayList<String> readPaymentMethods(Context context, boolean isEmployee, String mail, String paymentMethodType){

        String prov;
        String m = mail.replace(".", "_");
        if(isEmployee)
            prov = STRIPE_PATH;
        else
            prov = BRAINTREE_PATH;
        File userFile = new File(context.getFilesDir()+prov+m+"/"+paymentMethodType);

        FileInputStream inputStream;

        try {
            inputStream = new FileInputStream(userFile);

            StringBuffer fileContent = new StringBuffer();

            InputStreamReader isr = new InputStreamReader (inputStream);
            BufferedReader buffreader = new BufferedReader (isr);

            String readString;
            String result="";
            ArrayList<String> pms = new ArrayList<>();

            try{
                while ((readString = buffreader.readLine()) != null){
                    result+=readString;
                    pms.add(readString);
                }
                return pms;

            }catch (IOException ioe){
                ioe.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
    public static int deleteCard(Context context, boolean isEmployee, String mail, String id){

        String prov;
        String m = mail.replace(".", "_");
        if(isEmployee)
            prov = STRIPE_PATH;
        else
            prov = BRAINTREE_PATH;

        File file = new File(context.getFilesDir()+prov+m+"/"+CARD_DETAILS_FILE);
        File tempFile = new File(context.getFilesDir(),"tmp.txt");
        if(!tempFile.exists()){
            try {
                tempFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return 1;
        }
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(tempFile, true));
        } catch (IOException e) {
            e.printStackTrace();
            return 1;
        }

        String line = null;

        try{
            while ((line = reader.readLine()) != null){
                if(!line.contains(id)){
                    writer.write(line);
                    writer.write("\n");
                    writer.flush();
                }

            }
            reader.close();
            writer.close();

            if (!file.delete()) {
                System.out.println("Could not delete file");
                return 1;
            }


            if (!tempFile.renameTo(file)){
                System.out.println("Could not rename file");
                return 1;
            }

            return 0;




        }catch (IOException ioe){
            ioe.printStackTrace();
            return 1;
        }

    }

    public static void savePaypalDetails(Context context, String mail, String json){

        String userName = mail.replace(".","_");

        File userBraintree = new File(context.getFilesDir(), BRAINTREE_FILE+"/"+userName);
        if(!userBraintree.exists()) userBraintree.mkdirs();

        File paypalDets = new File(userBraintree.getPath()+"/"+ PAYPAL_DETAILS_FILE);
        if(!paypalDets.exists()){
            try {
                paypalDets.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        BufferedWriter bw = null;
        try {

            bw = new BufferedWriter(new FileWriter(paypalDets, true));
            bw.write(json);
            bw.newLine();
            bw.flush();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (bw != null) try {
                bw.close();
            } catch (IOException ioe2) {

            }
        }
    }
    public static String readPaypalDetails(Context context, String mail){

        String m = mail.replace(".", "_");

        File paypalDets = new File(context.getFilesDir()+BRAINTREE_PATH+m+"/"+ PAYPAL_DETAILS_FILE);

        FileInputStream inputStream;

        try {
            inputStream = new FileInputStream(paypalDets);

            StringBuffer fileContent = new StringBuffer();

            InputStreamReader isr = new InputStreamReader (inputStream);
            BufferedReader buffreader = new BufferedReader (isr);

            String readString;
            String result="";

            try{
                while ((readString = buffreader.readLine()) != null){
                    result+=readString;
                }
                return result;

            }catch (IOException ioe){
                ioe.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
    public static ArrayList<String> readPaypalAccounts(Context context, String mail){ //String

        String m = mail.replace(".", "_");

        File paypalDets = new File(context.getFilesDir()+BRAINTREE_PATH+m+"/"+ PAYPAL_DETAILS_FILE);

        FileInputStream inputStream;

        try {
            inputStream = new FileInputStream(paypalDets);

            StringBuffer fileContent = new StringBuffer();

            InputStreamReader isr = new InputStreamReader (inputStream);
            BufferedReader buffreader = new BufferedReader (isr);

            String readString;
            String result="";
            ArrayList<String> paypalAccs = new ArrayList<>();

            try{
                while ((readString = buffreader.readLine()) != null){
                    result+=readString;
                    paypalAccs.add(readString);
                }
                return paypalAccs;//result

            }catch (IOException ioe){
                ioe.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
    public static int deletePaypalDetails(Context context, String mail, String id){

        String m = mail.replace(".", "_");

        File file = new File(context.getFilesDir()+BRAINTREE_PATH+m+"/"+PAYPAL_DETAILS_FILE);
        File tempFile = new File(context.getFilesDir(),"tmp.txt");
        if(!tempFile.exists()){
            try {
                tempFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return 1;
        }
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(tempFile, true));
        } catch (IOException e) {
            e.printStackTrace();
            return 1;
        }

        String line = null;

        try{
            while ((line = reader.readLine()) != null){
                if(!line.contains(id)){
                    writer.write(line);
                    writer.write("\n");
                    writer.flush();
                }

            }
            reader.close();
            writer.close();

            if (!file.delete()) {
                System.out.println("Could not delete file");
                return 1;
            }


            if (!tempFile.renameTo(file)){
                System.out.println("Could not rename file");
                return 1;
            }

            return 0;




        }catch (IOException ioe){
            ioe.printStackTrace();
            return 1;
        }
    }

    public static void saveStripeIds2(Context context, String mail, String idName, String id ){

        String m = mail.replace(".", "_");

        File f = new File(context.getFilesDir(), STRIPE_FILE+"/"+m);
        if(!f.exists())
            f.mkdir();

        File file = new File(f.getPath()+"/"+ IDS_FILE);
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String fileContents = idName+"#"+id;


        BufferedWriter bw = null;
        try {

            bw = new BufferedWriter(new FileWriter(file, true));
            bw.write(fileContents);
            bw.newLine();
            bw.flush();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (bw != null) try {
                bw.close();
            } catch (IOException ioe2) {

            }
        }


    }
    public static String getStripeId2(Context context, String mail, String idName){

        String m = mail.replace(".", "_");

        File file = new File(context.getFilesDir()+STRIPE_PATH+m+"/"+ IDS_FILE);

        FileInputStream inputStream;

        try {
            inputStream = new FileInputStream(file);

            StringBuffer fileContent = new StringBuffer();

            InputStreamReader isr = new InputStreamReader (inputStream);
            BufferedReader buffreader = new BufferedReader (isr);

            String readString;


            try {
                while((readString = buffreader.readLine()) != null){
                    String[] arr = readString.split("#", 2);
                    if(arr[0].equals(idName))
                        return arr[1];
                }


            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static void saveProcessingInfo(Context context, String processorName, String mail, String idName, String id){
        String m = mail.replace(".", "_");

        File f = new File(context.getFilesDir(), processorName+"/"+m);
        if(!f.exists())
            f.mkdir();

        File file = new File(f.getPath()+"/"+ IDS_FILE);
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String fileContents = idName+"#"+id;


        BufferedWriter bw = null;
        try {

            bw = new BufferedWriter(new FileWriter(file, true));
            bw.write(fileContents);
            bw.newLine();
            bw.flush();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (bw != null) try {
                bw.close();
            } catch (IOException ioe2) {

            }
        }
    }
    public static String getProcessingInfo(Context context, boolean isEmployee, String mail, String idName){

        String processorName;
        if(isEmployee) processorName= StringExtras.PROCCESSOR_STRIPE;
        else processorName = StringExtras.PROCCESSOR_BRAINTREE;

        String m = mail.replace(".", "_");

        File file = new File(context.getFilesDir()+"/"+processorName+"/"+m+"/"+ IDS_FILE);

        FileInputStream inputStream;

        try {
            inputStream = new FileInputStream(file);

            StringBuffer fileContent = new StringBuffer();

            InputStreamReader isr = new InputStreamReader (inputStream);
            BufferedReader buffreader = new BufferedReader (isr);

            String readString;


            try {
                while((readString = buffreader.readLine()) != null){
                    String[] arr = readString.split("#", 2);
                    if(arr[0].equals(idName))
                        return arr[1];
                }


            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static int replaceProcessingInfo(Context context, boolean isEmployee, String mail, String idName, String value){

        String processorName;
        if(isEmployee) processorName= StringExtras.PROCCESSOR_STRIPE;
        else processorName = StringExtras.PROCCESSOR_BRAINTREE;

        String m = mail.replace(".", "_");

        File file = new File(context.getFilesDir()+"/"+processorName+"/"+m+"/"+ IDS_FILE);
        File tempFile = new File(context.getFilesDir(),"tmp.txt");
        if(!tempFile.exists()){
            try {
                tempFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return 1;
        }
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(tempFile, true));
        } catch (IOException e) {
            e.printStackTrace();
            return 1;
        }

        String line = null;

        try{
            while ((line = reader.readLine()) != null){
                if(!line.startsWith(idName)){
                    writer.write(line);
                    writer.write("\n");
                    writer.flush();
                }

            }
            reader.close();

            writer.write(idName+"#"+value);
            writer.close();

            if (!file.delete()) {
                System.out.println("Could not delete file");
                return 1;
            }


            if (!tempFile.renameTo(file)){
                System.out.println("Could not rename file");
                return 1;
            }

            return 0;




        }catch (IOException ioe){
            ioe.printStackTrace();
            return 1;
        }
    }

    public static int getPaymentMethod(Context context, boolean isEmployee, String mail){

        String m = mail.replace(".", "_");

        String prov = BRAINTREE_PATH;
        if(isEmployee) prov=STRIPE_PATH;

        File userFile = new File(context.getFilesDir()+prov+m+"/");

        File[] fs = userFile.listFiles();
        for(File f : fs){
            if(f.getName().equals(CARD_DETAILS_FILE)) return StringExtras.CARD_METHOD;
            else if(f.getName().equals(PAYPAL_DETAILS_FILE)) return StringExtras.PAYPAL_METHOD;
        }


        return -1;
    }

    //REMOVE TEST MAIL AFTER IMPLEMENTATION
    public static void saveProfile(Context context, String mail, String name, String fuelType, String fuelAmount){


        String m = mail.replace(".", "_");
        String nameExt = name+".txt";

        File userProfs = new File(context.getFilesDir()+EASYFUEL_PROFILES_PATH+m);
        if(!userProfs.exists()) userProfs.mkdirs();

        File profFile = new File(userProfs.getPath()+"/"+name);
        if(!profFile.exists()){

            try {
                profFile.createNewFile();
                String fileContents = fuelType+"@"+fuelAmount;
                FileOutputStream outputStream;
                try {
                    outputStream = new FileOutputStream(profFile.getPath());
                    //outputStream = context.openFileOutput(name, context.getApplicationContext().MODE_PRIVATE);
                    outputStream.write(fileContents.getBytes());
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
    public static void deleteProfile(Context context, String mail, String name){

        String m = mail.replace(".", "_");

        File file = new File(context.getFilesDir()+EASYFUEL_PROFILES_PATH+m+"/"+name);

        if(file.exists()){
            boolean deleted = file.delete();
            if(deleted)
                Log.d("FILE_DELETE", "FILE DELETED");
            else Log.d("FILE_DELETE", "FILE NOT DELETED");
        }

    }

    public static int profileCount(Context context, String mail){
        String m = mail.replace(".", "_");

        File userProfs = new File(context.getFilesDir()+EASYFUEL_PROFILES_PATH+m);
        if(!userProfs.exists()) return -1;

        return userProfs.listFiles().length;
    }
    public static File[] listProfiles(Context context, String mail){

        String m = mail.replace(".", "_");

        File userProfs = new File(context.getFilesDir()+EASYFUEL_PROFILES_PATH+m);
        if(!userProfs.exists()) return null;

        return userProfs.listFiles();

    }
    public static String[] readProfile(Context context, String mail, String name){

        String m = mail.replace(".", "_");

        File f = new File(context.getFilesDir()+EASYFUEL_PROFILES_PATH+m+"/"+name);
        if(!f.exists()) return null;

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(f.getPath());
            Log.d("AAAA", "INPUT STREAM FOUND");
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
        }

        return line.split("@");

    }


    public static void createUserDirs(Context c, String username){

        File easyFuelFile = new File(c.getFilesDir().getPath()+EASYFUEL_PATH);
        if(!easyFuelFile.exists()) easyFuelFile.mkdir();

        File easyFuelProfilesFile = new File(c.getFilesDir().getPath()+EASYFUEL_PROFILES_PATH+username);
        if(!easyFuelProfilesFile.exists()) easyFuelProfilesFile.mkdirs();

        File stripeFile = new File(c.getFilesDir().getPath()+STRIPE_PATH+username);
        if(!stripeFile.exists()) stripeFile.mkdirs();

        File braintreeFile = new File(c.getFilesDir().getPath()+BRAINTREE_PATH+username);
        if(!braintreeFile.exists()) braintreeFile.mkdirs();



    }





    public static void saveCardDetails(Context context, String json){

        File f = new File(context.getFilesDir(), STRIPE_FILE);
        if(!f.exists())
            f.mkdir();

        File file = new File(f.getPath()+"/"+ CARD_DETAILS_FILE);
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        BufferedWriter bw = null;
        try {

            bw = new BufferedWriter(new FileWriter(file, true));
            bw.write(json);
            bw.newLine();
            bw.flush();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (bw != null) try {
                bw.close();
            } catch (IOException ioe2) {

            }
        }



    }

    public static String readCardDetails(Context context){

        File file = new File(context.getFilesDir()+STRIPE_CARD_DETAILS_FILE_PATH);

        FileInputStream inputStream;

        try {
            inputStream = new FileInputStream(file);

            StringBuffer fileContent = new StringBuffer();

            InputStreamReader isr = new InputStreamReader (inputStream);
            BufferedReader buffreader = new BufferedReader (isr);

            String readString;
            String result="";

            try{
                while ((readString = buffreader.readLine()) != null){
                    result+=readString;
                }
                return result;

            }catch (IOException ioe){
                ioe.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;

    }


    public static void saveStripeIds(Context context, String idName, String id ){

        File f = new File(context.getFilesDir(), STRIPE_FILE);
        if(!f.exists())
            f.mkdir();

        File file = new File(f.getPath()+"/"+ IDS_FILE);
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String fileContents = idName+"#"+id;


        BufferedWriter bw = null;
        try {

            bw = new BufferedWriter(new FileWriter(file, true));
            bw.write(fileContents);
            bw.newLine();
            bw.flush();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (bw != null) try {
                bw.close();
            } catch (IOException ioe2) {

            }
        }


    }


    public static String getStripeId(Context context, String idName){

        File file = new File(context.getFilesDir()+STRIPE_IDS_FILE_PATH);

        FileInputStream inputStream;

        try {
            inputStream = new FileInputStream(file);

            StringBuffer fileContent = new StringBuffer();

            InputStreamReader isr = new InputStreamReader (inputStream);
            BufferedReader buffreader = new BufferedReader (isr);

            String readString;


            try {
                while((readString = buffreader.readLine()) != null){
                    String[] arr = readString.split("#", 2);
                    if(arr[0].equals(idName))
                        return arr[1];
                }


            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}
