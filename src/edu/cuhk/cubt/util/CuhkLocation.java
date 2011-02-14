package edu.cuhk.cubt.util;

import android.location.Location;

public class CuhkLocation extends Location{
	
	private static final double altitude = 22.419005;
	private static final double longitude = 114.206904;
	
	private static final double CUHK_RANGE = 1000;
	private static final double CUHK_CLOSE_RANGE = 4000;
	
	public static final int INSIDE = 1;
	public static final int CLOSE = 2;
	public static final int FAR = 3;
	
	private static CuhkLocation cuhkLocation = null;
	
	public static CuhkLocation getInstance(){
		if(cuhkLocation==null)
			cuhkLocation = new CuhkLocation();
		return cuhkLocation;
	}
	
	private CuhkLocation() {
		super("CUBT");
		this.setLatitude(altitude);
		this.setLongitude(longitude);
	}

	public int getDistanceDescriptionTo(Location location){
		double distance = this.distanceTo(location);
		if(distance < CUHK_RANGE)
			return INSIDE;
		if(distance < CUHK_CLOSE_RANGE)
			return CLOSE;
		else
			return FAR;
	}
	
}
