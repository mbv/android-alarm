package com.bsuir.mbv.lab5.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.bsuir.mbv.lab5.model.Alarm;

import java.util.ArrayList;
import java.util.List;

public class AlarmDAO {
    private static final String WHERE_ID_EQUALS = DataBaseHelper.ID_COLUMN
            + " =?";

    protected SQLiteDatabase database;
    private DataBaseHelper dbHelper;
    private Context _context;

    public AlarmDAO(Context context) {
        this._context = context;
        dbHelper = DataBaseHelper.getHelper(_context);
        open();

    }

    public void open() throws SQLException {
        if(dbHelper == null)
            dbHelper = DataBaseHelper.getHelper(_context);
        database = dbHelper.getWritableDatabase();
    }

    public long save(Alarm alarm) {

        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.RINGTONE_URI_COLUMN, alarm.getRingtone().toString());
        values.put(DataBaseHelper.TIME_COLUMN, alarm.getTime());

        return database
                .insert(DataBaseHelper.ALARMS_TABLE, null, values);
    }

    public long update(Alarm alarm) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.RINGTONE_URI_COLUMN, alarm.getRingtone().toString());
        values.put(DataBaseHelper.TIME_COLUMN, alarm.getTime());

        return (long) database.update(DataBaseHelper.ALARMS_TABLE,
                values, WHERE_ID_EQUALS,
                new String[] { String.valueOf(alarm.getId()) });
    }

    public int deleteDept(Alarm alarm) {
        return database.delete(DataBaseHelper.ALARMS_TABLE,
                WHERE_ID_EQUALS, new String[] { alarm.getId() + "" });
    }

    public List<Alarm> getAll() {
        List<Alarm> alarmsList = new ArrayList<Alarm>();
        try (Cursor cursor = database.query(DataBaseHelper.ALARMS_TABLE,
                new String[]{DataBaseHelper.ID_COLUMN, DataBaseHelper.RINGTONE_URI_COLUMN,
                        DataBaseHelper.TIME_COLUMN}, null, null, null,
                null, null)) {

            while (cursor.moveToNext()) {
                Alarm alarm = new Alarm();
                alarm.setId(cursor.getInt(0));
                alarm.setRingtone(Uri.parse(cursor.getString(1)));
                alarm.setTime(cursor.getLong(2));
                alarmsList.add(alarm);
            }
        }
        return alarmsList;
    }

    public Alarm get(int id) {
        try (Cursor cursor = database.query(DataBaseHelper.ALARMS_TABLE,
                new String[]{DataBaseHelper.ID_COLUMN, DataBaseHelper.RINGTONE_URI_COLUMN,
                        DataBaseHelper.TIME_COLUMN}, WHERE_ID_EQUALS,
                new String[]{String.valueOf(id)}, null, null, DataBaseHelper.ID_COLUMN)) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                Alarm alarm = new Alarm();
                alarm.setId(cursor.getInt(0));
                alarm.setRingtone(Uri.parse(cursor.getString(1)));
                alarm.setTime(cursor.getLong(2));
                return alarm;
            }
        }
        return null;
    }
}
