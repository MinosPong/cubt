package edu.cuhk.cubt.ui;

import java.util.Iterator;
import java.util.List;


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
import edu.cuhk.cubt.util.CuhkLocation;
import edu.cuhk.ie.cubt.R;

public class CubtMapView extends MapActivity {

	MapView mapView;
	BusStopOverlay stopOverlay;
	
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.mapview);
		
	    mapView = (MapView) findViewById(R.id.mapview);
	    mapView.setBuiltInZoomControls(true);
	    mapView.getController().setZoom(16);
	    
	    List<Overlay> mapOverlays = mapView.getOverlays();
	    Drawable drawable = this.getResources().getDrawable(android.R.drawable.star_on);
	    
	    stopOverlay = new BusStopOverlay(drawable);
	    
	    mapOverlays.add(stopOverlay);
	    
	    Location location = CuhkLocation.getInstance();
	    
	    mapView.getController().animateTo(new GeoPoint(
	    		(int)(location.getLatitude() * 1E6),
	    		(int)(location.getLongitude() * 1E6)));
	    
	    registerForContextMenu(mapView);
	    
	}
	
	
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		this.openContextMenu(mapView);
		return super.onPrepareOptionsMenu(menu);
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
		mapView.postInvalidate();
		return super.onContextItemSelected(item);
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}
