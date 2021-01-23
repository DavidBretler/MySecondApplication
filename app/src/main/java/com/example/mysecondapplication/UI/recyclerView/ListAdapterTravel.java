package com.example.mysecondapplication.UI.recyclerView;


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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mysecondapplication.Entities.Travel;
import com.example.mysecondapplication.*;
import com.example.mysecondapplication.UI.Fragments.FragmentsVM;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * show all the travel request of the user currently logged in except closed Travels
 */
public class ListAdapterTravel extends RecyclerView.Adapter<ListAdapterTravel.ViewHolder>  {
    private Travel[] listdata;
    Context context;
    Location location;
    FragmentsVM fragmentsVM;
    FragmentActivity viewModelStore;
    public SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    //constructor
    public ListAdapterTravel(Travel[] listdata, Context context, FragmentActivity viewModelStore) {
        this.listdata = listdata;
        this.context=context;
        this.viewModelStore=viewModelStore;
    }

    /**
     * connect the ViewHolder to the wanted layout
     * create instance of fragmentsVM
     * @param parent the view group that the view will be added to
     * @param viewType type of view
     * @return the view Holder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.travel_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        fragmentsVM = new ViewModelProvider(viewModelStore).get(FragmentsVM.class);

        return viewHolder;
    }
    /**
     * @param holder hold the wanted layout and graphic objects
     * @param position holds the current position in list
     * insert all the wanted data from the travel to the  graphic objects
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //create location of Pickup Address and destination Address
        location = new Location(LocationManager.GPS_PROVIDER);
        location.setLatitude(listdata[position].getPickupAddress().getLat());
        location.setLongitude(listdata[position].getPickupAddress().getLon());

        String pickupAddres="",DetentionAddress="";
        pickupAddres =getPlace(location);
        location.setLatitude(listdata[position].getDetentionAddress().getLat());
        location.setLongitude(listdata[position].getDetentionAddress().getLon());
        DetentionAddress=getPlace(location);

        final Travel myListData = listdata[position];
        holder.arrivalDateTextView.setText("Source : "+pickupAddres);
        holder.travelDateTextView.setText("Destination: "+DetentionAddress);
        holder.numOfPassengerTextView.setText("start Date:"+format.format(listdata[position].getTravelDate()));
        ArrayAdapter<String> adapter= new ArrayAdapter<> (context ,android.R.layout.simple_spinner_item, new ArrayList<String>( listdata[position].getCompany().keySet()) )  ;
        holder.spinner.setAdapter(adapter);

        //updates the travel status to run
        holder.Btn_chngeToRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean aproove=false;
                for (boolean status :myListData.getCompany().values()) {
                    if (status)//check that company in approved before change statuse of travel
                    {   aproove=true;
                        if (myListData.getRequestType() != Travel.RequestType.run) {
                                myListData.setRequestType(Travel.RequestType.run);
                                fragmentsVM.updateTravel(myListData);
                                Toast.makeText(context, "Data updated", Toast.LENGTH_LONG).show();
                                break;
                        } else
                           { Toast.makeText(context, " already in run statues", Toast.LENGTH_LONG).show();
                              break;}
                    }
                }
             if (!aproove)
                 Toast.makeText(context, "please approve company first", Toast.LENGTH_LONG).show();

            }
        });
        //updates the travel status to closed
        holder.Btn_chngeToclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean aproove=false;
                for (boolean status :myListData.getCompany().values())
                {
                    if (status){//check that compaby in approved before change statuse of travel
                        aproove=true;
                        myListData.setRequestType(Travel.RequestType.close);
                        fragmentsVM.updateTravel(myListData);
                        Toast.makeText(context, "Data updated", Toast.LENGTH_LONG).show();
                        break;}
                }
                if (!aproove)
                    Toast.makeText(context, "please approve company first", Toast.LENGTH_LONG).show();
                }
        });
        //Approve Company to provide travel service
        holder.Btn_AprroveCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Boolean> company=new HashMap<>();
                   company = myListData.getCompany();
                   if(!company.get(holder.spinner.getSelectedItem().toString())) {
                       company.put(holder.spinner.getSelectedItem().toString(), true);
                       myListData.setCompany(company);
                       fragmentsVM.updateTravel(myListData);
                       listdata[position].setCompany(company);
                       myListData.setCompany(company);
                       Toast.makeText(context, "Data updated", Toast.LENGTH_LONG).show();
                   }
                   else
                       Toast.makeText(context, "company is already approved", Toast.LENGTH_LONG).show();
            }
        });
    }
    /**
     * @return number of items
     */
    @Override
    public int getItemCount() {
        return listdata.length;
    }
    /**
     * linking the graphic objects to the fields in Travel
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView arrivalDateTextView;
        public TextView travelDateTextView;
        public TextView numOfPassengerTextView;
        public RelativeLayout relativeLayout;
        public Spinner spinner;
        Button Btn_chngeToRun;
        Button Btn_chngeToclose;
        Button Btn_AprroveCompany;
        public ViewHolder(View itemView) {
            super(itemView);
            this.Btn_chngeToRun =  itemView.findViewById(R.id.Btn_changeToRun);
            this.Btn_chngeToclose= itemView.findViewById(R.id.Btn_changeToClose);
            this.Btn_AprroveCompany=itemView.findViewById(R.id.Btn_approveCompany);
            this.arrivalDateTextView =  itemView.findViewById(R.id.Txt_ArrivalDate);
            this.travelDateTextView =  itemView.findViewById(R.id.Txt_TravelDate);
            this.numOfPassengerTextView =  itemView.findViewById(R.id.Txt_numPassenger);
           this.spinner= (Spinner) itemView.findViewById(R.id.spinner);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
        }
    }
    /**
     * convert location to address
     * @param location a instance holding latitude and longitude
     * @return string address of location
     */
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