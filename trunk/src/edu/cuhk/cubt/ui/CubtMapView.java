package edu.cuhk.cubt.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
import com.google.android.maps.OverlayItem;

import edu.cuhk.cubt.CubtApplication;
import edu.cuhk.cubt.R;
import edu.cuhk.cubt.bus.BusEventObject;
import edu.cuhk.cubt.bus.Poi;
import edu.cuhk.cubt.bus.Route;
import edu.cuhk.cubt.bus.RoutePrediction;
import edu.cuhk.cubt.bus.Stop;
import edu.cuhk.cubt.net.BusActivityRequest;
import edu.cuhk.cubt.net.BusActivityRequest.BusActivityRecord;
import edu.cuhk.cubt.net.BusPassedStopRequest;
import edu.cuhk.cubt.store.LocationHistory;
import edu.cuhk.cubt.store.PoiData;
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
	PathOverlay routeOverlay, routeOverlay2;

	static final int MENU_ROUTE = Menu.FIRST + 201;
	
	
	@Override
	protected void onResume() {
		super.onResume();
	    
	    Location location = CuhkLocation.getInstance();
	    
	    mapView.getController().animateTo(new GeoPoint(
	    		(int)(location.getLatitude() * 1E6),
	    		(int)(location.getLongitude() * 1E6)));
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapview);
	    mapView = (MapView) findViewById(R.id.mapview);
	    mapView.setBuiltInZoomControls(true);
	    mapView.getController().setZoom(16);
	    
	    List<Overlay> mapOverlays = mapView.getOverlays();
	    Drawable drawableStop, drawable;

	    Bitmap bmp = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
	    Drawable drawableBmp = new BitmapDrawable(bmp);
	    routeOverlay = new PathOverlay(drawableBmp ,mapView, this);
	    GeoPoint pointTmp = new GeoPoint((int)(22.4266*1e6),(int)(114.2109*1e6));
	    OverlayItem overlayitem2 = new OverlayItem(pointTmp, "Test","point");
	    routeOverlay.addOverlay(overlayitem2);
	    mapOverlays.add(routeOverlay);

	    /*stop: busStop2D, stop3d: busStop3D, pstop:busStop3D with base, left:busStop indicates left, right:busStop indicate right*/
	    drawableStop = this.getResources().getDrawable(R.drawable.pstop); //bus stop
	    stopOverlay = new BusStopOverlay(drawableStop,this);	    
	    mapOverlays.add(stopOverlay);
	    
	    //testing
	    //GeoPoint plast = new GeoPoint((int)(22.419830*1e6),(int)(114.207024*1e6)); //SRR
	    //GeoPoint pnext = new GeoPoint((int)(22.419860*1e6),(int)(114.203270*1e6)); //FKH
	    //Drawable drawablelast = this.getResources().getDrawable(R.drawable.left);
	    //Drawable drawableright = this.getResources().getDrawable(R.drawable.right);
	    routeOverlay2 = new PathOverlay(drawableStop ,mapView, this);
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
	    mapOverlays.add(routeOverlay2);
	    
	    drawable = this.getResources().getDrawable(R.drawable.bus2);
	    realOverlay = new ServiceOverlay(drawable, this);
	    mapOverlays.add(realOverlay); 
	    
	    
	    //Commented with bug, to be solved
	    //routeOverlay = new PathOverlay(drawable ,mapView, this);
	    //mapOverlays.add(routeOverlay);

	    
	    /* Location Histoy Overlay */
	    drawable = this.getResources().getDrawable(android.R.drawable.star_on); //stat_sys_upload
	    locationHistoryOverlay = new LocationHistoryOverlay(drawable,this);	    
	    //mapOverlays.add(locationHistoryOverlay);
	    
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
		//routeOverlay2.clearOverlay();
		unsetHandler();
		super.onDestroy();
	}
	

	public void setDisplayRoute(String routeName){
		//since dunno the predicted route BUT stops!?  
		//routeOverlay.setPath(routeName); //show predicted path
		  //stopOverlay.setRoute(routeName); //show predicted stops
	}
	
	
	public void displayRouteInfo(long id){
		//TODO display loading bar
		BusPassedStopRequest.getPassedStop(id, passedStopCallback);
		
	}
	
	BusPassedStopRequest.ResponseListener passedStopCallback = new BusPassedStopRequest.ResponseListener() {
		
		@Override
		public void onDataReceive(List<BusEventObject> stopEvents) {
			//TODO remove loading bar
			

			//AlertDialog.Builder dialog = new AlertDialog.Builder(CubtMapView.this);
			//dialog.setTitle(item.getTitle());
			//dialog.setMessage(item.getSnippet());
			//dialog.show();
			  
			LayoutInflater inflater = getLayoutInflater();
			View layout = inflater.inflate(R.layout.toastview,(ViewGroup)findViewById(R.id.toastview_root));
			ImageView image = (ImageView) layout.findViewById(R.id.image);
			image.setImageResource(R.drawable.bus2);
			TextView text = (TextView) layout.findViewById(R.id.text);
			//text.setText("Last Stop:"+ lStop + "\nPredicted Stop:"+ pStop  + "\nDirection: " + dir);
			String output = "", store = "";
			Poi poiStop = null;
			
			//stops, the passed Stop got from the server
			List<Stop> stops = new ArrayList<Stop>();
			Iterator<BusEventObject> iter = stopEvents.iterator();
			output="Passed Stop:\n";
			while(iter.hasNext()){
				Stop stop = iter.next().getStop();
				poiStop = stop;
				stops.add(stop);
				//output+= stop.getName() + "\n";
				routeOverlay2.LastStop(poiStop);
				//routeOverlay2.LastStop(PoiData.getByName(PoiData.STOP_ADM));
			}
			output += poiStop.getName() + "\n";
			//time, current Time
			Time time= new Time();
			time.setToNow();
			
			//routes the Predicted routes
			List<Route> routes = RoutePrediction.getRoutesByPassedStop(time.toMillis(false), stops);
			
			output+="\nPredicted Route:\n";
			Iterator<Route> riter = routes.iterator();
			int i = 2;
			while(riter.hasNext()&& i > 0){
				output+= riter.next().getName() + "\n";
				i--;
			}
			
			output+="\nPossible Next Stop:\n";
			
			Stop lastStop = (stops.isEmpty())? null : stops.get(stops.size()-1);
			Iterator<Stop> siter = RoutePrediction.getPossibleNextStop(
					routes.iterator(), 
					lastStop).iterator();
			int j = 2;
			while(siter.hasNext() && j > 0){
				Poi tmp2 = siter.next();				
				output+= tmp2.getName() + "\n";
				//output+= siter.next().getName() + "\n";
				routeOverlay2.PredictedStop(tmp2);
				j--;
				//routeOverlay2.PredictedStop(PoiData.getByName(PoiData.STOP_SRR));
			}
			routeOverlay2.updateDisplay();
			
			
			text.setText(output);
			
			Toast toast = new Toast(CubtMapView.this);
			toast.setGravity(Gravity.TOP, 0, 130);
			toast.setDuration(Toast.LENGTH_LONG);
			toast.setView(layout);
			toast.show();
			toast.show();
			toast.show();
			toast.show();
			toast.show();
			toast.show();
			
		}
	};
	
	
	
	private long updatePeriod;
	private long minPeriod = 3000;
	
	private static final int MSG_REQUEST_UPDATE = 28011;
	
	BusActivityRequest.ResponseListener busActivityCallback = new BusActivityRequest.ResponseListener(){
		@Override
		public void onDataReceive(List<BusActivityRecord> buses) {
			if(updatePeriod>0){
				Collection<GeoPoint> points = new ArrayList<GeoPoint>();
				Iterator<BusActivityRecord> busesIter = buses.iterator();
				realOverlay.updateDisplay(buses.iterator());
				routeOverlay2.updateDisplay();
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
		if(item.getItemId() == 0){
			stopOverlay.setRoute(null);
			routeOverlay.setPath(null);
		}else{
			stopOverlay.setRoute((String)item.getTitle());
			routeOverlay.setPath((String)item.getTitle());
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
				case MSG_REQUEST_UPDATE:
					BusActivityRequest.sendRequest(busActivityCallback);
					break;
			}
			super.handleMessage(msg);
		}	
		
	};

}
