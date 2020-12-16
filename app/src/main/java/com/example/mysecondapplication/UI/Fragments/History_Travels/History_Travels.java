package com.example.mysecondapplication.UI.Fragments.History_Travels;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.mysecondapplication.R;
import com.example.mysecondapplication.UI.NavigationDrawer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class History_Travels extends Fragment {
    public FirebaseAuth mAuth;
    private HistoryTravelsVM historyTravelsVM;
    private TextView Txt_welcomeUser;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        historyTravelsVM = new ViewModelProvider(this).get(HistoryTravelsVM.class);
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
            checkOwner(user.getEmail());

        final TextView textView = root.findViewById(R.id.text_slideshow);
        historyTravelsVM.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
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