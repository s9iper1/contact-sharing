package com.byteshaft.contactsharing.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.byteshaft.contactsharing.utils.AppGlobals;

import java.util.ArrayList;
import java.util.HashMap;


public class CardsDatabase extends SQLiteOpenHelper {

    public CardsDatabase(Context context) {
        super(context, DatabaseConstants.DATABASE_NAME, null, DatabaseConstants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseConstants.TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + DatabaseConstants.TABLE_NAME);
        onCreate(db);
    }

    public void createNewEntry(String name, String address, String jobTitle, String contactNumber,
                               String emailAddress, String organization, String jobzyId,
                               String imgUri, Integer isImagCard, int designInt, String logoImagepath) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseConstants.NAME_COLUMN, name);
        values.put(DatabaseConstants.ADDRESS_COLUMN, address);
        values.put(DatabaseConstants.JOB_TITLE_COLUMN, jobTitle);
        values.put(DatabaseConstants.CONTACT_NUMBER_COLUMN, contactNumber);
        values.put(DatabaseConstants.EMAIL_ADDRESS_COLUMN, emailAddress);
        values.put(DatabaseConstants.ORGANIZATION_COLUMN, organization);
        values.put(DatabaseConstants.JOBZI_ID, jobzyId);
        values.put(DatabaseConstants.IMG_COLUMN, imgUri);
        values.put(DatabaseConstants.IS_IMAGE_CARD_COLUMN, isImagCard);
        values.put(DatabaseConstants.SELECTED_CARD_DESIGN, designInt);
        values.put(DatabaseConstants.LOGO_IMAGE, logoImagepath);
        db.insert(DatabaseConstants.TABLE_NAME, null, values);
        db.close();
    }

    public boolean deleteEntry(Integer id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(DatabaseConstants.TABLE_NAME, DatabaseConstants.ID_COLUMN + "=" + id, null) > 0;
    }

    public void updateEntries(
            Integer id, String name, String address, String jobTitle,
                              String contactNumber, String emailAddress, String organization,
                              String jobzyId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseConstants.NAME_COLUMN, name);
        values.put(DatabaseConstants.ADDRESS_COLUMN, address);
        values.put(DatabaseConstants.JOB_TITLE_COLUMN, jobTitle);
        values.put(DatabaseConstants.CONTACT_NUMBER_COLUMN, contactNumber);
        values.put(DatabaseConstants.EMAIL_ADDRESS_COLUMN, emailAddress);
        values.put(DatabaseConstants.ORGANIZATION_COLUMN, organization);
        values.put(DatabaseConstants.JOBZI_ID, jobzyId);
        db.update(DatabaseConstants.TABLE_NAME, values, DatabaseConstants.ID_COLUMN + "=" + id, null);
        Log.i("Database", "Updated.......");
        db.close();
    }

