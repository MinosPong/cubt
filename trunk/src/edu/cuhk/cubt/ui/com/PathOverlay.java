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
        busLine.add(new GeoPoint((int)(22.41482*1e6),(int)(114.21041*1e6)));
        busLine.add(new GeoPoint((int)(22.41428*1e6),(int)(114.21010*1e6)));
        busLine.add(new GeoPoint((int)(22.41425*1e6),(int)(114.21004*1e6)));
        busLine.add(new GeoPoint((int)(22.41430*1e6),(int)(114.20995*1e6)));
        busLine.add(new GeoPoint((int)(22.41496*1e6),(int)(114.21027*1e6)));
        busLine.add(new GeoPoint((int)(22.41529*1e6),(int)(114.21046*1e6)));
        busLine.add(new GeoPoint((int)(22.41544*1e6),(int)(114.21056*1e6)));
        busLine.add(new GeoPoint((int)(22.41699*1e6),(int)(114.21220*1e6)));
        busLine.add(new GeoPoint((int)(22.41710*1e6),(int)(114.21228*1e6)));
        busLine.add(new GeoPoint((int)(22.41725*1e6),(int)(114.21232*1e6)));
        busLine.add(new GeoPoint((int)(22.41755*1e6),(int)(114.21235*1e6)));
        busLine.add(new GeoPoint((int)(22.41758*1e6),(int)(114.21158*1e6)));
        busLine.add(new GeoPoint((int)(22.41770*1e6),(int)(114.21111*1e6)));
        busLine.add(new GeoPoint((int)(22.41795*1e6),(int)(114.20977*1e6)));
        busLine.add(new GeoPoint((int)(22.41810*1e6),(int)(114.20944*1e6)));
        busLine.add(new GeoPoint((int)(22.41830*1e6),(int)(114.20900*1e6)));
        busLine.add(new GeoPoint((int)(22.41857*1e6),(int)(114.20855*1e6)));
        busLine.add(new GeoPoint((int)(22.41860*1e6),(int)(114.20843*1e6)));
        busLine.add(new GeoPoint((int)(22.41856*1e6),(int)(114.20799*1e6)));
        busLine.add(new GeoPoint((int)(22.41869*1e6),(int)(114.20798*1e6)));
        busLine.add(new GeoPoint((int)(22.41876*1e6),(int)(114.20799*1e6)));
        busLine.add(new GeoPoint((int)(22.41880*1e6),(int)(114.20806*1e6)));
        busLine.add(new GeoPoint((int)(22.41885*1e6),(int)(114.20875*1e6)));
        busLine.add(new GeoPoint((int)(22.41888*1e6),(int)(114.20885*1e6)));
        busLine.add(new GeoPoint((int)(22.41893*1e6),(int)(114.20895*1e6)));
        busLine.add(new GeoPoint((int)(22.41910*1e6),(int)(114.20906*1e6)));
        busLine.add(new GeoPoint((int)(22.41954*1e6),(int)(114.20922*1e6)));
        busLine.add(new GeoPoint((int)(22.41958*1e6),(int)(114.20922*1e6)));
        busLine.add(new GeoPoint((int)(22.41961*1e6),(int)(114.20915*1e6)));
        busLine.add(new GeoPoint((int)(22.41962*1e6),(int)(114.20899*1e6)));
        busLine.add(new GeoPoint((int)(22.41966*1e6),(int)(114.20881*1e6)));
        busLine.add(new GeoPoint((int)(22.41981*1e6),(int)(114.20860*1e6)));
        busLine.add(new GeoPoint((int)(22.41984*1e6),(int)(114.20854*1e6)));
        busLine.add(new GeoPoint((int)(22.41987*1e6),(int)(114.20327*1e6)));
        busLine.add(new GeoPoint((int)(22.41992*1e6),(int)(114.20314*1e6)));
        busLine.add(new GeoPoint((int)(22.42000*1e6),(int)(114.20307*1e6)));
        busLine.add(new GeoPoint((int)(22.42011*1e6),(int)(114.20304*1e6)));
        busLine.add(new GeoPoint((int)(22.42029*1e6),(int)(114.20304*1e6)));
        busLine.add(new GeoPoint((int)(22.42036*1e6),(int)(114.20308*1e6)));
        busLine.add(new GeoPoint((int)(22.42044*1e6),(int)(114.20319*1e6)));
        busLine.add(new GeoPoint((int)(22.42050*1e6),(int)(114.20330*1e6)));
        busLine.add(new GeoPoint((int)(22.42053*1e6),(int)(114.20339*1e6)));
        busLine.add(new GeoPoint((int)(22.42051*1e6),(int)(114.20348*1e6)));
        busLine.add(new GeoPoint((int)(22.42032*1e6),(int)(114.20370*1e6)));
        busLine.add(new GeoPoint((int)(22.42026*1e6),(int)(114.20385*1e6)));
        busLine.add(new GeoPoint((int)(22.42023*1e6),(int)(114.20399*1e6)));
        busLine.add(new GeoPoint((int)(22.42028*1e6),(int)(114.20445*1e6)));
        busLine.add(new GeoPoint((int)(22.42039*1e6),(int)(114.20512*1e6)));
        busLine.add(new GeoPoint((int)(22.42039*1e6),(int)(114.20551*1e6)));
        busLine.add(new GeoPoint((int)(22.42043*1e6),(int)(114.20568*1e6)));
        busLine.add(new GeoPoint((int)(22.42060*1e6),(int)(114.20592*1e6)));
        busLine.add(new GeoPoint((int)(22.42069*1e6),(int)(114.20607*1e6)));
        busLine.add(new GeoPoint((int)(22.42100*1e6),(int)(114.20697*1e6)));
        busLine.add(new GeoPoint((int)(22.42116*1e6),(int)(114.20730*1e6)));
        busLine.add(new GeoPoint((int)(22.42156*1e6),(int)(114.20732*1e6)));
        
        }
		return busLine;
	}
	
	
}
