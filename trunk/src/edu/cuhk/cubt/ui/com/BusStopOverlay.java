package edu.cuhk.cubt.ui.com;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Projection;

import edu.cuhk.cubt.bus.Poi;
import edu.cuhk.cubt.bus.Prediction;
import edu.cuhk.cubt.bus.Route;
import edu.cuhk.cubt.store.PoiData;
import edu.cuhk.cubt.store.RouteData;

public class BusStopOverlay extends ItemizedOverlay<OverlayItem> {

	private static final String Tag = "BusStopOverlay";
	
	private Context mContext;
	
	private List<Poi> mOverlays = new ArrayList<Poi>();
	public String routeName = null;
	    
	public BusStopOverlay(Drawable defaultMarker, Context context){
		super(boundCenterBottom(defaultMarker));
		mContext = context; //handle touch event
		updateDisplay();		
	}

	private void updateDisplay(){
		mOverlays.clear();
		if(routeName == null){
			//mOverlays.addAll(PoiData.getAllStops());
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
		String last = poi.getName();
		String next = poi.getName();
		int dir = 0; //direction=> up:0 down:1
		String dir2 = "";
		Prediction next2;
		long millis;
		//String next2s = next2.theNextStop(millis, location);
		
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
		
		if(dir == 0)
			dir2 = "up";
		else if(dir == 1)
			dir2 = "down";
		Toast.makeText(mContext, "Last Stop: "+last+"\nNext Stop :"+next+"\nDirection :"+dir2+"\nPredicted Bus Route: ", Toast.LENGTH_SHORT).show();
		//no need for the nextstop?
		//dialog.setMessage(msg);
		//dialog.show();		
		return true;
	}
}
