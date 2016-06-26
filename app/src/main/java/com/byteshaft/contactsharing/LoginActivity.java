package com.byteshaft.contactsharing;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.byteshaft.contactsharing.utils.AppGlobals;
import com.byteshaft.contactsharing.utils.Helpers;
import com.byteshaft.contactsharing.utils.WebServiceHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mLoginButton;
    private EditText mEmailAddress;
    private EditText mPassword;
    private TextView mSignUpText;
    private TextView mForgotPasswordTextView;


    private String mEmail;
    private String mPasswordEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmailAddress = (EditText) findViewById(R.id.email_address);
        mPassword = (EditText) findViewById(R.id.password_entry);
        mLoginButton = (Button) findViewById(R.id.login);
        mSignUpText = (TextView) findViewById(R.id.signup_text);
        mForgotPasswordTextView = (TextView) findViewById(R.id.tv_login_forgot_password);
        mLoginButton.setOnClickListener(this);
        mSignUpText.setOnClickListener(this);
        mForgotPasswordTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login:
                // TODO: 09/06/2016
                System.out.println("login");
                System.out.println(validateEditText());
                if (!validateEditText()) {
                    Toast.makeText(getApplicationContext(), "invalid credentials",
                            Toast.LENGTH_SHORT).show();
                } else {
                    new LoginTask().execute();
                }
                break;
            case R.id.signup_text:
                // TODO: 09/06/2016
                System.out.println("sign up");
                startActivity(new Intent(AppGlobals.getContext(), RegisterActivity.class));
                break;
            case R.id.tv_login_forgot_password:
                System.out.println("forgot password");
                startActivity(new Intent(AppGlobals.getContext(), ForgotPasswordActivity.class));
                break;
        }
    }

    private boolean validateEditText() {

        boolean valid = true;
        mPasswordEntry = mPassword.getText().toString();
        mEmail = mEmailAddress.getText().toString();

        if (mPasswordEntry.trim().isEmpty() || mPasswordEntry.length() < 4) {
            mPassword.setError("must contain 4 character");
            valid = false;
        } else {
            mPassword.setError(null);
        }

        if (mEmail.trim().isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()) {
            mEmailAddress.setError("invalid email");
            valid = false;
        } else {
            mEmailAddress.setError(null);
        }
        return valid;
    }

    class LoginTask extends AsyncTask<String, String, String> {

        private boolean noInternet = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            WebServiceHelper.showProgressDialog(LoginActivity.this, "LoggingIn");
        }

        @Override
        protected String doInBackground(String... params) {
            String data = null;
              if (WebServiceHelper.isNetworkAvailable() && WebServiceHelper.isInternetWorking()) {
                try {
                    data = WebServiceHelper.userLogin(mEmail, mPasswordEntry);
                    System.out.println(data + "working");
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            } else {
                noInternet = true;
            }
            return data;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            WebServiceHelper.dismissProgressDialog();
            if (noInternet) {
                Helpers.alertDialog(LoginActivity.this, "Connection error",
                        "Check your internet connection");
            } else if (AppGlobals.getResponseCode() == HttpURLConnection.HTTP_OK) {
                new GetUserDataTask().execute();
                Helpers.saveDataToSharedPreferences(AppGlobals.KEY_USER_TOKEN, response);
                Log.i("Token", " " + Helpers.getStringFromSharedPreferences(AppGlobals.KEY_USER_TOKEN));
                Helpers.saveUserLogin(true);
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }else {
                Toast.makeText(AppGlobals.getContext(), "Login Failed! Invalid Email or Password",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    class GetUserDataTask extends AsyncTask<Void, String, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            JSONObject jsonObject;

            try {
                jsonObject = WebServiceHelper.userData();
                if (AppGlobals.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    System.out.println(jsonObject + "userData");
                    String full_name = jsonObject.getString(AppGlobals.KEY_FULLNAME);
                    String email = jsonObject.getString(AppGlobals.KEY_EMAIL);

                    //saving values
                    Helpers.saveDataToSharedPreferences(AppGlobals.KEY_FULLNAME, full_name);
                    Log.i("First name", " " + Helpers.getStringFromSharedPreferences(AppGlobals.KEY_FULLNAME));
                    Helpers.saveDataToSharedPreferences(AppGlobals.KEY_EMAIL, email);
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
