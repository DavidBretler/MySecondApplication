package com.example.mysecondapplication.UI.Fragments;


import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.mysecondapplication.Entities.Travel;
import com.example.mysecondapplication.Entities.UserLocation;
import com.example.mysecondapplication.R;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class AddNewTravel extends Fragment {

    private static final int NUM_OF_FILDES = 8 ;
    private Travel travel;
    private List<EditText> fieldsArr = new ArrayList<EditText>(NUM_OF_FILDES);
    private List<UserLocation> destAddressArr = new ArrayList<UserLocation>();
    UserLocation userLocation =new UserLocation();
    DatePickerDialog picker;
    EditText TxtDepartingDate;
    EditText TxtReturnDate;
    private FragmentsVM fragmentsVM;
    Button BtnSaveTravelRequest;
    Location travelLocation;
    UserLocation destAddress;
    boolean vipbus;
    Context contex;
    View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        contex =this.getActivity();
        super.onCreate(savedInstanceState);
         root = inflater.inflate(R.layout.fragment_addtravel, container, false);
        //set buttons color
        BtnSaveTravelRequest =  root.findViewById(R.id.Btn_saveTravelRequest);
        BtnSaveTravelRequest.setBackgroundColor(Color.BLUE);

        fragmentsVM = new ViewModelProvider(this).get(FragmentsVM.class);
        fragmentsVM.getTravelUpdate().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override//check if travel been add to the firebase data
            public void onChanged(Boolean flag) {
                if (flag)
                    success();
                else
                    failed();

            }
        });

        ///array of input fields
        fieldsArr.add(0, root.findViewById(R.id.Txt_Email));
        fieldsArr.add(1, root.findViewById(R.id.Txt_phone));
        fieldsArr.add(2, root.findViewById(R.id.Txt_NumPassengers));
        fieldsArr.add(3, root.findViewById(R.id.Txt_name));
        fieldsArr.add(4, root.findViewById(R.id.Txt_pickupAddress));
        fieldsArr.add(5, root.findViewById(R.id.Txt_destAddress));
        fieldsArr.add(6, root.findViewById(R.id.Txt_departingDate));
        fieldsArr.add(7, root.findViewById(R.id.Txt_returnDate));

         //put user email in filde
        fieldsArr.get(0).setText(fragmentsVM.getUserEmail());

        TxtDepartingDate =  fieldsArr.get(6);
        TxtReturnDate =  fieldsArr.get(7);

        TxtDepartingDate.setHint("choose departing Date");
        TxtReturnDate.setHint("choose return Date");


        BtnSaveTravelRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    saveTravelRequest(v);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        TxtDepartingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              chooseDate(v);
            }
        });
        TxtReturnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseDate(v);
            }
        });

        //set edit date text as type null so keyboard wont open

        TxtDepartingDate.setInputType(InputType.TYPE_NULL);

        TxtReturnDate.setInputType(InputType.TYPE_NULL);

        return root;
    }

    /**
     * opens calender to pick the dates and insert the chosen date to the correct edit text
     * @param Etextview the edit text that been prassed
     */
    public void chooseDate(View Etextview) {

        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        picker = new DatePickerDialog(contex,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        //choose the date edit text that been press to enter the date in it
                        TxtDepartingDate = (EditText)Etextview;
                        TxtDepartingDate.setText(year+"-"+monthOfYear+"-"+dayOfMonth);
                    }
                }, year, month, day);
        picker.show();
    }


    public void success() {
        Toast.makeText(contex, "Travel saved successfully  ", Toast.LENGTH_LONG).show();
    }

    public void failed() {
        Toast.makeText(contex, "Travel not saved successfully  ", Toast.LENGTH_LONG).show();
    }

    /**
     * add the pickupp destanation address from the edit text to the user location list and empty the edit text
     * @param view
     */
    public void addAddress(View view) {
        try {


            if (fieldsArr.get(5).getText().toString().isEmpty())
                Toast.makeText(contex, "enter a destination address", Toast.LENGTH_LONG).show();
            else {
                LocationFromString(fieldsArr.get(5).getText().toString().trim());
                destAddress = userLocation.convertFromLocation(travelLocation);
                destAddressArr.add(destAddress);

                fieldsArr.get(5).setText("");
            }
        } catch (Exception e) {
            Toast.makeText(contex, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * get all the travel details from the grafic object,convert where needed according to travel contractor
     or add to exist list, make new travel with the details
     * @param view
     * @throws ParseException
     */




    //Approve Company to provide travel servi
    public void saveTravelRequest(View view) throws ParseException {
        try {


            if (confirmInput(view)) {
                Date departingDate = new Travel.DateConverter().fromTimestamp(fieldsArr.get(6).getText().toString().trim());
                Date returnDate = new Travel.DateConverter().fromTimestamp(fieldsArr.get(7).getText().toString().trim());
                int NumPassengers = Integer.parseInt(fieldsArr.get(2).getText().toString().trim());

                LocationFromString(fieldsArr.get(4).getText().toString().trim());
                UserLocation pickupAddress = userLocation.convertFromLocation(travelLocation);

                LocationFromString(fieldsArr.get(5).getText().toString().trim());
                destAddress = userLocation.convertFromLocation(travelLocation);
                destAddressArr.add(destAddress);
                vipbus = ((CheckBox) root.findViewById(R.id.CBvipbus)).isChecked();

                HashMap<String, Boolean> company;
                company=new HashMap<String, Boolean>();
                company.put("Eged",false);
                travel = new Travel(fieldsArr.get(3).getText().toString().trim(), fieldsArr.get(1).getText().toString().trim()
                        , fieldsArr.get(0).getText().toString().trim(), departingDate, returnDate, NumPassengers, pickupAddress,
                        destAddressArr.get(0), Travel.RequestType.accepted, vipbus,company);
                addTravel(travel);
                destAddressArr.clear();
            }
        } catch (Exception e) {
            Toast.makeText(contex, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    /**
     * convert from the address string to location object
     * @param value
     */
    public void LocationFromString(String value) throws Exception {


        try {
            Geocoder geocoder =new Geocoder(contex);
            List<Address> l = geocoder.getFromLocationName(value,1);
            if (!l.isEmpty()) {
                Address temp = l.get(0);
                travelLocation = new Location("travelLocation");
                travelLocation.setLatitude(temp.getLatitude());
                travelLocation.setLongitude(temp.getLongitude());

            } else {
                Exception exp=new Exception("Unable to understand address");
                throw (exp);
                // "4:Unable to understand address", Toast.LENGTH_LONG).show();

            }



        } catch (Exception e) {
            throw (e);
        }

    }

    public void addTravel(Travel travel) {
        fragmentsVM.addTravel(travel);

    }

    /**
     * confirm validation of all tha details from the user
     * @param v
     * @return if all details valid
     */
    public boolean confirmInput(View v) {
        boolean flag = true;
        for (EditText field : fieldsArr) {
            if (!validation(field))
                flag = false;
        }

        return flag;
    }
    private boolean validation(EditText InputId) {
        String Input = InputId.getText().toString().trim();
        if (Input.isEmpty()) {
            InputId.setError("Field can't be empty");
            return false;
        } else//check if the field need a specific validation
            switch (fieldsArr.indexOf(InputId)) {
                case 0:
                    return validateEmail();

                case 1:
                    return validatePhone();

                case 3:
                    return validateName();

                default: {
                    InputId.setError(null);
                    return true;
                }
            }
    }

    private boolean validateEmail() {
        String emailInput = fieldsArr.get(0).getText().toString().trim();

        if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            fieldsArr.get(0).setError("Please enter a valid email address");
            return false;
        } else {
            fieldsArr.get(0).setError(null);
            return true;
        }
    }
    private boolean validatePhone() {
        String phoneInput = fieldsArr.get(1).getText().toString().trim();
        if (!Patterns.PHONE.matcher(phoneInput).matches()) {
            fieldsArr.get(1).setError("Please enter a valid phone number");
            return false;
        } else {
            fieldsArr.get(1).setError(null);
            return true;
        }
    }
    private boolean validateName() {
        String nameInput = fieldsArr.get(3).getText().toString().trim();
        if (nameInput.matches(".*\\d.*")) {
            fieldsArr.get(3).setError("Please enter a valid name");
            return false;
        } else {
            fieldsArr.get(3).setError(null);
            return true;
        }
    }


}
