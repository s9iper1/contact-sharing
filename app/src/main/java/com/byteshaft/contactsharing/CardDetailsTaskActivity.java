package com.byteshaft.contactsharing;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.byteshaft.contactsharing.utils.AppGlobals;
import com.byteshaft.contactsharing.utils.Helpers;
import com.byteshaft.contactsharing.utils.WebServiceHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Created by husnain on 6/15/16.
 */
public class CardDetailsTaskActivity extends AppCompatActivity {

    class CardDetailsTask extends AsyncTask<String, String, String> {

        private boolean noInternet = false;

        private String mToken;
        private String mAddress;
        private String mContactNumber;
        private String mEmail;
        private String mImage;
        private String mJobTitle;
        private String mName;
        private String mOrganization;

        public CardDetailsTask(String token, String address, String contactNumber, String email,
                               String image, String jobTitle, String name, String organization) {

            mToken = token;
            mAddress = address;
            mContactNumber = contactNumber;
            mEmail = email;
            mImage = image;
            mJobTitle = jobTitle;
            mName = name;
            mOrganization = organization;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            WebServiceHelper.showProgressDialog(CardDetailsTaskActivity.this, "Creating Details");
        }

        @Override
        protected String doInBackground(String... params) {
            int response;
            if (WebServiceHelper.isNetworkAvailable() && WebServiceHelper.isInternetWorking()) {
                try {
                    response = WebServiceHelper.businessCardData(mToken, mAddress, mContactNumber,
                            mEmail, mImage, mJobTitle, mName, mOrganization);
                    System.out.println(response);
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
                Helpers.alertDialog(CardDetailsTaskActivity.this, "Connection error",
                        "Check your internet connection");
            }
            if (AppGlobals.getResponseCode() == HttpURLConnection.HTTP_OK) {
                Toast.makeText(AppGlobals.getContext(),
                        "Card Created Successfully", Toast.LENGTH_LONG).show();
            }
        }
    }
}
