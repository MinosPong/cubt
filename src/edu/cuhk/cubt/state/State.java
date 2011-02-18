package edu.cuhk.cubt.state;

public interface State {
		
	public static final int TYPE_START 		= 0;
	
	public static final int TYPE_UNKNOWN 	= TYPE_START + 1; 
	public static final int TYPE_LOCATION 	= TYPE_START + 2;
	public static final int TYPE_SPEED 		= TYPE_START + 3;
	
	public int getTypeID();
	
}
