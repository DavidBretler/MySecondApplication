package com.example.mysecondapplication.UI;


import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mysecondapplication.Entities.Travel;
import com.example.mysecondapplication.Entities.UserLocation;
import com.example.mysecondapplication.R;
import com.example.mysecondapplication.UI.Fragments.FragmentsVM;
import com.example.mysecondapplication.UI.NavigationDrawer.NavigationDrawer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MyListAdapter  extends RecyclerView.Adapter<MyListAdapter.ViewHolder>  {
    private Travel[] listdata;
    Context context;
    Location location;
    FragmentsVM fragmentsVM;
    public SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
     // public Context contex= getApplication()
    // RecyclerView com.example.mysecondapplication.UI.recyclerView;
    public MyListAdapter(Travel[] listdata,Context context) {
        this.listdata = listdata;
        this.context=context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.travel_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
     //   fragmentsVM = new ViewModelProvider(NavigationDrawer.class).get(FragmentsVM.class);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        location = new Location(LocationManager.GPS_PROVIDER);
        location.setLatitude(listdata[position].getPickupAddress().getLat());
        location.setLongitude(listdata[position].getPickupAddress().getLon());

        String pickupAddres="";
        pickupAddres =getPlace(location);
        final Travel myListData = listdata[position];
        holder.arrivalDateTextView.setText("Source : "+pickupAddres);
        holder.travelDateTextView.setText("Destination: "+pickupAddres);
        holder.numOfPassengerTextView.setText("start Date:"+format.format(listdata[position].getTravelDate()));

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"click on buttune: ",Toast.LENGTH_LONG).show();
                myListData.setRequestType(Travel.RequestType.run);
        //        fragmentsVM.updateTravel(myListData);
            }
        });


        ArrayAdapter<String> adapter= new ArrayAdapter<> (context ,android.R.layout.simple_spinner_item, new ArrayList<String>( listdata[position].getCompany().keySet()) )  ;
        holder.spinner.setAdapter(adapter);
          holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),"click on item: "+myListData.getClientName(),Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listdata.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView arrivalDateTextView;
        public TextView travelDateTextView;
        public TextView numOfPassengerTextView;
        public RelativeLayout relativeLayout;
        public Spinner spinner;
        Button button;
        public ViewHolder(View itemView) {
            super(itemView);
            this.button= (Button) itemView.findViewById(R.id.Btn_changeToRun);
            this.arrivalDateTextView = (TextView) itemView.findViewById(R.id.Txt_ArrivalDate);
            this.travelDateTextView = (TextView) itemView.findViewById(R.id.Txt_TravelDate);
            this.numOfPassengerTextView = (TextView) itemView.findViewById(R.id.Txt_numPassenger);
           this.spinner=(Spinner) itemView.findViewById(R.id.spinner);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout);
        }
    }
    public String getPlace(Location location) {
        String cityName="" ;
        String stateName="";
        String countryName="";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);


            if (addresses.size() > 0) {
                 cityName = addresses.get(0).getAddressLine(0);
                if (addresses.size() > 1)
                 stateName = addresses.get(0).getAddressLine(1);
                if (addresses.size() > 2)
                 countryName = addresses.get(0).getAddressLine(2);
                return stateName + " " + cityName + " " + countryName;
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
}