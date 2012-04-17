package com.willd.wandroid;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.R.bool;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class WAndroidActivity extends Activity {

	final List<Integer> RSSIlist = new ArrayList<Integer>();
	private boolean recieverflag = false;
	private String filename = null;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        

        final Button start = (Button) findViewById(R.id.start);
        final TextView tv = (TextView) findViewById(R.id.textview);
        final EditText et = (EditText) findViewById(R.id.edittext);
 		  
        final WifiManager mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
    		mWifiManager.createWifiLock("tag");
    			

        
        int icon = R.drawable.wireless_icon;
        
        
        start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v ) {
            	if(recieverflag == false) {
            		start.setText("Stop");
            		filename = CreateFile(et);
            		registerReceiver(mRssiChangeReceiver,
            				new IntentFilter(WifiManager.RSSI_CHANGED_ACTION));
            	}
            	else {
            		start.setText("Start");
            		unregisterReceiver(mRssiChangeReceiver);
            		recieverflag = false;
            	}
            }
        });
        	
    }
    private String CreateFile(EditText et) {
        Calendar c = Calendar.getInstance(); 
        String seconds = Integer.toString(c.get(Calendar.SECOND));
        String minute = Integer.toString(c.get(Calendar.MINUTE));
        String hour = Integer.toString(c.get(Calendar.HOUR_OF_DAY));
        String default_name = null;

        
        Editable edit_name = et.getText();
        if ((et.getText().toString().equals(""))) {
        	default_name =hour+"."+minute+"."+seconds+".txt";
        }
        else {
        	default_name =edit_name+".txt";
        }
		return default_name;
    	
    }
	private void WritetoFile(String value, String filename) {
   		try {
            File root = new File(Environment.getExternalStorageDirectory(), "WAndroid_Measurements");

            if (!root.exists())
                root.mkdir();
            
            
            File gpxfile = new File(root, filename);

            BufferedWriter bW;
            
            bW = new BufferedWriter(new FileWriter(gpxfile, true));
            bW.append(Integer.toString(RSSIlist.get(RSSIlist.size()-1)));
            bW.newLine();
            bW.flush();
            bW.close();
            
        }
        catch(IOException e) {
             e.printStackTrace();
        }
        
   	}
 	 private BroadcastReceiver mRssiChangeReceiver = new BroadcastReceiver() {

   		 @Override
   		 public void onReceive(Context arg0, Intent arg1) {
   			 recieverflag = true;  			 
   			 int newRSSI = arg1.getIntExtra(WifiManager.EXTRA_NEW_RSSI, 0);
   			 final TextView et_info = (TextView) findViewById(R.id.edit_info);
   			
   			 Toast.makeText(WAndroidActivity.this, String.valueOf(newRSSI), Toast.LENGTH_SHORT).show();
   			 RSSIlist.add(newRSSI);
   			 WritetoFile(Integer.toString(newRSSI), filename);
   			 et_info.setText(String.valueOf(newRSSI) );
   		 }};	
}
