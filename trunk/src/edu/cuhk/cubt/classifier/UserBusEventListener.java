package edu.cuhk.cubt.prediction;

import java.util.EventListener;

/**
 * Receives events notifying of changes that occur within a user and bus
 * location interaction
 * @author PKM
 *
 */
public interface UserBusEventListener 
	extends EventListener{
	
	/**
     * Indicates that a User has entered Bus
     * 
	 * @param evt The <tt>UserBusEvent</tt> contain information about the change
	 */
	public void busEntered(UserBusEvent evt);

	/**
     * Indicates that a User has left Bus
     * 
	 * @param evt The <tt>UserBusEvent</tt> contain information about the change
	 */
	public void busLeft(UserBusEvent evt);

	/**
     * Indicates that bus user on is stopped
     * 
	 * @param evt The <tt>UserBusEvent</tt> contain information about the change
	 */	
	public void busStopped(UserBusEvent evt);

	/**
     * Indicates that location of bus user on is changed
     * 
	 * @param evt The <tt>UserBusEvent</tt> contain information about the change
	 */		
	public void busLocationChanged(UserBusEvent evt);

}
