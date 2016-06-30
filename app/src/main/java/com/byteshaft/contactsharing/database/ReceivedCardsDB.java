package com.byteshaft.contactsharing.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ReceivedCardsDB extends SQLiteOpenHelper {

    public ReceivedCardsDB(Context context) {
        super(context, ReceiveCardsDatabseConstants.DATABASE_NAME, null,
                ReceiveCardsDatabseConstants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ReceiveCardsDatabseConstants.TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS" + ReceiveCardsDatabseConstants.TABLE_NAME);
        onCreate(db);
    }

    public void createNewEntry(
            String name, String address, String jobTitle, String contactNumber,
                               String emailAddress, String organization, String jobzyId,
                               String imgUri, Integer isImagCard,
                               int designInt, String logoImagepath) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ReceiveCardsDatabseConstants.NAME_COLUMN, name);
        values.put(ReceiveCardsDatabseConstants.ADDRESS_COLUMN, address);
        values.put(ReceiveCardsDatabseConstants.JOB_TITLE_COLUMN, jobTitle);
        values.put(ReceiveCardsDatabseConstants.CONTACT_NUMBER_COLUMN, contactNumber);
        values.put(ReceiveCardsDatabseConstants.EMAIL_ADDRESS_COLUMN, emailAddress);
        values.put(ReceiveCardsDatabseConstants.ORGANIZATION_COLUMN, organization);
        values.put(ReceiveCardsDatabseConstants.JOBZI_ID, jobzyId);
        values.put(ReceiveCardsDatabseConstants.IMG_COLUMN, imgUri);
        values.put(ReceiveCardsDatabseConstants.IS_IMAGE_CARD_COLUMN, isImagCard);
        values.put(ReceiveCardsDatabseConstants.SELECTED_CARD_DESIGN, designInt);
        values.put(ReceiveCardsDatabseConstants.LOGO_IMAGE, logoImagepath);
        db.insert(ReceiveCardsDatabseConstants.TABLE_NAME, null, values);
        db.close();
    }

    public boolean deleteEntry(Integer id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(ReceiveCardsDatabseConstants.TABLE_NAME,
                ReceiveCardsDatabseConstants.ID_COLUMN + "=" + id, null) > 0;
    }
}
