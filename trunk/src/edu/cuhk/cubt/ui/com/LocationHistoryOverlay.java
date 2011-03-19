package edu.cuhk.cubt.ui.com;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

import edu.cuhk.cubt.CubtApplication;
import edu.cuhk.cubt.store.LocationHistory;

public class LocationHistoryOverlay extends ItemizedOverlay<OverlayItem> {

	private Context mContext;
	private LocationHistory locationHistory;
	
	public LocationHistoryOverlay(Drawable defaultMarker, Context context) {
		super(boundCenterBottom(defaultMarker));
		mContext = context;
		locationHistory = ((CubtApplication)context.getApplicationContext()).getLocationHistory();
		updateDisplay();
	}

	private void updateDisplay(){
		populate();
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
