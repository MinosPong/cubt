package edu.cuhk.cubt.ui.com;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

import edu.cuhk.cubt.store.RouteData;

public class PathOverlay extends ItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	MapView mapView=null;
	Paint paint=new Paint();
	private Context mContext;
	//GeoPoint point = new GeoPoint((int)(22.41988*1e6),(int)(114.20551*1e6));
	//GeoPoint point2 = new GeoPoint((int)(22.4199*1e6),(int)(114.2035*1e6));
    //GeoPoint point3 = new GeoPoint((int)(22.4202*1e6),(int)(114.2028*1e6));
	
	
	public PathOverlay(Drawable defaultMarker,MapView mapview,Context context) {

	    super(boundCenterBottom(defaultMarker));
	    mapView=mapview;
	    mContext=context; 
	}
	
	public PathOverlay(Drawable defaultMarker) {
		super(defaultMarker);
	}

	public void addOverlay(OverlayItem overlay) {
	    mOverlays.add(overlay);
	    populate();
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
	
	@Override
    public void draw(Canvas canvas, MapView mapView, boolean shadow) {        
        super.draw(canvas, mapView, shadow);
        
        //dun need to create new variable, esp. inside class globle
        //busLine.add(point);
        //busLine.add(point2);
        //busLine.add(point3);
        
        
        //PKM: Dun use for, use while in this case, more readable
        //for (Iterator<GeoPoint> it = busLine.iterator(); ){
        //Change Param in getLinePoints;
        Iterator<GeoPoint> it = getLinePoints(RouteData.ROUTE_0).iterator();
    	GeoPoint prePoint = (GeoPoint)it.next();
        while(it.hasNext()){
        	GeoPoint currentPoint = (GeoPoint)it.next();
        	drawBasic(canvas, prePoint, currentPoint);
        	prePoint = currentPoint;
        }
        //drawBasic(canvas, point, point2);   
        //drawBasic(canvas, point2, point3); 
    }
	//setOnFocusChangeListener()?? PKM: Needa check it
	
	public void drawBasic(Canvas canvas, GeoPoint prePoint, GeoPoint currentPoint){
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

        paint.setStrokeWidth(1);
        canvas.drawLine(x1, y1, x2, y2, paint);
	}
	
	Collection<GeoPoint> getLinePoints(String RouteName){
		Collection<GeoPoint> busLine = new ArrayList<GeoPoint>();	
        
		//base on the RouteName, return the right List, the condition
        if(true){
        busLine.add(new GeoPoint((int)(22.41988*1e6),(int)(114.20551*1e6)));
        busLine.add(new GeoPoint((int)(22.4199*1e6),(int)(114.2035*1e6)));
        busLine.add(new GeoPoint((int)(22.4202*1e6),(int)(114.2028*1e6)));
        }
		return busLine;
	}
	
	
}
