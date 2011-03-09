package edu.cuhk.cubt.classifier;

import android.location.Location;
import edu.cuhk.cubt.bus.Poi;
import edu.cuhk.cubt.bus.Stop;
import edu.cuhk.cubt.state.PoiState;
import edu.cuhk.cubt.store.LocationHistory;
import edu.cuhk.cubt.store.PoiData;

public class PoiClassifier extends AbstractClassifier<PoiState> {


	ClassifierManager manager;
	
	LocationHistory locationHistory;
	
	Location location;
	
	Poi poi;
	
	public PoiClassifier(ClassifierManager manager, LocationHistory locationHistory) {
		super(PoiState.UNKNOWN);
		this.locationHistory = locationHistory;
		this.manager = manager;
	}
	
	public Location getLocation(){
		return location;
	}
	
	public Poi getPoi(){
		return poi;
	}

	@Override
	protected void processClassification() {
		if(locationHistory.getLast() == location) return;
		
		location = locationHistory.getLast();
		
		poi = PoiData.getPoiByLocation(location);
		
		if(poi==null){
			this.setState(PoiState.OUTSIDE_POI);
		}else if(poi instanceof Stop){
			this.setState(PoiState.INSIDE_BUS_STOP);
		}else{
			this.setState(PoiState.INSIDE_CHECKPOINT);
		}
		
	}

}
