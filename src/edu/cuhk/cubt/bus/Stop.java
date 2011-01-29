package edu.cuhk.cubt.bus;

import android.location.Location;

public class Stop extends Poi {	

	public static final String LOCATION_PROVIDER = "CUBT_STOP";

	public Stop(String name, double latitude, double longitude, float range) {
		super(name, latitude, longitude, range);
	}

	public static Stop getByLocation(Location dest){
		//TODO
		
		return null;
	}
}
