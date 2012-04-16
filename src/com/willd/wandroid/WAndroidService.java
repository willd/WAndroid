package com.willd.wandroid;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.TextView;
import android.widget.Toast;

public class WAndroidService extends Service {
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
        final WifiManager mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
    		mWifiManager.createWifiLock("tag");
    	final WifiInfo mWifiInfo = mWifiManager.getConnectionInfo();
    	
        this.registerReceiver(this.mRssiChangeReceiver,
          	       new IntentFilter(WifiManager.RSSI_CHANGED_ACTION));
        
	    return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	private BroadcastReceiver mRssiChangeReceiver = new BroadcastReceiver() {

   		@Override
   		public void onReceive(Context arg0, Intent arg1) {
   			int newRssi = arg1.getIntExtra(WifiManager.EXTRA_NEW_RSSI, 0);
   			
   			
   		 }};
}
