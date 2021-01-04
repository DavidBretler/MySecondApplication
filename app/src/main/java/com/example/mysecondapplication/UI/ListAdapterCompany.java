package com.example.mysecondapplication.UI;



import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mysecondapplication.Entities.Travel;
import com.example.mysecondapplication.R;
import com.example.mysecondapplication.UI.Fragments.FragmentsVM;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.collection.LLRBNode;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class ListAdapterCompany extends RecyclerView.Adapter<ListAdapterCompany.ViewHolder>  {
    private Travel[] listdata;
    Context context;
    Location location;
    FragmentsVM fragmentsVM;
    FragmentActivity viewModelStore;
    public FirebaseAuth mAuth;
    FirebaseUser user;
    String companyName="";
    public SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    public ListAdapterCompany(Travel[] listdata, Context context, FragmentActivity viewModelStore) {
        this.listdata = listdata;
        this.context=context;
        this.viewModelStore=viewModelStore;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.company_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        fragmentsVM = new ViewModelProvider(viewModelStore).get(FragmentsVM.class);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        companyName=user.getEmail().split("@")[0];
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
        holder.Txt_sourceAddress.setText("Source : "+pickupAddres);
        holder.Txt_destAddress.setText("Destination: "+pickupAddres);
        holder.Txt_numOfPassenger.setText("passengers: "+myListData.getNumOfPassenger());
        holder.Txt_travelDate.setText("start Date:"+format.format(myListData.getTravelDate()));
        holder.Txt_name.setText("name:"+myListData.getClientName());
        if(myListData.getCompany().get(companyName)!=null && myListData.getCompany().get(companyName))
        holder.relativeLayout.setBackgroundColor(Color.GREEN);

        holder.Btn_sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
          sendMail();
            }
        });
        holder.Btn_AprroveCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Boolean> company=new HashMap<>();
                company = myListData.getCompany();
               if(myListData.getCompany().get(companyName)==null)
               {
                company.put(companyName,false);
                myListData.setCompany(company);
                fragmentsVM.updateTravel(myListData);
                listdata[position].setCompany(company);
                myListData.setCompany(company);
                Toast.makeText(context, "request sent", Toast.LENGTH_LONG).show();
               }
                else
                    Toast.makeText(context, "company is waiting for response from client", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return listdata.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView Txt_name;
        TextView Txt_travelDate;
        TextView Txt_numOfPassenger;
        TextView Txt_sourceAddress;
        TextView Txt_destAddress;
        Button Btn_sendEmail;
        Button Btn_AprroveCompany;
        RelativeLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.Txt_name =  itemView.findViewById(R.id.Txt_name);
            this.Txt_travelDate =  itemView.findViewById(R.id.Txt_date);
            this.Txt_numOfPassenger =  itemView.findViewById(R.id.Txt_numOfPassenger);
            this.Txt_sourceAddress= itemView.findViewById(R.id.Txt_sourceAdress);
            this.Txt_destAddress=itemView.findViewById(R.id.Txt_desAdress);
            this.Btn_sendEmail =  itemView.findViewById(R.id.Btn_sendEmail);
            this.Btn_AprroveCompany =  itemView.findViewById(R.id.Btn_approve);

            relativeLayout = itemView.findViewById(R.id.relativeLayout2);
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
    private void sendMail() {
        String  recipientList = user.getEmail();
        String[] recipients = recipientList.split(",");

        String subject = "new travel company";
        String message = "Hallow, we offer you our ride services  ";
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, recipients);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);
        intent.setType("message/rfc822");
        viewModelStore.startActivity(Intent.createChooser(intent, "Choose an email client"));
    }
}