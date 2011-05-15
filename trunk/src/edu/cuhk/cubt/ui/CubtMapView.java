package edu.cuhk.cubt.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

import edu.cuhk.cubt.R;
import edu.cuhk.cubt.bus.BusEventObject;
import edu.cuhk.cubt.bus.Route;
import edu.cuhk.cubt.bus.RoutePrediction;
import edu.cuhk.cubt.bus.Stop;
import edu.cuhk.cubt.net.BusActivityRequest;
import edu.cuhk.cubt.net.BusActivityRequest.BusActivityRecord;
import edu.cuhk.cubt.net.BusPassedStopRequest;
import edu.cuhk.cubt.store.LocationHistory;
import edu.cuhk.cubt.store.RouteData;
import edu.cuhk.cubt.ui.com.BusStopOverlay;
import edu.cuhk.cubt.ui.com.LocationHistoryOverlay;
import edu.cuhk.cubt.ui.com.PathOverlay;
import edu.cuhk.cubt.ui.com.ServiceOverlay;
import edu.cuhk.cubt.util.CuhkLocation;

public class CubtMapView extends MapActivity {
	
	MapView mapView;
	BusStopOverlay stopOverlay;
	ServiceOverlay realOverlay;
	LocationHistoryOverlay locationHistoryOverlay;
	PathOverlay routeOverlay;
	View layout;
	TextView toastText;
	
	static final int MENU_ROUTE = Menu.FIRST + 201;
	
	
	@Override
	protected void onResume() {
		super.onResume();
	    
	    Location location = CuhkLocation.getInstance();
	    
	    mapView.getController().animateTo(new GeoPoint(
	    		(int)(location.getLatitude() * 1E6),
	    		(int)(location.getLongitude() * 1E6)));
	}

	private void setView(){
	    mapView = (MapView) findViewById(R.id.mapview);
	    mapView.setBuiltInZoomControls(true);
	    mapView.getController().setZoom(16);
		

	    /* the Toast */
	    LayoutInflater inflater = getLayoutInflater();
		layout = inflater.inflate(R.layout.toastview,(ViewGroup)findViewById(R.id.toastview_root));
		ImageView image = (ImageView) layout.findViewById(R.id.image);
		image.setImageResource(R.drawable.bus2);
		toastText = (TextView) layout.findViewById(R.id.text);
		
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapview);
	    
		setView();
		
	    List<Overlay> mapOverlays = mapView.getOverlays();
	    Drawable drawableStop, drawable;

	    /*
	    Bitmap bmp = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
	    Drawable drawableBmp = new BitmapDrawable(bmp);
	    routeOverlay = new PathOverlay(drawableBmp ,mapView, this);
	    
	    GeoPoint pointTmp = new GeoPoint((int)(22.4266*1e6),(int)(114.2109*1e6));
	    OverlayItem overlayitem2 = new OverlayItem(pointTmp, "Test","point");
	    routeOverlay.addOverlay(overlayitem2);
	    
	    mapOverlays.add(routeOverlay);
	    */

	    /*stop: busStop2D, stop3d: busStop3D, pstop:busStop3D with base, left:busStop indicates left, right:busStop indicate right*/
	    drawableStop = this.getResources().getDrawable(R.drawable.pstop); //bus stop
	    stopOverlay = new BusStopOverlay(drawableStop,this);	    
	    mapOverlays.add(stopOverlay);
	    
	    //testing
	    //GeoPoint plast = new GeoPoint((int)(22.419830*1e6),(int)(114.207024*1e6)); //SRR
	    //GeoPoint pnext = new GeoPoint((int)(22.419860*1e6),(int)(114.203270*1e6)); //FKH
	    //Drawable drawablelast = this.getResources().getDrawable(R.drawable.left);
	    //Drawable drawableright = this.getResources().getDrawable(R.drawable.right);
	    //routeOverlay2 = new PathOverlay(drawableright ,mapView, this);
	    /*
	     * 
	    routeOverlay2.LastStop(PoiData.getByName(PoiData.STOP_ADM));
	    routeOverlay2.PredictedStop(PoiData.getByName(PoiData.STOP_SRR));
	    */
	    //OverlayItem overlayitem3 = new OverlayItem(plast, "Test","point");
	    //OverlayItem overlayitem4 = new OverlayItem(pnext, "Test","point");
	    //routeOverlay2.addOverlay(overlayitem3);
	    //routeOverlay2.addOverlay(overlayitem4);
	    
	    

