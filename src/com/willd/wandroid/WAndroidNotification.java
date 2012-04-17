package com.willd.wandroid;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
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


public class WAndroidNotification extends Activity {
	final List<Integer> RSSIlist = new ArrayList<Integer>();

    /** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
		
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.sec);
        
        final Button sec_button = (Button) findViewById(R.id.sec_button);
        final Button save_rssi = (Button) findViewById(R.id.save_rssi);
        final TextView sec_tv = (TextView) findViewById(R.id.sec_text);	
        final EditText m_name = (EditText) findViewById(R.id.m_name);
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
        
        save_rssi.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
				WritetoFile(m_name, sec_tv);
				
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
   	private void WritetoFile(EditText m_name, TextView sec_tv) {
   		try {
            File root = new File(Environment.getExternalStorageDirectory(), "WAndroid_Measurements");

            if (!root.exists())
                root.mkdir();
            
            Calendar c = Calendar.getInstance(); 
            String seconds = Integer.toString(c.get(Calendar.SECOND));
            String minute = Integer.toString(c.get(Calendar.MINUTE));
            String hour = Integer.toString(c.get(Calendar.HOUR));
            String name2 = null;

            
            Editable name1 = m_name.getText();
            if ((m_name.getText().toString().equals(""))) {
            	name2 =hour+"."+minute+"."+seconds+".txt";
            }
            else {
               	name2 =name1+".txt";
            }
            
            File gpxfile = new File(root, name2);

            BufferedWriter bW;
            
            bW = new BufferedWriter(new FileWriter(gpxfile, true));
            bW.append(sec_tv.getText());
            //bW.append(hour);
            //bW.append(minute);
            //bW.append(seconds);
            bW.newLine();
            bW.flush();
            bW.close();
            
        }
        catch(IOException e) {
             e.printStackTrace();
        }
        
   	}

}	
