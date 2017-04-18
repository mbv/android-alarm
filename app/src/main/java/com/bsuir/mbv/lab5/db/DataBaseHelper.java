package com.bsuir.mbv.lab5.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "AlarmDB";
    private static final int DATABASE_VERSION = 1;

    public static final String ALARMS_TABLE = "tbl_alarms";
    public static final String ID_COLUMN = "id";
    public static final String RINGTONE_URI_COLUMN = "ringtone_uri";
    public static final String TIME_COLUMN = "time";

    public static final String CREATE_ALARM_TABLE = "CREATE TABLE "
            + ALARMS_TABLE + "(" + ID_COLUMN + " INTEGER PRIMARY KEY,"
            + RINGTONE_URI_COLUMN + " VARCHAR(255), " + TIME_COLUMN + " INTEGER)";


    private static DataBaseHelper instance;

    public static synchronized DataBaseHelper getHelper(Context context) {
        if (instance == null)
            instance = new DataBaseHelper(context);
        return instance;
    }

    private DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ALARM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
