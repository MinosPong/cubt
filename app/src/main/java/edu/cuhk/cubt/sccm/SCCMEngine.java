package edu.cuhk.cubt.sccm;

import android.content.Context;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import edu.cuhk.cubt.sccm.classifier.BusClassifier;
import edu.cuhk.cubt.sccm.classifier.ClassifierManager;
import edu.cuhk.cubt.sccm.classifier.LocationClassifier;
import edu.cuhk.cubt.sccm.classifier.PoiClassifier;
import edu.cuhk.cubt.sccm.classifier.SpeedClassifier;
import edu.cuhk.cubt.state.BusState;
import edu.cuhk.cubt.state.LocationState;
import edu.cuhk.cubt.state.PoiState;
import edu.cuhk.cubt.state.State;
import edu.cuhk.cubt.state.event.StateChangeEvent;
import edu.cuhk.cubt.ui.Settings;
public class SCCMEngine {
	
	
	Context mContext;
	
	final ClassifierManager classifierManager;
	
	LocationClassifier locationClassifier;
	PoiClassifier poiClassifier;
	BusClassifier busClassifier;
	SpeedClassifier speedClasifier;
	LocationSensor locationSensor;
	
	private boolean isStarted = false;
	
	public SCCMEngine(Context context){
		mContext = context;
		
		classifierManager = new ClassifierManager();
	}
	
	public boolean startEngine(){
		if(isStarted) return false;
		
		//init Location Manager and Location Sensor
		LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
		if(locationManager == null){
			throw new UnsupportedOperationException("Location serivce not exist");
		}
		locationSensor = new LocationSensor(locationManager);
	
		VirtualLocationSensor.setFile(
				mContext.getSharedPreferences(Settings.sharedPreferenceFile,0).
				getString(Settings.PREF_VIRTUAL_FILE, "")
				);
		
		locationSensor.setVirtual(
				mContext.getSharedPreferences(Settings.sharedPreferenceFile,0).
				getBoolean(Settings.PREF_VIRTUAL_SENSOR, false)
				);	
		
		//Initialize Classifiers
		classifierManager.initialize();
		
		locationClassifier = classifierManager.getClassifier(LocationClassifier.class);
		poiClassifier      = classifierManager.getClassifier(PoiClassifier.class);
		busClassifier      = classifierManager.getClassifier(BusClassifier.class);
		speedClasifier     = classifierManager.getClassifier(SpeedClassifier.class);

		locationSensor.addHandler(mHandler);
		
		locationClassifier.addHandler(mHandler);
		poiClassifier.addHandler(mHandler);
		busClassifier.addHandler(mHandler);
		
		
		locationSensor.addHandler(locationClassifier.getInboxHandler());
		
		
		locationClassifier.start();
		locationSensor.start();			

		isStarted = true;
		
		return isStarted;
	}
	
	public boolean stopEngine(){
		if(!isStarted) return true;
	
		locationSensor.stop();			

		locationSensor.removeHandler(locationClassifier.getInboxHandler());
		locationClassifier.stop();

		busClassifier.removeHandler(mHandler);	
		poiClassifier.removeHandler(mHandler);
		locationClassifier.removeHandler(mHandler);

		locationSensor.removeHandler(mHandler);		
		isStarted = false;
		return true;
	}
	
	public LocationSensor getLocationSensor(){
		return locationSensor;
	}	

	public ClassifierManager getClassifierManager(){
		return classifierManager;
	}		
	
	Handler mHandler = new Handler(){
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg){
			switch(msg.what)
			{
				case State.TYPE_LOCATION:
					StateChangeEvent<LocationState> locationEvt = (StateChangeEvent<LocationState>) msg.obj;
					LocationState locationState = locationEvt.getNewState();					
					
					locationSensor.setCapturingState(locationState);
					if(locationState == LocationState.INSIDE_CUHK){
						busClassifier.start();

						locationSensor.addHandler(speedClasifier.getInboxHandler());
						locationSensor.addHandler(poiClassifier.getInboxHandler());
						speedClasifier.start();
						poiClassifier.start();
					}else{
						poiClassifier.stop();
						speedClasifier.stop();
						locationSensor.removeHandler(speedClasifier.getInboxHandler());
						locationSensor.removeHandler(poiClassifier.getInboxHandler());
						busClassifier.stop();
					}
					break;
				case State.TYPE_POI:
					StateChangeEvent<PoiState> poiEvt = (StateChangeEvent<PoiState>) msg.obj;
					PoiState poiState = poiEvt.getNewState();
					if(poiState == PoiState.INSIDE_BUS_STOP ){
						//locationSensor.setCapturingState(LocationSensor.STATE_HOT);						
					}
					break;
				case State.TYPE_BUS:
					StateChangeEvent<BusState> busEvt = (StateChangeEvent<BusState>) msg.obj;	
					BusState busState = busEvt.getNewState();
					if(busState == BusState.OFFBUS){
						locationSensor.setCapturingState(LocationSensor.STATE_INSIDE);
					}else{
						locationSensor.setCapturingState(LocationSensor.STATE_HOT);							
					}
					break;
			}
		}		
	};	
	

}
