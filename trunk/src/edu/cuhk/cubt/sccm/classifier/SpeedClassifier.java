package edu.cuhk.cubt.sccm.classifier;

import android.location.Location;
import android.os.Message;
import edu.cuhk.cubt.sccm.LocationSensor;
import edu.cuhk.cubt.state.SpeedState;

public class SpeedClassifier extends AbstractClassifier<SpeedState> {
	
	private float SPEED_MOVING_AVERAGE = (float) 0.5;
	
	private float speed = 0;
	
	ClassifierManager manager;
	
	private Location lastLocation = null;
	private Location newLocation = null;
	private Location capturedLocation;
	
	SpeedClassifier(ClassifierManager manager) {
		super(SpeedState.UNKNOWN);
		this.manager = manager;
	}

	public float getSpeed() {
		return speed;
	}
	
	@Override
	protected void processClassification() {
		if(capturedLocation == null ||
				this.newLocation == capturedLocation ) return;
		
		
		lastLocation = newLocation;
		this.newLocation = capturedLocation;
		
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
		processClassification();
	}

	@Override
	protected void onStop() {
	}
	
	@Override
	void handleMessage(Message msg){
		switch(msg.what){
		case LocationSensor.MSG_NEW_LOCATION:
			capturedLocation = (Location)msg.obj;
			processClassification();
			break;
		}
	};
}
