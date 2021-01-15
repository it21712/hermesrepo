package gr.convr.hermes.core.resources;

import android.app.Activity;
import android.widget.Toast;

public class StringExtras {

    public static String PAYMENT_TOKEN_ID = "PAYMENT_TOKEN_ID";
    public static String CUSTOMER_ID = "CUSTOMER_ID";
    public static String PROCCESSOR_STRIPE = "stripe";
    public static String PROCCESSOR_BRAINTREE = "braintree";
    public static String COMPANY_VAT_ID = "COMPANY_VAT";
    public static int PAYMENT_METHOD_TYPE;
    public static String COMPANY_VAT;
    public static boolean IS_EMPLOYEE = false;
    public static String EMAIL_VALUE;

    public static String SECTOR_INTENT = "SECTOR";
    public static int FUEL_INTENT = 0;
    public static int STORES_INTENT = 1;
    public static  int LOCKERS_INTENT = 2;
    public static int CARDS_ACTIVITY_INTENT = 3;
    public static int PAYPAL_ACCOUNTS_ACTIVITY_INTENT = 4;

    public static final int CARD_METHOD = 0;
    public static final int PAYPAL_METHOD = 1;

    public static final String PROFILE_NAME = "prof_name";
    public static final String PROFILE_FUEL_TYPE = "prof_fuel_type";
    public static final String PROFILE_FUEL_AMOUNT = "prof_fuel_amount";


    public static final String PAYMENT_METHOD_OP = "pm";
    public static final String ADD_PAYMENT_METHOD = "add_pm";

    public static final String NEW_CUSTOMER = "new";

    public static String SELECTED_PAYMENT_TOKEN_ID;

    public static void setEmailValue(String value){
        EMAIL_VALUE = value;
    }

    public static void setIsEmployee(boolean b){IS_EMPLOYEE = b;}

    public static String getEmailValue(){return EMAIL_VALUE;}


    public static void showToast(Activity activity, String text, int length){

        activity.runOnUiThread(() -> {
            Toast.makeText(activity.getBaseContext(), text, length).show();
        });
    }
}

