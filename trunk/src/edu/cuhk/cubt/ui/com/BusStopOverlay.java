package edu.cuhk.cubt.ui.com;

import java.util.ArrayList;
import java.util.List;

import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

import edu.cuhk.cubt.bus.Poi;
import edu.cuhk.cubt.store.PoiData;

public class BusStopOverlay extends ItemizedOverlay<OverlayItem> {

	private List<Poi> mOverlays = new ArrayList<Poi>(PoiData.getPois().values());
	
	public BusStopOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
		populate();
		// TODO Auto-generated constructor stub
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
	
	
}
