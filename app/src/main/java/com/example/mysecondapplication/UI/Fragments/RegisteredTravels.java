package com.example.mysecondapplication.UI.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mysecondapplication.Entities.Travel;
import com.example.mysecondapplication.R;
import com.example.mysecondapplication.UI.MyListAdapter;

import java.util.List;


public class RegisteredTravels extends Fragment {

    private FragmentsVM fragmentsVM;
    MyListAdapter adapter;
    RecyclerView recyclerView;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        fragmentsVM =
                new ViewModelProvider(this).get(FragmentsVM.class);
        View root = inflater.inflate(R.layout.fragment_registered_travels, container, false);
        final TextView textView = root.findViewById(R.id.text_home);


        recyclerView = (RecyclerView) root.findViewById(R.id.user_travel_recyclerView);

//        MyListData[] myListData = new MyListData[] {
//                new MyListData("Email")
//
//        };



        fragmentsVM.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        fragmentsVM.getUserTravels().observe(getViewLifecycleOwner(), new Observer<List<Travel>>() {
            @Override
            public void onChanged(List<Travel> travels) {

                Travel[] travelsArr = new Travel[travels.size()];
                travels.toArray(travelsArr);
                adapter = new MyListAdapter((travelsArr));
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(RegisteredTravels.this.getActivity()));
                recyclerView.setAdapter(adapter);
                Toast.makeText(getActivity(), "name is :"+travels.get(0).getClientName(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }
}