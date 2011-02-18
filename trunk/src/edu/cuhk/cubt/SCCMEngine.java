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
import edu.cuhk.cubt.state.State;
import edu.cuhk.cubt.store.LocationHistory;
public class SCCMEngine {
	
	
	Context mContext;
	ClassifierManager classifierManager = new ClassifierManager(this);
	LocationHistory locationHistroy = new LocationHistory();
	
	LocationClassifier locationClassifier;
	
	public SCCMEngine(){
		//TODO
		
	}
	
	public boolean startEngine(){
		//TODO
		//init Location Manager
		LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
		if(locationManager == null){
			throw new UnsupportedOperationException("Location serivce not exist");
		}
		
		
		//Initialize Classifiers
		classifierManager.initialize();
		
		locationClassifier.addHandler(handler);
		
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
	
	
	
	
	
	Handler handler = new Handler(){
		//TODO
		public void handleMessage(Message msg){
			switch(msg.what)
			{
				case State.TYPE_LOCATION:
					
					break;
			}
		}		
	};	
	
	LocationListener locationListener = new LocationListener(){
		//TODO

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
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
