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
import java.util.ArrayList;

public class CardDetailsTask extends AsyncTask<String, String, Integer> {


    private boolean noInternet = false;
    private String mAddress;
    private String mContactNumber;
    private String mEmail;
    private String mHasImage;
    private String mJobTitle;
    private String mName;
    private String mOrganization;
    private String mFilepath;
    private int mDesignInt;
    private Activity activity;
    private String logoPath;
    private ArrayList<String> uploadingUris;


    public CardDetailsTask(Activity activity, String address, String contactNumber, String email,
                           String hasImage, String jobTitle, String name, String organization,
                           String filepath, int designInt, String logoPath) {

        mAddress = address;
        mContactNumber = contactNumber;
        mEmail = email;
        mHasImage = hasImage;
        mJobTitle = jobTitle;
        mName = name;
        mOrganization = organization;
        mFilepath = filepath;
        mDesignInt = designInt;
        this.activity = activity;
        this.logoPath = logoPath;
        uploadingUris = new ArrayList<>();

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
                multiPartUtility = new MultiPartUtility(mAddress, mContactNumber, mEmail,
                        mHasImage, mJobTitle, mName, mOrganization);
                System.out.println(multiPartUtility + "working");
                multiPartUtility.addFormField("address", mAddress);
                multiPartUtility.addFormField("contact_number", mContactNumber);
                multiPartUtility.addFormField("email", mEmail);
                multiPartUtility.addFormField("job_title", mJobTitle);
                multiPartUtility.addFormField("name", mName);
                multiPartUtility.addFormField("organization", mOrganization);
                multiPartUtility.addFormField("is_image", mHasImage);
                if (!logoPath.trim().isEmpty()) {
                    multiPartUtility.addFilePart("logo", new File(logoPath));
                    uploadingUris.add(logoPath);
                }
                multiPartUtility.addFormField("design", String.valueOf(mDesignInt));
                if (!mFilepath.trim().isEmpty()) {
                    multiPartUtility.addFilePart("image", new File(mFilepath));
                    uploadingUris.add(mFilepath);
                }

                final byte[] bytes = multiPartUtility.finish();
                for (String uri: uploadingUris) {
                    try {
                        OutputStream os = new FileOutputStream(uri);
                        os.write(bytes);

                    } catch (IOException e) {

                    }
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
        if (integer == HttpURLConnection.HTTP_CREATED) {
            activity.finish();
        }
        if (noInternet) {
            Helpers.alertDialog(activity, "Connection error",
                    "Check your internet connection");
        } else if (AppGlobals.getResponseCode() == HttpURLConnection.HTTP_OK) {
//                startActivity(new Intent(getApplicationContext(), BusinessCardsList.class));
        }
    }
}
