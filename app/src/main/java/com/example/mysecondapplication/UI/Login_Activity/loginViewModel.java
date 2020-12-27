package com.example.mysecondapplication.UI.Login_Activity;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.mysecondapplication.Data.Repository.ITravelRepository;
import com.example.mysecondapplication.Data.Repository.TravelRepository;
import com.example.mysecondapplication.Entities.Travel;

import java.util.List;

public class loginViewModel extends AndroidViewModel {
    ITravelRepository repository;
    public loginViewModel(Application p) {
        super(p);
        repository =  TravelRepository.getInstance(p);//??
    }
    void addTravel(Travel travel)
    {
        repository.addTravel(travel);
    }
    void updateTravel(Travel travel)
    {
        repository.updateTravel(travel);
    }
    MutableLiveData<List<Travel>> getAllTravels()
    {
        return (MutableLiveData<List<Travel>>)repository.getAllTravels();
    }
    MutableLiveData<Boolean> getIsSuccess()
    {
        return repository.getIsSuccess();
    }
}
