package com.example.mysecondapplication.UI.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mysecondapplication.Entities.Travel;
import com.example.mysecondapplication.R;
import com.example.mysecondapplication.UI.ListAdapterCompany;
import com.example.mysecondapplication.UI.ListAdapterTravel;

import java.util.LinkedList;
import java.util.List;


public class CompanyTravels extends Fragment {

    private FragmentsVM fragmentsVM;
    Location location;
    LocationManager locationManager ;
    LocationListener locationListener;
    double curLatitude;
    double curLongitude;
    Context contex;
    RecyclerView recyclerView;
    ListAdapterCompany adapter;
    public List<Travel> Travels;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        fragmentsVM =
                new ViewModelProvider(this).get(FragmentsVM.class);
        View root = inflater.inflate(R.layout.fragment_company_travels, container, false);
         contex =this.getActivity();

         locationManager = (LocationManager) this.getActivity().getSystemService(Context.LOCATION_SERVICE);

         locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                 //  Toast.makeText( contex , Double.toString(location.getLatitude()) + " : " + Double.toString(location.getLongitude()), Toast.LENGTH_LONG).show();
                 curLatitude = location.getLatitude();
                 curLongitude = location.getLongitude();
            }


            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

       getLocation();

        if (ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // return TODO;
        }
         location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
         curLatitude=location.getLatitude();
         curLongitude=location.getLongitude();

         contex =CompanyTravels.this.getActivity().getBaseContext();
        recyclerView = (RecyclerView) root.findViewById(R.id.company_travel_recyclerView);
        DividerItemDecoration itemDecor = new DividerItemDecoration(CompanyTravels.this.getActivity(),1);
        recyclerView.addItemDecoration(itemDecor);
         int  maxDis=30000;
        fragmentsVM.getOpenTravels(curLatitude,curLongitude,maxDis).observe(getViewLifecycleOwner(), new Observer<List<Travel>>() {
            @Override
            public void onChanged(List<Travel> travels) {
                Travels=new LinkedList<>(travels);

                Travel[] travelsArr = new Travel[travels.size()];
                travels.toArray(travelsArr);
                adapter = new ListAdapterCompany(travelsArr, contex,requireActivity());
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(CompanyTravels.this.getActivity()));
                recyclerView.setAdapter(adapter);
            }
        });

        return root;

    }
        public void getLocation()
            {

                //     Check the SDK version and whether the permission is already granted or not.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && this.getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 5);

                } else {
                    // Android version is lesser than 6.0 or the permission is already granted.

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 100, locationListener);
                }
            }
    }
