package ca.sfu.cmpt276.sudokulang.ui.game;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GameViewModel extends ViewModel {
    // TODO: Implement the ViewModel. Below is only sample code.

    private final MutableLiveData<String> mText;

    public GameViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is game fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}