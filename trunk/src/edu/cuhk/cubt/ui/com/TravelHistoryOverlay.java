package edu.cuhk.cubt.ui.com;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class TravelHistoryOverlay extends ItemizedOverlay<OverlayItem> {

	private List<OverlayItem> items = new ArrayList<OverlayItem>();

	private MapView mapView;
	
	public TravelHistoryOverlay(Drawable defaultMarker, MapView mapview) {
		super(boundCenterBottom(defaultMarker));
		this.mapView = mapview;
		updateDisplay();	
	}
	

	private void updateDisplay(){
		populate();
		setLastFocusedIndex(-1);
	}
	
	public void setRoute(Cursor cursor){
		items.clear();
		
		for(cursor.moveToFirst(); cursor.moveToNext(); cursor.isAfterLast()) {
			items.add(new OverlayItem(
					new GeoPoint((int)(cursor.getDouble(2) * 1E6)
							, (int)(cursor.getDouble(3) *1E6)), 
					String.valueOf(cursor.getLong(1)),  
					String.valueOf(cursor.getLong(1))));

		}
		updateDisplay();		
	}
	
    public void draw(Canvas canvas, MapView mapView, boolean shadow) {        
        super.draw(canvas, mapView, shadow);
        drawPortion(canvas, items.iterator()); //RouteData.ROUTE_0
    }
	
	private void drawBasic(Canvas canvas, GeoPoint prePoint, GeoPoint currentPoint){
		Paint paint=new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.RED); 
        Point screenCoords=new Point();
        Point screenCoords1=new Point();

        mapView.getProjection().toPixels(prePoint, screenCoords);
        int x1=screenCoords.x;
        int y1=screenCoords.y;

        mapView.getProjection().toPixels(currentPoint, screenCoords1);
        int x2=screenCoords1.x;
        int y2=screenCoords1.y;

        paint.setStrokeWidth(4);
        canvas.drawLine(x1, y1, x2, y2, paint);
	}
	
	
	private void drawPortion (Canvas canvas, Iterator<OverlayItem> it){
		if(!it.hasNext()) return;
		GeoPoint prePoint = (GeoPoint)it.next().getPoint();
        while(it.hasNext()){
        	GeoPoint currentPoint = (GeoPoint)it.next().getPoint();
        	drawBasic(canvas, prePoint, currentPoint);
        	prePoint = currentPoint;
        }
	}
	
	@Override
	protected OverlayItem createItem(int i) {
		//Log.i("ITEM "+ i,items.get(i).getPoint().getLatitudeE6() + " " + items.get(i).getPoint().getLongitudeE6());
		return items.get(i);
	}

	@Override
	public int size() {
		return items.size();
	}

}
