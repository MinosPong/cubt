package edu.cuhk.cubt.state;

public interface State {
		
	public static final int TYPE_START 		= 0;
	
	public static final int TYPE_UNKNOWN 	= TYPE_START + 1; 
	public static final int TYPE_LOCATION 	= TYPE_START + 2;
	public static final int TYPE_POI 		= TYPE_START + 3;
	public static final int TYPE_SPEED 		= TYPE_START + 4;
	public static final int TYPE_BUS 		= TYPE_START + 5;
	
	public int getTypeID();
	
	public String getStateString();
	
}
