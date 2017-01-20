package com.awn.androidproject2.wifitracker;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity  {

    ListView lv;
    WifiManager wifi;
    String wifis[];
    WifiScanReceiver wifiReciever;

    Button SCAN;
    Button MAP;

    Context ctx1 = this;
    int time = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        lv=(ListView)findViewById(R.id.listView);

        wifi=(WifiManager)getSystemService(Context.WIFI_SERVICE);
        wifiReciever = new WifiScanReceiver();

        SCAN = (Button)findViewById(R.id.button1);

        SCAN.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast.makeText(getApplicationContext(),"Scanning....Please wait!",Toast.LENGTH_SHORT).show();
                wifi.startScan();
            }
        });


        loadMap();

        Timer myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                BackgroundTask bgTask = new BackgroundTask(ctx1);
                String sleepTime = Integer.toString(time);
                bgTask.execute(sleepTime);
            }
        }, 0, 60000);

    }

    public void loadMap(){

        MAP = (Button)findViewById(R.id.button3);
        MAP.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                Toast.makeText(getApplicationContext(),"Loading....Please wait!",Toast.LENGTH_SHORT).show();
                Intent mintent = new Intent(MainActivity.this,MapsActivity.class);
                startActivity(mintent);

            }
        });

    }

    protected void onPause() {
        unregisterReceiver(wifiReciever);
        super.onPause();
    }

    protected void onResume() {
        registerReceiver(wifiReciever, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        super.onResume();
    }

    protected void onDestroy(){
        super.onDestroy();
    }

    String ssid;

    private class WifiScanReceiver extends BroadcastReceiver{
        public void onReceive(Context c, Intent intent) {
            List<ScanResult> wifiScanList = wifi.getScanResults();
            wifis = new String[wifiScanList.size()];


            for(int i = 0; i < wifiScanList.size(); i++){

                wifis[i] = ((wifiScanList.get(i)).SSID.toString()
                        +"\n"+Integer.toString((wifiScanList.get(i)).level)
                        +"\n"+Long.toString((wifiScanList.get(i)).timestamp)
                        +"\n"+(wifiScanList.get(i)).BSSID.toString());

            }

            lv.setAdapter(new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,wifis));

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    ssid = (adapterView.getItemAtPosition(i)).toString();
                    Intent s = new Intent(view.getContext(),SaveDetails.class);
                    s.putExtra("e1", ssid);
                    startActivity(s);

                    //Toast.makeText(getBaseContext(),adapterView.getItemAtPosition(i)+"is clicked",Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
