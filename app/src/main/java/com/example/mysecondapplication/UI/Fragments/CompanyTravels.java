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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.mysecondapplication.Entities.Travel;
import com.example.mysecondapplication.R;

import java.util.List;


public class CompanyTravels extends Fragment {

    private FragmentsVM fragmentsVM;
    Location location;
    LocationManager locationManager ;
    LocationListener locationListener;
    double curLatitude;
    double curLongitude;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        fragmentsVM =
                new ViewModelProvider(this).get(FragmentsVM.class);
        View root = inflater.inflate(R.layout.fragment_company_travels, container, false);

        final TextView textView = root.findViewById(R.id.text_gallery);

         locationManager = (LocationManager) this.getActivity().getSystemService(Context.LOCATION_SERVICE);

         locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                //    Toast.makeText(getBaseContext(), Double.toString(location.getLatitude()) + " : " + Double.toString(location.getLongitude()), Toast.LENGTH_LONG).show();
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

         int  maxDis=20000;
        fragmentsVM.getOpenTravels(curLatitude,curLongitude,maxDis).observe(getViewLifecycleOwner(), new Observer<List<Travel>>() {
            @Override
            public void onChanged(List<Travel> travels) {
                Toast.makeText(getActivity(), "size is :" + travels.size(),
                        Toast.LENGTH_SHORT).show();
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

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                }
            }
    }
