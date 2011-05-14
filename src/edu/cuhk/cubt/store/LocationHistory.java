package edu.cuhk.cubt.store;

import java.nio.BufferUnderflowException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import android.location.Location;
import android.os.Handler;
import android.os.Message;

public class LocationHistory extends CircularBuffer<Location>{

	public static final int DEFAULT_LOCATION_HISTORY_SIZE_LIMIT = 20;	//buffer size
	public static final int MSG_LOCATION_HISTORY_UPDATE = 25101;
	
	private static LocationHistory locationHistory = null;
		
	public static LocationHistory getInstance(){
		if(locationHistory == null){
			locationHistory = new LocationHistory();
		}
		return locationHistory;		
	}
	
	private LocationHistory(){
		this(DEFAULT_LOCATION_HISTORY_SIZE_LIMIT);
	}
	
	private LocationHistory(int bufferSize) {
		super(bufferSize);
	}	
	
	/**
	 * 
	 * @param location
	 */
	public void add(Location location)
		throws IllegalArgumentException{
		
		//assertNewLocation(location);
		super.add(location);
		fireLocationHistoryUpdate();
	}
		
	/**
	 * Verifies the input location
	 * @param location
	 * @throws IllegalArgumentException
	 */
	private void assertNewLocation(Location location)
		throws IllegalArgumentException{
		Location lastLocation =null;
		try{
			lastLocation = (Location) getLast();
		}catch (BufferUnderflowException e){}
		if(lastLocation != null){
			
			if(lastLocation.getTime() > location.getTime()) 
			{
				throw new IllegalArgumentException(
						"The location is older than the existed one!");
			}
			
			//Calculate the speed if it is not exist in the location point
			/*if(!location.hasSpeed()){
				location.setSpeed(location.distanceTo(lastLocation) * 1000 / 
						(location.getTime() - lastLocation.getTime() ));
			}*/
		}
	}

	
	private void fireLocationHistoryUpdate(){
		
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
						
			Message msg = handler.obtainMessage(MSG_LOCATION_HISTORY_UPDATE);
			handler.sendMessage(msg);
		}
		
	}
	

	private List<Handler> handlers = new Vector<Handler>();
	
	/**
	 * 
	 * @param handler location history changed handler
	 */
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
	
}
