package com.example.mysecondapplication.UI.Login_Activity;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mysecondapplication.R;
import com.example.mysecondapplication.UI.NavigationDrawer.NavigationDrawer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
        public FirebaseAuth mAuth;
        private EditText mEmailField;
        private EditText mPasswordField;
        private Button Btn_signIn;
        private Button Btn_signUp;
        private Button Btn_verify_email;
        private Button Btn_use_last_user_details;
        private ProgressDialog mProgressDialog;
        private TextView Txt_welcome;
        SharedPreferences sharedpreferences;
        private loginViewModel loginViewModel;
        private String regex = "^(?=.*[0-9])"//string to valid password
                + "(?=.*[a-z])(?=.*[A-Z])"
                + "(?=\\S+$)." +
                "{8,20}$";


    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);

            sharedpreferences = getSharedPreferences("USER", Context.MODE_PRIVATE);

            mAuth = FirebaseAuth.getInstance();
            // Views
            mEmailField = (EditText) findViewById(R.id.field_email);
            mPasswordField = (EditText) findViewById(R.id.field_password);
            Btn_signIn = (Button) findViewById(R.id.button_sign_in);
            Btn_signUp = (Button) findViewById(R.id.button_sign_up);
            Txt_welcome=(TextView) findViewById(R.id.Txt_welcome);
            Btn_verify_email=findViewById(R.id.Btn_verify_email);
            Btn_use_last_user_details=findViewById(R.id.user_details);
            //set objects color
            Btn_signIn.setBackgroundColor(Color.BLUE);
            Btn_signUp.setBackgroundColor(Color.BLUE);
            Txt_welcome.setTextColor(Color.BLUE);
            Btn_verify_email.setBackgroundColor(Color.RED);
            Btn_use_last_user_details.setBackgroundColor(Color.RED);
            // Click listeners
            Btn_signIn.setOnClickListener(this);
            Btn_signUp.setOnClickListener(this);
            Btn_verify_email.setOnClickListener(this);
            Btn_use_last_user_details.setOnClickListener(this);
            Btn_verify_email.setVisibility(View.GONE);

  //          checkData();
        }

