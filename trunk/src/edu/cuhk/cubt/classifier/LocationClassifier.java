package edu.cuhk.cubt.classifier;

import edu.cuhk.cubt.state.LocationState;
import edu.cuhk.cubt.store.LocationHistory;
import edu.cuhk.cubt.util.CuhkLocation;
import android.location.Location;

/**
 * This is a coarse location classifier to determinate the location distance to CUHK 
 * @author PKM
 *
 */
public class LocationClassifier extends AbstractClassifier<LocationState>
	implements Classifier{

	ClassifierManager manager;
	
	LocationHistory locationHistory;
	
	Location location;
	
	public LocationClassifier(ClassifierManager manager, 
			LocationHistory locationHistory) 
	{
		super(LocationState.UNKNOWN);
		this.locationHistory = locationHistory;
		this.manager = manager;
	}

	public Location getLocation(){
		return location;
	}
	
	@Override
	protected void processClassification(){
		if(locationHistory.getLast() == location) return;
		
		location = locationHistory.getLast();
		
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
	

}
