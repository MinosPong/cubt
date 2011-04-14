package edu.cuhk.cubt.classifier;

import java.util.Hashtable;
import java.util.Map;

import edu.cuhk.cubt.sccm.SCCMEngine;

public class ClassifierManager {

    private final Map<String, Classifier> classifiers
        = new Hashtable<String, Classifier>();
	
    private final SCCMEngine engine;
    
    public ClassifierManager(SCCMEngine engine){
    	this.engine = engine;
    }
   
	public void initialize()
		throws UnsupportedOperationException{
		
			LocationClassifier locationClassifier = new LocationClassifier(this, engine.getLocationSensor());
			addClassifier(LocationClassifier.class, locationClassifier);

			PoiClassifier poiClassifier = new PoiClassifier(this, engine.getLocationSensor());
			addClassifier(PoiClassifier.class, poiClassifier);
			
			SpeedClassifier speedClassifier = new SpeedClassifier(this, engine.getLocationSensor());
			addClassifier(SpeedClassifier.class, speedClassifier);	
			
			BusClassifier busClassifier = new BusClassifier(poiClassifier, speedClassifier);
			addClassifier(BusClassifier.class, busClassifier);

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
