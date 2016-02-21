package edu.cuhk.cubt.bus;

import android.location.Location;

/**
 * An implementation of a POI (Point of interest).
 * It added some features: name, range
 * Adaptive Pattern of <tt>Location</tt>, Please check android.location.Location for more detail
 * @author PKM
 *
 */
public class Poi{
	
	protected Location location;
	
	protected String name;
	protected float range;
	
	protected static final String LOCATION_PROVIDER = "CUBT_POI";
	
	protected int type;
	
	/**
	 * 
	 * @param name
	 * @param latitude
	 * @param longitude
	 * @param range
	 */
	public Poi(String name, double latitude, double longitude, float range){
		location = new Location(LOCATION_PROVIDER);
		location.setLatitude(latitude);
		location.setLongitude(longitude);
		this.range = range;
		this.name = name;
	};
	
	public String getName(){
		return name;
	}
	
	public float getRange(){
		return range;
	}
	
	public double getLatitude(){
		return location.getLatitude();
	}
	
	public double getLongitude(){
		return location.getLongitude();
	}
	
	/**
	 * @see android.location.Location.bearingTo()
	 */
	public double bearingTo(Location dest){
		return location.bearingTo(dest);
	}
	
	/**
	 * @see android.location.Location.distanceTo()
	 */
	public double distanceTo(Location dest){
		return location.distanceTo(dest);
	}
	
	/**
	 * Returns true if the input <tt>Location</tt> inside the range of this POI
	 * @param dest Location
	 * @return true if the input <tt>Location</tt> inside the range of this POI
	 */
	public boolean isCovered(Location dest){
		return location.distanceTo(dest) <= this.range;
	}
	
}
