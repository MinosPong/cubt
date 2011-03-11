package edu.cuhk.cubt.classifier;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import edu.cuhk.cubt.state.State;
import edu.cuhk.cubt.state.event.StateChangeEvent;

/**
 * Provides implementations for common <tt>Classifier</tt>
 * It implements a State change handler, which include the state change event
 * The generic class T extends <tt>State</tt>, which is the state that the classifier
 * are concern on.
 * @author PKM
 *
 * @param <T>
 */
public abstract class AbstractClassifier<T extends State> implements Classifier {

	/**
	 * the <tt>State</tt> classify by this classifier
	 */
	private T state;
	private boolean bStarted = false;
	
	private List<Handler> handlers = new Vector<Handler>();;
	
	/**
	 * Constructor of the Abstract Classifier
	 * @param initState define the initialise State
	 */
	public AbstractClassifier(T initState){
		this.state = initState;
	}
	
	protected void setState(T newState){
		T oldState = this.state;
		if(oldState != newState){
			this.state = newState;
			fireStateChangeEvent(oldState, newState);
		}
	}
	
	public T getState(){
		return state;
	}
	
	public void addHandler(Handler handler){
		if(handler == null)
			throw new NullPointerException("handler");
		
		synchronized(handlers)
		{
			if(!handlers.contains(handler))
				handlers.add(handler);
		}
	}
	
	public void removeHandler(Handler handler){
		if(handler != null)
			synchronized(handlers)
			{
				handlers.remove(handler);
			}
	}
	
	/**
	 * Creates a <tt>StateChangeEvent</tt> with this class as
     * <tt>sourceClassifier</tt>,  and the specified <tt>eventID</tt> and old and new
     * values and dispatches it on all currently registered listeners.
	 * @param oldState
	 * @param newState
	 */
	protected void fireStateChangeEvent(T oldState, T newState){
		
		//TODO Create Event - Done
		//TODO call state change listener / Message Handler
		Log.i("State Changed", "State Change : " + oldState.getStateString() + " > " + newState.getStateString());
		
		StateChangeEvent<T> evt = 
			new StateChangeEvent<T>(this, oldState, newState);
		
		Iterator<Handler> handlers;
		synchronized(this.handlers){
			handlers = 
				new ArrayList<Handler>(this.handlers).iterator();
		}
		
		/**
		 * Send the Message to every registered event Handler
		 */
		while(handlers.hasNext()){
			Handler handler = handlers.next();
			
			Log.i(this.getClass().getName(), "Handler: " + handler.getClass().getName() + 
					", State Change : " + oldState.getStateString() + " > " + newState.getStateString());
			
			Message msg = handler.obtainMessage(newState.getTypeID() , evt);
			handler.sendMessage(msg);
		}
	}

	public synchronized void start(){
		if(bStarted) return;
		Log.i(this.getClass().getName(),"Classifier Started - " + this.getClass().getName());
		onStart();
		bStarted = true;
	}
	public synchronized void stop(){
		if(!bStarted) return;
		onStop();
		Log.i(this.getClass().getName(),"Classifier Stopped - " + this.getClass().getName());
		bStarted = false;
	}

	@Override
	/**
	 * Request the classifier to process
	 * The template of classification process
	 */
	public void process() {
		processClassification();
	}

	protected abstract void processClassification();
	
	protected abstract void onStart();
	protected abstract void onStop();
	
}
