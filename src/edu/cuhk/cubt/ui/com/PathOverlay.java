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
        drawRoute(canvas, RouteData.ROUTE_0);
    }
	

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
	
	
	public void drawPortion (Canvas canvas, Iterator<GeoPoint> it){
		GeoPoint prePoint = (GeoPoint)it.next();
        while(it.hasNext()){
        	GeoPoint currentPoint = (GeoPoint)it.next();
        	drawBasic(canvas, prePoint, currentPoint);
        	prePoint = currentPoint;
        }
	}
	
	public void drawRoute (Canvas canvas, String RouteName){
		if(true){
			//MTR=>NA
			if(RouteName == RouteData.ROUTE_1){ 
				drawPortion(canvas, getPortion("MTRtoUGym").iterator());
				drawPortion(canvas, getPortion("UGymtoTCW").iterator());
				drawPortion(canvas, getPortion("TCWtoNA").iterator());
			}
			//NA=>MTR
			if(RouteName == RouteData.ROUTE_1){
				drawPortion(canvas, getPortion("MTRtoUGym").iterator());
				drawPortion(canvas, getPortion("TCWtoUGym").iterator());
				drawPortion(canvas, getPortion("TCWtoNA").iterator());
			}
			//MTR=>SRR=>MTR
			if(RouteName == RouteData.ROUTE_1){        		
				drawPortion(canvas, getPortion("MTRtoUGym").iterator());
				drawPortion(canvas, getPortion("UGymtoTCW").iterator());
				drawPortion(canvas, getPortion("TCWtoUGym").iterator());
			}
			//MTR=>SHAW=>MTR
			if(RouteName == RouteData.ROUTE_0){
				drawPortion(canvas, getPortion("MTRtoUGym").iterator());
				drawPortion(canvas, getPortion("UGymtoTCW").iterator());
				drawPortion(canvas, getPortion("TCWtoUGym").iterator());
				drawPortion(canvas, getPortion("TCWtoSHAW").iterator());
				drawPortion(canvas, getPortion("SHAWcircle").iterator());
			}
		}
	}
	Collection<GeoPoint> getPortion(String portion){
		Collection<GeoPoint> busLine = new ArrayList<GeoPoint>();
		if(portion == "MTRtoUGym"){
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
		}
		else if(portion == "UGymtoTCW"){
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
	        busLine.add(new GeoPoint((int)(22.41983*1e6),(int)(114.2043*1e6)));
		}
		else if(portion == "TCWtoNA"){
			busLine.add(new GeoPoint((int)(22.41983*1e6),(int)(114.2043*1e6)));
			busLine.add(new GeoPoint((int)(22.41982*1e6),(int)(114.2033*1e6)));
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
		else if(portion == "TCWtoUGym"){
	        busLine.add(new GeoPoint((int)(22.41983*1e6),(int)(114.2043*1e6)));
	        busLine.add(new GeoPoint((int)(22.41873*1e6),(int)(114.2043*1e6)));
	        busLine.add(new GeoPoint((int)(22.41872*1e6),(int)(114.2054*1e6)));
	        busLine.add(new GeoPoint((int)(22.41856*1e6),(int)(114.2061*1e6)));
	        busLine.add(new GeoPoint((int)(22.41843*1e6),(int)(114.2076*1e6)));
	        busLine.add(new GeoPoint((int)(22.41844*1e6),(int)(114.2080*1e6)));
	        busLine.add(new GeoPoint((int)(22.41860*1e6),(int)(114.20843*1e6)));
		}
		else if(portion == "TCWtoSHAW"){
			busLine.add(new GeoPoint((int)(22.41983*1e6),(int)(114.2043*1e6)));
			busLine.add(new GeoPoint((int)(22.41987*1e6),(int)(114.20327*1e6)));
	        busLine.add(new GeoPoint((int)(22.41992*1e6),(int)(114.20314*1e6)));
	        busLine.add(new GeoPoint((int)(22.42000*1e6),(int)(114.20307*1e6)));
	        busLine.add(new GeoPoint((int)(22.42011*1e6),(int)(114.20304*1e6)));
	        busLine.add(new GeoPoint((int)(22.42029*1e6),(int)(114.20304*1e6)));
	        busLine.add(new GeoPoint((int)(22.42036*1e6),(int)(114.20308*1e6)));
	        busLine.add(new GeoPoint((int)(22.42044*1e6),(int)(114.20319*1e6)));
	        busLine.add(new GeoPoint((int)(22.42050*1e6),(int)(114.20330*1e6)));
	        busLine.add(new GeoPoint((int)(22.42053*1e6),(int)(114.20339*1e6)));	        
	        busLine.add(new GeoPoint((int)(22.4216*1e6),(int)(114.2034*1e6)));
	        
	        busLine.add(new GeoPoint((int)(22.4218*1e6),(int)(114.2024*1e6)));
	        busLine.add(new GeoPoint((int)(22.4219*1e6),(int)(114.2018*1e6)));
	        busLine.add(new GeoPoint((int)(22.4221*1e6),(int)(114.2013*1e6)));
	        
		}
		else if(portion == "SHAWcircle"){
			busLine.add(new GeoPoint((int)(22.4221*1e6),(int)(114.2013*1e6)));
	        busLine.add(new GeoPoint((int)(22.4220*1e6),(int)(114.2007*1e6)));
	        busLine.add(new GeoPoint((int)(22.4222*1e6),(int)(114.2005*1e6)));
	        busLine.add(new GeoPoint((int)(22.4226*1e6),(int)(114.2005*1e6)));
	        busLine.add(new GeoPoint((int)(22.4227*1e6),(int)(114.2006*1e6)));
	        busLine.add(new GeoPoint((int)(22.4229*1e6),(int)(114.2009*1e6)));
	        busLine.add(new GeoPoint((int)(22.4231*1e6),(int)(114.2009*1e6)));
	        busLine.add(new GeoPoint((int)(22.4235*1e6),(int)(114.2007*1e6)));
	        busLine.add(new GeoPoint((int)(22.4239*1e6),(int)(114.2008*1e6)));
	        busLine.add(new GeoPoint((int)(22.4242*1e6),(int)(114.2016*1e6)));
	        busLine.add(new GeoPoint((int)(22.4245*1e6),(int)(114.2018*1e6)));
	        busLine.add(new GeoPoint((int)(22.4249*1e6),(int)(114.2020*1e6)));
	        busLine.add(new GeoPoint((int)(22.4251*1e6),(int)(114.2023*1e6)));
	        busLine.add(new GeoPoint((int)(22.4252*1e6),(int)(114.2025*1e6)));
	        busLine.add(new GeoPoint((int)(22.4253*1e6),(int)(114.2032*1e6)));
	        busLine.add(new GeoPoint((int)(22.4253*1e6),(int)(114.2035*1e6)));
	        busLine.add(new GeoPoint((int)(22.4259*1e6),(int)(114.2046*1e6)));
	        busLine.add(new GeoPoint((int)(22.4255*1e6),(int)(114.2076*1e6)));
	        busLine.add(new GeoPoint((int)(22.4251*1e6),(int)(114.2076*1e6)));
	        busLine.add(new GeoPoint((int)(22.4251*1e6),(int)(114.2072*1e6)));
	        busLine.add(new GeoPoint((int)(22.4245*1e6),(int)(114.2066*1e6)));
	        busLine.add(new GeoPoint((int)(22.4241*1e6),(int)(114.2066*1e6)));
	        busLine.add(new GeoPoint((int)(22.4232*1e6),(int)(114.2060*1e6)));
	        busLine.add(new GeoPoint((int)(22.4232*1e6),(int)(114.2058*1e6)));
	        busLine.add(new GeoPoint((int)(22.4232*1e6),(int)(114.2055*1e6)));
	        busLine.add(new GeoPoint((int)(22.4234*1e6),(int)(114.2052*1e6)));
	        busLine.add(new GeoPoint((int)(22.4232*1e6),(int)(114.2046*1e6)));
	        busLine.add(new GeoPoint((int)(22.4229*1e6),(int)(114.2045*1e6)));
	        busLine.add(new GeoPoint((int)(22.4224*1e6),(int)(114.2046*1e6)));
	        busLine.add(new GeoPoint((int)(22.4221*1e6),(int)(114.2046*1e6)));
	        busLine.add(new GeoPoint((int)(22.4221*1e6),(int)(114.2041*1e6)));
	        busLine.add(new GeoPoint((int)(22.4219*1e6),(int)(114.2039*1e6)));
	        busLine.add(new GeoPoint((int)(22.4216*1e6),(int)(114.2034*1e6))); 
		}
		return busLine;
	}
	
}
