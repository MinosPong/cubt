package edu.cuhk.cubt.sccm.classifier;

import edu.cuhk.cubt.sccm.LocationSensor;
import edu.cuhk.cubt.state.LocationState;
import edu.cuhk.cubt.util.CuhkLocation;
import android.location.Location;
import android.os.Message;

/**
 * This is a coarse location classifier to determinate the location distance to CUHK 
 * @author PKM
 *
 */
public class LocationClassifier extends AbstractClassifier<LocationState>
	implements Classifier{

	final ClassifierManager manager;
	
	
	private Location location;
	private Location capturedLocation;
	
	LocationClassifier(ClassifierManager manager) 
	{
		super(LocationState.UNKNOWN);
		this.manager = manager;
	}
	
	@Override
	protected void processClassification(){
		if(capturedLocation == null ||
				this.location == capturedLocation ) return;
		
		this.location = capturedLocation;
		
		
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
