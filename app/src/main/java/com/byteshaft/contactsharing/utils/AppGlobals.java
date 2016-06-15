package com.byteshaft.contactsharing.utils;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;

public class AppGlobals extends Application {

    //register

    public static final String KEY_FULLNAME = "full_name";
    public static final String KEY_EMAIL = "email";

    //login
    public static final String KEY_USER_TOKEN = "token";
    public static final String KEY_USER_LOGIN = "user_login";

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
    public static final String IMG_URI = "img_uri";
    public static final String IS_IMAGE =  "is_image";
    public static final String DATA_TO_BE_SENT = "data_to_be_sent";
    // -->
    public static final String CURRENT_COLOR = "current_color";
    public static int responseCode = 0;

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

    public static void setResponseCode(int code) {
        responseCode = code;
    }

    public static int getResponseCode() {
        return responseCode;
    }
}
