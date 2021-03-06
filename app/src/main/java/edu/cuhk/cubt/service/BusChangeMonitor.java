package edu.cuhk.cubt.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.cuhk.cubt.bus.Poi;
import edu.cuhk.cubt.bus.Route;
import edu.cuhk.cubt.bus.RoutePrediction;
import edu.cuhk.cubt.bus.Stop;
import edu.cuhk.cubt.bus.BusEventObject;
import edu.cuhk.cubt.db.DbTravelLocation;
import edu.cuhk.cubt.net.BusLocationUploader;
import edu.cuhk.cubt.sccm.LocationSensor;
import edu.cuhk.cubt.sccm.classifier.BusClassifier;
import edu.cuhk.cubt.state.State;
import edu.cuhk.cubt.ui.CubtService;
import android.location.Location;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * The Bus Change Monitor Track the bus event and change of bus location.
 * Upload the Bus Location information to the server 
 * @author PKM
 *
 */
public class BusChangeMonitor implements IServiceMonitor{
	
	BusClassifier busClassifier;
	LocationSensor locationSensor;
	DbTravelLocation db;
	int tid;
	List<Stop> busStopList = new ArrayList<Stop>();
	
	boolean isOnBus = false;
		
	public void start(CubtService service){
		locationSensor = service.getSCCMEngine().getLocationSensor();
		db = DbTravelLocation.getInstance(service.getApplicationContext());
		busClassifier = service.getSCCMEngine().getClassifierManager().getClassifier(BusClassifier.class);
		busClassifier.addHandler(handler);
	}
	
	public void stop(CubtService service){
		locationSensor.removeHandler(handler);
		busClassifier.removeHandler(handler);
		if(isOnBus){
			BusLocationUploader.remove();
		}
	}
	
	protected void busLocationChange(Location location){
		BusLocationUploader.updateLocation(location.getLatitude(), location.getLongitude());
		db.insert(tid,locationSensor.getLastLocation());
	}
	
	protected void busEnterEvent(BusEventObject evt){
		
		busStopList.clear();
		BusLocationUploader.add(locationSensor.getLastLocation());
		addBusStop(evt);

		tid = db.getTid();
		db.insert(tid,locationSensor.getLastLocation());
		
		locationSensor.addHandler(handler);
		
		isOnBus = true;
	}
	
	protected void busExitEvent(BusEventObject evt){
		isOnBus = false;		

		
		addBusStop(evt);	
		locationSensor.removeHandler(handler);	
		
		BusLocationUploader.remove();
	}
	
	protected void busStopPassedEvent(BusEventObject evt){
		addBusStop(evt);
	}
	
	private void addBusStop(BusEventObject evt){
		BusLocationUploader.addStopEvent(evt);
		
		Poi poi = evt.getStop();
		long time = evt.getLeaveTime();
		busStopList.add((Stop) poi);
		
		Log.w("Route Prediction", "Stop Passed: " + poi.getName());
		List<Route> routes = RoutePrediction.getRoutesByPassedStop(time,busStopList);
		Iterator<Route> iter = routes.iterator();
		int i = 1;
		while(iter.hasNext()){
			Log.w("Route Prediction", (i++) + ":" + iter.next().getName());
		}		
	}
	
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			
			switch(msg.what){
				case State.TYPE_BUS:
					/*@SuppressWarnings("unchecked")
					StateChangeEvent<BusState> evt = (StateChangeEvent<BusState>)msg.obj;
					if(evt.getNewState().equals(BusState.ONBUS)){
						busEnterEvent();
					}else if(
						evt.getOldState().equals(BusState.ONBUS))
					{		
						busExitEvent();	
					}*/
					break;
				case BusEventObject.BUS_ENTER_EVENT:
					busEnterEvent((BusEventObject) msg.obj);
					break;
				case BusEventObject.BUS_EXIT_EVENT:
					busExitEvent((BusEventObject) msg.obj);
					break;					
				case BusEventObject.BUS_STOP_PASSED_EVENT:	
					busStopPassedEvent((BusEventObject) msg.obj);
					break;
				case LocationSensor.MSG_NEW_LOCATION:					
					if(isOnBus) busLocationChange((Location) msg.obj);
					break;
			}
			super.handleMessage(msg);
		}
	};
}
