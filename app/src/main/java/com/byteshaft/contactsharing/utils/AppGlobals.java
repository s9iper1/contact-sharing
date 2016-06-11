package com.byteshaft.contactsharing.utils;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Environment;

public class AppGlobals extends Application {

    public static final String BASE_URL = "";
    public static final String LOGIN_URL = "";
    public static final String REGISTER_URL = "";

    private static Context sContext;
    public static Typeface typeface;
    public static boolean sNewEntryCreated = false;
    public static String CARD_ID = "card_id";
    public static String NAME = "name";
    public static String ID = "id";
    public static String ADDRESS = "address";
    public static String JOB_TITLE = "job_title";
    public static String JOBZY_ID = "jobzy_id";
    public static String NUMBER = "number";
    public static String EMAIL = "email";
    public static String ORG = "org";

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        typeface = Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/fonts.ttf");
    }

    public static Context getContext() {
        return sContext;
    }


    static String getAppDataDirectory() {
        String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android";
        String dataDirectory = sdcard + Environment.getDataDirectory().getAbsolutePath();
        String packageLocation = dataDirectory + "/" + sContext.getPackageName();
        return packageLocation + "/" + ".netdata/";
    }
}
