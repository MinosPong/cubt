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
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class LocationSensor {

	private static final String tag = "LocationSensor"; 
	
	Criteria coarseCriteria = null;
	Criteria fineCriteria = null;
	
	public static final int MSG_NEW_LOCATION = 14101;
	public static final int MSG_PROVIDER_DISABLE = 14102;
	public static final int MSG_PROVIDER_ENABLE = 14103;
	public static final int MSG_PROVIDER_STATUS_CHANGE = 14104;

	public static final int STATE_UNKNOWN = 0;
	public static final int STATE_FAR = 1;
	public static final int STATE_CLOSE = 2;
	public static final int STATE_INSIDE = 3;
	public static final int STATE_HOT = 4;
	
	private int capturingState = 0;

	private String provider = "";
	
	LocationManager locationManager;
	SCCMEngine engine;
	Location lastLocation = null;
	
	private boolean isStart = false;
	
	private boolean isVirtual = false;

	public LocationSensor(LocationManager locationManager) {
		this.locationManager = locationManager;
	}
	
	public void setVirtual(boolean b){
		Log.d(tag, "Virtual sensor? " + b);
		if(b == isVirtual) return;
		isVirtual = b;
		stop();
		start();
	}

	private List<Handler> handlers = new Vector<Handler>();;
	

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
	
	public Location getLastLocation(){
		return lastLocation;
	}
	
	public void start(){
		if(isStart) return;
		isStart = true;
		setCapturingState(STATE_UNKNOWN);
	}
	
	public void stop(){
		if(!isStart) return;
		isStart = false;
		
		locationManager.removeUpdates(locationListener);
		VirtualLocationSensor.getInstance().removeListener(locationListener);
	}
	
	public void setCapturingState(LocationState state){
		if(state == null || state == LocationState.UNKNOWN) setCapturingState(STATE_UNKNOWN);
		else if(state == LocationState.INSIDE_CUHK) setCapturingState(STATE_INSIDE);
		else if(state == LocationState.CLOSE_TO_CUHK) setCapturingState(STATE_CLOSE);
		else if(state == LocationState.FAR_FROM_CUHK) setCapturingState(STATE_FAR);			
	}
	
	
	public void setCapturingState(int state){
		if(isVirtual){
			setVirtualLocationListener();
			return;
		}
		
		long minTime = 0;
		float minDistance = 0;
		capturingState = state;
		if (state == 0 || state == STATE_UNKNOWN){
			provider = getCoarseProvider();
			minTime = 3000;
	    }else if(state == STATE_HOT){
			provider = getFineProvider();
			minTime = 1000;		
	    }else if(state == STATE_INSIDE){
			provider = getFineProvider();
			minTime = 15 * 1000;			
		}else if(state == STATE_CLOSE){
			provider = getCoarseProvider();
			minTime = 60 * 1000;						
		}else if(state == STATE_FAR){
			provider = getCoarseProvider();	
			minTime = 10 * 60 * 1000;			
		}			

		Log.i(tag,"Provdier Changed:" + provider + ", minTime:" + minTime);
		fireMessageToHandlers(MSG_PROVIDER_STATUS_CHANGE,"Provider Change " + provider + ",minTime:" + minTime);
		//locationManager.removeUpdates(locationListener);
		locationManager.requestLocationUpdates(
				provider, 
				minTime, 
				minDistance, 
				locationListener);
	}
	
	private void setVirtualLocationListener(){
		VirtualLocationSensor.getInstance().setListener(locationListener);
		fireMessageToHandlers(MSG_PROVIDER_STATUS_CHANGE,"Provider Change: Virtual Sensor");
	}
	
	protected void fireNewLocation(Location location){
		fireMessageToHandlers(MSG_NEW_LOCATION,location);
	}

	protected void fireMessageToHandlers(int what, Object obj){
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
			
			Message msg = handler.obtainMessage(what,obj);
			handler.sendMessage(msg);
		}		
	}
	
	private String getCoarseProvider(){
		return LocationManager.NETWORK_PROVIDER;
	}
	
	private String getFineProvider(){
		return LocationManager.GPS_PROVIDER;
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
			fineCriteria.setSpeedRequired(true);
		}
		return fineCriteria;
	}
	
	private boolean fromValidProvider(Location location){
		if(!provider.equals(location.getProvider()))
			Log.i(tag, "Location from invalid provider");
		return provider.equals(location.getProvider());
	}
	
	LocationListener locationListener = new LocationListener(){
		//TODO

		@Override
		public void onLocationChanged(Location location) {
			if(lastLocation != location){
				if(isVirtual || fromValidProvider(location)){
					lastLocation = location;
					fireNewLocation(location);
				}
			}
		}

		@Override
		public void onProviderDisabled(String provider) {
			Log.i(tag, "Provider Disabled" + provider);
			fireMessageToHandlers(MSG_PROVIDER_DISABLE,"Provider Disabled" + provider);
		}

		@Override
		public void onProviderEnabled(String provider) {
			Log.i(tag, "Provider Enabled" + provider);
			fireMessageToHandlers(MSG_PROVIDER_ENABLE,"Provider Enabled" + provider);
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			String msg = "";
			switch(status){
			case LocationProvider.AVAILABLE:
				msg = "avaliable";
				break;
			case LocationProvider.OUT_OF_SERVICE:
				msg = "out of service";
				break;
			case LocationProvider.TEMPORARILY_UNAVAILABLE:
				msg = "temporarily unavailable";
				break;				
			}
			fireMessageToHandlers(MSG_PROVIDER_STATUS_CHANGE, "Status Changed " + provider + ":" + msg);
			Log.i(tag, "Status Changed " + provider + ":" + msg);
		}	
	};
}
