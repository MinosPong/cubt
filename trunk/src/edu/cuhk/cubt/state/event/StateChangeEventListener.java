package edu.cuhk.cubt.state.event;

import java.util.EventListener;

public interface StateChangeEventListener
	extends EventListener {
	
	public void stateChanged(StateChangeEvent<?> evt);
	
}
