package Utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;


import com.google.android.gms.maps.model.LatLng;


import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GPS extends Activity {

    // Acquire a reference to the system Location Manager
    LocationManager locationManager;
    // Define a listener that responds to location updates
    LocationListener locationListener;
    double curLatitude, curLongitude;
    private static final int REQUEST_LOCATION = 1;
    String latitude;
    String longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                Toast.makeText(getBaseContext(), Double.toString(location.getLatitude()) + " : " + Double.toString(location.getLongitude()), Toast.LENGTH_LONG).show();
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

        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };
        timer.start();

//            String newAddress="1600, Amphitheatre Pkwy, Mountain View, CA 94043, United States";
//            LatLng Loc=getLocationFromAddress(this,newAddress);

        //           float dis=calculateDistance(curLatitude,curLongitude,Loc.latitude,Loc.longitude);
//
//
//            Location temp = new Location(LocationManager.GPS_PROVIDER);
//            temp.setLatitude(Loc.latitude);
//            temp.setLongitude(Loc.longitude);
//
//            String add1=getPlace(temp);


    }


    public void getLocation() {

        //     Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 5);

        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            Location LocationGps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location LocationNetwork=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location LocationPassive=locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (LocationGps !=null)
            {
                double lat=LocationGps.getLatitude();
                double longi=LocationGps.getLongitude();

                latitude=String.valueOf(lat);
                longitude=String.valueOf(longi);
                Toast.makeText(this,"Your Location:"+"\n"+"Latitude= "+latitude+"\n"+"Longitude= "+longitude,Toast.LENGTH_LONG).show();
            }
            else if (LocationNetwork !=null)
            {
                double lat=LocationNetwork.getLatitude();
                double longi=LocationNetwork.getLongitude();

                latitude=String.valueOf(lat);
                longitude=String.valueOf(longi);

                Toast.makeText(this,"Your Location:"+"\n"+"Latitude= "+latitude+"\n"+"Longitude= "+longitude,Toast.LENGTH_LONG).show();
            }
            else if (LocationPassive !=null)
            {
                double lat=LocationPassive.getLatitude();
                double longi=LocationPassive.getLongitude();

                latitude=String.valueOf(lat);
                longitude=String.valueOf(longi);

                Toast.makeText(this,"Your Location:"+"\n"+"Latitude= "+latitude+"\n"+"Longitude= "+longitude,Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(this, "Can't Get Your Location", Toast.LENGTH_SHORT).show();
            }

            //Thats All Run Your App
           // locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }

    }

    //getLocation From Address
    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng LatLan = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            LatLan = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return LatLan;
    }


    public String getPlace(Location location) {

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);


            if (addresses.size() > 0) {
                String cityName = addresses.get(0).getAddressLine(0);
                String stateName = addresses.get(0).getAddressLine(1);
                String countryName = addresses.get(0).getAddressLine(2);
                return stateName + "\n" + cityName + "\n" + countryName;
            }

            return "no place: \n (" + location.getLongitude() + " , " + location.getLatitude() + ")";
        } catch (
                IOException e) {
            e.printStackTrace();
        }
        return "IOException ...";
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 5) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the location", Toast.LENGTH_SHORT).show();
            }
        }

    }


//    private void getLocation1() {
//
//
//            Location LocationGps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//            Location LocationNetwork=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//            Location LocationPassive=locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
//
//            if (LocationGps !=null)
//            {
//                double lat=LocationGps.getLatitude();
//                double longi=LocationGps.getLongitude();
//
//                latitude=String.valueOf(lat);
//                longitude=String.valueOf(longi);
//                Toast.makeText(this,"Your Location:"+"\n"+"Latitude= "+latitude+"\n"+"Longitude= "+longitude,Toast.LENGTH_LONG).show();
//            }
//            else if (LocationNetwork !=null)
//            {
//                double lat=LocationNetwork.getLatitude();
//                double longi=LocationNetwork.getLongitude();
//
//                latitude=String.valueOf(lat);
//                longitude=String.valueOf(longi);
//
//                Toast.makeText(this,"Your Location:"+"\n"+"Latitude= "+latitude+"\n"+"Longitude= "+longitude,Toast.LENGTH_LONG).show();
//            }
//            else if (LocationPassive !=null)
//            {
//                double lat=LocationPassive.getLatitude();
//                double longi=LocationPassive.getLongitude();
//
//                latitude=String.valueOf(lat);
//                longitude=String.valueOf(longi);
//
//                Toast.makeText(this,"Your Location:"+"\n"+"Latitude= "+latitude+"\n"+"Longitude= "+longitude,Toast.LENGTH_LONG).show();
//            }
//            else
//            {
//                Toast.makeText(this, "Can't Get Your Location", Toast.LENGTH_SHORT).show();
//            }
//
//            //Thats All Run Your App
//        }



    public float calculateDistance1(double venueLat, double venueLng) {
//getLocation1();
        Location temp = new Location(LocationManager.GPS_PROVIDER);
        temp.setLatitude(venueLat);
        temp.setLongitude(venueLng);

        Location temp1 = new Location(LocationManager.EXTRA_PROVIDER_NAME);
        temp1.setLatitude(curLatitude);
         temp1.setLongitude(curLongitude);



      return   temp.distanceTo(temp1);

    }

    public final static double AVERAGE_RADIUS_OF_EARTH = 6371;
    public float calculateDistance(double userLat, double userLng, double venueLat, double venueLng) {

        double latDistance = Math.toRadians(userLat - venueLat);
        double lngDistance = Math.toRadians(userLng - venueLng);

        double a = (Math.sin(latDistance / 2) * Math.sin(latDistance / 2)) +
                (Math.cos(Math.toRadians(userLat))) *
                        (Math.cos(Math.toRadians(venueLat))) *
                        (Math.sin(lngDistance / 2)) *
                        (Math.sin(lngDistance / 2));

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return (float) (Math.round(AVERAGE_RADIUS_OF_EARTH * c));

    }



    }

