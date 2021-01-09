package com.example.mysecondapplication.UI.NavigationDrawer;



/**
 * Created by ezra on 14/12/2020.
 */

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

public class myService extends Service {
    Integer sum = 0;
    boolean isThreadOn = false;
    public final String TAG = "myService";
    FragmentsVM fragmentsVM;
    NavigationDrawer navigationDrawer;
    ITravelRepository repository;
    private  List<Travel> travelList;
    int travelsNum=-1;
    @Override
    public void onCreate() {
        super.onCreate();
   //     Toast.makeText(this,"onCreate", Toast.LENGTH_LONG).show();
        repository = TravelRepository.getInstance(getApplication());
        navigationDrawer=new NavigationDrawer();

//        fragmentsVM = new ViewModelProvider(navigationDrawer.getself()).get(FragmentsVM.class);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(!isThreadOn)
        {
            isThreadOn = true;
            Intent intent2 = new Intent("MyCustomIntent");
           repository.getAllTravels().observeForever( new Observer<List<Travel>>() {
                @Override
                public void onChanged(List<Travel> travels) {
                    travelList=new LinkedList<>(travels);
                    if(travelsNum!=-1 && travelsNum!=travelList.size())//change to <
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
        if(!isThreadOn)
            Toast.makeText(this,"onDestroy. sum is:" + sum, Toast.LENGTH_LONG).show();
        sum=0;
 //       Toast.makeText(this,"onDestroy", Toast.LENGTH_LONG).show();
        Log.d(TAG," onDestroy");
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

}
