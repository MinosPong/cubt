package edu.cuhk.cubt.classifier;

import java.util.Hashtable;
import java.util.Map;

import android.content.Context;
import android.location.LocationManager;

public class ClassifierManager {
	
	private Context context;

	private boolean bVirtual = false;

    private final Map<String, Classifier> classifiers
        = new Hashtable<String, Classifier>();
	
	
	protected void initialize()
		throws UnsupportedOperationException{
		
		if(!bVirtual){
			
			//init Location Classifier
			LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
			if(locationManager == null){
				throw new UnsupportedOperationException("Location serivce not exist");
			}
			LocationClassifier locationClassifier = new LocationClassifier(this);
			addClassifier(LocationClassifier.class, locationClassifier);

			PoiClassifier poiClassifier = new PoiClassifier();
			addClassifier(PoiClassifier.class, poiClassifier);
			
			SpeedClassifier speedClassifier = new SpeedClassifier();
			addClassifier(SpeedClassifier.class, speedClassifier);
			
			
			
		}	
		
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
