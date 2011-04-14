package edu.cuhk.cubt.classifier;

import edu.cuhk.cubt.sccm.LocationSensor;
import edu.cuhk.cubt.state.LocationState;
import edu.cuhk.cubt.util.CuhkLocation;
import android.location.Location;
import android.os.Handler;
import android.os.Message;

/**
 * This is a coarse location classifier to determinate the location distance to CUHK 
 * @author PKM
 *
 */
public class LocationClassifier extends AbstractClassifier<LocationState>
	implements Classifier{

	final ClassifierManager manager;
	
	final LocationSensor locationSensor;
	
	Location location;
	
	public LocationClassifier(ClassifierManager manager, 
			LocationSensor locationSensor) 
	{
		super(LocationState.UNKNOWN);
		this.locationSensor = locationSensor;
		this.manager = manager;
	}
	
	@Override
	protected void processClassification(){
		if(locationSensor.getLastLocation() == null ||
				this.location == locationSensor.getLastLocation() ) return;
		
		this.location = locationSensor.getLastLocation();
		
		
		CuhkLocation cuhkLocation = CuhkLocation.getInstance();
		switch(cuhkLocation.getDistanceDescriptionTo(location))
		{
			case CuhkLocation.FAR:
				this.setState(LocationState.FAR_FROM_CUHK);
				break;
				
			case CuhkLocation.CLOSE:
				this.setState(LocationState.CLOSE_TO_CUHK);
				break;
				
			case CuhkLocation.INSIDE:
				this.setState(LocationState.INSIDE_CUHK);
				break;
		}
	}

	
	@Override
	protected void onStart() {
		locationSensor.addHandler(mHandler);
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
