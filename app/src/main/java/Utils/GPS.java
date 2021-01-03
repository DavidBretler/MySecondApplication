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

import com.example.mysecondapplication.R;
import com.google.android.gms.maps.model.LatLng;


import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GPS extends Activity
{

        // Acquire a reference to the system Location Manager
        LocationManager locationManager;
        // Define a listener that responds to location updates
        LocationListener locationListener;
        double curLatitude,curLongitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        // Define a listener that responds to location updates
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


        private void getLocation() {

            //     Check the SDK version and whether the permission is already granted or not.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 5);

            } else {
                // Android version is lesser than 6.0 or the permission is already granted.

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }

        }

        //getLocation From Address
        public LatLng getLocationFromAddress(Context context,String strAddress) {

            Geocoder coder = new Geocoder(context);
            List<Address> address;
            LatLng LatLan= null;

            try {
                // May throw an IOException
                address = coder.getFromLocationName(strAddress, 5);
                if (address == null) {
                    return null;
                }

                Address location = address.get(0);
                LatLan= new LatLng(location.getLatitude(), location.getLongitude() );

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

                return "no place: \n ("+location.getLongitude()+" , "+location.getLatitude()+")";
            }
            catch(
                    IOException e)

            {
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


        public final static double AVERAGE_RADIUS_OF_EARTH = 6371;
        public float calculateDistance( double venueLat, double venueLng) {

            double latDistance = Math.toRadians(curLatitude - venueLat);
            double lngDistance = Math.toRadians(curLongitude - venueLng);

            double a = (Math.sin(latDistance / 2) * Math.sin(latDistance / 2)) +
                    (Math.cos(Math.toRadians(curLatitude))) *
                            (Math.cos(Math.toRadians(venueLat))) *
                            (Math.sin(lngDistance / 2)) *
                            (Math.sin(lngDistance / 2));

            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

            return (float) (Math.round(AVERAGE_RADIUS_OF_EARTH * c));

        }
    }


