package edu.cuhk.cubt.classifier;

import java.util.EventObject;

import android.location.Location;

public class UserBusEvent 
	extends EventObject{

	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = 0L;

	
	public UserBusEvent(Location location) {
		super(location);
		// TODO Auto-generated constructor stub
	}
	
	public Location getLocation(){
		return (Location) super.source;
	}


}