//package com.coderpakistan.currentlocation;
//
//        import android.Manifest;
//        import android.content.Context;
//        import android.content.DialogInterface;
//        import android.content.Intent;
//        import android.content.pm.PackageManager;
//        import android.location.Location;
//        import android.location.LocationManager;
//        import android.provider.Settings;
//        import android.support.v4.app.ActivityCompat;
//        import android.support.v7.app.AlertDialog;
//        import android.support.v7.app.AppCompatActivity;
//        import android.os.Bundle;
//        import android.view.View;
//        import android.widget.Button;
//        import android.widget.TextView;
//        import android.widget.Toast;
//
//public class MainActivity extends AppCompatActivity {
//
//    private static  final int REQUEST_LOCATION=1;
//
//    Button getlocationBtn;
//    TextView showLocationTxt;
//
//    LocationManager locationManager;
//    String latitude,longitude;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        //Add permission
//
//        ActivityCompat.requestPermissions(this,new String[]
//                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
//
//        showLocationTxt=findViewById(R.id.show_location);
//        getlocationBtn=findViewById(R.id.getLocation);
//
//        getlocationBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
//
//                //Check gps is enable or not
//
//                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
//                {
//                    //Write Function To enable gps
//
//                    OnGPS();
//                }
//                else
//                {
//                    //GPS is already On then
//
//                    getLocation();
//                }
//            }
//        });
//
//    }
//
//    private void getLocation() {
//
//        //Check Permissions again
//
//        if (ActivityCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this,
//
//                Manifest.permission.ACCESS_COARSE_LOCATION) !=PackageManager.PERMISSION_GRANTED)
//        {
//            ActivityCompat.requestPermissions(this,new String[]
//                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
//        }
//        else
//        {
//            Location LocationGps= locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//            Location LocationNetwork=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//            Location LocationPassive=locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
//
//            if (LocationGps !=null)
//            {
//                double lat=LocationGps.getLatitude();
//                double longi=LocationGps.getLongitude();
//
//                latitude=String.valueOf(lat);
//                longitude=String.valueOf(longi);
//
//                showLocationTxt.setText("Your Location:"+"\n"+"Latitude= "+latitude+"\n"+"Longitude= "+longitude);
//            }
//            else if (LocationNetwork !=null)
//            {
//                double lat=LocationNetwork.getLatitude();
//                double longi=LocationNetwork.getLongitude();
//
//                latitude=String.valueOf(lat);
//                longitude=String.valueOf(longi);
//
//                showLocationTxt.setText("Your Location:"+"\n"+"Latitude= "+latitude+"\n"+"Longitude= "+longitude);
//            }
//            else if (LocationPassive !=null)
//            {
//                double lat=LocationPassive.getLatitude();
//                double longi=LocationPassive.getLongitude();
//
//                latitude=String.valueOf(lat);
//                longitude=String.valueOf(longi);
//
//                showLocationTxt.setText("Your Location:"+"\n"+"Latitude= "+latitude+"\n"+"Longitude= "+longitude);
//            }
//            else
//            {
//                Toast.makeText(this, "Can't Get Your Location", Toast.LENGTH_SHORT).show();
//            }
//
//            //Thats All Run Your App
//        }
//
//    }
//
//    private void OnGPS() {
//
//        final AlertDialog.Builder builder= new AlertDialog.Builder(this);
//
//        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//            }
//        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//                dialog.cancel();
//            }
//        });
//        final AlertDialog alertDialog=builder.create();
//        alertDialog.show();
//    }
//}
