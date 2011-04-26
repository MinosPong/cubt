package edu.cuhk.cubt.service;

import android.location.Location;
import android.os.Handler;
import android.os.Message;
import edu.cuhk.cubt.sccm.LocationSensor;
import edu.cuhk.cubt.sccm.SCCMEngine;
import edu.cuhk.cubt.store.LocationHistory;
import edu.cuhk.cubt.ui.CubtService;

/**
 * The Location History Service to Monitor the GPS location Change and store in Memory (RAM) 
 * @author PKM
 *
 */
public class LocationChangeMonitor implements IServiceMonitor{
	private SCCMEngine engine;

	public void start(CubtService service){
		engine = service.getSCCMEngine();		
		
		engine.getLocationSensor().addHandler(mHandler);
	}
	
	public void stop(CubtService service){
		engine.getLocationSensor().removeHandler(mHandler);
		engine = null;
	}
	
	Handler mHandler = new Handler(){
		public void handleMessage(Message msg){
			switch(msg.what){
			case LocationSensor.MSG_NEW_LOCATION:
					try{
						Location location = (Location)msg.obj;
						LocationHistory.getInstance().add(location);
					}catch(Exception e){
						
					}
				break;
			}
		}
	};
	
}
