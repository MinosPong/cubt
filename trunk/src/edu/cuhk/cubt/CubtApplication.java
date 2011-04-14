package edu.cuhk.cubt;

import edu.cuhk.cubt.store.LocationHistory;
import edu.cuhk.cubt.ui.CubtService;
import android.app.Application;
import android.content.Intent;

public class CubtApplication extends Application {

	public static final String TAG = "CubtApplication";	
	private boolean serviceStarted;
	
	@Override
	public void onCreate(){
		
	}
	
	
	public void startService(){
		if(serviceStarted) return;
		Intent intent = new Intent(this, CubtService.class);
		startService(intent);
		serviceStarted = true;		
	}
	
	public void stopService(){
		if(!serviceStarted) return;
		Intent intent = new Intent(this, CubtService.class);
		stopService(intent);
		serviceStarted = false;		
	}
	
	public boolean isServiceStarted()
	{
		return serviceStarted;
	}
	
	private LocationHistory locationHistory = null;
	public LocationHistory getLocationHistory(){
		if(locationHistory == null) locationHistory = new LocationHistory();
		return locationHistory;
	}
}
