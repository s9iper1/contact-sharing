package com.byteshaft.contactsharing;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.byteshaft.contactsharing.utils.AppGlobals;
import com.byteshaft.contactsharing.utils.Helpers;
import com.byteshaft.contactsharing.utils.WebServiceHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;

public class RegisterActivity extends AppCompatActivity {

    private Button mRegisterButton;
    private EditText mFullName;
    private EditText mEmailAddress;
    private EditText mPassword;

    private String mFullNameString;
    private String mEmail;
    private String mPasswordEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFullName = (EditText) findViewById(R.id.full_name);
        mEmailAddress = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);
        mRegisterButton = (Button) findViewById(R.id.register_button);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(validateEditText());
                if (!validateEditText()) {
                    Toast.makeText(getApplicationContext(), "invalid credentials",
                            Toast.LENGTH_SHORT).show();
                } else {
                    new RegistrationTask().execute();
                }

            }
        });
    }

    private boolean validateEditText() {

        boolean valid = true;
        mFullNameString = mFullName.getText().toString();
        mPasswordEntry = mPassword.getText().toString();
        mEmail = mEmailAddress.getText().toString();

        if (mFullNameString.trim().isEmpty() || mFullNameString.length() < 3) {
            mFullName.setError("enter at least 3 characters");
            valid = false;
        } else {
            mFullName.setError(null);
        }

        if (mPasswordEntry.trim().isEmpty() || mPasswordEntry.length() < 4) {
            mPassword.setError("enter at least 4 characters");
            valid = false;
        } else {
            mPassword.setError(null);
        }

        if (mEmail.trim().isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()) {
            mEmailAddress.setError("please provide a valid email");
            valid = false;
        } else {
            mEmailAddress.setError(null);
        }
        return valid;
    }

    class RegistrationTask extends AsyncTask<String, String, String> {

        private boolean noInternet = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            WebServiceHelper.showProgressDialog(RegisterActivity.this, "Registering");
        }

        @Override
        protected String doInBackground(String... params) {

            JSONObject jsonObject;
            if (WebServiceHelper.isNetworkAvailable() && WebServiceHelper.isInternetWorking()) {
                try {
                    jsonObject = WebServiceHelper.registerUser(mFullNameString, mEmail,mPasswordEntry);
                    if (AppGlobals.getResponseCode() == HttpURLConnection.HTTP_CREATED) {
                        System.out.println(jsonObject + "working");
                        String first_name = jsonObject.getString(AppGlobals.KEY_FULLNAME);
                        String email = jsonObject.getString(AppGlobals.KEY_EMAIL);


                        //saving values
                        Helpers.saveDataToSharedPreferences(AppGlobals.KEY_FULLNAME, first_name);
                        Log.i("Full name", " " + Helpers.getStringFromSharedPreferences(AppGlobals.KEY_FULLNAME));
                        Helpers.saveDataToSharedPreferences(AppGlobals.KEY_EMAIL, email);

                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            } else {
                noInternet = true;
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            WebServiceHelper.dismissProgressDialog();
            if (noInternet) {
                Helpers.alertDialog(RegisterActivity.this, "Connection error",
                        "Check your internet connection");
            }
            if (AppGlobals.getResponseCode() == HttpURLConnection.HTTP_CREATED) {
                Toast.makeText(AppGlobals.getContext(),
                        "Account Created Successfully",
                        Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            } else if (AppGlobals.getResponseCode() == HttpURLConnection.HTTP_BAD_REQUEST) {
                Toast.makeText(AppGlobals.getContext(), "Registration failed. Email already in use",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
