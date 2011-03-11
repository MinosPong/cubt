package edu.cuhk.cubt.classifier;

/**
 * interface of classifier
 * @author PKM
 *
 */
public interface Classifier {
	
	/**
	 * Request the classifier to process
	 */
	public void start();
	public void stop();
	
	public void process();
}
