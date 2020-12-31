package com.example.mysecondapplication.UI;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mysecondapplication.Entities.Travel;
import com.example.mysecondapplication.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class MyListAdapter  extends RecyclerView.Adapter<MyListAdapter.ViewHolder>  {
    private Travel[] listdata;
    Context context;
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
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Travel myListData = listdata[position];
        holder.arrivalDateTextView.setText("Source :\n"+listdata[position].getPickupAddress());
        holder.travelDateTextView.setText("Destination :\n"/*+listdata[position].getPickupAddress()*/);
        holder.numOfPassengerTextView.setText("start Date:\n"+format.format(listdata[position].getTravelDate()));

//        ArrayAdapter<String> adapter= new ArrayAdapter<> (context ,android.R.layout.simple_spinner_item, new ArrayList<String>( listdata[position].getCompany().keySet()) )  ;
//        holder.spinner.setAdapter(adapter);
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
        public ViewHolder(View itemView) {
            super(itemView);
            this.arrivalDateTextView = (TextView) itemView.findViewById(R.id.Txt_ArrivalDate);
            this.travelDateTextView = (TextView) itemView.findViewById(R.id.Txt_TravelDate);
            this.numOfPassengerTextView = (TextView) itemView.findViewById(R.id.Txt_numPassenger);
           this.spinner=(Spinner) itemView.findViewById(R.id.spinner);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout);
        }
    }
}