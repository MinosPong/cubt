package edu.cuhk.cubt.sccm.classifier;

import java.util.Hashtable;
import java.util.Map;


public class ClassifierManager {

	
    private final Map<String, Classifier> classifiers
        = new Hashtable<String, Classifier>();
	    
    public ClassifierManager(){
    }
   
	public void initialize()
		throws UnsupportedOperationException{
		
			LocationClassifier locationClassifier = new LocationClassifier(this);
			addClassifier(LocationClassifier.class, locationClassifier);

			PoiClassifier poiClassifier = new PoiClassifier(this);
			addClassifier(PoiClassifier.class, poiClassifier);
			
			SpeedClassifier speedClassifier = new SpeedClassifier(this);
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