	    routeOverlay = new PathOverlay(drawableStop ,mapView, this);
	    mapOverlays.add(routeOverlay);
	    
	    drawable = this.getResources().getDrawable(R.drawable.bus2);
	    realOverlay = new ServiceOverlay(drawable, this);
	    mapOverlays.add(realOverlay); 
	    	    
	    /* Location Histoy Overlay */
	    drawable = this.getResources().getDrawable(android.R.drawable.star_on); //stat_sys_upload
	    locationHistoryOverlay = new LocationHistoryOverlay(drawable,this);
	    
	    if(getSharedPreferences(Settings.sharedPreferenceFile,0).
			getBoolean(Settings.PREF_DISPLAY_LOCATION_HISTORY, false)
		) mapOverlays.add(locationHistoryOverlay);
	    
	    
	    Location location = CuhkLocation.getInstance();
	    
	    mapView.getController().animateTo(new GeoPoint(
	    		(int)(location.getLatitude() * 1E6),
	    		(int)(location.getLongitude() * 1E6)));
	    
	    registerForContextMenu(mapView);
	    
	    setHandler();
	    /*
	    Toast.makeText(getBaseContext(), 
                "Bus Stop" , 
                Toast.LENGTH_SHORT).show();
	    */
	    long period = 3000;
		String periodString = this.getSharedPreferences(Settings.sharedPreferenceFile,0)
			.getString(Settings.PREF_MAPVIEW_REFRESH_PERIOD, "");
		try{
			period = Long.parseLong(periodString);
		}catch(NumberFormatException e){
			//period = 3000;
		}
	    
