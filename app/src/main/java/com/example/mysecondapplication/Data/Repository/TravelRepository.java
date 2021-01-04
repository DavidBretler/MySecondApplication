package com.example.mysecondapplication.Data.Repository;

import android.app.Application;
import android.location.Location;
import android.location.LocationManager;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.mysecondapplication.Data.FBtravelDataSource;
import com.example.mysecondapplication.Data.IFBtravelDataSource;
import com.example.mysecondapplication.Data.IRMhistoryDataSource;
import com.example.mysecondapplication.Data.RMhistoryDataSource;
import com.example.mysecondapplication.Entities.Travel;
import com.example.mysecondapplication.UI.NavigationDrawer.NavigationDrawer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.LinkedList;
import java.util.List;

import Utils.GPS;

public class TravelRepository implements ITravelRepository {
    IFBtravelDataSource iFBtravelDataSource;
    private IRMhistoryDataSource iRMhistoryDataSource;
    private MutableLiveData<List<Travel>> openTravels = new MutableLiveData<>();
    private MutableLiveData<List<Travel>> UserTravels = new MutableLiveData<>();
    private MutableLiveData<List<Travel>> HistoryTravels = new MutableLiveData<>();
    private List<Travel> travelList;
    private List<Travel> userTravelList;
    List<Travel> historyTravelList;
    public FirebaseAuth mAuth;
    public FirebaseUser user;
    private Application application;
    private MutableLiveData<List<Travel>> AllTravels = new MutableLiveData<>();
    Location location;
    LocationManager locationManager;
    GPS gps;
    private static TravelRepository instance;
    NavigationDrawer navigationDrawer;
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
        this.application = application;
        List<Travel> historyTravelList=new  LinkedList<Travel>();

        IFBtravelDataSource.NotifyToTravelListListener notifyToTravelListListener = new IFBtravelDataSource.NotifyToTravelListListener() {
            @Override
            //get all travels from fire base split to some list to the fragments
            //react to changes
            public void onTravelsChanged() {
                travelList = iFBtravelDataSource.getAllTravels();
                AllTravels.setValue(travelList);

                findUserTravelList();
           //     findOpenTravelList();
           //     findHistoryTravelList();
              //puse all close travel to the room
                for (Travel t :travelList)
                    if(t.getRequestType().toString().equals("close"))
                        historyTravelList.add(t);

                iRMhistoryDataSource.clearTable();
                iRMhistoryDataSource.addTravel(historyTravelList);
            }
        };
        // make sure we react to changes in the firebase
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

    public void findUserTravelList() {
        //find the connected user travel list
        userTravelList = new LinkedList<Travel>();
        String UserEmail = user.getEmail();
        for (Travel t : travelList)
            if (t.getClientEmail().equals(UserEmail) && t.getRequestType() != Travel.RequestType.close)
                userTravelList.add(t);
        UserTravels.setValue(userTravelList);
    }

    public void findOpenTravelList(double lat,double lon,int maxDis) {
        //returns the open travels to the company
        // when  dis is less then maxDis from picapp address

        navigationDrawer = new NavigationDrawer();
        LinkedList<Travel> companyTravels = new LinkedList<Travel>();
        for (Travel t : travelList) {
            if (t.getRequestType().toString().equals("sent") || t.getRequestType().toString().equals("accepted")) {
                Location temp = new Location(LocationManager.GPS_PROVIDER);
                temp.setLatitude(lat);
                temp.setLongitude(lon);

                Location temp1 = new Location(LocationManager.GPS_PROVIDER);
                temp1.setLatitude(t.getPickupAddress().getLat());
                temp1.setLongitude(t.getPickupAddress().getLon());

                double  distance= temp.distanceTo(temp1);
          //     Toast.makeText(this.application.getApplicationContext(), " dis is :" + distance, Toast.LENGTH_LONG).show();
            if(distance<maxDis)
              companyTravels.add(t);

            }
        }
        openTravels.setValue(companyTravels);
    }
    public void findHistoryTravelList() {
        // TODO: 30/12/2020
//        LiveData<List<Travel>> travelList = new MutableLiveData<>();
//        LiveData<List<Travel>> travelList1 = new MutableLiveData<>();
//        travelList = iRMhistoryDataSource.getTravels();
//        Object o = travelList.getValue();
        //        Transformations.
        //       List<Travel> historyTravelList=new  LinkedList<Travel>();

//        for (Travel t :historyTravelList)
//        if(!t.getRequestType().toString().equals("close"))
//            historyTravelList.remove(t);


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
    public MutableLiveData<List<Travel>> getUserTravels() {
        return UserTravels;
    }

    @Override
    //returns the open travels to the company
    // when  dis is less then maxDis from picapp address
    //return the mutable live data that observ from the fragments view model
    public MutableLiveData<List<Travel>> getOpenTravels(double lat ,double lon,int maxDis) {
        // TODO: 04/01/2021 check if good
        findOpenTravelList(lat,lon,maxDis);

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