package com.byteshaft.contactsharing.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by husnain on 6/14/16.
 */
public class WebServiceHelper {

    private static ProgressDialog progressDialog;

    public WebServiceHelper() {
    }

    public static HttpURLConnection openConnectionForUrl(String targetUrl, String method) throws IOException {
        URL url = new URL(targetUrl);
        System.out.println(targetUrl);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("charset", "utf-8");
        connection.setRequestMethod(method);
        return connection;
    }

    public static JSONObject registerUser(String fullname, String email, String password) throws IOException, JSONException {
        String data = getRegistrationData(fullname, email, password);
        System.out.println(data);
        String url = "http://128.199.195.245:8000/api/register";
        HttpURLConnection connection = openConnectionForUrl(url, "POST");
        sendRequestData(connection, data);
        AppGlobals.setResponseCode(connection.getResponseCode());
        System.out.println(connection.getResponseCode());
        return readResponse(connection);
    }

    public static String getRegistrationData(String fullname, String email, String password) {
        JSONObject object = new JSONObject();

        try {
            object.put("full_name", fullname);
            object.put("email", email);
            object.put("password", password);
        } catch (JSONException var8) {
            var8.printStackTrace();
        }

        return object.toString();
    }

    public static String userLogin(String email, String password) throws IOException, JSONException {
        String data = getLoginData(email, password);
        System.out.println(data);
        String url = "http://178.62.37.43:8000/api/login";
        HttpURLConnection connection = openConnectionForUrl(url, "POST");
        sendRequestData(connection, data);
        AppGlobals.setResponseCode(connection.getResponseCode());
        JSONObject jsonObj = readResponse(connection);
        return (String)jsonObj.get("token");
    }

    public static String getLoginData(String email, String password) {
        JSONObject object = new JSONObject();

        try {
            object.put("username", email);
            object.put("password", password);
        } catch (JSONException var4) {
            var4.printStackTrace();
        }

        return object.toString();
    }

    private static void sendRequestData(HttpURLConnection connection, String body) throws IOException {
        byte[] outputInBytes = body.getBytes("UTF-8");
        OutputStream os = connection.getOutputStream();
        os.write(outputInBytes);
        os.close();
    }

    private static JSONObject readResponse(HttpURLConnection connection) throws IOException, JSONException {
        InputStream is = connection.getInputStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        StringBuilder response = new StringBuilder();

        String line;
        while((line = rd.readLine()) != null) {
            response.append(line);
            response.append('\r');
        }

        Log.i("TG", response.toString());
        return new JSONObject(response.toString());
    }

    public static boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)
                AppGlobals.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static boolean isInternetWorking() {
        boolean success = false;

        try {
            URL e = new URL("https://google.com");
            HttpURLConnection connection = (HttpURLConnection)e.openConnection();
            connection.setConnectTimeout(10000);
            connection.connect();
            success = connection.getResponseCode() == 200;
        } catch (IOException var3) {
            var3.printStackTrace();
        }

        return success;
    }

    public static void showProgressDialog(Activity context, String message) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    public static void dismissProgressDialog() {
        if(progressDialog != null) {
            progressDialog.dismiss();
        }

    }
}
