package edu.cuhk.cubt.classifier;

import android.location.Location;
import android.os.Handler;
import android.os.Message;
import edu.cuhk.cubt.bus.Poi;
import edu.cuhk.cubt.bus.Stop;
import edu.cuhk.cubt.sccm.LocationSensor;
import edu.cuhk.cubt.state.PoiState;
import edu.cuhk.cubt.store.LocationHistory;
import edu.cuhk.cubt.store.PoiData;

public class PoiClassifier extends AbstractClassifier<PoiState> {


	ClassifierManager manager;
	
	LocationHistory locationHistory;
	LocationSensor locationSensor;
	
	Location location;
	
	Poi poi;
	
	public PoiClassifier(ClassifierManager manager, 
			LocationSensor locationSensor) {
		super(PoiState.UNKNOWN);
		this.locationSensor = locationSensor;
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
		if(this.location == locationSensor.getLastLocation() ) return;
		
		this.location = locationSensor.getLastLocation();
		
		poi = PoiData.getPoiByLocation(location);
		
		if(poi==null){
			this.setState(PoiState.OUTSIDE_POI);
		}else if(poi instanceof Stop){
			this.setState(PoiState.INSIDE_BUS_STOP);
		}else{
			this.setState(PoiState.INSIDE_CHECKPOINT);
		}
		
	}
	
	@Override
	protected void onStart() {
		locationSensor.addHandler(mHandler);
		processClassification();
	}

	@Override
	protected void onStop() {
		locationSensor.removeHandler(mHandler);
	}
	
	Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			switch(msg.what){
			case LocationSensor.MSG_NEW_LOCATION:
				processClassification();
				break;
			}
		}
	};

}
