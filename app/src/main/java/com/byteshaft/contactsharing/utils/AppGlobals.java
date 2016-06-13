package com.byteshaft.contactsharing.utils;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;

public class AppGlobals extends Application {

    public static final String BASE_URL = "";
    public static final String LOGIN_URL = "";
    public static final String REGISTER_URL = "";

    private static Context sContext;
    public static Typeface typeface;
    public static Typeface regularTypeface;
    public static boolean sNewEntryCreated = false;
    public static String CARD_ID = "card_id";
    // bluetooth
    public static final String NAME = "name";
    public static final String ID = "id";
    public static final String ADDRESS = "address";
    public static final String JOB_TITLE = "job_title";
    public static final String JOBZY_ID = "jobzy_id";
    public static final String NUMBER = "number";
    public static final String EMAIL = "email";
    public static final String ORG = "org";
    public static final String DATA_TO_BE_SENT = "data_to_be_sent";
    // -->
    public static final String CURRENT_COLOR = "current_color";

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        typeface = Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/fonts.ttf");
        regularTypeface = Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/regular.ttf");
    }

    public static Context getContext() {
        return sContext;
    }
}
