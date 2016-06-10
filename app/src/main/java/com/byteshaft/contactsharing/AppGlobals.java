package com.byteshaft.contactsharing;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;

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
}
