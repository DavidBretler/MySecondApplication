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

/**
 * the  fragment displayes to owner of the app all the travels that are closed
 */
public class HistoryTravels extends Fragment {
    private FragmentsVM fragmentsVM;
    Context context;
    RecyclerView recyclerView;
    ListAdpterHistory adapter;
    public List<Travel> Travels;

    /**
     * the on create is activted whean opening history trvel fragment
     * only alwoes owner off app to enter
     * @param inflater xml object
     * @param container contains other views
     * @param savedInstanceState maping from string to parcble
     * @return the root that contain the inflater and the container
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        fragmentsVM = new ViewModelProvider(this).get(FragmentsVM.class);
        View root = inflater.inflate(R.layout.fragment_history_travels, container, false);

        checkOwner(fragmentsVM.getUserEmail());
        context =HistoryTravels.this.getActivity().getBaseContext();
        recyclerView = (RecyclerView) root.findViewById(R.id.list_travel_history);
        //make lines between layout in list
        DividerItemDecoration itemDecor = new DividerItemDecoration(HistoryTravels.this.getActivity(),1);
        recyclerView.addItemDecoration(itemDecor);



        fragmentsVM.gethistoryTravels().observe(getViewLifecycleOwner(), new Observer<List<Travel>>() {
            /**
             * every change to the travel list in fire base will activate this func
             * @param travels -the updated list of travels
             */
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

    /**
     *   verify that only the manger of app will see this data-owner credentials are stored in recourse
     * @param email a string containg the of the user trying to access the fragment
     */
    private void checkOwner(String email) {
        if(!email.equals(getString(R.string.owner_Email)) ) {
            Toast.makeText(getActivity(), "only Owner can access this information " , Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }
    }

}