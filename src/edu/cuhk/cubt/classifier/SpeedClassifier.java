package edu.cuhk.cubt.classifier;

import android.location.Location;
import edu.cuhk.cubt.state.SpeedState;

public class SpeedClassifier extends AbstractClassifier<SpeedState> {
	
	private float SPEED_MOVING_AVERAGE = (float) 0.5;
	
	private float speed = 0;
	
	private Location lastLocation;
	private Location newLocation;
	
	public SpeedClassifier() {
		super(SpeedState.UNKNOWN);
	}

	public float getSpeed() {
		return speed;
	}
	
	@Override
	protected void processClassification() {
		if(newLocation == null) return;
	
		// Calculate the speed
		float smav = SPEED_MOVING_AVERAGE;
		
		if(lastLocation != null){
			long timeInterval = newLocation.getTime() - lastLocation.getTime();
			smav = (timeInterval < 1000)? smav = (float) 0.7 : (float) (1.5/ Math.log10(timeInterval)); 
		}
		
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
	};
}