//    public void imageEntry(String name, String uri) {
//        SQLiteDatabase db = getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(DatabaseConstants.NAME_COLUMN, name);
//        values.put(DatabaseConstants.IMG_COLUMN, uri);
//        db.insert(DatabaseConstants.TABLE_NAME, null, values);
//        db.close();
//    }

    public ArrayList<HashMap<Integer, String>> getNamesOfSavedCards() {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + DatabaseConstants.TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        HashMap<Integer, String> map = new HashMap<>();
        ArrayList<HashMap<Integer, String>> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(
                    cursor.getColumnIndex(DatabaseConstants.ID_COLUMN));
            String name = cursor.getString(
                    cursor.getColumnIndex(DatabaseConstants.NAME_COLUMN));
            map.put(id, name);
            list.add(map);
        }
        db.close();
        cursor.close();
        return list;
    }


    public ArrayList<Integer> getIdOfSavedCards() {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + DatabaseConstants.TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<Integer> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(
                    cursor.getColumnIndex(DatabaseConstants.ID_COLUMN));
            list.add(id);
        }
        db.close();
        cursor.close();
        return list;
    }

    public HashMap<String, String[]> getBusinessCard() {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + DatabaseConstants.TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        HashMap<String, String[]> hashMap = new HashMap<>();
        while (cursor.moveToNext()) {
            int unique_id = cursor.getInt(
                    cursor.getColumnIndex(DatabaseConstants.ID_COLUMN));
            String name = cursor.getString(
                    cursor.getColumnIndex(DatabaseConstants.NAME_COLUMN));
//            String address = cursor.getString(
//                    cursor.getColumnIndex(DatabaseConstants.ADDRESS_COLUMN));
//            String jobTitle = cursor.getString(
//                    cursor.getColumnIndex(DatabaseConstants.JOB_TITLE_COLUMN));
//
//            String jobzyId = cursor.getString(
//                    cursor.getColumnIndex(DatabaseConstants.JOBZI_ID));
//
//            String contactNumber = cursor.getString(
//                    cursor.getColumnIndex(DatabaseConstants.CONTACT_NUMBER_COLUMN));
//
//            String emailAddress = cursor.getString(
//                    cursor.getColumnIndex(DatabaseConstants.EMAIL_ADDRESS_COLUMN));
//
//            String organization = cursor.getString(
//                    cursor.getColumnIndex(DatabaseConstants.ORGANIZATION_COLUMN));
//
            int isImage = cursor.getInt(
                    cursor.getColumnIndex(DatabaseConstants.IS_IMAGE_CARD_COLUMN));
//
//            String logoPath = cursor.getString(
//                    cursor.getColumnIndex(DatabaseConstants.LOGO_IMAGE));

            String imageUri = cursor.getString(
                    cursor.getColumnIndex(DatabaseConstants.IMG_COLUMN));
            int cardDesign = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.SELECTED_CARD_DESIGN));
            Log.e("Name", name);
            String[] data = new String[] {name, String.valueOf(cardDesign), imageUri,
                    String.valueOf(isImage)};
            hashMap.put(String.valueOf(unique_id), data);
        }
        db.close();
        cursor.close();
        return hashMap;
    }

    public ArrayList<HashMap> getAllRecords() {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + DatabaseConstants.TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<HashMap> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            int unique_id = cursor.getInt(
                    cursor.getColumnIndex(DatabaseConstants.ID_COLUMN));
            String name = cursor.getString(
                    cursor.getColumnIndex(DatabaseConstants.NAME_COLUMN));
            String address = cursor.getString(
                    cursor.getColumnIndex(DatabaseConstants.ADDRESS_COLUMN));
            String jobTitle = cursor.getString(
                    cursor.getColumnIndex(DatabaseConstants.JOB_TITLE_COLUMN));

            int contactNumber = cursor.getInt(
                    cursor.getColumnIndex(DatabaseConstants.CONTACT_NUMBER_COLUMN));

            String emailAddress = cursor.getString(
                    cursor.getColumnIndex(DatabaseConstants.EMAIL_ADDRESS_COLUMN));

            String organization = cursor.getString(
                    cursor.getColumnIndex(DatabaseConstants.ORGANIZATION_COLUMN));

            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("unique_id", String.valueOf(unique_id));
            hashMap.put("name", name);
            hashMap.put("address", address);
            hashMap.put("job_title", jobTitle);
            hashMap.put("contact_number", String.valueOf(contactNumber));
            hashMap.put("email_address", emailAddress);
            hashMap.put("organization", organization);
            list.add(hashMap);
        }
        db.close();
        cursor.close();
        return list;
    }

    public HashMap<String, String> getCardDetails(int id) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + DatabaseConstants.TABLE_NAME + " WHERE " +
                DatabaseConstants.ID_COLUMN+ " = " +id + " LIMIT 1";
        Cursor cursor = db.rawQuery(query, null);
        HashMap<String, String> hashMap = new HashMap<>();
        while (cursor.moveToNext()) {
            int unique_id = cursor.getInt(
                    cursor.getColumnIndex(DatabaseConstants.ID_COLUMN));
            String name = cursor.getString(
                    cursor.getColumnIndex(DatabaseConstants.NAME_COLUMN));
            String address = cursor.getString(
                    cursor.getColumnIndex(DatabaseConstants.ADDRESS_COLUMN));
            String jobTitle = cursor.getString(
                    cursor.getColumnIndex(DatabaseConstants.JOB_TITLE_COLUMN));

            String jobzyId = cursor.getString(
                    cursor.getColumnIndex(DatabaseConstants.JOBZI_ID));

            String contactNumber = cursor.getString(
                    cursor.getColumnIndex(DatabaseConstants.CONTACT_NUMBER_COLUMN));

            String emailAddress = cursor.getString(
                    cursor.getColumnIndex(DatabaseConstants.EMAIL_ADDRESS_COLUMN));

            String organization = cursor.getString(
                    cursor.getColumnIndex(DatabaseConstants.ORGANIZATION_COLUMN));

            int isImage = cursor.getInt(
                    cursor.getColumnIndex(DatabaseConstants.IS_IMAGE_CARD_COLUMN));
            String logoPath = cursor.getString(
                    cursor.getColumnIndex(DatabaseConstants.LOGO_IMAGE));

            String imageUri = cursor.getString(
                    cursor.getColumnIndex(DatabaseConstants.IMG_COLUMN));
            int cardDesign = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.SELECTED_CARD_DESIGN));
            hashMap.put(AppGlobals.KEY_FULL_NAME, name);
            hashMap.put(AppGlobals.KEY_ADDRESS, address);
            hashMap.put(AppGlobals.KEY_JOB_TITLE, jobTitle);
            hashMap.put(AppGlobals.KEY_JOBZY_ID, jobzyId);
            hashMap.put(AppGlobals.KEY_CONTACT_NUMBER, contactNumber);
            hashMap.put(AppGlobals.KEY_MAIL, emailAddress);
            hashMap.put(AppGlobals.KEY_ORG, organization);
            hashMap.put(AppGlobals.KEY_LOGO, logoPath);
            hashMap.put(AppGlobals.KEY_DESIGN, String.valueOf(cardDesign));
            hashMap.put(AppGlobals.KEY_IMAGE, imageUri);
            hashMap.put(AppGlobals.KEY_ISIMAGE, String.valueOf(isImage));

        }
        db.close();
        cursor.close();
        return hashMap;
    }

    public boolean hasObject(String name) {
        SQLiteDatabase db = getWritableDatabase();
        String selectString = "SELECT * FROM " + DatabaseConstants.TABLE_NAME + " WHERE " + DatabaseConstants.NAME_COLUMN + " =?";

        // Add the String you are searching by here.
        // Put it in an array to avoid an unrecognized token error
        Cursor cursor = db.rawQuery(selectString, new String[] {name});

        boolean hasObject = false;
        if(cursor.moveToFirst()){
            hasObject = true;

            //region if you had multiple records to check for, use this region.

            int count = 0;
            while(cursor.moveToNext()){
                count++;
            }
            //here, count is records found
            Log.d("Tag", String.format("%d records found", count));

            //endregion

        }

        cursor.close();          // Dont forget to close your cursor
        db.close();              //AND your Database!
        return hasObject;
    }
}
