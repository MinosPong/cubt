package edu.cuhk.cubt.bus;

import java.util.Collection;

import android.location.Location;
import android.text.format.Time;

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
	 * @param millis The UTC time
	 * @param location The location
	 * @author Kalyn
	 * @return Collection<Route> The possible route
	 */
	public static Collection<Route> findRoutesByLocationTime(long millis, Location location){
		//TODO
		Time time = new Time();
		time.set(millis);
		
		
		return null;
	}
	
	
	
	
	

}
