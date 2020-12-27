package com.example.mysecondapplication.Data;

import androidx.lifecycle.MutableLiveData;

import com.example.mysecondapplication.Entities.Travel;

import java.util.List;

public interface IFBtravelDataSource {
    void addTravel(Travel travel);
    void updateTravel(Travel travel);
    List<Travel> getAllTravels();
    MutableLiveData<Boolean> getIsSuccess();

    interface NotifyToTravelListListener {
        void onTravelsChanged();
    }
    void setNotifyToTravelListListener(NotifyToTravelListListener l);
}