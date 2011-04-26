package edu.cuhk.cubt;

import edu.cuhk.cubt.ui.CubtService;
import android.app.Application;
import android.content.Intent;
import android.util.Log;

public class CubtApplication extends Application {

	public static final String TAG = "CubtApplication";	
	private boolean serviceStarted;
	
	@Override
	public void onCreate(){
		
	}
	
	public void startService(){
		if(serviceStarted) return;
		Intent intent = new Intent(this, CubtService.class);
		if(startService(intent) != null){
			serviceStarted = true;							
		}else{
			Log.e(TAG, "Failed to start service");
		}
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
}
