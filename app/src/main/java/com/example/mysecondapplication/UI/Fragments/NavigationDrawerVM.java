package com.example.mysecondapplication.UI.Fragments;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.example.mysecondapplication.Data.Repository.ITravelRepository;
import com.example.mysecondapplication.Data.Repository.TravelRepository;
import com.example.mysecondapplication.Entities.Travel;

import java.util.List;

public class NavigationDrawerVM extends AndroidViewModel {

    private MutableLiveData<String> mText;
    ITravelRepository repository;
    public NavigationDrawerVM(Application p) {

        super(p);
        mText = new MutableLiveData<>();
//        if (NavigationDrawerVM.super.getClass() instanceof  RegisteredTravels.class)
//        mText.setValue("This is RegisteredTravels fragment");
//        else
//            mText.setValue("This is not  RegisteredTravels fragment");

        repository = TravelRepository.getInstance(p);
    }

    public LiveData<String> getText() {
        return mText;
    }
    public void addTravel(Travel travel)
    {
        repository.addTravel(travel);
    }
    public void updateTravel(Travel travel)
    {
        repository.updateTravel(travel);
    }

    public MutableLiveData<List<Travel>> getAllTravels()
    {
        return (MutableLiveData<List<Travel>>)repository.getAllTravels();
    }
    public MutableLiveData<Boolean> getIsSuccess()
    {
        return repository.getIsSuccess();
    }
}
