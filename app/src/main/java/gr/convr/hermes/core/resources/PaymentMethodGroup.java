package gr.convr.hermes.core.resources;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import gr.convr.hermes.R;


public class PaymentMethodGroup {

    private CheckBox selectorBtn;
    private ImageView deleteBtn;
    private ImageView pmIcon;
    private TextView pmDetails;

    public static final String VISA_NAME = "Visa";
    public static final String MASTERCARD_NAME = "MasterCard";
    public static final String DISCOVERCARD_NAME = "Discover";
    public static final String PAYPAL_NAME = "PayPal";

    private int selectorId;

    private float pmIconScale = 0.7f;
    private float detailsTextSize = 16;

    public PaymentMethodGroup(Context context, int selectorId, String iconName, String details){

        selectorBtn = new CheckBox(context);
        selectorBtn.setId(selectorId);
        selectorBtn.setButtonDrawable(R.drawable.payment_methods_button_background);

        pmIcon = new ImageView(context);
        pmIcon.setScaleX(pmIconScale);
        pmIcon.setScaleY(pmIconScale);
        switch (iconName) {
            case VISA_NAME:
                pmIcon.setImageResource(R.drawable.bt_ic_vaulted_visa);
                break;
            case MASTERCARD_NAME:
                pmIcon.setImageResource(R.drawable.bt_ic_vaulted_mastercard);
                break;
            case PAYPAL_NAME:
                pmIcon.setImageResource(R.drawable.bt_ic_vaulted_paypal);
                break;
            case DISCOVERCARD_NAME:
                pmIcon.setImageResource(R.drawable.bt_ic_vaulted_discover);
        }

        pmDetails = new TextView(context);
        pmDetails.setText(details);
        pmDetails.setTextColor(context.getColor(R.color.colorBlack));
        pmDetails.setTextSize(detailsTextSize);



        deleteBtn = new ImageView(context);
        deleteBtn.setImageResource(R.drawable.baseline_delete_black_24);



    }


    public CheckBox getSelectorBtn() {
        return selectorBtn;
    }

    public void setSelectorBtn(CheckBox selectorBtn) {
        this.selectorBtn = selectorBtn;
    }

    public ImageView getDeleteBtn() {
        return deleteBtn;
    }

    public void setDeleteBtn(ImageView deleteBtn) {
        this.deleteBtn = deleteBtn;
    }

    public ImageView getPmIcon() {
        return pmIcon;
    }

    public void setPmIcon(ImageView pmIcon) {
        this.pmIcon = pmIcon;
    }

    public TextView getPmDetails() {
        return pmDetails;
    }

    public void setPmDetails(TextView pmDetails) {
        this.pmDetails = pmDetails;
    }

    public int getSelectorId() {
        return selectorId;
    }

    public void setSelectorId(int selectorId) {
        this.selectorId = selectorId;
    }



}
