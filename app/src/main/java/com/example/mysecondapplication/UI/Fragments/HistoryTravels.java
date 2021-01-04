package com.example.mysecondapplication.UI.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mysecondapplication.Entities.Travel;
import com.example.mysecondapplication.R;
import com.example.mysecondapplication.UI.recyclerView.ListAdapterCompany;
import com.example.mysecondapplication.UI.recyclerView.ListAdpterHistory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.LinkedList;
import java.util.List;


public class HistoryTravels extends Fragment {
    public FirebaseAuth mAuth;
    private FragmentsVM fragmentsVM;
    Context context;
    RecyclerView recyclerView;
    ListAdpterHistory adapter;
    public List<Travel> Travels;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        fragmentsVM = new ViewModelProvider(this).get(FragmentsVM.class);
        View root = inflater.inflate(R.layout.fragment_history_travels, container, false);
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
            checkOwner(user.getEmail());
        context =HistoryTravels.this.getActivity().getBaseContext();
        recyclerView = (RecyclerView) root.findViewById(R.id.list_travel_history);
        //make lines between layout in list
        DividerItemDecoration itemDecor = new DividerItemDecoration(HistoryTravels.this.getActivity(),1);
        recyclerView.addItemDecoration(itemDecor);



        fragmentsVM.gethistoryTravels().observe(getViewLifecycleOwner(), new Observer<List<Travel>>() {
            @Override
            public void onChanged(List<Travel> travels) {
                Travels=new LinkedList<>(travels);
                Travel[] travelsArr = new Travel[travels.size()];
                travels.toArray(travelsArr);
                adapter = new ListAdpterHistory(travelsArr, context,requireActivity());
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(HistoryTravels.this.getActivity()));
                recyclerView.setAdapter(adapter);
            }
        });

        return root;
    }

    private void checkOwner(String email) {
        if(!email.equals(getString(R.string.owner_Email)) ) {
            Toast.makeText(getActivity(), "only Owner can access this information " , Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }
    }

}