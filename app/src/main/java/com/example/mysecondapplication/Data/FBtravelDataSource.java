package com.example.mysecondapplication.Data;



import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.mysecondapplication.Entities.Travel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * gets the info from firebase to app and from app to firebase
 */
public class FBtravelDataSource implements IFBtravelDataSource {

    private static final String TAG = "Firebase";

    private MutableLiveData<Boolean> isSuccess= new MutableLiveData<>();
    private List<Travel> allTravelsList;

    private NotifyToTravelListListener notifyToTravelListListener;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference travels = firebaseDatabase.getReference("ExistingTravels");



    private static FBtravelDataSource instance;

    public static FBtravelDataSource getInstance() {
        if (instance == null)
            instance = new FBtravelDataSource();
        return instance;
    }


    private FBtravelDataSource() {
        allTravelsList = new ArrayList<>();
        travels.addValueEventListener(new ValueEventListener() {
            @Override
            //listener to changes in firebase
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allTravelsList.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Travel travel = snapshot.getValue(Travel.class);
                        allTravelsList.add(travel);
                    }
                }
                if (notifyToTravelListListener != null)
                    notifyToTravelListListener.onTravelsChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public void setNotifyToTravelListListener(NotifyToTravelListListener l) {
        notifyToTravelListListener = l;
    }


    @Override
    public void addTravel(Travel p) {
        String id = travels.push().getKey();
        p.setTravelId(id);
        travels.child(id).setValue(p).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                isSuccess.setValue(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                isSuccess.setValue(false);
            }
        });
    }


    public  void removeTravel(String id) {
        travels.child(id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.e(TAG, "Travel Removed");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Failure removing Travel");
            }
        });
    }


    @Override
    public void updateTravel(final Travel toUpdate) {
     //   removeTravel(toUpdate.getTravelId());
    //    addTravel(toUpdate);
        travels.child(toUpdate.getTravelId()).setValue(toUpdate);
    }

    @Override
    public List<Travel> getAllTravels() {
        return allTravelsList;
    }

    public MutableLiveData<Boolean> getIsSuccess() {
        return isSuccess;
    }
}
