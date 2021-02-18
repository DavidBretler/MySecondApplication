package com.example.mysecondapplication.UI.Fragments;

import android.app.Application;
import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.example.mysecondapplication.Data.Repository.ITravelRepository;
import com.example.mysecondapplication.Data.Repository.TravelRepository;
import com.example.mysecondapplication.Entities.Travel;

import java.util.List;

/**
 * transfers the information between the 3 fragments and the lower levels
 * the VM is not the terminated when we are in the fragments because its linked to navigation drawer
 */
public class FragmentsVM extends AndroidViewModel {
  private Context context;
    private MutableLiveData<String> mText;
    ITravelRepository repository; //instance of repository to access data
    private MutableLiveData<List<Travel>> UserTravelList = new  MutableLiveData<>();
    private  List<Travel> travelList;
    //constructor
    public FragmentsVM(Application p) {
      super(p);

        repository = TravelRepository.getInstance(p);
    }

  /**
   *ads a new travel throw repository
   * @param travel new
   */
    public void addTravel(Travel travel)
    {
        repository.addTravel(travel);
    }

  /**
   * update the travel in fire base by sending to repository
   * @param travel update
   */
  public void updateTravel(Travel travel)
    {
        repository.updateTravel(travel);
    }

  /**
   *
   * @param lat latitude location
   * @param lon longtitude location
   * @param maxDis maximum distance between company and the departure location
   * @return the open travels of the logged in company
   */
    public MutableLiveData<List<Travel>> getOpenTravels(double lat, double lon,int maxDis)
    {
        return (MutableLiveData<List<Travel>>) repository.getOpenTravels(lat,lon,maxDis);
    }


  /**
   *
   * @return the not closed travel requests of the logged in user
   */
  public MutableLiveData<List<Travel>> getUserTravels()
    {

        return  repository.getUserTravels();

    }

  /**
   *
   * @return all the travel requests in app history that are closed
   * updated from the room
   */
  public MutableLiveData<List<Travel>> gethistoryTravels()
    {
        return (MutableLiveData<List<Travel>>) repository.getHistoryTravels();
    }

  /**
   *
   * @return if the update in firebase was successful
   */
  public MutableLiveData<Boolean> getIsSuccess()
    {
        return repository.getIsSuccess();
    }

  /**
   *
    * @return return the email the current user logged in
   */
    public String getUserEmail(){return repository.getUserEmail();}

  public LiveData<Boolean> getTravelUpdate() {
    return repository.getIsSuccess();
  }
}
