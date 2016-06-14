package com.byteshaft.contactsharing.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;

/**
 * Created by husnain on 6/14/16.
 */
public class Helpers  {

    public Helpers() {
    }

    public static SharedPreferences getPreferenceManager() {
        return PreferenceManager.getDefaultSharedPreferences(AppGlobals.getContext());
    }

    public static void saveDataToSharedPreferences(String key, String value) {
        SharedPreferences sharedPreferences = getPreferenceManager();
        sharedPreferences.edit().putString(key, value).apply();
    }

    public static String getStringFromSharedPreferences(String key) {
        SharedPreferences sharedPreferences = getPreferenceManager();
        return sharedPreferences.getString(key, "");
    }

//    public static void saveUserLogin(boolean value) {
//        SharedPreferences sharedPreferences = getPreferenceManager();
//        sharedPreferences.edit().putBoolean(AppGlobals.KEY_USER_LOGIN, value).apply();
//    }
//
//    public static boolean isUserLoggedIn() {
//        SharedPreferences sharedPreferences = getPreferenceManager();
//        return sharedPreferences.getBoolean(AppGlobals.KEY_USER_LOGIN, false);
//    }

    public static void alertDialog(Activity activity, String title, String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(msg).setCancelable(false).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
