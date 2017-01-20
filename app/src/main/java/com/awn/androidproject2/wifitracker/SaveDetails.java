package com.awn.androidproject2.wifitracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SaveDetails extends Activity {

    String e1;
    Button SAVE;
    Button VIEWDATA;
    Context ctx = this;
    GPSTracker gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_details);

        TextView tv = (TextView) findViewById(R.id.textView4);
        Intent intent = getIntent();
        e1 = intent.getStringExtra("e1");

        final String ssid,deviceid;
        final String timestamp,rssi;
        final double latitude;
        final double longitude;
        double lat=0,lon=0;

        gps = new GPSTracker(ctx);

        if(gps.canGetLocation()){
            lat = gps.getLatitude();
            lon = gps.getLongitude();
        }
        else {
            gps.showSettingsAlert();
        }

        String Str = e1;
        String[] parts = Str.split("\n");

        ssid = parts[0];
        rssi = parts[1];
        timestamp = parts[2];
        deviceid = parts[3];
        latitude = lat;
        longitude = lon;

        tv.setText("SSID: "+ssid+"\nRSSI: "+rssi+"\nTIME: "+timestamp+"\nDEV ID: "+deviceid+"\nLATITUDE: "+latitude+"\nLONGITUDE: "+longitude);

        Log.d("Split&Save: ",ssid);

        SAVE = (Button) findViewById(R.id.saveButton);
        SAVE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseOps dops = new DatabaseOps(ctx);
                dops.insertDetails(dops,ssid,rssi,timestamp,deviceid,latitude,longitude);
                Toast.makeText(getApplicationContext(),"Saved Successfully!",Toast.LENGTH_LONG).show();

                //BackgroundTask bgtask = new BackgroundTask(ctx);
                //bgtask.execute(ssid.toString(),rssi.toString(),timestamp.toString(),deviceid.toString(),Double.toString(latitude),Double.toString(longitude));

            }
        });

        showLocalData();

    }

    public void showLocalData(){

        VIEWDATA = (Button)findViewById(R.id.viewData);
        VIEWDATA.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                Toast.makeText(getApplicationContext(),"Loading....Please wait!",Toast.LENGTH_SHORT).show();
                StringBuffer buffer = new StringBuffer();

                DatabaseOps dopsm = new DatabaseOps(ctx);
                Cursor cr = dopsm.getDetails(dopsm);

                int rowCount = cr.getCount();

                if(rowCount != 0) {

                    cr.moveToFirst();

                    do {
                        buffer.append("SSID: " + cr.getString(0) + "\n");
                        buffer.append("RSSI: " + cr.getString(1) + "\n");
                        buffer.append("TIME: " + cr.getString(2) + "\n");
                        buffer.append("DEVID: " + cr.getString(3) + "\n");
                        buffer.append("LAT: " + cr.getString(4) + "\n");
                        buffer.append("LONG: " + cr.getString(5) + "\n");
                        buffer.append("***" + "\n\n");
                    } while (cr.moveToNext());

                    showMessage("Local device data", buffer.toString());
                }

                else {
                    Toast.makeText(getApplicationContext(),"No data in local base!",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void showMessage(String title,String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

}
