package com.example.mysecondapplication.Data;

import androidx.lifecycle.LiveData;

import com.example.mysecondapplication.Entities.Travel;

import java.util.List;
/**
 * interface for repository to enable us to change data source
 * design by contract
 */
public interface IRMhistoryDataSource {
    public void addTravel(Travel p);

    public void addTravel(List<Travel> travelList);

    public void editTravel(Travel p);

    public void deleteTravel(Travel p);

    public void clearTable();
    public LiveData <List<Travel>> getTravels();
}
