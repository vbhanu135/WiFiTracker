package com.awn.androidproject2.wifitracker;


import android.provider.BaseColumns;

public class SignalDetails {

    public SignalDetails()
    {

    }

    public static abstract class SignalInfo implements BaseColumns
    {
        public static final String SSID = "ssid";
        public static final String RSSI = "rssi";
        public static final String TIMESTAMP = "timestamp";
        public static final String DEVICEID = "deviceid";
        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "longitude";
        public static final String DATABASE_NAME = "signal_info";
        public static final String TABLE_NAME = "signal_details";


    }
}
