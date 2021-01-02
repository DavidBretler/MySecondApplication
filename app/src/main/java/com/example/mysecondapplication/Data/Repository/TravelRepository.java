package com.example.mysecondapplication.Data.Repository;

import android.app.Application;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import com.example.mysecondapplication.Data.RMhistoryDataSource;
import com.example.mysecondapplication.Data.IRMhistoryDataSource;
import com.example.mysecondapplication.Data.IFBtravelDataSource;
import com.example.mysecondapplication.Data.FBtravelDataSource;
import com.example.mysecondapplication.Entities.Travel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TravelRepository implements ITravelRepository  {
    IFBtravelDataSource iFBtravelDataSource;
    private IRMhistoryDataSource iRMhistoryDataSource;
    private MutableLiveData<List<Travel>> openTravels = new MutableLiveData<>();
    private MutableLiveData<List<Travel>> UserTravels = new MutableLiveData<>();
    private MutableLiveData<List<Travel>> HistoryTravels = new  MutableLiveData<>();
    private  List<Travel> travelList;
    private  List<Travel> userTravelList;
    public FirebaseAuth mAuth;
    public FirebaseUser user;
    private Application application;
    private MutableLiveData<List<Travel>> AllTravels = new MutableLiveData<>();

    private static TravelRepository instance;
    public static TravelRepository getInstance(Application application) {
        if (instance == null) {
            instance = new TravelRepository(application);
        }
        return instance;
    }

    private TravelRepository(Application application) {
        iFBtravelDataSource = FBtravelDataSource.getInstance();
        iRMhistoryDataSource = new RMhistoryDataSource(application.getApplicationContext());
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
      this.application=application;
        IFBtravelDataSource.NotifyToTravelListListener notifyToTravelListListener = new IFBtravelDataSource.NotifyToTravelListListener() {
            @Override
            public void onTravelsChanged() {
                travelList = iFBtravelDataSource.getAllTravels();
                AllTravels.setValue(travelList);

                findUserTravelList();
                findOpenTravelList();
                findHistoryTravelList();

                iRMhistoryDataSource.clearTable();
                iRMhistoryDataSource.addTravel(travelList);
            }
        };

        iFBtravelDataSource.setNotifyToTravelListListener(notifyToTravelListListener);

        iRMhistoryDataSource.getTravels().observeForever(new Observer<List<Travel>>()  {
            @Override
            public void onChanged(List<Travel> historyTravelList) {
                for (Travel t :historyTravelList)
                    if(!t.getRequestType().toString().equals("close"))
                        historyTravelList.remove(t);
                HistoryTravels.setValue(historyTravelList);
            }
        });

    }
    public void findUserTravelList(){
        //find the connected user travel list
        userTravelList=new LinkedList<Travel>();
        String UserEmail=user.getEmail();
        for (Travel t : travelList)
            if(t.getClientEmail().equals(UserEmail))
                userTravelList.add(t);
        UserTravels.setValue(userTravelList);
    }

    public void findOpenTravelList(){
        //returns the open travels to the company
        LinkedList<Travel> companyTravels = new LinkedList<Travel>();
        for (Travel t : travelList)
            if(t.getRequestType().toString().equals("sent")||t.getRequestType().toString().equals("accepted"))
                companyTravels.add(t);
        openTravels.setValue(companyTravels);
    }

    public void  findHistoryTravelList(){
        // TODO: 30/12/2020
      LiveData <List<Travel>> travelList = new MutableLiveData<>();
       travelList =  iRMhistoryDataSource.getTravels();
//        Transformations.
 //       List<Travel> historyTravelList=new  LinkedList<Travel>();

//        for (Travel t :historyTravelList)
//        if(!t.getRequestType().toString().equals("close"))
//            historyTravelList.remove(t);
       // fragmentsVM.getOpenTravels().observe(this, new Observer<List<Travel>>() {



    }
    @Override
    public void addTravel(Travel travel) {
        iFBtravelDataSource.addTravel(travel);
    }

    @Override
    public void updateTravel(Travel travel) {
        iFBtravelDataSource.updateTravel(travel);
    }

    @Override
    public MutableLiveData<List<Travel>> getAllTravels() {
        return AllTravels;
    }

    @Override
    public MutableLiveData<List<Travel>> getUserTravels()
    {
        return UserTravels;
    }

    @Override
    public MutableLiveData<List<Travel>> getOpenTravels() {
        return openTravels;
    }
    @Override
    public MutableLiveData<List<Travel>> getHistoryTravels() {

        return HistoryTravels;
    }

    @Override
    public MutableLiveData<Boolean> getIsSuccess() {
        return iFBtravelDataSource.getIsSuccess();
    }
}
