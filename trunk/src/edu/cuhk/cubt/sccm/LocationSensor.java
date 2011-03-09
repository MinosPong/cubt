package edu.cuhk.cubt.sccm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import edu.cuhk.cubt.state.LocationState;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class LocationSensor {

	Criteria coarseCriteria = null;
	Criteria fineCriteria = null;
	
	public static final int MSG_NEW_LOCATION = 14231;
	
	LocationManager locationManager;
	SCCMEngine engine;

	private List<Handler> handlers = new Vector<Handler>();;
	
	public LocationSensor(LocationManager locationManager) {
		this.locationManager = locationManager;
	}

	public void addHandler(Handler handler){
		if(handler == null)
			throw new NullPointerException("handler");
		
		synchronized(handlers)
		{
			if(!handlers.contains(handler))
				handlers.add(handler);
		}
	}
	
	public void removeHandler(Handler handler){
		if(handler != null)
			synchronized(handlers)
			{
				handlers.remove(handler);
			}
	}
	
	private Criteria getCoarseCriteria(){
		if( coarseCriteria == null){
			coarseCriteria  = new Criteria();
			coarseCriteria .setAccuracy(Criteria.ACCURACY_COARSE);
			coarseCriteria .setPowerRequirement(Criteria.POWER_LOW);
		}
		return coarseCriteria;
	}
	
	private Criteria getFineCriteria(){
		if (fineCriteria == null ){
			fineCriteria = new Criteria();
			fineCriteria.setAccuracy(Criteria.ACCURACY_FINE);
		}
		return fineCriteria;
	}
	
	public void start(){
		setCapturingState(LocationState.UNKNOWN);
	}
	
	public void stop(){
		locationManager.removeUpdates(locationListener);		
	}
	
	public void setCapturingState(LocationState state){
		long minTime = 0;
		float minDistance = 0;
		Criteria criteria = null;
		if (state == null || state == LocationState.UNKNOWN){
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
	
	private void fireNewLocation(Location location){
		Iterator<Handler> handlers;
		synchronized(this.handlers){
			handlers = 
				new ArrayList<Handler>(this.handlers).iterator();
		}
		
		/**
		 * Send the Message to every registered event Handler
		 */
		while(handlers.hasNext()){
			Handler handler = handlers.next();
			
			Message msg = handler.obtainMessage(MSG_NEW_LOCATION,location);
			handler.sendMessage(msg);
		}
	}
		
	
	
	LocationListener locationListener = new LocationListener(){
		//TODO

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			//newLocation(location);
			fireNewLocation(location);
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
