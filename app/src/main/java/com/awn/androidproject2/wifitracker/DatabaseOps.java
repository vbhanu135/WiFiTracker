package com.awn.androidproject2.wifitracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static com.awn.androidproject2.wifitracker.SignalDetails.SignalInfo.TABLE_NAME;


public class DatabaseOps extends SQLiteOpenHelper {

    public static final int database_version = 1;
    public String CREATE_QUERY = "CREATE TABLE "+ TABLE_NAME+"("+SignalDetails.SignalInfo.SSID+" TEXT,"
                                    +SignalDetails.SignalInfo.RSSI+" INTEGER,"+SignalDetails.SignalInfo.TIMESTAMP+" INTEGER,"
                                    +SignalDetails.SignalInfo.DEVICEID+" TEXT,"+SignalDetails.SignalInfo.LATITUDE+" REAL,"
                                    +SignalDetails.SignalInfo.LONGITUDE+" REAL);";

    public DatabaseOps(Context context){

        super(context, SignalDetails.SignalInfo.DATABASE_NAME, null, database_version);
        Log.d("Database Ops", "Database Created");

    }

    @Override
    public void onCreate(SQLiteDatabase sdb){

        sdb.execSQL(CREATE_QUERY);
        Log.d("Database Ops", "Table Created");

    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2){

    }

    public void insertDetails(DatabaseOps dop, String ssid, String rssi, String timestamp, String deviceid, double latitude, double longitude){

        SQLiteDatabase sq = dop.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(SignalDetails.SignalInfo.SSID, ssid);
        cv.put(SignalDetails.SignalInfo.RSSI, rssi);
        cv.put(SignalDetails.SignalInfo.TIMESTAMP, timestamp);
        cv.put(SignalDetails.SignalInfo.DEVICEID, deviceid);
        cv.put(SignalDetails.SignalInfo.LATITUDE, latitude);
        cv.put(SignalDetails.SignalInfo.LONGITUDE, longitude);
        long k = sq.insert(TABLE_NAME, null, cv);
        Log.d("Database Ops", "Row Inserted");

    }

    public Cursor getDetails(DatabaseOps dops){

        SQLiteDatabase sq = dops.getReadableDatabase();
        String[] columns = {SignalDetails.SignalInfo.SSID, SignalDetails.SignalInfo.RSSI, SignalDetails.SignalInfo.TIMESTAMP,
                            SignalDetails.SignalInfo.DEVICEID, SignalDetails.SignalInfo.LATITUDE, SignalDetails.SignalInfo.LONGITUDE};
        Cursor cr = sq.query(SignalDetails.SignalInfo.TABLE_NAME,columns,null,null,null,null,null);
        return cr;

    }

}