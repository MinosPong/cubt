package edu.cuhk.cubt.ui.com;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

import edu.cuhk.cubt.bus.Poi;
import edu.cuhk.cubt.bus.Route;
import edu.cuhk.cubt.store.PoiData;
import edu.cuhk.cubt.store.RouteData;

public class BusStopOverlay extends ItemizedOverlay<OverlayItem> {

	private static final String Tag = "BusStopOverlay";
	
	private Context mContext;
	
	private List<Poi> mOverlays = new ArrayList<Poi>();
	private String routeName = null;
	
	public BusStopOverlay(Drawable defaultMarker, Context context){
		super(boundCenterBottom(defaultMarker));
		mContext = context;
		updateDisplay();		
	}

	private void updateDisplay(){
		mOverlays.clear();
		if(routeName == null){
			mOverlays.addAll(PoiData.getAllStops());
		}
		else{
			Route route = RouteData.getRouteByName(routeName);
			if(route != null){
				Iterator<Poi> iter = route.getPois();
				while(iter.hasNext()){
					mOverlays.add(iter.next());
				}
			}
		}
		populate();
	}

	public void setRoute(String routeName){
		if(this.routeName != routeName){
			this.routeName = routeName;
			updateDisplay();
		}
	}
	
	@Override
	protected OverlayItem createItem(int i) {
		Poi poi = mOverlays.get(i);
		return new OverlayItem(new GeoPoint((int)(poi.getLatitude() * 1E6), (int)(poi.getLongitude() *1E6)), poi.getName(), poi.getName());
	}

	@Override
	public int size() {
		return mOverlays.size();
	}
	
	@Override
	protected boolean onTap(int index){
		Log.d(Tag,"TAP : (" + index + "/" +size() + ")");
		Poi poi= mOverlays.get(index);
		AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		dialog.setCancelable(true);
		dialog.setTitle(poi.getName());
		String msg = "Possible Route:\n";
		
		Route route;
		Iterator<Route> routes = RouteData.getRoutes().values().iterator();
		int i =1;
		while(routes.hasNext()){
			route = routes.next();
			if(route.isPassThrough(poi)){
				msg += i + ". " + route.getName() +"\n"; 
				i++;
			}
		}		
		dialog.setMessage(msg);
		dialog.show();		
		return true;
	}
	
	
	
}
