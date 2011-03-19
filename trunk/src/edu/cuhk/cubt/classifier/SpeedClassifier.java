package edu.cuhk.cubt.classifier;

import android.location.Location;
import android.os.Handler;
import android.os.Message;
import edu.cuhk.cubt.sccm.LocationSensor;
import edu.cuhk.cubt.state.SpeedState;

public class SpeedClassifier extends AbstractClassifier<SpeedState> {
	
	private float SPEED_MOVING_AVERAGE = (float) 0.5;
	
	private float speed = 0;
	
	LocationSensor locationSensor;
	ClassifierManager manager;
	
	private Location lastLocation = null;
	private Location newLocation = null;
	
	public SpeedClassifier(ClassifierManager manager, 
			LocationSensor locationSensor) {
		super(SpeedState.UNKNOWN);
		this.locationSensor = locationSensor;
		this.manager = manager;
	}

	public float getSpeed() {
		return speed;
	}
	
	@Override
	protected void processClassification() {
		if(locationSensor.getLastLocation() == null ||
				locationSensor.getLastLocation() == newLocation) return;
		
		lastLocation = newLocation;
		newLocation = locationSensor.getLastLocation();
		
		if(lastLocation == null) return;
	
		// Calculate the speed
		float smav = SPEED_MOVING_AVERAGE;
		
		if(lastLocation != null){
			long timeInterval = newLocation.getTime() - lastLocation.getTime();
			smav = (timeInterval < 1000)? smav = (float) 0.7 : (float) (1.5/ Math.log10(timeInterval)); 
		}
		
		//remove smav for test
		smav = 0;
		
		speed = speed * (smav) + lastLocation.getSpeed() * (1 - smav);
		
		//Do classification
		if(speed > 10){
			this.setState(SpeedState.HIGH);
		}else if(speed >3){
			this.setState(SpeedState.NORMAL);
		}else if(speed > 0.2){
			this.setState(SpeedState.SLOW);
		}else{
			this.setState(SpeedState.STILL);
		}
	}

	@Override
	protected void onStart() {
		locationSensor.addHandler(mHandler);
		processClassification();
	}

	@Override
	protected void onStop() {
		locationSensor.removeHandler(mHandler);
	}
	
	Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			switch(msg.what){
			case LocationSensor.MSG_NEW_LOCATION:
				processClassification();
				break;
			}
		}
	};
}
