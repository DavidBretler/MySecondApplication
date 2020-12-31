package com.example.mysecondapplication.UI.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mysecondapplication.Entities.Travel;
import com.example.mysecondapplication.R;
import com.example.mysecondapplication.UI.MyListAdapter;

import java.util.LinkedList;
import java.util.List;


public class RegisteredTravels extends Fragment {


    private FragmentsVM fragmentsVM;
    MyListAdapter adapter;
    RecyclerView recyclerView;
    Spinner spinner;
    Context context;
    Button button;
    public List<Travel> Travels;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        fragmentsVM =
                new ViewModelProvider(this).get(FragmentsVM.class);
        View root = inflater.inflate(R.layout.fragment_registered_travels, container, false);

        button=(Button) root.findViewById(R.id.Btn_changeToRun);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(v.getContext(),"click on buttune: ",Toast.LENGTH_LONG).show();
//            }
//        });{

 //       }
        context =RegisteredTravels.this.getActivity().getBaseContext();
        recyclerView = (RecyclerView) root.findViewById(R.id.user_travel_recyclerView);
        DividerItemDecoration itemDecor = new DividerItemDecoration(RegisteredTravels.this.getActivity(),1);
        recyclerView.addItemDecoration(itemDecor);


        spinner= (Spinner) root.findViewById(R.id.spinner);

        fragmentsVM.getUserTravels().observe(getViewLifecycleOwner(), new Observer<List<Travel>>() {
            @Override
            public void onChanged(List<Travel> travels) {
                Travels=new LinkedList<>(travels);

                Travel[] travelsArr = new Travel[travels.size()];
                travels.toArray(travelsArr);
                adapter = new MyListAdapter(travelsArr, context);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(RegisteredTravels.this.getActivity()));
                recyclerView.setAdapter(adapter);


            }
        });

        return root;
    }
}