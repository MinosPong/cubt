package edu.cuhk.cubt.sccm;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import edu.cuhk.cubt.CubtApplication;
import edu.cuhk.cubt.classifier.BusClassifier;
import edu.cuhk.cubt.classifier.ClassifierManager;
import edu.cuhk.cubt.classifier.LocationClassifier;
import edu.cuhk.cubt.classifier.PoiClassifier;
import edu.cuhk.cubt.classifier.SpeedClassifier;
import edu.cuhk.cubt.state.BusState;
import edu.cuhk.cubt.state.LocationState;
import edu.cuhk.cubt.state.PoiState;
import edu.cuhk.cubt.state.State;
import edu.cuhk.cubt.state.event.StateChangeEvent;
import edu.cuhk.cubt.store.LocationHistory;
import edu.cuhk.cubt.ui.Settings;
public class SCCMEngine {
	
	
	Context mContext;
	
	final ClassifierManager classifierManager;
	final BusChangeMonitor busChangeMonitor;
	
	LocationClassifier locationClassifier;
	PoiClassifier poiClassifier;
	BusClassifier busClassifier;
	SpeedClassifier speedClasifier;
	LocationSensor locationSensor;
	
	
	public SCCMEngine(Context context){
		mContext = context;
		
		classifierManager = new ClassifierManager(this);
		busChangeMonitor = new BusChangeMonitor(this);
	}
	
	public boolean startEngine(){
		//init Location Manager and Location Sensor
		LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
		if(locationManager == null){
			throw new UnsupportedOperationException("Location serivce not exist");
		}
		locationSensor = new LocationSensor(locationManager);
		
		locationSensor.setVirtual(
				mContext.getSharedPreferences(Settings.sharedPreferenceFile,0).
				getBoolean(Settings.PREF_VIRTUAL_SENSOR, false));
		
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
		
		locationClassifier.start();
		locationSensor.start();		
		busChangeMonitor.start();		
		return true;
	}
	
	public boolean stopEngine(){

		busChangeMonitor.stop();	
		locationSensor.stop();	
		locationClassifier.stop();

		busClassifier.removeHandler(mHandler);	
		poiClassifier.removeHandler(mHandler);
		locationClassifier.removeHandler(mHandler);

		locationSensor.removeHandler(mHandler);		
		return true;
	}
	
	public BusChangeMonitor getBusChangeMonitor(){
		return busChangeMonitor;
	}
	
	public LocationSensor getLocationSensor(){
		return locationSensor;
	}	

	public ClassifierManager getClassifierManager(){
		return classifierManager;
	}	
	
	private void newLocation(Location location){
		try{
			LocationHistory locationHistory = ((CubtApplication)mContext.getApplicationContext()).getLocationHistory();
			locationHistory.add(location);
		}catch(Exception e){
			
		}
	}
	
	
	Handler mHandler = new Handler(){
		//TODO
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
						speedClasifier.start();
						poiClassifier.start();
					}else{
						poiClassifier.stop();
						speedClasifier.stop();
						busClassifier.stop();
					}
					break;
				case State.TYPE_POI:
					StateChangeEvent<PoiState> poiEvt = (StateChangeEvent<PoiState>) msg.obj;
					PoiState poiState = poiEvt.getNewState();
					if(poiState == PoiState.INSIDE_BUS_STOP ){
						locationSensor.setCapturingState(LocationSensor.STATE_HOT);						
					}
					break;
				case State.TYPE_BUS:
					StateChangeEvent<BusState> busEvt = (StateChangeEvent<BusState>) msg.obj;	
					BusState busState = busEvt.getNewState();
					if(busState == BusState.OFFBUS){
						locationSensor.setCapturingState(LocationSensor.STATE_INSIDE);
					}
					break;
				case LocationSensor.MSG_NEW_LOCATION:
					newLocation((Location) msg.obj);
					break;
			}
		}		
	};	
	

}
