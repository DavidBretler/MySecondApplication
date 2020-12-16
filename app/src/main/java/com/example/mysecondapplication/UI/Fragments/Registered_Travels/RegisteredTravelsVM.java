package com.example.mysecondapplication.UI.Fragments.Registered_Travels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RegisteredTravelsVM extends ViewModel {

    private MutableLiveData<String> mText;

    public RegisteredTravelsVM() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}