package com.byteshaft.contactsharing;

import android.app.Activity;
import android.os.AsyncTask;

import com.byteshaft.contactsharing.utils.AppGlobals;
import com.byteshaft.contactsharing.utils.Helpers;
import com.byteshaft.contactsharing.utils.WebServiceHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class CardDetailsTask extends AsyncTask<String, String, Integer> {


        private boolean noInternet = false;

        private String mToken;
        private String mAddress;
        private String mContactNumber;
        private String mEmail;
        private String mImage;
        private String mJobTitle;
        private String mName;
        private String mOrganization;
        private String mFilepath;
        private Activity activity;

        public CardDetailsTask(Activity activity, String token, String address, String contactNumber, String email,
                               String hasimage, String jobTitle, String name, String organization,
                               String filepath) {

            mToken = token;
            mAddress = address;
            mContactNumber = contactNumber;
            mEmail = email;
            mImage = hasimage;
            mJobTitle = jobTitle;
            mName = name;
            mOrganization = organization;
            mFilepath = filepath;
            this.activity = activity;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            WebServiceHelper.showProgressDialog(activity, "Creating Card");
        }

        @Override
        protected Integer doInBackground(String... params) {

            if (WebServiceHelper.isNetworkAvailable() && WebServiceHelper.isInternetWorking()) {

                MultiPartUtility multiPartUtility;
                try {
                    multiPartUtility = new MultiPartUtility(mToken, mAddress, mContactNumber, mEmail,
                            mImage, mJobTitle, mName, mOrganization);
                    multiPartUtility.addFormField("address", params[1]);
                    multiPartUtility.addFormField("contact_number", params[2]);
                    multiPartUtility.addFormField("email", params[3]);
                    multiPartUtility.addFormField("job_title", params[5]);
                    multiPartUtility.addFormField("name", params[6]);
                    multiPartUtility.addFormField("organization", params[7]);
                    multiPartUtility.addFilePart("is_image", new File(mFilepath));

                    final byte[] bytes = multiPartUtility.finishFilesUpload();
                    try {
                        OutputStream os = new FileOutputStream(mFilepath);
                        os.write(bytes);
                    } catch (IOException e) {

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                noInternet = true;
            }
            return AppGlobals.postResponse;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            WebServiceHelper.dismissProgressDialog();
            if (noInternet) {
                Helpers.alertDialog(activity, "Connection error",
                        "Check your internet connection");
            } else if (AppGlobals.getResponseCode() == HttpURLConnection.HTTP_OK) {
//                startActivity(new Intent(getApplicationContext(), BusinessCardsList.class));
            }
        }
    }
