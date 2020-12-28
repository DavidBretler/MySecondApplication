package com.example.mysecondapplication.Data;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.mysecondapplication.Entities.Travel;

import java.util.List;

public class RMhistoryDataSource implements IRMhistoryDataSource {
    private TravelDao travelDao;

    public RMhistoryDataSource(Context context){
        RoomDataSource database= RoomDataSource.getInstance(context);
        travelDao =database.getTravelDao();
        travelDao.clear();
    }

    public LiveData<List<Travel>> getTravels(){
        return travelDao.getAll();
    }


    public LiveData<Travel> getTravel(String id){
        return travelDao.get(id);
    }

    public void addTravel(Travel p) {
        travelDao.insert(p);
    }

    public void addTravel(List<Travel> travelList) {
        travelDao.insert(travelList);
    }

    public void editTravel(Travel p) {
        travelDao.update(p);
    }

    public void deleteTravel(Travel p){
        travelDao.delete(p);
    }

    public void clearTable(){travelDao.clear();}


}
