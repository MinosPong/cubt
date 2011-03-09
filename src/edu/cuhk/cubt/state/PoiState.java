package edu.cuhk.cubt.state;


/**
 * The LocationState class reflects the current state of a location 
 * @author PKM
 *
 */
public class PoiState implements State{

	public static final int TYPE_ID = State.TYPE_POI;
	
	
	public static final String _INSIDE_BUS_STOP = "Inside Bus Stop";
	
	public static final PoiState INSIDE_BUS_STOP = 
		new PoiState(_INSIDE_BUS_STOP);

	
	public static final String _INSIDE_CHECKPOINT = "Inside Check Point";
	
	public static final PoiState INSIDE_CHECKPOINT = 
		new PoiState(_INSIDE_CHECKPOINT);
	
	
	public static final String _OUTSIDE_POI = "Outside POI";

	public static final PoiState OUTSIDE_POI = 
		new PoiState( _OUTSIDE_POI);
	
	
	public static final String _UNKNOWN = "Unknown Location";
	
	public static final PoiState UNKNOWN = 
		new PoiState(_UNKNOWN);
	
	
	
	private String locationState;
	
	private PoiState(String locationState){
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
