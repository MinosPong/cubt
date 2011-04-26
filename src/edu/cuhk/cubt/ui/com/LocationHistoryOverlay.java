package edu.cuhk.cubt.ui.com;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

import edu.cuhk.cubt.CubtApplication;
import edu.cuhk.cubt.store.LocationHistory;

public class LocationHistoryOverlay extends ItemizedOverlay<OverlayItem> {

	private static final String tag = "LocationHistoryOverlay";
	
	private final Context mContext;
	private final LocationHistory locationHistory;
	
	public LocationHistoryOverlay(Drawable defaultMarker, Context context) {
		super(boundCenterBottom(defaultMarker));
		mContext = context;
		locationHistory = LocationHistory.getInstance();
		updateDisplay();
	}

	private void updateDisplay(){
		populate();
	}
	
	
	
	@Override
	protected int getIndexToDraw(int drawingOrder) {
		populate();
		return super.getIndexToDraw(drawingOrder);
	}

	@Override
	protected OverlayItem createItem(int i) {
		Location location = locationHistory.getLast(i);
		return new OverlayItem(
				new GeoPoint((int)(location.getLatitude() * 1E6), (int)(location.getLongitude() *1E6)), 
				String.valueOf(location.getTime()),  
				String.valueOf(location.getTime()));
	}

	@Override
	public int size() {
		return locationHistory.count();
	}

}
