package gr.convr.hermes.easyfuel.tests.ui.paymentmethod.cards;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CardsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CardsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
