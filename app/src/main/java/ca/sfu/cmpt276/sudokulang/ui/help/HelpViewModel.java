package ca.sfu.cmpt276.sudokulang.ui.help;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


// See: https://developer.android.com/topic/libraries/architecture/viewmodel
public class HelpViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public HelpViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is help fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}