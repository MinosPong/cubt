package edu.cuhk.cubt.state;


/**
 * The LocationState class reflects the current state of a location 
 * @author PKM
 *
 */
public class LocationState implements State{

	public static final int TYPE_ID = State.TYPE_LOCATION;
	
	public static final String _FAR_FROM_CUHK = "Far from CUHK";
	
	public static final LocationState FAR_FROM_CUHK = 
		new LocationState(_FAR_FROM_CUHK);
	

	public static final String _CLOSE_TO_CUHK = "Close to CUHK";
	
	public static final LocationState CLOSE_TO_CUHK = 
		new LocationState(_CLOSE_TO_CUHK);
	
	
	public static final String _INSIDE_CUHK = "Inside CUHK";
	
	public static final LocationState INSIDE_CUHK = 
		new LocationState(_INSIDE_CUHK);
	
	
	public static final String _INSIDE_BUS_STOP = "Inside Bus Stop";
	
	public static final LocationState INSIDE_BUS_STOP = 
		new LocationState(_INSIDE_BUS_STOP);
	
	
	public static final String _INSIDE_CHECKPOINT = "Inside Check Point";
	
	public static final LocationState INSIDE_CHECKPOINT = 
		new LocationState(_INSIDE_CHECKPOINT);
	
	
	public static final String _OUTSIDE_POI = "Outside POI";

	public static final LocationState OUTSIDE_POI = 
		new LocationState( _OUTSIDE_POI);
	
	
	public static final String _UNKNOWN = "Unknown Location";
	
	public static final LocationState UNKNOWN = 
		new LocationState(_UNKNOWN);
	
	
	
	private String locationState;
	
	private LocationState(String locationState){
		this.locationState = locationState;
	}
	
	public String getStateString(){
		return locationState;
	}
	
	/**
	 * Returns a string representation of this location state
	 *  
     * @return a string representation of this object
	 */
	public String toString(){
		return getClass().getName() + ":" + getStateString();
	}

	@Override
	public int getTypeID() {
		return TYPE_ID;
	}
	
}
