package edu.cuhk.cubt.state.event;

import java.util.EventObject;

import edu.cuhk.cubt.state.State;

public class StateChangeEvent<T extends State> extends EventObject {


	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = 0L;
	
	
	protected T oldState;
	protected T newState;

	
	public StateChangeEvent(Object source, T oldState, T newState) {
		super(source);
		this.oldState = oldState;
		this.newState = newState;
	}

	
	public T getOldState(){
		return oldState;
	}
	
	public T getNewState(){
		return newState;
	}

}
