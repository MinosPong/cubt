package edu.cuhk.cubt.sccm;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import edu.cuhk.cubt.classifier.ClassifierManager;
import edu.cuhk.cubt.classifier.LocationClassifier;
import edu.cuhk.cubt.classifier.PoiClassifier;
import edu.cuhk.cubt.classifier.SpeedClassifier;
import edu.cuhk.cubt.state.LocationState;
import edu.cuhk.cubt.state.State;
import edu.cuhk.cubt.state.event.StateChangeEvent;
import edu.cuhk.cubt.store.LocationHistory;
public class SCCMEngine {
	
	
	Context mContext;
	ClassifierManager classifierManager = new ClassifierManager(this);
	LocationHistory locationHistroy = new LocationHistory();
	
	LocationClassifier locationClassifier;
	PoiClassifier poiClassifier;
	SpeedClassifier speedClassifier;
	LocationManager locationManager;
	LocationSensor locationSensor;
	
	public SCCMEngine(Context context){
		mContext = context;
		//TODO		
	}
	
	public boolean startEngine(){
		//TODO
		//init Location Manager
		locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
		if(locationManager == null){
			throw new UnsupportedOperationException("Location serivce not exist");
		}
		
		locationSensor = new LocationSensor(locationManager);
		
		//Initialize Classifiers
		classifierManager.initialize();
		
		locationClassifier = classifierManager.getClassifier(LocationClassifier.class);
		poiClassifier      = classifierManager.getClassifier(PoiClassifier.class);
		speedClassifier    = classifierManager.getClassifier(SpeedClassifier.class);
		
		locationClassifier.addHandler(locationChangeHandler);
		locationSensor.addHandler(locationChangeHandler);
		
		locationSensor.start();
		
		return true;
	}
	
	public LocationSensor getLocationSensor(){
		return locationSensor;
	}
	
	public LocationHistory getLocationHistory(){
		return locationHistroy;
	}
	

	public ClassifierManager getClassifierManager(){
		return classifierManager;
	}
	
	
	private void newLocation(Location location){
		locationHistroy.add(location);
		locationClassifier.process();
		if(locationClassifier.getState() == LocationState.INSIDE_CUHK){
			speedClassifier.process();
			poiClassifier.process();
		}		
	}
	
	
	Handler locationChangeHandler = new Handler(){
		//TODO
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg){
			switch(msg.what)
			{
				case State.TYPE_LOCATION:
					StateChangeEvent<LocationState> evt = (StateChangeEvent<LocationState>) msg.obj;
					LocationState state = evt.getNewState();
					locationSensor.setCapturingState(state);		
					break;
				case LocationSensor.MSG_NEW_LOCATION:
					newLocation((Location) msg.obj);
					break;
			}
		}		
	};	
	

}
