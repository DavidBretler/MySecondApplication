package com.example.mysecondapplication.UI.Fragments.History_Travels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HistoryTravelsVM extends ViewModel {

    private MutableLiveData<String> mText;
    public HistoryTravelsVM() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}