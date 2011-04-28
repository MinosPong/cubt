package edu.cuhk.cubt.ui;

import java.util.Iterator;
import java.util.List;


import android.content.Intent;
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

	static final int MENU_ROUTE = Menu.FIRST ;
	static final int MENU_OPTION = Menu.FIRST + 1;
	static final int MENU_EXIT = Menu.FIRST + 2;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapview);
		
	    mapView = (MapView) findViewById(R.id.mapview);
	    mapView.setBuiltInZoomControls(true);
	    mapView.getController().setZoom(16);
	    
	    List<Overlay> mapOverlays = mapView.getOverlays();
	    Drawable drawableStop, drawable;
	    
	    drawableStop = this.getResources().getDrawable(android.R.drawable.ic_delete); //bus stop star
	    stopOverlay = new BusStopOverlay(drawableStop,this);	    
	    mapOverlays.add(stopOverlay);
	    
	    drawable = this.getResources().getDrawable(R.drawable.bus);
	    realOverlay = new ServiceOverlay(drawable, this);
	    GeoPoint point = new GeoPoint((int)(22.41988*1e6),(int)(114.20551*1e6));
	    OverlayItem overlayitem = new OverlayItem(point, "Last Stop", "Predicted Route:"+ pRoute + "\nDirection: " + dir + "\nLast Stop:"+ lStop);
	    realOverlay.addOverlay(overlayitem);
	    mapOverlays.add(realOverlay);
	    
	    
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
	}
	
	
	@Override
	protected void onDestroy() {
		unsetHandler();
		super.onDestroy();
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		int i=0;
		menu.add(0, MENU_ROUTE, i++, R.string.menu_routes).setIcon(android.R.drawable.ic_menu_search);
		menu.add(0, MENU_OPTION, i++, R.string.menu_settings).setIcon(android.R.drawable.ic_menu_edit);
		menu.add(0, MENU_EXIT, i++, R.string.menu_exit).setIcon(android.R.drawable.ic_menu_close_clear_cancel);
		return super.onCreateOptionsMenu(menu);
	}





	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
			case MENU_ROUTE:
				this.openContextMenu(mapView);
				return true;
			case MENU_OPTION:
				Intent intent = new Intent(this, Settings.class);
				startActivity(intent);
				return true;
			case MENU_EXIT:
				finish();
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
		}else{
			stopOverlay.setRoute((String)item.getTitle());
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
		((CubtApplication)getApplication()).getLocationHistory().addHandler(handler);		
	}
	
	private void unsetHandler(){
		((CubtApplication)getApplication()).getLocationHistory().removeHandler(handler);
	}
	
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
				case LocationHistory.MSG_LOCATION_HISTORY_UPDATE:
					mapView.invalidate();
					break;
			}
			super.handleMessage(msg);
		}	
		
	};

}
