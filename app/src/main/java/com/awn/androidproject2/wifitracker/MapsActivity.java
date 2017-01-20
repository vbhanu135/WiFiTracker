package com.awn.androidproject2.wifitracker;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    Context ctx2 = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if(netInfo != null && netInfo.isConnected())
        {
            try {

                String link = "http://project2website.000webhostapp.com/select_data.php?user=1&ssid=wsu-secure";
                URL url = new URL(link);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String response = "";
                String line = "";
                while((line = bufferedReader.readLine()) != null)
                {
                    response+= line;
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                String firstSplit[] = response.split("\\[");
                String secondSplit[] = firstSplit[1].split("\\]");
                String thirdSplit[] = secondSplit[0].split(",");
                ArrayList splitValuesList = new ArrayList();
                ArrayList latLongList = new ArrayList();

                String k = "";
                String v = "";
                int flag1 = 1;


                for(int i=0; i<thirdSplit.length; i++)
                {
                    String fourthSplit[] = thirdSplit[i].split(":");
                    String tempStr = fourthSplit[1].replaceAll("\"","").trim();

                    if( fourthSplit[0].contains("rssi"))
                    {
                        k =  tempStr;
                    }

                    if( fourthSplit[0].contains("latitude") || fourthSplit[0].contains("longitude") )
                    {
                        if(flag1 == 1)
                        {
                            v +=  tempStr + ",";
                        }
                        else
                        {
                            v +=  tempStr;
                        }
                        flag1++;
                    }

                    if(flag1==3)
                    {
                        splitValuesList.add(k);
                        latLongList.add(v.replaceAll("\\}", "" ));
                        k="";
                        v="";
                        flag1=1;
                    }

                }


                for(int i=0; i<splitValuesList.size(); i++)
                {
                    System.out.println(splitValuesList.get(i) +"======" + latLongList.get(i) );

                    String ssid = "wsu-secure";
                    int rssi = Integer.parseInt(splitValuesList.get(i).toString());

                    String Str = latLongList.get(i).toString();
                    String[] parts = Str.split(",");

                    Double lat1 = Double.parseDouble(parts[0]);
                    Double lon1 = Double.parseDouble(parts[1]);

                    LatLng point = new LatLng(lat1,lon1);

                    // Drawing marker on the map
                    drawMarker(point, ssid, rssi);

                }


            }catch (IOException e){
                e.printStackTrace();
            }



        }

        else {
            DatabaseOps dopsm = new DatabaseOps(ctx2);
            Cursor cr = dopsm.getDetails(dopsm);

            int rowCount = cr.getCount();

            if(rowCount != 0) {

                Log.d("Retrieving","Data from local base.");
                cr.moveToFirst();
                do {
                    String ssid = cr.getString(0);
                    int rssi = Integer.parseInt(cr.getString(1));

                    LatLng point = new LatLng(Double.parseDouble(cr.getString(4)), Double.parseDouble(cr.getString(5)));

                    // Drawing marker on the map
                    drawMarker(point, ssid, rssi);

                } while (cr.moveToNext());
            }

            else {
                Toast.makeText(getApplicationContext(),"No data in local base!",Toast.LENGTH_SHORT).show();
            }
        }

    }

    public BitmapDescriptor getMarkerIcon(String color){

        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(color),hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }

    private void drawMarker(LatLng point,String ssid,int rssi){

        String d1 = Integer.toString(rssi);
        Log.d("Marker",d1);
        Log.d("Space","Checker");

        // Creating an instance of MarkerOptions
        MarkerOptions markerOptions = new MarkerOptions();

        // Setting latitude and longitude for the marker
        markerOptions.position(point);
        markerOptions.title(ssid);

        if (rssi >= -35){
            markerOptions.icon(getMarkerIcon("#6bb120"));
        }
        else if (rssi >= -60 && rssi < -35){
            markerOptions.icon(getMarkerIcon("#9afe2e"));
        }
        else if (rssi >= -70 && rssi < -60){
            markerOptions.icon(getMarkerIcon("#aefe57"));
        }
        else if (rssi >= -90 && rssi < -70){
            markerOptions.icon(getMarkerIcon("#fe5757"));
        }
        else if (rssi < -90){
            markerOptions.icon(getMarkerIcon("#cb2424"));
        }

        // Adding marker on the Google Map
        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point,15));

    }
}
