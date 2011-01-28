package edu.cuhk.cubt.bus;

import java.util.Collection;

import android.location.Location;

/**
 * An implementation of a Timetable.
 * @author PKM
 *
 */
public class Timetable {
	
	public enum OperationDay{
		WEEKDAY,
		SATURDAY,
		SUNDAY,
		HOLIDAY
	};
	
	
	/**
	 * Returns all the routes on the given time and location
	 * 
	 * @param time The time
	 * @param location The location
	 * @author Kalyn
	 * @return Collection<Route> The possible route
	 */
	public static Collection<Route> findRoutesByLocationTime(long time, Location location){
		
		
		return null;
	}
	
	
	

}
