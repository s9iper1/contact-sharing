package com.byteshaft.contactsharing;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.byteshaft.contactsharing.utils.Helpers;

/**
 * Created by husnain on 6/16/16.
 */
public class LogoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showLogoutDialog();
    }

    public void showLogoutDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                LogoutActivity.this);
        alertDialogBuilder.setTitle("Logout");
        alertDialogBuilder
                .setMessage("Are you sure?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferences sharedpreferences = Helpers.getPreferenceManager();
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.clear();
                        editor.commit();
                        closeApplication();
                        if (!Helpers.isUserLoggedIn()) {
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            Helpers.saveUserLogin(false);
                        }

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void closeApplication() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
        startActivity(startMain);
        finish();
    }
}
