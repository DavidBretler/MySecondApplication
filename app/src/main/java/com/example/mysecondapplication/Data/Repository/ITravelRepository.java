package com.example.mysecondapplication.Data.Repository;

import androidx.lifecycle.MutableLiveData;


import com.example.mysecondapplication.Entities.Travel;

import java.util.List;

/**
 * interface for travel repository to enable us to change data source
 * design by contract
 */
public interface ITravelRepository {

    void addTravel(Travel travel);
    void updateTravel(Travel travel);
    MutableLiveData<List<Travel>> getAllTravels();

     MutableLiveData<List<Travel>> getUserTravels();

    MutableLiveData<List<Travel>> getOpenTravels(double lat,double lon,int maxDis);

    MutableLiveData<List<Travel>> getHistoryTravels();

    MutableLiveData<Boolean> getIsSuccess();
     String getUserEmail();
}
