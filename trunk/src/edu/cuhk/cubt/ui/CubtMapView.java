package edu.cuhk.cubt.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import edu.cuhk.cubt.CubtApplication;
import edu.cuhk.cubt.R;
import edu.cuhk.cubt.net.BusActivityRequest;
import edu.cuhk.cubt.net.BusActivityRequest.BusActivityRecord;
import edu.cuhk.cubt.net.BusActivityRequest.ResponseListener;
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
	String pRoute, dir, lStop;

	static final int MENU_ROUTE = Menu.FIRST + 201;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapview);
		
	    mapView = (MapView) findViewById(R.id.mapview);
	    mapView.setBuiltInZoomControls(true);
	    mapView.getController().setZoom(16);
	    
	    List<Overlay> mapOverlays = mapView.getOverlays();
	    Drawable drawableStop, drawable;
	    
	    drawableStop = this.getResources().getDrawable(R.drawable.stop); //bus stop star
	    stopOverlay = new BusStopOverlay(drawableStop,this);	    
	    mapOverlays.add(stopOverlay);
	    
	    drawable = this.getResources().getDrawable(R.drawable.bus);
	    realOverlay = new ServiceOverlay(drawable, this);
	    mapOverlays.add(realOverlay); 

	    Bitmap bmp = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
	    Drawable drawableBmp = new BitmapDrawable(bmp);
	    routeOverlay = new PathOverlay(drawableBmp ,mapView, this);
	    GeoPoint pointTmp = new GeoPoint((int)(22.4266*1e6),(int)(114.2109*1e6));
	    OverlayItem overlayitem2 = new OverlayItem(pointTmp, "Test","point");
	    routeOverlay.addOverlay(overlayitem2);
	    mapOverlays.add(routeOverlay);
	    
	    //Commented with bug, to be solved
	    //routeOverlay = new PathOverlay(drawable ,mapView, this);
	    //mapOverlays.add(routeOverlay);

	    
	    /* Location Histoy Overlay */
	    drawable = this.getResources().getDrawable(android.R.drawable.star_on); //stat_sys_upload
	    locationHistoryOverlay = new LocationHistoryOverlay(drawable,this);	    
	    mapOverlays.add(locationHistoryOverlay);
	    
	    Location location = CuhkLocation.getInstance();
	    
	    mapView.getController().animateTo(new GeoPoint(
	    		(int)(location.getLatitude() * 1E6),
	    		(int)(location.getLongitude() * 1E6)));
	    
	    registerForContextMenu(mapView);
	    
	    setHandler();
	    
	    Toast.makeText(getBaseContext(), 
                "Bus Stop" , 
                Toast.LENGTH_SHORT).show();
	    
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
		  routeOverlay.setPath(routeName); //show predicted path
		  stopOverlay.setRoute(routeName); //show predicted stops
	}
	
	
	private long updatePeriod;
	private long minPeriod = 3000;
	
	private static final int MSG_REQUEST_UPDATE = 28011;
	
	ResponseListener callback = new ResponseListener(){
		@Override
		public void onDataReceive(List<BusActivityRecord> buses) {
			if(updatePeriod>0){
				Collection<GeoPoint> points = new ArrayList<GeoPoint>();
				Iterator<BusActivityRecord> busesIter = buses.iterator();
				while(busesIter.hasNext()){
					BusActivityRecord bus = busesIter.next();
					points.add(new GeoPoint((int)(bus.getLatitude()*1e6) , (int)(bus.getLongitude()*1e6)));			
				}
				realOverlay.updateDisplay(points.iterator());
				mapView.invalidate();
				handler.sendEmptyMessageDelayed(MSG_REQUEST_UPDATE,updatePeriod);
			}		
		}		
	};
	
	public void startServiceUpdate(long period){
		this.updatePeriod = (period < minPeriod ) ? minPeriod : period;
		BusActivityRequest.sendRequest(callback);
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
		menu.add(0, i++, 0, "All Stops");
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
					BusActivityRequest.sendRequest(callback);
					break;
			}
			super.handleMessage(msg);
		}	
		
	};

}
