package com.willd.wandroid;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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

	final List<String> RSSIlist = new ArrayList<String>();
	private boolean recieverflag = false;
	private String filename = null;
	private int counter = 0;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        

        final Button start = (Button) findViewById(R.id.start);
        final Button root = (Button) findViewById(R.id.root);
        final Button increment = (Button) findViewById(R.id.increment);
        final Button reset = (Button) findViewById(R.id.reset);
                
        final TextView tv = (TextView) findViewById(R.id.textview);
        final TextView edit = (TextView) findViewById(R.id.edit_info);
        
        final EditText et = (EditText) findViewById(R.id.edittext);
        final EditText valuecount = (EditText) findViewById(R.id.valuecount);
 		  
        final WifiManager mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
    		mWifiManager.createWifiLock("tag");
    			

        
        int icon = R.drawable.wireless_icon;
        /*
    	try {
			rootprocess = getRoot();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}*/
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
        root.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v ) {
            	String measurement = Integer.toString(counter);
            	String value = null;
            	int countable = Integer.parseInt(valuecount.getText().toString());
            	if(valuecount.getText().equals("")) 
            		countable = 30;
            	
            	for(int x=0; x <= countable;x++) {
            			value = ReadfromShell("/data/local/tmp/iwpriv.sh");
            			value = value.trim();
            			RSSIlist.add(value + " " + measurement);
            	}
            			edit.setText("-"+value + " " + measurement);
                    	
                    	
            }        	
                    
        });
        increment.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v ) {
            	counter+=1;
            	tv.setText(RSSIlist.toString());
            }        	
            
        });
        reset.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v ) {
        		filename = CreateFile(et);
        		WritetoFile(filename, RSSIlist);
        		
        		counter = 0;
}        	
    
});
                
    }
    private Process getRoot() throws IOException {
    	Process process = Runtime.getRuntime().exec("su");
    	return process;
    }
    private String ReadfromShell(String command) {
		try {
		    // Executes the command.
			Process process = Runtime.getRuntime().exec("su");
		    OutputStreamWriter osw = new OutputStreamWriter(process.getOutputStream());
		    
	        osw.write(command);
	        osw.flush();
	        osw.close();
	
		    // Reads stdout.
		    // NOTE: You can write to stdin of the command using
		    //       process.getOutputStream().
		    BufferedReader reader = new BufferedReader(
		            new InputStreamReader(process.getInputStream()));
		    int read;
		    char[] buffer = new char[4096];
		    StringBuffer output = new StringBuffer();
		    while ((read = reader.read(buffer)) > 0) {
		        output.append(buffer, 0, read);
		    }
		    reader.close();
		    
		    // Waits for the command to finish.
		    process.waitFor();
		    
		    return output.toString();
		} catch (IOException e) {
		    throw new RuntimeException(e);
		} catch (InterruptedException e) {
		    throw new RuntimeException(e);
		}
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
	private void WritetoFile(String filename, List list) {
   		try {
            File root = new File(Environment.getExternalStorageDirectory(), "WAndroid_Measurements");

            if (!root.exists())
                root.mkdir();
            
            
            File gpxfile = new File(root, filename);
            
            BufferedWriter bW;
            
            bW = new BufferedWriter(new FileWriter(gpxfile, true));
            if(list == null) {
            	
            
            bW.append((RSSIlist.get(RSSIlist.size()-1)));
            bW.newLine();
            }
            else {
            	for(int x=0;x <= RSSIlist.size()-1; x++) {
            		bW.append(RSSIlist.get(x));
                    bW.newLine();		
            	}
            }
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
   			 final EditText et = (EditText) findViewById(R.id.edittext);
   			 
   			 Toast.makeText(WAndroidActivity.this, String.valueOf(newRSSI), Toast.LENGTH_SHORT).show();
   			 RSSIlist.add(Integer.toString(newRSSI));
   			 WritetoFile(filename, null);
   			 et_info.setText(String.valueOf(newRSSI) );
   		 }};	
}
