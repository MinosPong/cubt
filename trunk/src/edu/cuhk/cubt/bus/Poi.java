package edu.cuhk.cubt.bus;

import android.location.Location;

/**
 * An implementation of a POI (Point of interest).
 * It is a subclass of <tt>Location</tt>, Please check android.location.Location for more detail
 * It added some feature, like name, range, type.
 * @author PKM
 *
 */
public class Poi extends Location{
	
	protected String name;
	protected float range;
	protected int type;
	
	private static final String LOCATION_PROVIDER = "CUBT_POI";
	
	public Poi(String name, double latitude, double longitude, float range){
		super(LOCATION_PROVIDER);
		this.setLatitude(latitude);
		this.setLongitude(longitude);
		this.range = range;
		this.name = name;
	};
	
	
	
	
}
