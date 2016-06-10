package com.byteshaft.contactsharing;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Environment;

public class AppGlobals extends Application {

    private static Context sContext;
    public static Typeface typeface;

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
