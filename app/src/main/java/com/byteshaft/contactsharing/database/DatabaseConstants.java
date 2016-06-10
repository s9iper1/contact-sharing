package com.byteshaft.contactsharing.database;

public class DatabaseConstants {

    public static final String DATABASE_NAME = "BusinessCards.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "CardsDatabase";
    public static final String NAME_COLUMN = "NAME";
    public static final String ADDRESS_COLUMN = "ADDRESS";
    public static final String JOB_TITLE_COULMN = "JOB_TITLE";
    public static final String CONTACT_NUMBER_COULMN = "CONTACT_NUMBER";
    public static final String EMAIL_ADDRESS_COULMN = "EMAIL";
    public static final String ORGANIZATION_COULMN = "ORGANIZATION";
    public static final String ID_COLUMN = "ID";

    private static final String OPENING_BRACE = "(";
    private static final String CLOSING_BRACE = ")";

    public static final String TABLE_CREATE = "CREATE TABLE "
            + TABLE_NAME
            + OPENING_BRACE
            + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + NAME_COLUMN + " TEXT,"
            + ADDRESS_COLUMN + " TEXT,"
            + JOB_TITLE_COULMN + " TEXT,"
            + CONTACT_NUMBER_COULMN + " TEXT,"
            + EMAIL_ADDRESS_COULMN + " TEXT,"
            + ORGANIZATION_COULMN + " TEXT"
            + CLOSING_BRACE;
}
