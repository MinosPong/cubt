package edu.cuhk.cubt.classifier;

import java.util.Hashtable;
import java.util.Map;

import edu.cuhk.cubt.SCCMEngine;

public class ClassifierManager {

    private final Map<String, Classifier> classifiers
        = new Hashtable<String, Classifier>();
	
    private SCCMEngine engine;
    
    public ClassifierManager(SCCMEngine engine){
    	this.engine = engine;
    }
   
	public void initialize()
		throws UnsupportedOperationException{
		
			
			LocationClassifier locationClassifier = new LocationClassifier(this, engine.getLocationHistory());
			addClassifier(LocationClassifier.class, locationClassifier);

			PoiClassifier poiClassifier = new PoiClassifier(this, engine.getLocationHistory());
			addClassifier(PoiClassifier.class, poiClassifier);
			
			SpeedClassifier speedClassifier = new SpeedClassifier(this, engine.getLocationHistory());
			addClassifier(SpeedClassifier.class, speedClassifier);	
			

	}
	
	protected <T extends Classifier> void addClassifier(
			Class<T> classifierClass, 
			T classifier)
	{
		classifiers.put(classifierClass.getName(), classifier);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Classifier> T getClassifier(Class<T> classifierClass){
		return (T)classifiers.get(classifierClass.getName());		
	}
	
}
