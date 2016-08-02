package com.byteshaft.contactsharing.utils;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.Log;

import com.byteshaft.contactsharing.bluetooth.ClientThread;
import com.byteshaft.contactsharing.bluetooth.ProgressData;
import com.byteshaft.contactsharing.bluetooth.ServerThread;

import java.util.Set;

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
    public static final String IS_IMAGE_SHARE = "is_image_share";
    public static final String IMAGE = "image";
    public static final String CARD_DESIGN = "card_design";

    public static int userExistResponse;
    public static int postResponse;

    public static boolean isFirstTime = false;
    private static final String LOGTAG = "LOGTAG";
    public static int sSelectedDesign = 0;

    public static final String USER_ACTIVE = "user_active";
    public static final String REGISTRATION_DONE = "registration_done";

    // bluetooth connection and handling part private static String TAG = "AppGlobals";
    public static BluetoothAdapter adapter;
    public static Set<BluetoothDevice> pairedDevices;
    public static Handler clientHandler;
    public static Handler serverHandler;
    public static ClientThread clientThread;
    public static ServerThread serverThread;
    public static ProgressData progressData = new ProgressData();

    public static final int PICTURE_RESULT_CODE = 1234;
    public static final int IMAGE_QUALITY = 100;
    public static final int NO_DESIGN = 4;
    public static boolean sIncomingImage = false;
    public static BluetoothDevice sBluetoothDevice;
    public static String imageOwner = "";
    public static int isImageState;
    public static boolean sInComingLogo = false;
    public static String KEY_LOGO = "key_logo";
    public static String logoPath = "";
    public static String name = "";
    public static String address = "";
    public static String jobTitle = "";
    public static String jobzyId = "";
    public static String contectNumber = "";
    public static String email = "" ;
    public static String org = "";
    public static int design = 4;
    public static String PROCESS_CARD_ID = "card_id";

    // Card Creation

    public static String toBeCreatedCardName;
    public static String toBeCreatedAddress;
    public static String toBeCreatedJobTitle;
    public static String toBeCreatedjobzyId;
    public static String toBeCreatedcontactNumber;
    public static String toBeCreatedEmail;
    public static String toBeCreatedOrg;

    public static final String KEY_FULL_NAME= "Full Name";
    public static final String KEY_ADDRESS = "Address";
    public static final String KEY_JOB_TITLE = "Job Title";
    public static final String KEY_JOBZY_ID = "Jobzy Id";
    public static final String KEY_CONTACT_NUMBER = "Contact Number";
    public static final String KEY_MAIL = "Email";
    public static final String KEY_ORG = "Organization";
    public static final String KEY_DESIGN = "Design";
    public static final String KEY_IMAGE = "Image";
    public static final String KEY_ISIMAGE = "is_image";
    public static boolean sIsEdit = false;



    @Override
    public void onCreate() {
        super.onCreate();
        adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter != null) {
            if (adapter.isEnabled()) {
                pairedDevices = adapter.getBondedDevices();
            } else {
                Log.e(getLogTag(getClass()), "Bluetooth is not enabled");
            }
        } else {
            Log.e(getLogTag(getClass()), "Bluetooth is not supported on this device");
        }
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

    // Globally set the value for userExistResponse it takes integer value as parameter
    public static void setUserExistResponse(int value) {
        userExistResponse = value;
    }

    // Get the value of UserExist where needed it returns integer value
    public static int getUserExistResponse() {
        return userExistResponse;
    }

    public static void setPostResponse(int value) {
        postResponse = value;
    }

    public static int getPostResponse() {
        return postResponse;
    }

    public static String getLogTag(Class aClass) {
        return LOGTAG + aClass.getSimpleName();
    }

}
