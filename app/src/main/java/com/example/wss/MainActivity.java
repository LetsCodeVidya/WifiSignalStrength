package com.example.wss;

import static com.github.karthyks.runtimepermissions.PermissionActivity.REQUEST_PERMISSION_CODE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.karthyks.runtimepermissions.Permission;
import com.github.karthyks.runtimepermissions.googleapi.LocationSettingsHelper;
import com.google.android.gms.location.LocationRequest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;




public class MainActivity extends AppCompatActivity {
    private Button permissions;

    TextView st1;
    TextView st2;
    TextView st3;
    TextView st4;
    TextView st5;
    TextView st6;
    TextView st7;
    TextView st8;
    TextView st9;
    TextView st10;
    TextView st11;
    TextView st12;

    List results;
    Button bt;

    Button reset;
    Handler handler;
    double operating_band;
    int rssi;
    String ssid = new String();
    String bssid = new String();
    int frequency;
    int Linkspeed;
    int ChannelNumber;
    int RxLinkSpeed;
    int TxLinkSpeed;
    int count;
    int Bandwidth;
    int SLNO = 0;
    int mynum;
   // Context context = this;
    String currentSSID = new String();
    String previousSSID = new String();
    File file;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        st1 = (TextView) findViewById(R.id.textView1);
        st2 = (TextView) findViewById(R.id.textView2);
        st3 = (TextView) findViewById(R.id.textView3);
        st4 = (TextView) findViewById(R.id.textView4);
        st5 = (TextView) findViewById(R.id.textView5);
        st6 = (TextView) findViewById(R.id.textView6);
        st7 = (TextView) findViewById(R.id.textView7);
        st8 = (TextView) findViewById(R.id.textView8);
        st9 = (TextView) findViewById(R.id.textView9);
        st10 = (TextView) findViewById(R.id.textView10);
        st11 = (TextView) findViewById(R.id.textView11);
        st12 = (TextView) findViewById(R.id.textView12);


        bt = (Button) findViewById(R.id.button5);
        reset = (Button) findViewById(R.id.button);

        // measures all the values and saves in the file


