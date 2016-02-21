package edu.cuhk.cubt.state;

/**
 * The SpeedState class reflects the current state of a speed
 * @author PKM
 *
 */
public class SpeedState implements State{
	
	public static final int TYPE_ID = State.TYPE_SPEED;
	
	public static final String _HIGH = "Fast";
	
	public static final SpeedState HIGH = 
		new SpeedState(_HIGH);

	public static final String _NORMAL = "Normal";
	
	public static final SpeedState NORMAL = 
		new SpeedState(_NORMAL);
	
	
	public static final String _SLOW = "Slow";
	
	public static final SpeedState SLOW = 
		new SpeedState(_SLOW);
	
	
	public static final String _STILL = "Still";
	
	public static final SpeedState STILL = 
		new SpeedState(_STILL);
	
	
	public static final String _UNKNOWN = "Unknown Location";
	
	public static final SpeedState UNKNOWN = 
		new SpeedState(_UNKNOWN);
	
	
	
	private String speedState;
	
	/**
	 * @param speedState
	 */
	private SpeedState(String speedState){
		this.speedState = speedState;
	}
	
	public String getStateString(){
		return speedState;
	}
	
	/**
	 * Returns a string representation of this speed state
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