		startServiceUpdate(period); //run in real time
	}
	
	
	@Override
	protected void onDestroy() {
		stopServiceUpdate();
		unsetHandler();
		super.onDestroy();
	}
	

	public void setDisplayRoute(String routeName){
		//since dunno the predicted route BUT stops!?  
		routeOverlay.drawRoute(routeName); //show predicted path
		stopOverlay.setRoute(routeName); //show predicted stops
	}
	
	private long busShowingId = -1;
	private boolean newClick = false;
	private static final int MSG_REQUEST_STOP_UPDATE = 28012;
	
	
	public void doDisplayRouteInfo(long id){
		setDisplayRoute(null);
		displayRouteInfo(id);
	}
	private void displayRouteInfo(long id){
		if(busShowingId != id){
			busShowingId = id;
		}
		newClick = true;
		displayRouteInfo();
	}
	private void displayRouteInfo(){
		if(busShowingId>=0){
			BusPassedStopRequest.getPassedStop(busShowingId, passedStopCallback);
		}else{
			//routeOverlay.clear();			
		}
	}
	
	
	BusPassedStopRequest.ResponseListener passedStopCallback = new BusPassedStopRequest.ResponseListener() {
		BusEventObject lastEvent = null;
		
		@Override
		public void onDataReceive(List<BusEventObject> stopEvents) {

			
			if(busShowingId>=0){
				if(stopEvents.size() == 0){
					if(newClick){
						toastText.setText("No Stop Data");
						Toast toast = new Toast(CubtMapView.this);
						toast.setGravity(Gravity.TOP, 0, 130);
						toast.setDuration(Toast.LENGTH_LONG);
						toast.setView(layout);
						toast.show();
						toast.show();
						newClick = false;
					}
					
				}else{
					BusEventObject newEvent = stopEvents.get(stopEvents.size()-1);
					if(		lastEvent == null || newClick ||
							newEvent.getEnterTime() != lastEvent.getEnterTime() || 
							newEvent.getStop() != lastEvent.getStop()){
						lastEvent = newEvent;						  
						
						doPredictionUpdate(stopEvents,newClick);	
						newClick = false;
					}
				}
				
				//looping for request
				handler.sendEmptyMessageDelayed(MSG_REQUEST_STOP_UPDATE,updatePeriod);
			}
		}
		
		void doPredictionUpdate(List<BusEventObject> stopEvents, boolean displayToast){
			String output = "";
			
			//stops, the passed Stop got from the server
			List<Stop> stops = new ArrayList<Stop>();
			Iterator<BusEventObject> iter = stopEvents.iterator();
			while(iter.hasNext()){
				Stop stop = iter.next().getStop();
				stops.add(stop);
			}
			
			output+="Passed Stop:\n";
			
			Stop lastStop = (stops.isEmpty())? null : stops.get(stops.size()-1);
			if(lastStop!=null){
				output += lastStop.getName() + "\n";
			}
			
			//time, current Time
			Time time= new Time();
			time.setToNow();
			
			//routes the Predicted routes
			List<Route> routes = RoutePrediction.getRoutesByPassedStop(time.toMillis(false), stops);
			
			output+="\nPredicted Route:\n";
			Iterator<Route> riter = routes.iterator();
			while(riter.hasNext()){
				output+= riter.next().getName() + "\n";
			}
			
			output+="\nPossible Next Stop:\n";
			
			List<Stop> predictedStops = RoutePrediction.getPossibleNextStop(routes.iterator(),lastStop);
			Iterator<Stop> siter = predictedStops.iterator();
			while(siter.hasNext()){			
				output+= siter.next().getName() + "\n";
			}
			routeOverlay.drawPrediction(lastStop, predictedStops);
			
			if(predictedStops.size() == 0){
				output = "Route Ended";
			}
			
			
			if(displayToast){
				toastText.setText(output);
				Toast toast = new Toast(CubtMapView.this);
				toast.setGravity(Gravity.TOP, 0, 130);
				toast.setDuration(Toast.LENGTH_LONG);
				toast.setView(layout);
				toast.show();
				toast.show();
				toast.show();
				toast.show();
			}
		}
	};
	
	
	
	private long updatePeriod;
	private long minPeriod = 3000;
	
	private static final int MSG_REQUEST_UPDATE = 28011;
	
	BusActivityRequest.ResponseListener busActivityCallback = new BusActivityRequest.ResponseListener(){
		@Override
		public void onDataReceive(List<BusActivityRecord> buses) {
			if(updatePeriod>0){
				//Collection<GeoPoint> points = new ArrayList<GeoPoint>();
				
				//remove the showing Bus if the bus gone
				if(busShowingId>=0){
					Iterator<BusActivityRecord> busesIter = buses.iterator();				
					boolean find = false;				
					while(busesIter.hasNext()){
						if(busesIter.next().getId() == busShowingId){
							find = true;
							break;
						}
					}
					if(!find){ 
						displayRouteInfo(-1);
						routeOverlay.clear();
					}
				}
				
				realOverlay.updateDisplay(buses.iterator());
				mapView.invalidate();
				
				handler.sendEmptyMessageDelayed(MSG_REQUEST_UPDATE,updatePeriod);
			}		
		}		
	};
	
	public void startServiceUpdate(long period){
		this.updatePeriod = (period < minPeriod ) ? minPeriod : period;
		BusActivityRequest.sendRequest(busActivityCallback);
		//routeOverlay2.updateDisplay(); //<<<<---????
	}
	
	public void stopServiceUpdate(){
		this.updatePeriod = 0;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, MENU_ROUTE, MENU_ROUTE, R.string.menu_routes).setIcon(android.R.drawable.ic_menu_search);
		return super.onCreateOptionsMenu(menu);
	}





	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
			case MENU_ROUTE:
				this.openContextMenu(mapView);
				return true;
		}
		
		return super.onOptionsItemSelected(item);
	}


//Menu Route list, need to change
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		
		int i = 0;
		menu.add(0, i++, 0, "Route Cleared");
		Iterator<String> iter = RouteData.getRoutes().keySet().iterator();
		while(iter.hasNext()){
			menu.add(0, i++, 0, iter.next());
		}
	}	
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		displayRouteInfo(-1);
		if(item.getItemId() == 0){
			setDisplayRoute(null);
		}else{
			setDisplayRoute((String)item.getTitle());
		}
		mapView.invalidate();
		return super.onContextItemSelected(item);
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	

	private void setHandler() {
		LocationHistory.getInstance().addHandler(handler);		
	}
	
	private void unsetHandler(){
		LocationHistory.getInstance().removeHandler(handler);
	}
	
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
				case LocationHistory.MSG_LOCATION_HISTORY_UPDATE:
					mapView.invalidate();
					break;
				case MSG_REQUEST_STOP_UPDATE:
					displayRouteInfo();
					break;
				case MSG_REQUEST_UPDATE:
					BusActivityRequest.sendRequest(busActivityCallback);
					break;
			}
			super.handleMessage(msg);
		}	
		
	};

}
