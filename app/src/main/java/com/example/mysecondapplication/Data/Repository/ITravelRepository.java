package com.example.mysecondapplication.Data.Repository;

import androidx.lifecycle.MutableLiveData;


import com.example.mysecondapplication.Entities.Travel;

import java.util.List;

public interface ITravelRepository {

    void addTravel(Travel travel);
    void updateTravel(Travel travel);
    MutableLiveData<List<Travel>> getAllTravels();
    MutableLiveData<Boolean> getIsSuccess();
}
