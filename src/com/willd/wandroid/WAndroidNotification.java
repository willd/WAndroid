package com.willd.wandroid;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class WAndroidNotification extends Activity {
	final List<Integer> RSSIlist = new ArrayList<Integer>();

    /** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
		
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.sec);
        
        final Button sec_button = (Button) findViewById(R.id.sec_button);
        final TextView sec_tv = (TextView) findViewById(R.id.sec_text);	  
        final WifiManager mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
    		mWifiManager.createWifiLock("tag");
    	final WifiInfo mWifiInfo = mWifiManager.getConnectionInfo();
    	
        this.registerReceiver(this.mRssiChangeReceiver,
          	       new IntentFilter(WifiManager.RSSI_CHANGED_ACTION));

        sec_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v ) {
            	sec_tv.setText(RSSIlist.toString());
            		
            	}
        });
        



	}

  	 private BroadcastReceiver mRssiChangeReceiver = new BroadcastReceiver() {

   		 @Override
   		 public void onReceive(Context arg0, Intent arg1) {
   		  int newRssi = arg1.getIntExtra(WifiManager.EXTRA_NEW_RSSI, 0);
   		  final TextView sec_tv2 = (TextView) findViewById(R.id.sec_text2);
   		  Toast.makeText(WAndroidNotification.this, String.valueOf(newRssi), Toast.LENGTH_SHORT).show();
   		  RSSIlist.add(newRssi);
   		  sec_tv2.setText(String.valueOf(newRssi) );
   		 }};


}