        //  resets everything and deletes the file
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                file.delete();
                SLNO = 0;
                st1.setText("\n\t\tNumber of times=" + SLNO);
                st2.setText("\n\t\tSignal Strength of Unknown SSID");
                st3.setText("\n\t\tRSSI=0");
                st4.setText("\n\t\tFrequency=0");
                st5.setText("\n\t\tLinkspeed=0");
                st6.setText("\n\t\tRxLinkSpeed=0");
                st7.setText("\n\t\tTxLinkSpeed=0");
                st8.setText("\n\t\tOperating band=0");
                st9.setText("\n\n\t");
                st10.setText("\n\n\t");
                st11.setText("\n\n\t");

            }

        });

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disp();
                savetofile();
                checkStoragePermission();
                checkLocationSettings();
                SLNO++;


            }
        });


    }

    private void checkStoragePermission() {
        Permission permission = new Permission.PermissionBuilder(Permission.REQUEST_STORAGE)
                .usingActivity(this).withRationale("Storage permission!").build();
        permission.requestPermission(REQUEST_PERMISSION_CODE);
    }
    private void checkLocationSettings() {


        Permission permission = new Permission.PermissionBuilder(Permission.REQUEST_LOCATION)
                .usingActivity(this).withRationale("Storage permission!").build();
        permission.requestPermission(REQUEST_PERMISSION_CODE);

        LocationRequest locationRequest =  LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsHelper settingsApi = new LocationSettingsHelper(MainActivity.this,
                locationRequest, true, false);
        settingsApi.checkLocationRequest();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }




    // Strength calc and display
    @SuppressLint("SetTextI18n")
    private void disp() {
        //  try {
        WifiManager wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();

        rssi = wifiManager.getConnectionInfo().getRssi();
        bssid = wifiManager.getConnectionInfo().getBSSID();
        frequency = wifiManager.getConnectionInfo().getFrequency();

        Linkspeed = wifiManager.getConnectionInfo().getLinkSpeed();
        RxLinkSpeed = wifiManager.getConnectionInfo().getRxLinkSpeedMbps();
        TxLinkSpeed = wifiManager.getConnectionInfo().getTxLinkSpeedMbps();

            try{


            results = wifiManager.getScanResults();
            List<ScanResult> results = wifiManager.getScanResults();


            for (ScanResult result : results) {
                if (bssid.equals(result.BSSID)) {
                    if (result.channelWidth == 0) {
                        Bandwidth = 20;
                    } else if (result.channelWidth == 1) {
                        Bandwidth = 40;
                    } else if (result.channelWidth == 2) {
                        Bandwidth = 80;
                    } else if (result.channelWidth == 3) {
                        Bandwidth = 160;
                    } else if (result.channelWidth == 4) {
                        Bandwidth = 160;
                    } else {
                        Bandwidth = 320;
                    }

                    if (frequency >= 2412 && frequency < 2484) {
                        ChannelNumber = ((frequency - 2412) / 5 + 1);
                    } else if (frequency >= 5170 && frequency <= 5825) {
                        ChannelNumber = ((frequency - 5170) / 5 + 34);
                    } else if (frequency == 2484) {
                        ChannelNumber = 14;
                    }
                    st9.setText("\n\n\t\t" + "Bandwidth=" + result.channelWidth + "\n\n\t\tCenter frequency0=" + result.centerFreq0 + "\n\n\t\tCenter Frequency1=" + result.centerFreq1 + "\n\n\t\tChannel Number=" + ChannelNumber);

                }
            }






                ssid = wifiInfo.getSSID();
            Log.d("Vidya", "currentSSID");
            currentSSID = ssid;
            Log.d("Vidya", "prevSSID");
            // to obtain the previous AP information
            if (previousSSID.equals(currentSSID)) {
                st10.setText("\n\t\t" + currentSSID + "=" + SLNO);
                count = SLNO;
            } else if (currentSSID.equals("<unknown ssid>")) {
                st10.setText("\n\t\t" + previousSSID + "=" + (SLNO));

            } else {
                if (count != 0) {
                    st11.append("\t" + previousSSID + "=" + count);
                    SLNO = 1;
                }
            }
            if (!currentSSID.equals("<unknown ssid>")) {
                previousSSID = currentSSID;
           }


            if (frequency > 2400 && frequency < 3000)
                operating_band = 2.4;
            else
                operating_band = 5.0;

            // Display on the screen
            st1.setText("\n\t\tNumber of times=" + SLNO);
            st2.setText("\n\t\tSignal Strength of " + ssid);
            st3.setText("\n\t\tRSSI = " + rssi + " dbm");
            st4.setText("\n\t\tFrequency=" + frequency);
            st5.setText("\n\t\tLinkspeed=" + Linkspeed);
            st6.setText("\n\t\tRxLinkSpeed=" + RxLinkSpeed);
            st7.setText("\n\t\tTxLinkSpeed=" + TxLinkSpeed);
            st8.setText("\n\t\tOperating band=" + operating_band);
            //st12.setText("result"+result);


        }
            catch(Exception e)
            {
                Toast.makeText(MainActivity.this,"Device is not connected to any network", Toast.LENGTH_LONG).show();

            }


    }


    //Store in a file
    private void savetofile() {
        Log.v("Vidyashree", "entering save file");
        File directory = null;

        directory = new File(Environment.getExternalStorageDirectory() + java.io.File.separator + "WSS");
        if (!directory.exists())
            Toast.makeText(this, (directory.mkdirs() ? "Directory has been created" : "Directory not created"), Toast.LENGTH_SHORT).show();

        System.out.println(directory);
        file = new File(Environment.getExternalStorageDirectory() + java.io.File.separator + "WSS" + java.io.File.separator + "WSS.txt");
        System.out.println(file);

        Date currentTime = Calendar.getInstance().getTime();
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        try {
            OutputStreamWriter file_writer = new OutputStreamWriter(new FileOutputStream(file, true));
            BufferedWriter buffered_writer = new BufferedWriter(file_writer);
            if (SLNO==0) {
                buffered_writer.write("\nNumber of times" + "\tSSID" + "\tRSSI" + "\tFrequency" + "\tLinkSpeed" + "\tRxLinkSpeed" + "\tTxLinkSpeed" + "\toperating_band");
            }
            else {
                buffered_writer.write("\n" + SLNO + "\t" + ssid + "\t" + rssi + "\t" + frequency + "\t" + Linkspeed + "\t" + RxLinkSpeed + "\t" + TxLinkSpeed + "\t" + operating_band);
            }
            buffered_writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
