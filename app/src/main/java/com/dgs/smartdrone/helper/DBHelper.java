package com.dgs.smartdrone.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

import com.dgs.smartdrone.entity.Settings;
import com.dgs.smartdrone.entity.SettingsDetail;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "MyDBName.db";
    public static final String CONTACTS_TABLE_NAME = "settings";
    public static final String CONTACTS_TABLE_DETAIL_NAME = "settingsdetail";
    public static final String CONTACTS_COLUMN_ID = "id";
    public static final String CONTACTS_COLUMN_NAME = "name";
    public static final String CONTACTS_COLUMN_ALTITUDE = "altitude";
    public static final String CONTACTS_COLUMN_GOWAY = "goway";
    public static final String CONTACTS_COLUMN_HANDLING = "handling";
    public static final String CONTACTS_COLUMN_SPEED = "speed";
    public static final String CONTACTS_COLUMN_LAT = "lat";
    public static final String CONTACTS_COLUMN_LONG = "longs";
    public static final String CONTACTS_COLUMN_GIMBAL = "gimbal";
    public static final String CONTACTS_COLUMN_PHOTO = "photo";
    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table " + CONTACTS_TABLE_NAME + " " +
                        "(id integer primary key, name text,goway int,handling int, speed float)"
        );

        db.execSQL(
                "create table " + CONTACTS_TABLE_DETAIL_NAME + " " +
                        "(id integer primary key, ids integer, altitude int, longs float, lat float, gimbal int, photo int)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + CONTACTS_TABLE_NAME);
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS " + CONTACTS_TABLE_DETAIL_NAME);
        onCreate(db);
    }

    public long insertSetting (String name, int goway, int handling, float speed) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("goway", goway);
        contentValues.put("handling", handling);
        contentValues.put("speed", speed);
        long id = db.insert( CONTACTS_TABLE_NAME, null, contentValues);
        return id;
    }

    public boolean insertSettingDetail (long id, float altitude, double lan, double longs, int gimbal, int photo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ids  ", id);
        contentValues.put("altitude", altitude);
        contentValues.put("lat", lan);
        contentValues.put("longs", longs);
        contentValues.put("gimbal", gimbal);
        contentValues.put("photo", photo);
        long ss = db.insert(CONTACTS_TABLE_DETAIL_NAME, null, contentValues);
        return true;
    }

    public ArrayList<Settings> getAllSettings() {
        ArrayList<Settings> array_list = new ArrayList<Settings>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + CONTACTS_TABLE_NAME, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            Settings data = new Settings();

            data.setId(res.getInt(res.getColumnIndex(CONTACTS_COLUMN_ID)));
            data.setName(res.getString(res.getColumnIndex(CONTACTS_COLUMN_NAME)));
            data.setGoway(res.getInt(res.getColumnIndex(CONTACTS_COLUMN_GOWAY)));
            data.setHandling(res.getInt(res.getColumnIndex(CONTACTS_COLUMN_HANDLING)));
            data.setSpeed(res.getInt(res.getColumnIndex(CONTACTS_COLUMN_SPEED)));
            array_list.add(data);
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<SettingsDetail> getAllSettingsDetail(int id) {
        ArrayList<SettingsDetail> array_list = new ArrayList<SettingsDetail>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + CONTACTS_TABLE_DETAIL_NAME + " where ids=" + id, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            SettingsDetail data = new SettingsDetail();

            data.setId(res.getInt(res.getColumnIndex(CONTACTS_COLUMN_ID)));
            data.setAltitude(res.getInt(res.getColumnIndex(CONTACTS_COLUMN_ALTITUDE)));
            data.setLan(res.getDouble(res.getColumnIndex(CONTACTS_COLUMN_LAT)));
            data.setLongs(res.getDouble(res.getColumnIndex(CONTACTS_COLUMN_LONG)));
            data.setGimbal(res.getInt(res.getColumnIndex(CONTACTS_COLUMN_GIMBAL)));
            data.setPhoto(res.getInt(res.getColumnIndex(CONTACTS_COLUMN_PHOTO)));
            array_list.add(data);
            res.moveToNext();
        }
        return array_list;
    }

    public Settings getData(int id) {
        Settings data= new Settings();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs =  db.rawQuery( "select * from " + CONTACTS_TABLE_NAME + " where id="+id+"", null );
        rs.moveToFirst();
        data.setId(rs.getInt(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_ID)));
        data.setName(rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_NAME)));
        data.setGoway(rs.getInt(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_GOWAY)));
        data.setHandling(rs.getInt(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_HANDLING)));
        data.setSpeed(rs.getFloat(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_SPEED)));
        return data;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, CONTACTS_TABLE_NAME);
        return numRows;
    }

    public Integer deleteSettings (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(CONTACTS_TABLE_NAME,
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public Integer deleteSettingsDetail (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(CONTACTS_TABLE_DETAIL_NAME,
                "ids = ? ",
                new String[] { Integer.toString(id) });
    }


}