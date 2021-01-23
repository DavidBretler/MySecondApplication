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

import com.example.mysecondapplication.Entities.UserLocation;
import com.example.mysecondapplication.R;
import com.example.mysecondapplication.UI.NavigationDrawer.NavigationDrawer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * the entering  screen of the app
 * requires signing up and logging into app via firebase authentication with email and password
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
        public FirebaseAuth mAuth;// handler to firebase service's
        private EditText mEmailField;
        private EditText mPasswordField;
        private Button Btn_signIn;
        private Button Btn_signUp;
        private Button Btn_verify_email;
        private Button Btn_use_last_user_details;
        private ProgressDialog mProgressDialog;
        private TextView Txt_welcome;
        SharedPreferences sharedpreferences;//save the user details
        private String regex = "^(?=.*[0-9])"//string to validate password
                + "(?=.*[a-z])(?=.*[A-Z])"
                + "(?=\\S+$)." +
                "{8,20}$";

    /**
     * initializing the logging in view
     * @param savedInstanceState
     */
    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);

            sharedpreferences = getSharedPreferences("USER", Context.MODE_PRIVATE);//create instance of shared preferences to save user details

            mAuth = FirebaseAuth.getInstance();
            // linking the variables the the graphic objects
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



    }

    /**
     * paring the clicks of various  buttons to the wanted function
     * @param v
     */
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

    /**
     *  sign in the user to the app with firebase authentication with email and password
     */
        private void signIn() {
            if (!validateForm()) { //check validation of the user fields
                return;
            }
             showProgressDialog();
            // save the user details to use next time
            String email = mEmailField.getText().toString();
            String password = mPasswordField.getText().toString();
            final FirebaseUser user = mAuth.getCurrentUser();
           // Listener to see if sing in  successful
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d("MainActivity", "signIn:onComplete:" + task.isSuccessful());
                                hideProgressDialog();
                                if (user.isEmailVerified())
                                    //check if user has valid his email address if not he wont be able to go to next screen
                                {
                                    if (task.isSuccessful()) {//check that the Sign In was successful
                                        saveUserDetails(email,password);
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

    /**
     * pass the user to the NavigationDrawer screen
     * @param email the user email
     */
    private void goToNavigationDrawer(String email){
             Intent i = new Intent(this, NavigationDrawer.class );
             i.putExtra("email",email);
             startActivity(i);

         }

    /**
     * create new account to the user with firebase authentication with email and password
     */
    private void newAccount() {
            if (!validateForm()) {//check validation of the user fields
                return;
            }
            showProgressDialog();
            // save the user details to use next time
            String email = mEmailField.getText().toString();
            String password = mPasswordField.getText().toString();
            // create User With Email And Password , Listener to see if work successful
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d("MainActivity", "createUser:onComplete:" + task.isSuccessful());
                            hideProgressDialog();
                            if (task.isSuccessful()) {//check  if creation was successful
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

    /**
     * save the user details to next use
     * @param email the user email
     * @param passward the user passward
     */
        private void saveUserDetails(String email ,String passward){
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("Email", email);
            editor.putString("Password", passward);
            editor.commit();
        }
    /**
     * get the user details from last use
     */
        public void fillUserDetails() {
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

    /**
     * send Email Verification to the user
     */
        private void sendEmailVerification() {
            // Disable Verify Email button
            findViewById(R.id.Btn_verify_email).setEnabled(false);
            final FirebaseUser user = mAuth.getCurrentUser();
            user.sendEmailVerification()//Listener to see if email sent successfully
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

    /**
     * @return if the user details are valid - in the wanted Pattern
     */
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

    }