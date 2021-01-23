package com.example.mysecondapplication.UI.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mysecondapplication.Entities.Travel;
import com.example.mysecondapplication.*;
import com.example.mysecondapplication.UI.recyclerView.ListAdapterTravel;

import java.util.LinkedList;
import java.util.List;

/**
 * the travels requests relevant to the specific user that is now logged in the app.
 * displays the travel requests that are not closed.
 */
public class RegisteredTravels extends Fragment {


    private FragmentsVM fragmentsVM;
    ListAdapterTravel adapter;
    RecyclerView recyclerView;
    Context context;
    public List<Travel> Travels;

    /**
     *creates instance of view model
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        fragmentsVM = new ViewModelProvider(this).get(FragmentsVM.class);

        View root = inflater.inflate(R.layout.fragment_registered_travels, container, false);


        context =RegisteredTravels.this.getActivity().getBaseContext();
        recyclerView = (RecyclerView) root.findViewById(R.id.company_travel_recyclerView);
        //make lines between layout in the graphic list
        DividerItemDecoration itemDecor = new DividerItemDecoration(RegisteredTravels.this.getActivity(),1);
        recyclerView.addItemDecoration(itemDecor);
        //  get all the travels that of the user from the view model and insert into the graphic list
        //  react to changes
        fragmentsVM.getUserTravels().observe(getViewLifecycleOwner(), new Observer<List<Travel>>() {
            @Override
            public void onChanged(List<Travel> travels) {
                Travels=new LinkedList<>(travels);

                Travel[] travelsArr = new Travel[travels.size()];
                travels.toArray(travelsArr);
                adapter = new ListAdapterTravel(travelsArr, context,requireActivity());
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(RegisteredTravels.this.getActivity()));
                recyclerView.setAdapter(adapter);

            }
        });

    return  root;

    }
}