package com.example.mysecondapplication.UI.Fragments.Company_Travels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CompanyTravelsVM extends ViewModel {

    private MutableLiveData<String> mText;

    public CompanyTravelsVM() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}