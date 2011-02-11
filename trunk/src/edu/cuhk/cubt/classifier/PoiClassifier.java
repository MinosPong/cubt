package edu.cuhk.cubt.classifier;

import android.location.Location;
import edu.cuhk.cubt.bus.Poi;
import edu.cuhk.cubt.bus.Stop;
import edu.cuhk.cubt.state.LocationState;
import edu.cuhk.cubt.store.LocationHistory;
import edu.cuhk.cubt.store.PoiData;

public class PoiClassifier extends AbstractClassifier<LocationState> {


	LocationHistory locationHistory;
	
	public PoiClassifier() {
		super(LocationState.UNKNOWN);
	}
	
	public Location getLastLocation(){
		return locationHistory.getLast();
	}

	@Override
	protected void processClassification() {
		// TODO Auto-generated method stub

		Location location = getLastLocation();
		
		Poi poi= PoiData.getPoiByLocation(location);
		
		if(poi==null){
			this.setState(LocationState.OUTSIDE_POI);
		}else if(poi instanceof Stop){
			this.setState(LocationState.INSIDE_BUS_STOP);
		}else{
			this.setState(LocationState.INSIDE_CHECKPOINT);
		}
		
	}

}
