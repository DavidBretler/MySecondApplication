package com.example.mysecondapplication.UI.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.mysecondapplication.Entities.Travel;
import com.example.mysecondapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;


public class HistoryTravels extends Fragment {
    public FirebaseAuth mAuth;
    private FragmentsVM fragmentsVM;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        fragmentsVM = new ViewModelProvider(this).get(FragmentsVM.class);
        View root = inflater.inflate(R.layout.fragment_history_travels, container, false);
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
            checkOwner(user.getEmail());

        final TextView textView = root.findViewById(R.id.text_slideshow);

        fragmentsVM.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        fragmentsVM.gethistoryTravels().observe(getViewLifecycleOwner(), new Observer<List<Travel>>() {
            @Override
            public void onChanged(List<Travel> travels) {
                Toast.makeText(getActivity(), "size is :"+travels.size(),
                        Toast.LENGTH_SHORT).show();
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