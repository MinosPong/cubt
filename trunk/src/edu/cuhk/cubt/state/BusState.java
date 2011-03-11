package edu.cuhk.cubt.state;

public class BusState implements State {

	public static final int TYPE_ID = State.TYPE_BUS;

	
	public static final String _ONBUS = "On Bus";
	public static final BusState ONBUS = 
		new BusState(_ONBUS);
	
	public static final String _OFFBUS = "Not on Bus";
	public static final BusState OFFBUS = 
		new BusState(_OFFBUS);
	
	public static final String _WAITBUS = "Waiting Bus";
	public static final BusState WAITBUS = 
		new BusState(_WAITBUS);
	
	public static final String _UNKNOWN = "Unknown";
	public static final BusState UNKNOWN = 
		new BusState(_UNKNOWN);
	
	
	private String busState;
	
	private BusState(String busState){
		this.busState = busState;
	}
	
	@Override
	public String getStateString(){
		return busState;
	}
	
	@Override
	public int getTypeID() {
		return TYPE_ID;
	}


}
