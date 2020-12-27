package com.example.mysecondapplication.UI.Login_Activity;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.mysecondapplication.Data.Repository.ITravelRepository;
import com.example.mysecondapplication.Data.Repository.TravelRepository;
import com.example.mysecondapplication.Entities.Travel;

import java.util.List;

public class loginViewModel extends AndroidViewModel {


    public loginViewModel(Application p)
    {
        super(p);
    }
}