//
//            try {
//                List<UserLocation> list = null;
//                String travelDate ;
//                travelDate =  "2020"+"-"+"02"+"-"+"25";
//                Date tDate = new Travel.DateConverter().fromTimestamp(travelDate);
//                if (tDate == null)
//                    throw new Exception("שגיאה בתאריך");
//
//                HashMap<String, Boolean> company =new HashMap<String, Boolean>();
//                company.put("Afikim",Boolean.FALSE);
//                company.put("SuperBus",Boolean.FALSE);
//                company.put("SmartBus",Boolean.FALSE);
//                company.put("SmartBus",Boolean.TRUE);
//
//                Travel travel1 = new Travel("Yossi","026456677","Yossi05489@gmail.com",tDate,tDate,5,
//                        new UserLocation(10.0, 20.0),list ,true,company);
//
//                travel1.setClientName("ayala");
//                loginViewModel.updateTravel(travel1);
//
////                Travel travel2 = new Travel();
////                travel2.setClientName("Ronit");
////                travel2.setClientPhone("026334512");
////                travel2.setClientEmail("RonitMarxs@gmail.com");
////                travel2.setPickupAddress(new UserLocation(15.0, 25.0));
////                travel2.setTravelDate(tDate);
////                travel2.setArrivalDate(tDate);
////                travel2.setRequestType(Travel.RequestType.sent);
////                travel2.setCompany(new HashMap<String, Boolean>());
////                travel2.getCompany().put("Egged",Boolean.FALSE);
////                travel2.getCompany().put("TsirTour",Boolean.FALSE);
////
////                loginViewModel.addTravel(travel2);
//
//
//
//            } catch (Exception e) {
//                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_sign_in:
                    signIn();
                    break;
                case R.id.button_sign_up:
                    newAccount();
                    break;
                case  R.id.Btn_verify_email:
                    sendEmailVerification();
                    break;
                case R.id.user_details:
                    fillUserDetails();
            }
        }

        private void signIn() {
            Log.d("MainActivity", "signIn");
            if (!validateForm()) {
                return;
            }
             showProgressDialog();
            String email = mEmailField.getText().toString();
            String password = mPasswordField.getText().toString();
            final FirebaseUser user = mAuth.getCurrentUser();


                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d("MainActivity", "signIn:onComplete:" + task.isSuccessful());
                                hideProgressDialog();
                                if (user.isEmailVerified())
                                {
                                  //  Toast.makeText(getApplicationContext(), "email verifid Successfuly", Toast.LENGTH_SHORT).show();
                                    if (task.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this, "Sign In Successful",
                                                Toast.LENGTH_SHORT).show();
                                        goToNavigationDrawer(email);

                                    } else {
                                        Toast.makeText(LoginActivity.this, "Sign In Failed",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            else
                                Toast.makeText(getApplicationContext(), "Sign In Failed, make sure your email have been verified", Toast.LENGTH_SHORT).show();
                            }
                        });
        }
         private void goToNavigationDrawer(String email){
             Intent i = new Intent(this, NavigationDrawer.class );
             i.putExtra("email",email);
             startActivity(i);

         }
        private void newAccount() {
            Log.d("MainActivity", "signUp");
            if (!validateForm()) {
                return;
            }
            showProgressDialog();
            String email = mEmailField.getText().toString();
            String password = mPasswordField.getText().toString();
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d("MainActivity", "createUser:onComplete:" + task.isSuccessful());
                            hideProgressDialog();
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Successful! Please verify your email to Sign In",
                                        Toast.LENGTH_SHORT).show();
                                    saveUserDetails(email,password);
                                Btn_verify_email.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(LoginActivity.this, "Sign Up Failed",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        private void saveUserDetails(String email ,String passward){

            SharedPreferences.Editor editor = sharedpreferences.edit();


            editor.putString("Email", email);
            editor.putString("Password", passward);
            editor.commit();
        }
        public void fillUserDetails() {
            //   result.setText("Name is "+sharedpreferences.getString("Email","No name")+" Address "+ sharedpreferences.getString("Password","No Password"));
         //   Toast.makeText(LoginActivity.this, ("Name is "+sharedpreferences.getString("Email","No Email")+" Address "+ sharedpreferences.getString("Password","No Password"))
        //            ,  Toast.LENGTH_SHORT).show();
            mEmailField.setText(sharedpreferences.getString("Email","No Email"));
            mPasswordField.setText(sharedpreferences.getString("Password","No Password"));
        }
        public void showProgressDialog() {
            if (mProgressDialog == null) {
                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setCancelable(false);
                mProgressDialog.setMessage("Loading...");
            }
            mProgressDialog.show();
        }
        public void hideProgressDialog() {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        }


        ActionCodeSettings actionCodeSettings =
                ActionCodeSettings.newBuilder()
                        // URL you want to redirect back to. The domain (www.example.com) for this
                        // URL must be whitelisted in the Firebase Console.
                        .setUrl("https://www.example.com/finishSignUp?cartId=1234")
                        // This must be true
                        .setHandleCodeInApp(true)
                        .setIOSBundleId("com.example.ios")
                        .setAndroidPackageName(
                                "com.example.android",
                                true, /* installIfNotAvailable */
                                "12"    /* minimumVersion */)
                        .build();


        private void sendEmailVerification() {
            // Disable Verify Email button
            findViewById(R.id.Btn_verify_email).setEnabled(false);
            final FirebaseUser user = mAuth.getCurrentUser();
            user.sendEmailVerification()
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            // Re-enable Verify Email button
                            findViewById(R.id.Btn_verify_email).setEnabled(true);
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Verification email sent to " + user.getEmail(), Toast.LENGTH_SHORT).show();
                                if (user.isEmailVerified())
                                {
                                    // user is verified, so you can finish this activity or send user to activity which you want.
                                   //  finish();
                                    Toast.makeText(getApplicationContext(), "email verifid Successfuly", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Log.e("error", "sendEmailVerification failed!", task.getException());
                                Toast.makeText(getApplicationContext(), "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

        private boolean validateForm() {
            boolean result = true;
            if (    TextUtils.isEmpty(mEmailField.getText().toString()) ||
                    (!Patterns.EMAIL_ADDRESS.matcher(mEmailField.getText().toString()).matches()))  {
                mEmailField.setError("Required valid email");
                result = false;
            } else {
                mEmailField.setError(null);
            }
            if (TextUtils.isEmpty(mPasswordField.getText().toString())
                    ||!mPasswordField.getText().toString().matches(regex)) {
                mPasswordField.setError("Required valid password");
                result = false;
            } else {
                mPasswordField.setError(null);
            }
            return result;
        }
//        @Override
//        public boolean onCreateOptionsMenu(Menu menu) {
//            // Inflate the menu; this adds items to the action bar if it is present.
//            getMenuInflater().inflate(R.menu.menu_main, menu);
//            return true;
//        }
//        @Override
//        public boolean onOptionsItemSelected(MenuItem item) {
//            // Handle action bar item clicks here. The action bar will
//            // automatically handle clicks on the Home/Up button, so long
//            // as you specify a parent activity in AndroidManifest.xml.
//            int id = item.getItemId();
//            //noinspection SimplifiableIfStatement
//         //   if (id == R.id.action_settings) {
//                return true;
//            }
//        //    return super.onOptionsItemSelected(item);
//        }

    }