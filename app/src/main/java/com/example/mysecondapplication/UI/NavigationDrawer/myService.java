package com.example.mysecondapplication.UI.NavigationDrawer;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mysecondapplication.Data.Repository.ITravelRepository;
import com.example.mysecondapplication.Data.Repository.TravelRepository;
import com.example.mysecondapplication.Entities.Travel;
import com.example.mysecondapplication.UI.Fragments.FragmentsVM;
import com.example.mysecondapplication.UI.Fragments.RegisteredTravels;
import com.example.mysecondapplication.UI.recyclerView.ListAdapterTravel;

import java.util.LinkedList;
import java.util.List;
/**
 *Service that will work when app in on or in the background
 */
public class myService extends Service {
    Integer sum = 0;
    boolean isThreadOn = false;
    public final String TAG = "myService";
    NavigationDrawer navigationDrawer;
    ITravelRepository repository;
    private  List<Travel> travelList;
    int travelsNum=-1;

    @Override
    public void onCreate() {
        super.onCreate();
        repository = TravelRepository.getInstance(getApplication());
        navigationDrawer=new NavigationDrawer();
    }

    /**
     * send broadcast to receiver
     * @param intent send in action
     * @param flags flags to spacial situation
     * @param startId start id to service
     * @return type of service
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(!isThreadOn)
        {
            isThreadOn = true;
            Intent intent2 = new Intent("MyCustomIntent");
           repository.getAllTravels().observeForever( new Observer<List<Travel>>() {
                @Override
                public void onChanged(List<Travel> travels) {//check if new travel have been added
                    travelList=new LinkedList<>(travels);
                    if(travelsNum!=-1 && travelsNum < travelList.size())
                    {
                        intent2.putExtra("message", "new travel added");
                        intent2.setAction("com.javacodegeeks.android.A_CUSTOM_INTENT");
                        sendBroadcast(intent2);
                    }
                    travelsNum=travelList.size();
                }
            });

            // add data to the Intent

        }
  //      Toast.makeText(this,"onStartCommand."+travelsNum , Toast.LENGTH_LONG).show();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

}
