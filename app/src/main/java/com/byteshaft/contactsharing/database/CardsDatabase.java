package com.byteshaft.contactsharing.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
                               String emailAddress, String organization, String jobzyId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseConstants.NAME_COLUMN, name);
        values.put(DatabaseConstants.ADDRESS_COLUMN, address);
        values.put(DatabaseConstants.JOB_TITLE_COULMN, jobTitle);
        values.put(DatabaseConstants.CONTACT_NUMBER_COULMN, contactNumber);
        values.put(DatabaseConstants.EMAIL_ADDRESS_COULMN, emailAddress);
        values.put(DatabaseConstants.ORGANIZATION_COULMN, organization);
        values.put(DatabaseConstants.JOBZI_ID, jobzyId);
        db.insert(DatabaseConstants.TABLE_NAME, null, values);
        db.close();
    }

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

    public HashMap<String, String> getSingleBusinessCard(int id) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + DatabaseConstants.TABLE_NAME + " WHERE ID =" +id + " LIMIT 1";
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
                    cursor.getColumnIndex(DatabaseConstants.JOB_TITLE_COULMN));

            String jobzyId = cursor.getString(
                    cursor.getColumnIndex(DatabaseConstants.JOBZI_ID));

            String contactNumber = cursor.getString(
                    cursor.getColumnIndex(DatabaseConstants.CONTACT_NUMBER_COULMN));

            String emailAddress = cursor.getString(
                    cursor.getColumnIndex(DatabaseConstants.EMAIL_ADDRESS_COULMN));

            String organization = cursor.getString(
                    cursor.getColumnIndex(DatabaseConstants.ORGANIZATION_COULMN));

            hashMap.put(AppGlobals.ID, String.valueOf(unique_id));
            hashMap.put(AppGlobals.NAME, name);
            hashMap.put(AppGlobals.ADDRESS, address);
            hashMap.put(AppGlobals.JOB_TITLE, jobTitle);
            hashMap.put(AppGlobals.JOBZY_ID, jobzyId);
            hashMap.put(AppGlobals.NUMBER, contactNumber);
            hashMap.put(AppGlobals.EMAIL, emailAddress);
            hashMap.put(AppGlobals.ORG, organization);
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
                    cursor.getColumnIndex(DatabaseConstants.JOB_TITLE_COULMN));

            int contactNumber = cursor.getInt(
                    cursor.getColumnIndex(DatabaseConstants.CONTACT_NUMBER_COULMN));

            String emailAddress = cursor.getString(
                    cursor.getColumnIndex(DatabaseConstants.EMAIL_ADDRESS_COULMN));

            String organization = cursor.getString(
                    cursor.getColumnIndex(DatabaseConstants.ORGANIZATION_COULMN));

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
}
