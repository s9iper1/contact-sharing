package com.byteshaft.contactsharing;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.byteshaft.contactsharing.utils.AppGlobals;
import com.byteshaft.contactsharing.utils.Helpers;
import com.byteshaft.contactsharing.utils.WebServiceHelper;

import org.json.JSONException;

import java.io.IOException;
import java.net.HttpURLConnection;

public class CodeConfirmationActivity extends Activity {

    private Button mSubmitButton;
    private EditText mEmail;
    private EditText mCode;

    private String mConfirmationEmail;
    private String mConformationCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirmation_code_activity);
        mEmail = (EditText) findViewById(R.id.et_confirmation_code_email);
        mCode = (EditText) findViewById(R.id.et_confirmation_code);
        mSubmitButton = (Button) findViewById(R.id.btn_confirmation_code_submit);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConfirmationEmail = mEmail.getText().toString();
                mConformationCode = mCode.getText().toString();
                System.out.println(mConfirmationEmail);
                System.out.println(mConformationCode);
                if (validateConfirmationCode()) {
                    new UserConfirmationTask().execute();
                }
            }
        });
        String email = Helpers.getStringFromSharedPreferences(AppGlobals.KEY_EMAIL);
        mEmail.setText(email);
        mConfirmationEmail = RegisterActivity.mEmail;
    }

    public boolean validateConfirmationCode() {
        boolean valid = true;
        if (mConformationCode.isEmpty() || mConformationCode.length() < 4) {
            mCode.setError("Minimum 4 Characters");
            valid = false;
        } else {
            mCode.setError(null);
        }
        return valid;
    }

    private class UserConfirmationTask extends AsyncTask<String, Integer, String> {

        private boolean noInternet = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            WebServiceHelper.showProgressDialog(CodeConfirmationActivity.this, "Activating User");

        }

        @Override
        protected String doInBackground(String... params) {
            int jsonObject;
            if (WebServiceHelper.isNetworkAvailable() && WebServiceHelper.isInternetWorking()) {

                try {
                    jsonObject = WebServiceHelper.ActivationCodeConfirmation(mConfirmationEmail
                            , mConformationCode);
                    System.out.println(jsonObject + "okay");

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            } else {
                noInternet = true;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String aString) {
            super.onPostExecute(aString);
            WebServiceHelper.dismissProgressDialog();
            if (noInternet) {
                Helpers.alertDialog(CodeConfirmationActivity.this, "Connection error",
                        "Check your internet connection");
            }
            if (AppGlobals.getResponseCode() == HttpURLConnection.HTTP_OK) {
                Toast.makeText(AppGlobals.getContext(),
                        "Confirmation successful",
                        Toast.LENGTH_LONG).show();
                finish();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                Toast.makeText(CodeConfirmationActivity.this, "please login with your account details",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(AppGlobals.getContext(),
                        "Confirmation failed, check internet and retry", Toast.LENGTH_LONG).show();
            }
        }
    }
}
