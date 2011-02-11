package edu.cuhk.cubt.classifier;

import edu.cuhk.cubt.state.LocationState;
import edu.cuhk.cubt.store.LocationHistory;
import edu.cuhk.cubt.util.CuhkLocation;
import android.location.Location;

public class LocationClassifier extends AbstractClassifier<LocationState>
	implements Classifier{

	ClassifierManager manager;
	
	LocationHistory locationHistory;
	
	public LocationClassifier(ClassifierManager manager) {
		
		super(LocationState.UNKNOWN);
		
		this.manager = manager;
	}
	
	public Location getLastLocation(){
		return locationHistory.getLast();
	}
	
	@Override
	protected void processClassification(){
		CuhkLocation cuhkLocation = CuhkLocation.getInstance();
		Location location = getLastLocation();
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
	

}
