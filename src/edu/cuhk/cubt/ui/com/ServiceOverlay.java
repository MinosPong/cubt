package edu.cuhk.cubt.ui.com;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

import edu.cuhk.cubt.store.RouteData;
import edu.cuhk.cubt.ui.CubtMapView;

public class ServiceOverlay extends ItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private Context mContext;
	String routeName = RouteData.ROUTE_0;
	String pRoute, dir, lStop;
	
	public ServiceOverlay(Drawable defaultMarker, Context context) {
		super(boundCenterBottom(defaultMarker));
		mContext = context;
		updateDisplay();
		// TODO Auto-generated constructor stub
	}

	public void updateDisplay(){
		mOverlays.clear();
		Iterator<GeoPoint> it = busInService().iterator();
		if(it != null){
			while(it.hasNext()){
			GeoPoint busLoc = it.next();
			mOverlays.add(new OverlayItem(busLoc, "Last Stop", "Predicted Route:"+ pRoute + "\nDirection: " + dir + "\nLast Stop:"+ lStop));
			}
		}
		populate();
	}

	@Override
	protected boolean onTap(int index) {
	  OverlayItem item = mOverlays.get(index);
	  CubtMapView.routeOverlay.setPath(routeName); //show predicted path
	  CubtMapView.stopOverlay.setRoute(routeName); //show predicted stops
	  AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
	  dialog.setTitle(item.getTitle());
	  dialog.setMessage(item.getSnippet());
	  dialog.show();
	  return true;
	}
	
	@Override
	protected OverlayItem createItem(int i) {
		// TODO Auto-generated method stub
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return mOverlays.size();
		//return 0;
	}

	//Received bus locations
	Collection<GeoPoint> busInService(){
		Collection<GeoPoint> bus = new ArrayList<GeoPoint>();
		bus.add(new GeoPoint((int)(22.41988*1e6),(int)(114.20551*1e6)));
		bus.add(new GeoPoint((int)(22.41608*1e6),(int)(114.2088*1e6)));
		return bus;
	}
}