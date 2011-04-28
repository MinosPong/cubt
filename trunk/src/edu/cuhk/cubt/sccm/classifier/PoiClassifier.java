package edu.cuhk.cubt.sccm.classifier;

import android.location.Location;
import android.os.Message;
import edu.cuhk.cubt.bus.Poi;
import edu.cuhk.cubt.bus.Stop;
import edu.cuhk.cubt.sccm.LocationSensor;
import edu.cuhk.cubt.state.PoiState;
import edu.cuhk.cubt.store.PoiData;

public class PoiClassifier extends AbstractClassifier<PoiState> {

	public class PoiChangeEventObject{
		PoiChangeEventObject(int event, Poi poi, Location cause){
			this.event = event;
			this.poi = poi;
			this.cause = cause;			
		}
		private final int event;
		private final Poi poi;
		private final Location cause;
		
		public int getEvent(){return event;}
		public Poi getPoi(){return poi;}
		public Location getCause(){return cause;}
	};
	
	public static final int CHECKPOINT_ENTER_EVENT = 10611;
	public static final int CHECKPOINT_LEAVE_EVENT = 10612;
	public static final int STOP_ENTER_EVENT = 10621;
	public static final int STOP_LEAVE_EVENT = 10622;
	
	ClassifierManager manager;
	
	private Location location;
	private Location capturedLocation;
	
	
	Poi poi;
	
	PoiClassifier(ClassifierManager manager) {
		super(PoiState.UNKNOWN);
		this.manager = manager;
	}
	
	public Location getLocation(){
		return location;
	}
	
	public Poi getPoi(){
		return poi;
	}

	@Override
	protected void processClassification() {
		if(capturedLocation == null ||
				this.location == capturedLocation ) return;
		
		this.location = capturedLocation;
		
		setPoi(PoiData.getPoiByLocation(location));
		
	}
	
	/**
	 * Set the new Poi and fire Poi change event
	 * @param newPoi new Poi
	 */
	private void setPoi(Poi newPoi){
		if(poi == newPoi) return;
		
		
		if(poi == null){

		}else if(poi instanceof Stop){
			
			this.notifyOutgoingHandlers(STOP_LEAVE_EVENT, 
					new PoiChangeEventObject(STOP_LEAVE_EVENT, poi, capturedLocation));
			
		}else{

			this.notifyOutgoingHandlers(CHECKPOINT_LEAVE_EVENT, 
					new PoiChangeEventObject(CHECKPOINT_LEAVE_EVENT, poi, capturedLocation));
		}

		poi = newPoi;
		
		if(newPoi == null){

			this.setState(PoiState.OUTSIDE_POI);
		}else if(newPoi instanceof Stop){
			this.setState(PoiState.INSIDE_BUS_STOP);
			this.notifyOutgoingHandlers(STOP_ENTER_EVENT, 
					new PoiChangeEventObject(STOP_ENTER_EVENT, newPoi, capturedLocation));
		}else{
			this.setState(PoiState.INSIDE_CHECKPOINT);
			this.notifyOutgoingHandlers(CHECKPOINT_ENTER_EVENT,
					new PoiChangeEventObject(CHECKPOINT_ENTER_EVENT, newPoi, capturedLocation));					
		}		
		
	}

	@Override
	protected void onStart() {
		processClassification();
	}

	@Override
	protected void onStop() {
	}
	
	@Override
	void handleMessage(Message msg){
		switch(msg.what){
		case LocationSensor.MSG_NEW_LOCATION:
			capturedLocation = (Location)msg.obj;
			processClassification();
			break;
		}
	};

}
