package com.example.mysecondapplication.UI.recyclerView;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mysecondapplication.Entities.Travel;
import com.example.mysecondapplication.R;
import com.example.mysecondapplication.UI.Fragments.FragmentsVM;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;



    public class ListAdpterHistory extends RecyclerView.Adapter<com.example.mysecondapplication.UI.recyclerView.ListAdpterHistory.ViewHolder>
    {
        private Travel[] listdata;
        Context context;
        Location location;
        FragmentsVM fragmentsVM;
        FragmentActivity viewModelStore;
        public FirebaseAuth mAuth;
        FirebaseUser user;
        public SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        public ListAdpterHistory(Travel[] listdata, Context context, FragmentActivity viewModelStore) {
            this.listdata = listdata;
            this.context=context;
            this.viewModelStore=viewModelStore;
        }


        @Override
        public  ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem= layoutInflater.inflate(R.layout.history_layout, parent, false);
            ViewHolder viewHolder = new ViewHolder(listItem);
            fragmentsVM = new ViewModelProvider(viewModelStore).get(FragmentsVM.class);
            mAuth = FirebaseAuth.getInstance();
            user = mAuth.getCurrentUser();
            return viewHolder;
        }


        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            location = new Location(LocationManager.GPS_PROVIDER);
            location.setLatitude(listdata[position].getPickupAddress().getLat());
            location.setLongitude(listdata[position].getPickupAddress().getLon());

            Date date;
            final Travel myListData = listdata[position];
            holder.Txt_name.setText("name : "+myListData.getClientName());

            long diff = myListData.getArrivalDate().getTime() - myListData.getTravelDate().getTime();
            holder.Txt_numDaysRide.setText("num of days: "+diff / (1000 * 60 * 60 * 24));


            holder.Btn_sendEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HashMap<String, Boolean> company=new HashMap<>();
                    String email="";
                    company = myListData.getCompany();
                    for (String name:company.keySet())
                        if(company.get(name))
                             email=name+"@gmail.com";

                    sendMail(email);
                }
            });
            holder.Btn_CompanyPayd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myListData.setRequestType(Travel.RequestType.payed);
                    fragmentsVM.updateTravel(myListData);
                    Toast.makeText(context, "Data updated", Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return listdata.length;
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
             TextView Txt_name;
             TextView Txt_numDaysRide;
             Button Btn_sendEmail;
             Button Btn_CompanyPayd;
             RelativeLayout relativeLayout;
            public ViewHolder(View itemView) {
                super(itemView);

                this.Btn_sendEmail = itemView.findViewById(R.id.Btn_sendEmail2);
                this.Btn_CompanyPayd =itemView.findViewById(R.id.Btn_compantPayd);
                this.Txt_name =  itemView.findViewById(R.id.Txt_name2);
                this.Txt_numDaysRide =  itemView.findViewById(R.id.Txt_date2);
                this.relativeLayout = itemView.findViewById(R.id.relativeLayout3);
            }
        }

        private void sendMail(String email) {
            String  recipientList = email;
            String[] recipients = recipientList.split(",");

            String subject = "request to pay for app services";
            String message = "Hello, we offerd  our app services and we wait for payment  ";
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_EMAIL, recipients);
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(Intent.EXTRA_TEXT, message);
            intent.setType("message/rfc822");
            viewModelStore.startActivity(Intent.createChooser(intent, "Choose an email client"));
        }

    }
