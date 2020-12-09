package com.example.mysecondapplication.UI.Login_Activity;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;

import android.provider.CalendarContract;
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
//import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;

    public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
        private FirebaseAuth mAuth;
        private EditText mEmailField;
        private EditText mPasswordField;
        private Button Btn_signIn;
        private Button Btn_signUp;
        private Button Btn_verify_email;
        private ProgressDialog mProgressDialog;
        private TextView Txt_welcome;
        private String regex = "^(?=.*[0-9])"//string to valid password
                + "(?=.*[a-z])(?=.*[A-Z])"
                + "(?=\\S+$)." +
                "{8,20}$";
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);


            mAuth = FirebaseAuth.getInstance();
            // Views
            mEmailField = (EditText) findViewById(R.id.field_email);
            mPasswordField = (EditText) findViewById(R.id.field_password);
            Btn_signIn = (Button) findViewById(R.id.button_sign_in);
            Btn_signUp = (Button) findViewById(R.id.button_sign_up);
            Txt_welcome=(TextView) findViewById(R.id.Txt_welcome);
            Btn_verify_email=findViewById(R.id.Btn_verify_email);

            //set objects color
            Btn_signIn.setBackgroundColor(Color.BLUE);
            Btn_signUp.setBackgroundColor(Color.BLUE);
            Txt_welcome.setTextColor(Color.BLUE);
            Btn_verify_email.setBackgroundColor(Color.RED);
            // Click listeners
            Btn_signIn.setOnClickListener(this);
            Btn_signUp.setOnClickListener(this);
            Btn_verify_email.setOnClickListener(this);
            Btn_verify_email.setVisibility(View.GONE);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_sign_in:
                    signIn();
                    break;
                case R.id.button_sign_up:
                    signUp();
                    break;
                case  R.id.Btn_verify_email:
                    sendEmailVerification();
            }
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

        private void signIn() {
            Log.d("MainActivity", "signIn");
            if (!validateForm()) {
                return;
            }
            showProgressDialog();
            String email = mEmailField.getText().toString();
            String password = mPasswordField.getText().toString();
            final FirebaseUser user = mAuth.getCurrentUser();
            if (user.isEmailVerified())
            {
                // user is verified, so you can finish this activity or send user to activity which you want.
                //  finish();
                Toast.makeText(getApplicationContext(), "email verifid Successfuly", Toast.LENGTH_SHORT).show();
            }
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d("MainActivity", "signIn:onComplete:" + task.isSuccessful());
                            hideProgressDialog();
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Sign In Successful",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LoginActivity.this, "Sign In Failed",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        private void signUp() {
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
                                Toast.makeText(LoginActivity.this, "Successful! Please Sign In",
                                        Toast.LENGTH_SHORT).show();
                                Btn_verify_email.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(LoginActivity.this, "Sign Up Failed",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
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

//        @Override
//        public boolean onCreateOptionsMenu(Menu menu) {
//            // Inflate the menu; this adds items to the action bar if it is present.
//         //   getMenuInflater().inflate(R.menu.menu_main, menu);
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