package com.example.mysecondapplication.UI;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mysecondapplication.Entities.Travel;
import com.example.mysecondapplication.R;


 public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder>{
    private Travel[] listdata;

    // RecyclerView com.example.mysecondapplication.UI.recyclerView;
    public MyListAdapter(Travel[] listdata) {
        this.listdata = listdata;
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
        holder.arrivalDateTextView.setText(listdata[position].getArrivalDate().toString());
        holder.travelDateTextView.setText(listdata[position].getTravelDate().toString());
        holder.numOfPassengerTextView.setText(Integer.toString(listdata[position].getNumOfPassenger()));

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
        public ViewHolder(View itemView) {
            super(itemView);
            this.arrivalDateTextView = (TextView) itemView.findViewById(R.id.Txt_ArrivalDate);
            this.travelDateTextView = (TextView) itemView.findViewById(R.id.Txt_TravelDate);
            this.numOfPassengerTextView = (TextView) itemView.findViewById(R.id.Txt_numPassenger);

            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout);
        }
    }
}