package edu.cuhk.cubt.ui;

import java.util.Iterator;
import java.util.List;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

import edu.cuhk.cubt.store.RouteData;
import edu.cuhk.cubt.ui.com.BusStopOverlay;
import edu.cuhk.cubt.ui.com.LocationHistoryOverlay;
import edu.cuhk.cubt.util.CuhkLocation;
import edu.cuhk.ie.cubt.R;

public class CubtMapView extends MapActivity {

	MapView mapView;
	BusStopOverlay stopOverlay;

	static final int MENU_ROUTE = Menu.FIRST ;
	static final int MENU_OPTION = Menu.FIRST + 1;
	static final int MENU_EXIT = Menu.FIRST + 2;
	
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.mapview);
		
	    mapView = (MapView) findViewById(R.id.mapview);
	    mapView.setBuiltInZoomControls(true);
	    mapView.getController().setZoom(16);
	    
	    List<Overlay> mapOverlays = mapView.getOverlays();
	    Drawable drawable;
	    drawable = this.getResources().getDrawable(android.R.drawable.star_on);
	    
	    stopOverlay = new BusStopOverlay(drawable,this);	    
	    mapOverlays.add(stopOverlay);
	    

	    drawable = this.getResources().getDrawable(android.R.drawable.stat_sys_upload);
	    LocationHistoryOverlay locationOverlay = new LocationHistoryOverlay(drawable,this);
	    mapOverlays.add(locationOverlay);
	    
	    Location location = CuhkLocation.getInstance();
	    
	    mapView.getController().animateTo(new GeoPoint(
	    		(int)(location.getLatitude() * 1E6),
	    		(int)(location.getLongitude() * 1E6)));
	    
	    registerForContextMenu(mapView);
	    
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

}
