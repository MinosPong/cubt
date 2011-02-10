package edu.cuhk.cubt.classifier;

import edu.cuhk.cubt.bus.Stop;
import edu.cuhk.cubt.state.LocationState;
import edu.cuhk.cubt.store.LocationHistory;
import android.location.Location;

public class LocationClassifier extends AbstractClassifier<LocationState>
	implements Classifier{

	ClassifierManager manager;
	
	LocationHistory locationHistory;
	
	public LocationClassifier(ClassifierManager manager, 
			LocationHistory locationHistory) {
		
		super(LocationState.UNKNOWN);
		
		this.manager = manager;
		this.locationHistory = locationHistory;
	}
	
	public Location getLastLocation(){
		return locationHistory.getLast();
	}
	
	@Override
	protected void processClassification(){
		CuhkLocation cuhkLocation = CuhkLocation.getInstance();
		Location location = getLastLocation();
		switch(cuhkLocation.getDistanceToDescription(location))
		{
			case CuhkLocation.FAR:
				this.setState(LocationState.FAR_FROM_CUHK);
				break;
				
			case CuhkLocation.CLOSE:
				this.setState(LocationState.CLOSE_TO_CUHK);
				break;
				
			case CuhkLocation.INSIDE:
				Stop stop = Stop.getByLocation(location);
				if(stop!=null){
					this.setState(LocationState.INSIDE_BUS_STOP);
					//TODO more on stop
				}else{
					this.setState(LocationState.INSIDE_CUHK);
				}
				break;
		}
	}
	
	private static class CuhkLocation extends Location
	{		
		private static final double altitude = 22.419005;
		private static final double longitude = 114.206904;
		
		private static final double CUHK_RANGE = 1000;
		private static final double CUHK_CLOSE_RANGE = 4000;
		
		public static final int INSIDE = 1;
		public static final int CLOSE = 2;
		public static final int FAR = 3;
		
		private static CuhkLocation cuhkLocation = null;
		
		public static CuhkLocation getInstance(){
			if(cuhkLocation==null)
				cuhkLocation = new CuhkLocation();
			return cuhkLocation;
		}
		
		private CuhkLocation() {
			super("CUBT");
			this.setAltitude(altitude);
			this.setLongitude(longitude);
		}

		public int getDistanceToDescription(Location location){
			double distance = this.distanceTo(location);
			if(distance < CUHK_RANGE)
				return INSIDE;
			if(distance < CUHK_CLOSE_RANGE)
				return CLOSE;
			else
				return FAR;
		}
		
	}

}
