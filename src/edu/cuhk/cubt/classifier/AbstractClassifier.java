package edu.cuhk.cubt.classifier;

import edu.cuhk.cubt.state.State;

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
	
	/**
	 * Creates a <tt>StateChangeEvent</tt> with this class as
     * <tt>sourceClassifier</tt>,  and the specified <tt>eventID</tt> and old and new
     * values and dispatches it on all currently registered listeners.
	 * @param oldState
	 * @param newState
	 */
	protected void fireStateChangeEvent(T oldState, T newState){
		//TODO Create Event
		//TODO call state change listener
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
}
