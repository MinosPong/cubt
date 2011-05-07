package edu.cuhk.cubt.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.cuhk.cubt.bus.Poi;
import edu.cuhk.cubt.bus.Route;
import edu.cuhk.cubt.bus.RoutePrediction;
import edu.cuhk.cubt.bus.Stop;
import edu.cuhk.cubt.net.BusLocationUploader;
import edu.cuhk.cubt.sccm.LocationSensor;
import edu.cuhk.cubt.sccm.SCCMEngine;
import edu.cuhk.cubt.sccm.classifier.BusClassifier;
import edu.cuhk.cubt.sccm.classifier.BusClassifier.BusEventObject;
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

	BusLocationUploader uploader;
	
	BusClassifier busClassifier;
	SCCMEngine engine;
	LocationSensor locationSensor;
	
	List<Stop> busStopList = new ArrayList<Stop>();
	
	boolean isOnBus = false;
		
	public void start(CubtService service){
		this.engine = service.getSCCMEngine();
		locationSensor = engine.getLocationSensor();
		busClassifier = engine.getClassifierManager().getClassifier(BusClassifier.class);
		busClassifier.addHandler(handler);		
	}
	
	public void stop(CubtService service){
		locationSensor.removeHandler(handler);
		busClassifier.removeHandler(handler);		
	}
	
	protected void busLocationChange(Location location){
		uploader.updateLocation(location.getLatitude(), location.getLongitude());
	}
	
	protected void busEnterEvent(BusEventObject evt){
		busStopList.clear();
		addBusStop(evt.getStop());
		
		busEnterEvent();
	}
	
	protected void busEnterEvent(){
		isOnBus = true;
				
		Location location = locationSensor.getLastLocation();
		locationSensor.addHandler(handler);
		
		uploader = new BusLocationUploader();
		uploader.updateLocation(location.getLatitude() , location.getLongitude());		
	}
	
	protected void busExitEvent(BusEventObject evt){
		addBusStop(evt.getStop());
	}
	
	protected void busExitEvent(){	
		isOnBus = false;
		
		locationSensor.removeHandler(handler);
		uploader.remove();
		uploader = null;
	}
	
	protected void busStopPassedEvent(BusEventObject evt){
		addBusStop(evt.getStop());
	}
	
	private void addBusStop(Poi poi){
		busStopList.add((Stop) poi);
		
		Log.w("Route Prediction", "Stop Passed: " + poi.getName());
		List<Route> routes = RoutePrediction.getRoutesByPassedStop(busStopList);
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
				case BusClassifier.BUS_ENTER_EVENT:
					busEnterEvent((BusEventObject) msg.obj);
					break;
				case BusClassifier.BUS_EXIT_EVENT:
					busExitEvent((BusEventObject) msg.obj);
					break;					
				case BusClassifier.BUS_STOP_PASSED_EVENT:	
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
