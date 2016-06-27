package com.byteshaft.contactsharing.database;

public class DatabaseConstants {

    public static final String DATABASE_NAME = "BusinessCards.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "CardsDatabase";
    public static final String JOBZI_ID = "jobzi_id";
    public static final String NAME_COLUMN = "NAME";
    public static final String ADDRESS_COLUMN = "ADDRESS";
    public static final String JOB_TITLE_COLUMN = "JOB_TITLE";
    public static final String CONTACT_NUMBER_COLUMN = "CONTACT_NUMBER";
    public static final String EMAIL_ADDRESS_COLUMN = "EMAIL";
    public static final String ORGANIZATION_COLUMN = "ORGANIZATION";
    public static final String ID_COLUMN = "ID";
    public static final String IMG_COLUMN = "image_column";
    public static final String IS_IMAGE_CARD_COLUMN = "is_img_card";
    public static final String SELECTED_CARD_DESIGN = "selected_card_design";
    public static final String LOGO_IMAGE = "logo_image";

    private static final String OPENING_BRACE = "(";
    private static final String CLOSING_BRACE = ")";

    public static final String TABLE_CREATE = "CREATE TABLE "
            + TABLE_NAME
            + OPENING_BRACE
            + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + NAME_COLUMN + " TEXT,"
            + JOBZI_ID + " TEXT,"
            + ADDRESS_COLUMN + " TEXT,"
            + JOB_TITLE_COLUMN + " TEXT,"
            + CONTACT_NUMBER_COLUMN + " TEXT,"
            + EMAIL_ADDRESS_COLUMN + " TEXT,"
            + ORGANIZATION_COLUMN + " TEXT,"
            + IMG_COLUMN + " TEXT,"
            + IS_IMAGE_CARD_COLUMN + " INTEGER,"
            + SELECTED_CARD_DESIGN + " INTEGER,"
            + LOGO_IMAGE + " TEXT"
            + CLOSING_BRACE;
}
