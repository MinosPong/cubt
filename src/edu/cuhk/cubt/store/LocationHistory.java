package edu.cuhk.cubt.store;

import java.nio.BufferUnderflowException;

import android.location.Location;

public class LocationHistory extends CircularBuffer<Location>{

	public static final int DEFAULT_LOCATION_HISTORY_SIZE_LIMIT = 20;	//buffer size
	
	public LocationHistory(){
		this(DEFAULT_LOCATION_HISTORY_SIZE_LIMIT);
	}
	
	public LocationHistory(int bufferSize) {
		super(bufferSize);
	}	
	
	/**
	 * 
	 * @param location
	 */
	public void add(Location location)
		throws IllegalArgumentException{
		
		
		assertNewLocation(location);		
		super.add(location);
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
			if(!location.hasSpeed()){
				location.setSpeed(location.distanceTo(lastLocation) * 1000 / 
						(location.getTime() - lastLocation.getTime() ));
			}
		}
	}

}
