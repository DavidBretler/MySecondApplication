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

/**
 * abstract by transferring  the data from different data base- firebase and room  to upper layers
 */
public class TravelRepository implements ITravelRepository {
    IFBtravelDataSource iFBtravelDataSource;
    private IRMhistoryDataSource iRMhistoryDataSource;

    private MutableLiveData<List<Travel>> openTravels = new MutableLiveData<>(); //the list for the company
    private MutableLiveData<List<Travel>> UserTravels = new MutableLiveData<>();//the list for the user
    private MutableLiveData<List<Travel>> HistoryTravels = new MutableLiveData<>();//the list for the owner of app
    private List<Travel> travelList;
    private List<Travel> userTravelList;

    public FirebaseAuth mAuth;
    public FirebaseUser user;
    private Application application;
    private MutableLiveData<List<Travel>> AllTravels = new MutableLiveData<>();

    private static TravelRepository instance;
    NavigationDrawer navigationDrawer;

    /**
     * singleton to make sure we have only one instance of the TravelRepository
     * @param application for the constructor
     * @return the instance old or new
     */
    public static TravelRepository getInstance(Application application) {
        if (instance == null) {
            instance = new TravelRepository(application);
        }
        return instance;
    }

    /**
     * get tha data from data base and pass to the upper layers and to the room
     * @param application
     */
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

              //push all close travel to the room
                for (Travel t :travelList)
                    if(t.getRequestType().toString().equals("close"))
                        historyTravelList.add(t);

                iRMhistoryDataSource.clearTable();
                iRMhistoryDataSource.addTravel(historyTravelList);
            }
        };
        // make sure we react to changes in the firebase
        iFBtravelDataSource.setNotifyToTravelListListener(notifyToTravelListListener);

        //get the data from the room
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
    public String getUserEmail(){  return  user.getEmail();}

    /**
     * find users travel list using his Email
     */
    public void findUserTravelList() {
        userTravelList = new LinkedList<Travel>();
        String UserEmail = user.getEmail();
        for (Travel t : travelList)
            if (t.getClientEmail().equals(UserEmail) && t.getRequestType() != Travel.RequestType.close
                    && t.getRequestType() != Travel.RequestType.payed)
                userTravelList.add(t);
        UserTravels.setValue(userTravelList);
    }

    /**
     * returns the open travels to the company
     *  when  dis is less then maxDis from pickup address
     * @param lat latitude position
     * @param lon longitude position
     * @param maxDis maximum distance from company the present travels
     */
    public void findOpenTravelList(double lat,double lon,int maxDis) {
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


               if(distance<maxDis)
                  companyTravels.add(t);

            }
        }
        openTravels.setValue(companyTravels);
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