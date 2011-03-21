package edu.cuhk.cubt.sccm;

import edu.cuhk.cubt.classifier.BusClassifier;
import edu.cuhk.cubt.net.BusLocationUploader;
import edu.cuhk.cubt.net.HttpRequest;
import edu.cuhk.cubt.state.BusState;
import edu.cuhk.cubt.state.State;
import edu.cuhk.cubt.state.event.StateChangeEvent;
import android.location.Location;
import android.os.Handler;
import android.os.Message;

public class BusChangeMonitor {

	BusLocationUploader uploader;
	
	BusClassifier busClassifier;
	SCCMEngine engine;
	LocationSensor locationSensor;
	
	String postId = "";
	
	boolean isOnBus = false;
	
	public BusChangeMonitor(SCCMEngine engine){
		this.engine = engine;
	}
	
	public void start(){
		locationSensor = engine.getLocationSensor();
		locationSensor.addHandler(handler);
		busClassifier = engine.getClassifierManager().getClassifier(BusClassifier.class);
		busClassifier.addHandler(handler);		
	}
	
	@Override
	protected void finalize(){
		locationSensor.removeHandler(handler);
		busClassifier.removeHandler(handler);
	}
	
	protected void busLocationChange(Location location){
		uploader.updateLocation(location.getLatitude(), location.getLongitude());
	}
	
	protected void busEnterEvent(){
		Location location = locationSensor.getLastLocation();
		uploader = new BusLocationUploader();
		uploader.updateLocation(location.getLatitude() , location.getLongitude());		
	}
	
	protected void busExitEvent(){
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
