package edu.cuhk.cubt.sccm;

import java.io.*;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationProvider;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

public class VirtualLocationSensor {
	boolean mExternalStorageAvailable = false;
	boolean mExternalStorageWriteable = false;
	
	String demoPath = "/sdcard/fyptest/demo";
	String demoFile = "gps_demo.txt";
	File file;
	DataInputStream is;
	private Handler handler = new Handler();

	private String tag = "VirtualLocationSensor";
	
	boolean virtualGPSrunning = false;
	
	LocationListener listener;

	int period = 0;
	
	public final String provideName = "CUBusVirtualGPS";

	private boolean isStart = false;
	
	private static VirtualLocationSensor instance= null;
	private VirtualLocationSensor(){
		Init();
	}
	
	public static VirtualLocationSensor getInstance(){
		if(instance == null){
			instance = new VirtualLocationSensor();
		}
		return instance;
	}
	
	protected void Init() {
		// TODO Auto-generated method stub
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
		    // We can read and write the media
		    mExternalStorageAvailable = mExternalStorageWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
		    // We can only read the media
		    mExternalStorageAvailable = true;
		    mExternalStorageWriteable = false;
		} else {
		    // Something else is wrong. It may be one of many other states, but all we need
		    //  to know is we can neither read nor write
		    mExternalStorageAvailable = mExternalStorageWriteable = false;
		}
	}


	private Location lastLocation = null;
	
	private Runnable sendGPSPoint = new Runnable(){
		public void run(){
			String buffer;
			String buf[];
			try{
				if((buffer = is.readLine()) != null){
					Location location = new Location(provideName);
					buf = buffer.split(",", 5);
					location.setTime(Long.parseLong(buf[0]));
					location.setLatitude(Double.parseDouble(buf[1]));
					location.setLongitude(Double.parseDouble(buf[2]));
					if(lastLocation!= null && location.getTime() > lastLocation.getTime()){
						location.setSpeed(location.distanceTo(lastLocation) * 1000 / 
								(location.getTime() - lastLocation.getTime() ));
					}
					lastLocation = location;
					listener.onLocationChanged(location);
					virtualGPSrunning = true;
					period = 1000;	//Virtual GPS set period to 3s
					handler.postDelayed(this, period);
				}else{
					listener.onProviderDisabled(provideName);
					listener.onStatusChanged(provideName,  LocationProvider.OUT_OF_SERVICE, null);
				}
			} catch (IOException e) {  

			} 
		}		
	};
	
	private boolean Start() {
		if(isStart) return true;
		
		if(mExternalStorageAvailable){
		    try{
		    	//demoPath = service.getExternalFilesDir(null);
		    	new File(demoPath).mkdirs();
			    file = new File(demoPath , demoFile);
		    	handler.removeCallbacks(sendGPSPoint);
		    	is = new DataInputStream(new FileInputStream(file));
		    	virtualGPSrunning = true;
				listener.onStatusChanged(provideName,  LocationProvider.AVAILABLE, null);
		    	handler.postDelayed(sendGPSPoint, period);
				isStart = true;
		    }catch(IOException e){
		        Log.w(tag, "Error reading " + file, e);
				listener.onProviderDisabled(this.provideName);
				listener.onStatusChanged(provideName,  LocationProvider.OUT_OF_SERVICE, null);
		    }
		}else{
			listener.onProviderDisabled(this.provideName);
			listener.onStatusChanged(provideName,  LocationProvider.OUT_OF_SERVICE, null);
		}
		return isStart;
	}

	private void stop() {
		if(!isStart) return;
		
		try {
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		isStart = false;
	}
	
	public boolean isStarted(){
		return isStart;
	}
	
	public void removeListener(LocationListener listener){
		stop();
		listener = null;		
	}
	
	public void setListener(LocationListener listener){
		this.listener = listener;
		Start();
	}
	
}
