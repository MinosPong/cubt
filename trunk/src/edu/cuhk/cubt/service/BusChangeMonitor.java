package edu.cuhk.cubt.service;

import edu.cuhk.cubt.net.BusLocationUploader;
import edu.cuhk.cubt.sccm.LocationSensor;
import edu.cuhk.cubt.sccm.SCCMEngine;
import edu.cuhk.cubt.sccm.classifier.BusClassifier;
import edu.cuhk.cubt.state.BusState;
import edu.cuhk.cubt.state.State;
import edu.cuhk.cubt.state.event.StateChangeEvent;
import edu.cuhk.cubt.ui.CubtService;
import android.location.Location;
import android.os.Handler;
import android.os.Message;

/**
 * The Bus Change Monitor Track the bus event and change of bus location.
 * Upload the Bus Location information to the server 
 * @author PKM
 *
 */
public class BusChangeMonitor implements IServiceMonitor{

	BusLocationUploader uploader;
	
	BusClassifier busClassifier;
	SCCMEngine engine;
	LocationSensor locationSensor;
	
	boolean isOnBus = false;
		
	public void start(CubtService service){
		this.engine = service.getSCCMEngine();
		locationSensor = engine.getLocationSensor();
		busClassifier = engine.getClassifierManager().getClassifier(BusClassifier.class);
		busClassifier.addHandler(handler);		
	}
	
	public void stop(CubtService service){
		locationSensor.removeHandler(handler);
		busClassifier.removeHandler(handler);		
	}
	
	protected void busLocationChange(Location location){
		uploader.updateLocation(location.getLatitude(), location.getLongitude());
	}
	
	protected void busEnterEvent(){
		Location location = locationSensor.getLastLocation();
		locationSensor.addHandler(handler);
		
		uploader = new BusLocationUploader();
		uploader.updateLocation(location.getLatitude() , location.getLongitude());		
	}
	
	protected void busExitEvent(){
		locationSensor.removeHandler(handler);
		uploader.remove();
		uploader = null;
	}
	
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			
			switch(msg.what){
				case State.TYPE_BUS:
					@SuppressWarnings("unchecked")
					StateChangeEvent<BusState> evt = (StateChangeEvent<BusState>)msg.obj;
					if(evt.getNewState().equals(BusState.ONBUS)){
						isOnBus = true;
						busEnterEvent();
					}else if(
						evt.getOldState().equals(BusState.ONBUS))
					{		
						busExitEvent();				
						isOnBus = false;
					}
					break;
				case LocationSensor.MSG_NEW_LOCATION:					
					if(isOnBus) busLocationChange((Location) msg.obj);
					break;
			}
			super.handleMessage(msg);
		}
	};
}
