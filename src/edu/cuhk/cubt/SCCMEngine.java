package edu.cuhk.cubt;

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
				
		
		//Initialize Classifiers
		classifierManager.initialize();
		
		locationClassifier = classifierManager.getClassifier(LocationClassifier.class);
		poiClassifier      = classifierManager.getClassifier(PoiClassifier.class);
		speedClassifier    = classifierManager.getClassifier(SpeedClassifier.class);
		
		locationClassifier.addHandler(locationChangeHandler);
		
		setLocationCapture(locationClassifier.getState());
		return true;
	}
	
	public LocationHistory getLocationHistory(){
		return locationHistroy;
	}
	

	public ClassifierManager getClassifierManager(){
		return classifierManager;
	}
	
	
	private Criteria getCoarseCriteria(){
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_COARSE);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		return criteria;
	}
	
	private Criteria getFineCriteria(){
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		return criteria;
	}
	
	
	private void newLocation(Location location){
		locationHistroy.add(location);
		locationClassifier.process();
		if(locationClassifier.getState() == LocationState.INSIDE_CUHK){
			speedClassifier.process();
			poiClassifier.process();
		}		
	}
	
	private void setLocationCapture(LocationState state){
		if(state == null) state = LocationState.UNKNOWN;
		long minTime = 0;
		float minDistance = 0;
		Criteria criteria = null;
		if (state == LocationState.UNKNOWN){
			criteria = getCoarseCriteria();
			minTime = 1000;
	    }else if(state == LocationState.INSIDE_CUHK){
			criteria = getFineCriteria();
			minTime = 1000;			
		}else if(state == LocationState.CLOSE_TO_CUHK){
			criteria = getCoarseCriteria();
			minTime = 60 * 1000;						
		}else if(state == LocationState.FAR_FROM_CUHK){
			criteria = getCoarseCriteria();	
			minTime = 15 * 60 * 1000;			
		}	
		
		//locationManager.removeUpdates(locationListener);
		locationManager.requestLocationUpdates(
				locationManager.getBestProvider(criteria, true), 
				minTime, 
				minDistance, 
				locationListener);
		
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
					setLocationCapture(state);		
					break;
			}
		}		
	};	
	
	
	LocationListener locationListener = new LocationListener(){
		//TODO

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			newLocation(location);
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub	
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
		}	
	};

}
