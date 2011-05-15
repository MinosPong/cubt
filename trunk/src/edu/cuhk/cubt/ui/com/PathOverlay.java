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

import edu.cuhk.cubt.bus.Poi;
import edu.cuhk.cubt.bus.Route;
import edu.cuhk.cubt.bus.Stop;
import edu.cuhk.cubt.store.PoiData;
import edu.cuhk.cubt.store.RouteData;
import edu.cuhk.cubt.ui.com.ServiceOverlay.ServiceOverlayItem;

public class PathOverlay extends ItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	MapView mapView=null;
	Paint paint=new Paint();
	private Context mContext;
	private String routeName = null; //RouteData.ROUTE_0
	private Poi lastStop = null;
	private Poi nextStop = null;
	//private String last = "", next = "";
	
	public PathOverlay(Drawable defaultMarker,MapView mapview,Context context) {

	    super(boundCenterBottom(defaultMarker));
	    mapView=mapview;
	    mContext=context; 
	    updateDisplay();
	}
	
	public PathOverlay(Drawable defaultMarker) {
		super(defaultMarker);
	}

	//connect to the CubtMapView(menus)
	public void setPath(String routeName){
		if(this.routeName!= routeName){
			this.routeName = routeName;
		}
	}
	
	public void updateDisplay() {		
		clearOverlay();
		if(lastStop != null){			
			GeoPoint stopLoc = new GeoPoint((int)(lastStop.getLatitude()*1e6),(int)(lastStop.getLongitude()*1e6));
			addOverlay(new OverlayItem(stopLoc, "last", "stop"));
		}
		if(nextStop != null){			
			GeoPoint stopLoc = new GeoPoint((int)(nextStop.getLatitude()*1e6),(int)(nextStop.getLongitude()*1e6));
			addOverlay(new OverlayItem(stopLoc, "next", "stop"));
		}
	}
	
	public void clearOverlay(){
		mOverlays.clear();
		setLastFocusedIndex(-1);
		populate();		
	}
	
	public void addOverlay(OverlayItem overlay) {
		mOverlays.add(overlay);
		setLastFocusedIndex(-1);
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
        if(routeName != null)
        	newDrawRoute(canvas, routeName); //RouteData.ROUTE_0
        //last = PoiData.STOP_CCS;
        //next = PoiData.STOP_MTR;
        String last = "", next = "";
        if(lastStop != null)
        	last = lastStop.getName();
        else last = null;
        if(nextStop != null)
        	next = nextStop.getName();
        else next = null;
        //if(last != null && next != null)
        	drawPrediction(canvas, last, next);
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

        paint.setStrokeWidth(2);
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
	
	public void drawPrediction(Canvas canvas,String lastStop, String nextStop){
		String path = null;
		if(lastStop == PoiData.STOP_MTR && nextStop == PoiData.STOP_SPU)
			path = "MTRtoSPU";
		else if(lastStop == PoiData.STOP_SPD && nextStop == PoiData.STOP_MTR)
			path = "MTRtoSPU";
		else if(lastStop == PoiData.STOP_MTR && nextStop == PoiData.STOP_PGH){
        	path = "SPDtoPGH";
			drawPortion(canvas, getPortion(path).iterator());
			path = "MTRtoSPU";
		}
		else if(lastStop == PoiData.STOP_PGH && nextStop == PoiData.STOP_SPU)
        	path = "SPDtoPGH";
		else if(lastStop == PoiData.STOP_MTR && nextStop == PoiData.STOP_ADM){
        	path = "FKHtoR34";
        	drawPortion(canvas, getPortion(path).iterator());
			path = "FKHtoADM";
		}
		else if(lastStop == PoiData.STOP_SPU && nextStop == PoiData.STOP_SRR)
        	path = "SPUtoSRR";
		else if(lastStop == PoiData.STOP_ADM && nextStop == PoiData.STOP_SRR)
        	path = "ADMtoSRR";
		else if(lastStop == PoiData.STOP_SRR && nextStop == PoiData.STOP_FKH)
        	path = "SRRtoFKH";
		else if(lastStop == PoiData.STOP_FKH && nextStop == PoiData.STOP_UCS)
        	path = "FKHtoUCS";
		else if(lastStop == PoiData.STOP_UCS && nextStop == PoiData.STOP_NAS)
        	path = "UCStoNAS";
		else if(lastStop == PoiData.STOP_NAS && nextStop == PoiData.STOP_UCS)
        	path = "UCStoNAS";
		else if(lastStop == PoiData.STOP_UCS && nextStop == PoiData.STOP_ADM){
        	path = "FKHtoUCS";
        	drawPortion(canvas, getPortion(path).iterator());
			path = "FKHtoADM";
		}
		else if(lastStop == PoiData.STOP_SRR && nextStop == PoiData.STOP_NAS)
        	path = "SRRtoNAS";
		else if(lastStop == PoiData.STOP_UCS && nextStop == PoiData.STOP_R34)
        	path = "UCStoR34";
		else if(lastStop == PoiData.STOP_FKH && nextStop == PoiData.STOP_R34)
        	path = "FKHtoR34";
		else if(lastStop == PoiData.STOP_R34 && nextStop == PoiData.STOP_ADM){
	        	path = "FKHtoR34";
	        	drawPortion(canvas, getPortion(path).iterator());
				path = "FKHtoADM";
			}
		else if(lastStop == PoiData.STOP_FKH && nextStop == PoiData.STOP_SCS){
        	path = "FKHtoR34";
        	drawPortion(canvas, getPortion(path).iterator());
			path = "R34toSCS";
		}
		else if(lastStop == PoiData.STOP_R34 && nextStop == PoiData.STOP_ADM){
        	path = "FKHtoR34";
        	drawPortion(canvas, getPortion(path).iterator());
			path = "FKHtoADM";
		}
		else if(lastStop == PoiData.STOP_CCH && nextStop == PoiData.STOP_FKH){
        	path = "FKHtoR34";
        	drawPortion(canvas, getPortion(path).iterator());
			path = "CCHtoR34";
		}
		else if(lastStop == PoiData.STOP_R34 && nextStop == PoiData.STOP_SCS)
        	path = "R34toSCS";
		else if(lastStop == PoiData.STOP_R34 && nextStop == PoiData.STOP_UCS)
        	path = "UCStoR34";
		else if(lastStop == PoiData.STOP_SCS && nextStop == PoiData.STOP_R34)
        	path = "R34toSCS";
		else if(lastStop == PoiData.STOP_SCS && nextStop == PoiData.STOP_R11)
        	path = "SCStoR11";
		else if(lastStop == PoiData.STOP_R11 && nextStop == PoiData.STOP_R15)
        	path = "R11toR15";
		else if(lastStop == PoiData.STOP_R15 && nextStop == PoiData.STOP_R11)
        	path = "R11toR15";
		else if(lastStop == PoiData.STOP_R15 && nextStop == PoiData.STOP_RUC)
        	path = "R15toRUC";
		else if(lastStop == PoiData.STOP_RUC && nextStop == PoiData.STOP_R15)
        	path = "R15toRUC";
		else if(lastStop == PoiData.STOP_RUC && nextStop == PoiData.STOP_CCH)
        	path = "RUCtoCCH";
		else if(lastStop == PoiData.STOP_CCH && nextStop == PoiData.STOP_R34)
        	path = "CCHtoR34";
		else if(lastStop == PoiData.STOP_SPD && nextStop == PoiData.STOP_PGH)
        	path = "SPDtoPGH";
		else if(lastStop == PoiData.STOP_SPD && nextStop == PoiData.STOP_CCS)
        	path = "SPDtoCCS";
		else if(lastStop == PoiData.STOP_FKH && nextStop == PoiData.STOP_ADM)
            path = "FKHtoADM";
		else if(lastStop == PoiData.STOP_ADM && nextStop == PoiData.STOP_P5H)
        	path = "ADMtoP5H";
		else if(lastStop == PoiData.STOP_P5H && nextStop == PoiData.STOP_SPD)
        	path = "P5HtoSPD";
		else if(lastStop == PoiData.STOP_SRR && nextStop == PoiData.STOP_ADM)
        	path = "SRRtoADM";
		else if(lastStop == PoiData.STOP_CCS && nextStop == PoiData.STOP_MTR)
        	path = "CCStoMTR";
		else if(lastStop == PoiData.STOP_R34 && nextStop == PoiData.STOP_RUC){
        	path = "RUCtoCCH";
        	drawPortion(canvas, getPortion(path).iterator());
        	path = "CCHtoR34";
        }
		else if(lastStop == PoiData.STOP_CCS && nextStop == PoiData.STOP_SPU){
        	path = "CCStoMTR";
        	drawPortion(canvas, getPortion(path).iterator());
        	path = "MTRtoSPU";
        }
		else {
			lastStop = null;
			nextStop = null;
		}
		if(lastStop != null && nextStop != null)
			drawPortion(canvas, getPortion(path).iterator());
	}
	
	public void newDrawRoute(Canvas canvas,String routeName){
		Route route = RouteData.getRouteByName(routeName);
		Iterator<Stop> stops = route.getStops();
		Stop prevStop,nextStop;
		if(stops.hasNext()){
			prevStop = stops.next();
			while(stops.hasNext()){
				nextStop = stops.next();
				drawPrediction(canvas, prevStop.getName(),nextStop.getName());
				prevStop = nextStop;
			}
		}
		
	}
	
	public void drawRoute (Canvas canvas, String routeName){
		//MTR=>NA(1) && //MTR=>10+11(17) &&//CC=>SHAW(4) &&//MTR=>10+11(Sun)(0*)
		if(		routeName == RouteData.RM_MTR_NA || 
				routeName == RouteData.RD_MTR_NA || 
				routeName == RouteData.RH_MTR_R11 || 
				routeName == RouteData.RH_MTR_SHAW || 
				routeName == RouteData.RE_MTR_R11){ 
			drawPortion(canvas, getPortion("MTRtoSPU").iterator());
			drawPortion(canvas, getPortion("SPUtoSRR").iterator());
			drawPortion(canvas, getPortion("SRRtoFKH").iterator());
			drawPortion(canvas, getPortion("FKHtoUCS").iterator());
			drawPortion(canvas, getPortion("UCStoNAS").iterator());
			//MTR=>10+11
			if(routeName == RouteData.RE_MTR_R11){
				drawPortion(canvas, getPortion("FKHtoR34").iterator());
				drawPortion(canvas, getPortion("R11toR15").iterator());
				drawPortion(canvas, getPortion("R15toRUC").iterator());
				drawPortion(canvas, getPortion("RUCtoCCH").iterator());
				drawPortion(canvas, getPortion("CCHtoR34").iterator());
				//drawPortion(canvas, getPortion("R11toTerminal").iterator());
				drawPortion(canvas, getPortion("SPDtoPGH").iterator());					
			}
			//MTR=>10+11(Sun)
			if(routeName == RouteData.RH_MTR_R11){
				drawPortion(canvas, getPortion("FKHtoR34").iterator());
				drawPortion(canvas, getPortion("R11toR15").iterator());
				drawPortion(canvas, getPortion("R15toRUC").iterator());
				drawPortion(canvas, getPortion("RUCtoCCH").iterator());
				drawPortion(canvas, getPortion("CCHtoR34").iterator());
				drawPortion(canvas, getPortion("R34toSCS").iterator());
				drawPortion(canvas, getPortion("SHAWsmallcircle").iterator());
			}
			//CC=>SHAW
			if(routeName == RouteData.RH_MTR_SHAW){
				drawPortion(canvas, getPortion("FKHtoR34").iterator());
				drawPortion(canvas, getPortion("R34toSCS").iterator());
				drawPortion(canvas, getPortion("SHAWsmallcircle").iterator());
			}
		}
		
		//NA=>CC(8) && SHAW=>CC(9)
		if(routeName == RouteData.RC_NA_CC || routeName == RouteData.RC_SHAW_CC){
			drawPortion(canvas, getPortion("SPDtoCCS").iterator());
			drawPortion(canvas, getPortion("FKHtoADM").iterator());
			drawPortion(canvas, getPortion("ADMtoP5H").iterator());
			drawPortion(canvas, getPortion("P5HtoSPD").iterator());
			drawPortion(canvas, getPortion("FKHtoUCS").iterator());
			drawPortion(canvas, getPortion("UCStoNAS").iterator());
			//SHAW=>CC
			if(routeName == RouteData.RC_SHAW_CC){
				drawPortion(canvas, getPortion("R34toSCS").iterator());
				drawPortion(canvas, getPortion("FKHtoR34").iterator());
			}
		}
		
		//NA=>MTR(5) && //SHAW=>MTR(6) && //10+11=>MTR(7)
		if(		routeName == RouteData.RM_NA_MTR || 
				routeName == RouteData.RD_NA_MTR || 
				routeName == RouteData.RE_SHAW_MTR || 
				routeName == RouteData.RH_SHAW_MTR || 
				routeName == RouteData.RE_R11_MTR){
			drawPortion(canvas, getPortion("MTRtoSPU").iterator());
			drawPortion(canvas, getPortion("FKHtoADM").iterator());
			drawPortion(canvas, getPortion("ADMtoP5H").iterator());
			drawPortion(canvas, getPortion("P5HtoSPD").iterator());
			drawPortion(canvas, getPortion("FKHtoUCS").iterator());
			drawPortion(canvas, getPortion("UCStoNAS").iterator());
			//SHAW=>MTR && SHAE=>MTR(Sun)<same>`
			if(routeName == RouteData.RE_SHAW_MTR || routeName == RouteData.RH_SHAW_MTR
					){
				drawPortion(canvas, getPortion("R34toSCS").iterator());
				drawPortion(canvas, getPortion("FKHtoR34").iterator());
			}
			//10+11=>MTR && 10+11=>MTR(Sun)
			if(routeName == RouteData.RE_R11_MTR){
				drawPortion(canvas, getPortion("FKHtoR34").iterator());
				drawPortion(canvas, getPortion("R11toR15").iterator());
				drawPortion(canvas, getPortion("R15toRUC").iterator());
				drawPortion(canvas, getPortion("RUCtoCCH").iterator());
				drawPortion(canvas, getPortion("CCHtoR34").iterator());
				//drawPortion(canvas, getPortion("R11toTerminal").iterator());
				if(routeName == RouteData.RE_R11_MTR) //10+11=>MTR (not Sun)*
					drawPortion(canvas, getPortion("SPDtoPGH").iterator());
				drawPortion(canvas, getPortion("R34toSCS").iterator());
				drawPortion(canvas, getPortion("SHAWsmallcircle").iterator());
			}
		}
		
		//MTR=>SRR=>MTR(10)
		if(routeName == RouteData.RM_MTR_SRR){        		
			drawPortion(canvas, getPortion("MTRtoSPU").iterator());
			drawPortion(canvas, getPortion("SPUtoSRR").iterator());
			drawPortion(canvas, getPortion("SRRtoADM").iterator());
			drawPortion(canvas, getPortion("ADMtoP5H").iterator());
			drawPortion(canvas, getPortion("P5HtoSPD").iterator());
		}
		
		//MTR=>SHAW=>MTR(2)
		if(routeName == RouteData.RD_SHAW_C){
			drawPortion(canvas, getPortion("MTRtoSPU").iterator());
			drawPortion(canvas, getPortion("SPUtoSRR").iterator());
			drawPortion(canvas, getPortion("SRRtoFKH").iterator());
			drawPortion(canvas, getPortion("FKHtoADM").iterator());
			drawPortion(canvas, getPortion("ADMtoP5H").iterator());
			drawPortion(canvas, getPortion("P5HtoSPD").iterator());
			drawPortion(canvas, getPortion("FKHtoR34").iterator());
			drawPortion(canvas, getPortion("R34toSCS").iterator());
			drawPortion(canvas, getPortion("SCStoR11").iterator());
			drawPortion(canvas, getPortion("R11toR15").iterator());
			drawPortion(canvas, getPortion("R15toRUC").iterator());
			drawPortion(canvas, getPortion("RUCtoCCH").iterator());
			drawPortion(canvas, getPortion("CCHtoR34").iterator());
			drawPortion(canvas, getPortion("FKHtoR34").iterator());
		}
		
		//LHC=>SHAW=>LHC(11) && SHAW=>ADMIN=>SHAW<same>
		if(routeName == RouteData.RM_SRR_SHAW){
			drawPortion(canvas, getPortion("ADMtoSRR").iterator());
			drawPortion(canvas, getPortion("FKHtoR34").iterator());			
			drawPortion(canvas, getPortion("FKHtoADM").iterator());
			drawPortion(canvas, getPortion("R34toSCS").iterator());
			drawPortion(canvas, getPortion("FKHtoUCS").iterator());
			drawPortion(canvas, getPortion("UCStoNAS").iterator());
			drawPortion(canvas, getPortion("SRRtoNAS").iterator());
			drawPortion(canvas, getPortion("SHAWsmallcircle").iterator());
		}
		
		//MTR=>SHAW(3) && MTR=>SHAW(Sun)<same>
		if(routeName == RouteData.RE_MTR_SHAW){
			drawPortion(canvas, getPortion("MTRtoSPU").iterator());
			drawPortion(canvas, getPortion("SPUtoSRR").iterator());
			drawPortion(canvas, getPortion("SRRtoNAS").iterator());
			drawPortion(canvas, getPortion("UCStoNAS").iterator());
			drawPortion(canvas, getPortion("UCStoR34").iterator());				
			drawPortion(canvas, getPortion("R34toSCS").iterator());
			drawPortion(canvas, getPortion("SHAWsmallcircle").iterator());
		}
	}
	
	Collection<GeoPoint> getPortion(String portion){
		Collection<GeoPoint> busLine = new ArrayList<GeoPoint>();
		if(portion == "MTRtoSPU"){
			busLine.add(new GeoPoint((int)(22.414670*1e6),(int)(114.210292*1e6))); //MTR
	        busLine.add(new GeoPoint((int)(22.41529*1e6),(int)(114.21046*1e6)));
	        busLine.add(new GeoPoint((int)(22.41544*1e6),(int)(114.21056*1e6)));
	        busLine.add(new GeoPoint((int)(22.41699*1e6),(int)(114.21220*1e6)));
	        busLine.add(new GeoPoint((int)(22.41710*1e6),(int)(114.21228*1e6)));
	        busLine.add(new GeoPoint((int)(22.41725*1e6),(int)(114.21232*1e6)));
	        busLine.add(new GeoPoint((int)(22.41755*1e6),(int)(114.21235*1e6)));
	        busLine.add(new GeoPoint((int)(22.41758*1e6),(int)(114.21158*1e6)));
	        busLine.add(new GeoPoint((int)(22.417773*1e6),(int)(114.211350*1e6))); //SPD
	        busLine.add(new GeoPoint((int)(22.41790*1e6),(int)(114.21028*1e6))); //SPU
		}
		else if(portion == "SPUtoSRR"){
			busLine.add(new GeoPoint((int)(22.41790*1e6),(int)(114.21028*1e6))); //SPU
			busLine.add(new GeoPoint((int)(22.41795*1e6),(int)(114.20977*1e6)));
	        busLine.add(new GeoPoint((int)(22.41810*1e6),(int)(114.20944*1e6)));
	        busLine.add(new GeoPoint((int)(22.41830*1e6),(int)(114.20900*1e6)));
	        busLine.add(new GeoPoint((int)(22.41857*1e6),(int)(114.20855*1e6)));
	        busLine.add(new GeoPoint((int)(22.41860*1e6),(int)(114.20843*1e6)));
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
	        busLine.add(new GeoPoint((int)(22.419830*1e6),(int)(114.207024*1e6))); //SRR
		}
		else if(portion == "ADMtoSRR"){
			busLine.add(new GeoPoint((int)(22.4187801e6),(int)(114.205260*1e6))); //ADM
			busLine.add(new GeoPoint((int)(22.41872*1e6),(int)(114.2054*1e6)));
	        busLine.add(new GeoPoint((int)(22.41856*1e6),(int)(114.2061*1e6)));
	        busLine.add(new GeoPoint((int)(22.41843*1e6),(int)(114.2076*1e6)));
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
	        busLine.add(new GeoPoint((int)(22.419830*1e6),(int)(114.207024*1e6))); //SRR
		}
		else if(portion == "SRRtoFKH"){
			busLine.add(new GeoPoint((int)(22.419830*1e6),(int)(114.207024*1e6))); //SRR
			busLine.add(new GeoPoint((int)(22.41981*1e6),(int)(114.2061*1e6)));
	        busLine.add(new GeoPoint((int)(22.41983*1e6),(int)(114.2043*1e6)));
	        busLine.add(new GeoPoint((int)(22.41983*1e6),(int)(114.2043*1e6)));
			busLine.add(new GeoPoint((int)(22.41982*1e6),(int)(114.2033*1e6)));
			busLine.add(new GeoPoint((int)(22.41987*1e6),(int)(114.20327*1e6)));
	        busLine.add(new GeoPoint((int)(22.41992*1e6),(int)(114.20314*1e6)));
	        busLine.add(new GeoPoint((int)(22.42000*1e6),(int)(114.20307*1e6))); //FKH	        
		}
		else if(portion == "FKHtoUCS"){
			busLine.add(new GeoPoint((int)(22.419860*1e6),(int)(114.203270*1e6))); //FKH 
			busLine.add(new GeoPoint((int)(22.42000*1e6),(int)(114.20307*1e6))); //TCW curve start
			busLine.add(new GeoPoint((int)(22.42011*1e6),(int)(114.20304*1e6))); 
	        busLine.add(new GeoPoint((int)(22.42029*1e6),(int)(114.20304*1e6)));
	        busLine.add(new GeoPoint((int)(22.42036*1e6),(int)(114.20308*1e6)));
	        busLine.add(new GeoPoint((int)(22.42044*1e6),(int)(114.20319*1e6)));
	        busLine.add(new GeoPoint((int)(22.42050*1e6),(int)(114.20330*1e6)));
	        busLine.add(new GeoPoint((int)(22.42053*1e6),(int)(114.20339*1e6))); //TCW curve end
	  
			busLine.add(new GeoPoint((int)(22.42050*1e6),(int)(114.20330*1e6)));
	        busLine.add(new GeoPoint((int)(22.42053*1e6),(int)(114.20339*1e6)));
	        busLine.add(new GeoPoint((int)(22.42051*1e6),(int)(114.20348*1e6)));
	        busLine.add(new GeoPoint((int)(22.42032*1e6),(int)(114.20370*1e6)));
	        busLine.add(new GeoPoint((int)(22.42026*1e6),(int)(114.20385*1e6)));
	        busLine.add(new GeoPoint((int)(22.42023*1e6),(int)(114.20399*1e6)));
	        busLine.add(new GeoPoint((int)(22.42028*1e6),(int)(114.20445*1e6)));
	        busLine.add(new GeoPoint((int)(22.420363*1e6),(int)(114.205180*1e6)));	//UCS   
		}
		else if(portion == "UCStoNAS"){
			busLine.add(new GeoPoint((int)(22.420363*1e6),(int)(114.205180*1e6))); //UCS
	        busLine.add(new GeoPoint((int)(22.42043*1e6),(int)(114.20568*1e6)));
	        busLine.add(new GeoPoint((int)(22.42060*1e6),(int)(114.20592*1e6)));
	        busLine.add(new GeoPoint((int)(22.42069*1e6),(int)(114.20607*1e6)));
	        busLine.add(new GeoPoint((int)(22.42100*1e6),(int)(114.20697*1e6)));
	        busLine.add(new GeoPoint((int)(22.421279*1e6),(int)(114.207486*1e6)));//NAS
		}
		else if(portion == "SRRtoNAS"){
			busLine.add(new GeoPoint((int)(22.419830*1e6),(int)(114.207024*1e6))); //SRR
			busLine.add(new GeoPoint((int)(22.41981*1e6),(int)(114.2061*1e6)));
			busLine.add(new GeoPoint((int)(22.42021*1e6),(int)(114.2061*1e6)));
			busLine.add(new GeoPoint((int)(22.42093*1e6),(int)(114.2074*1e6)));
			busLine.add(new GeoPoint((int)(22.42089*1e6),(int)(114.2076*1e6)));
			busLine.add(new GeoPoint((int)(22.42052*1e6),(int)(114.2080*1e6)));
			busLine.add(new GeoPoint((int)(22.42053*1e6),(int)(114.2081*1e6)));
			busLine.add(new GeoPoint((int)(22.42061*1e6),(int)(114.2082*1e6)));
			busLine.add(new GeoPoint((int)(22.42086*1e6),(int)(114.2078*1e6)));
			busLine.add(new GeoPoint((int)(22.42102*1e6),(int)(114.2076*1e6)));
			busLine.add(new GeoPoint((int)(22.421279*1e6),(int)(114.207486*1e6)));//NAS
		}
		else if(portion == "UCStoR34"){
			busLine.add(new GeoPoint((int)(22.420363*1e6),(int)(114.205180*1e6))); //UCS
			busLine.add(new GeoPoint((int)(22.42028*1e6),(int)(114.20445*1e6)));
			busLine.add(new GeoPoint((int)(22.42023*1e6),(int)(114.20399*1e6)));
			busLine.add(new GeoPoint((int)(22.42026*1e6),(int)(114.20385*1e6)));
			busLine.add(new GeoPoint((int)(22.42032*1e6),(int)(114.20370*1e6)));
			busLine.add(new GeoPoint((int)(22.42051*1e6),(int)(114.20348*1e6)));
			busLine.add(new GeoPoint((int)(22.42050*1e6),(int)(114.20330*1e6)));
			busLine.add(new GeoPoint((int)(22.421340*1e6),(int)(114.203450*1e6))); //R34 
			
		}
		else if(portion == "FKHtoR34"){
			busLine.add(new GeoPoint((int)(22.419860*1e6),(int)(114.203270*1e6))); //FKH 
			busLine.add(new GeoPoint((int)(22.42000*1e6),(int)(114.20307*1e6))); //TCW curve start
			busLine.add(new GeoPoint((int)(22.42011*1e6),(int)(114.20304*1e6))); 
	        busLine.add(new GeoPoint((int)(22.42029*1e6),(int)(114.20304*1e6)));
	        busLine.add(new GeoPoint((int)(22.42036*1e6),(int)(114.20308*1e6)));
	        busLine.add(new GeoPoint((int)(22.42044*1e6),(int)(114.20319*1e6)));
	        busLine.add(new GeoPoint((int)(22.42050*1e6),(int)(114.20330*1e6)));
	        busLine.add(new GeoPoint((int)(22.42053*1e6),(int)(114.20339*1e6))); //TCW curve end
			busLine.add(new GeoPoint((int)(22.421340*1e6),(int)(114.203450*1e6))); //R34 
		}
		else if(portion == "R34toSCS"){
			busLine.add(new GeoPoint((int)(22.421340*1e6),(int)(114.203450*1e6))); //R34    
	        busLine.add(new GeoPoint((int)(22.4216*1e6),(int)(114.2034*1e6)));        
	        busLine.add(new GeoPoint((int)(22.4218*1e6),(int)(114.2024*1e6)));
	        busLine.add(new GeoPoint((int)(22.4219*1e6),(int)(114.2018*1e6)));
	        busLine.add(new GeoPoint((int)(22.4221*1e6),(int)(114.2013*1e6)));
			busLine.add(new GeoPoint((int)(22.422397*1e6),(int)(114.201395*1e6))); //SCS
			
		}
		else if(portion == "SHAWsmallcircle"){
			busLine.add(new GeoPoint((int)(22.422397*1e6),(int)(114.201395*1e6))); //SCS
			busLine.add(new GeoPoint((int)(22.42211*1e6),(int)(114.2011*1e6)));
			busLine.add(new GeoPoint((int)(22.42233*1e6),(int)(114.2010*1e6)));
			busLine.add(new GeoPoint((int)(22.42268*1e6),(int)(114.2015*1e6)));
			busLine.add(new GeoPoint((int)(22.42286*1e6),(int)(114.2015*1e6)));
			busLine.add(new GeoPoint((int)(22.42284*1e6),(int)(114.2017*1e6)));
			busLine.add(new GeoPoint((int)(22.42271*1e6),(int)(114.2018*1e6)));
			busLine.add(new GeoPoint((int)(22.42257*1e6),(int)(114.2017*1e6)));
			busLine.add(new GeoPoint((int)(22.42256*1e6),(int)(114.2015*1e6)));
			busLine.add(new GeoPoint((int)(22.42264*1e6),(int)(114.2014*1e6)));
		}
		else if(portion == "SCStoR11"){
			busLine.add(new GeoPoint((int)(22.422397*1e6),(int)(114.201395*1e6))); //SCS
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
	        busLine.add(new GeoPoint((int)(22.425152*1e6),(int)(114.207891*1e6)));	//R11
		}
		/*
		else if(portion == "R11toTerminal"){
			busLine.add(new GeoPoint((int)(22.4251*1e6),(int)(114.2076*1e6)));
			busLine.add(new GeoPoint((int)(22.42488*1e6),(int)(114.2078*1e6)));
			busLine.add(new GeoPoint((int)(22.42441*1e6),(int)(114.2080*1e6)));
			
		}
		*/
		else if(portion == "R11toR15"){
			busLine.add(new GeoPoint((int)(22.425152*1e6),(int)(114.207891*1e6)));	//R11
			busLine.add(new GeoPoint((int)(22.42508*1e6),(int)(114.2077*1e6)));
			busLine.add(new GeoPoint((int)(22.42498*1e6),(int)(114.2072*1e6)));
			busLine.add(new GeoPoint((int)(22.42471*1e6),(int)(114.2069*1e6)));
			busLine.add(new GeoPoint((int)(22.42446*1e6),(int)(114.2067*1e6)));
			busLine.add(new GeoPoint((int)(22.42414*1e6),(int)(114.2067*1e6)));
	        busLine.add(new GeoPoint((int)(22.423766*1e6),(int)(114.206700*1e6))); //R15
		}
		else if(portion == "R15toRUC"){
			busLine.add(new GeoPoint((int)(22.423766*1e6),(int)(114.206700*1e6))); //R15
			busLine.add(new GeoPoint((int)(22.42322*1e6),(int)(114.2061*1e6)));
			busLine.add(new GeoPoint((int)(22.42305*1e6),(int)(114.2058*1e6)));
			busLine.add(new GeoPoint((int)(22.42322*1e6),(int)(114.2053*1e6)));
			busLine.add(new GeoPoint((int)(22.423364*1e6),(int)(114.205308*1e6))); //RUC
		}
		else if (portion == "RUCtoCCH"){
			busLine.add(new GeoPoint((int)(22.423364*1e6),(int)(114.205308*1e6))); //RUC
			busLine.add(new GeoPoint((int)(22.42332*1e6),(int)(114.2051*1e6)));
			busLine.add(new GeoPoint((int)(22.42304*1e6),(int)(114.2046*1e6)));
			busLine.add(new GeoPoint((int)(22.42272*1e6),(int)(114.2046*1e6)));
			busLine.add(new GeoPoint((int)(22.42238*1e6),(int)(114.2048*1e6)));
			busLine.add(new GeoPoint((int)(22.42203*1e6),(int)(114.2047*1e6)));
			busLine.add(new GeoPoint((int)(22.421850*1e6),(int)(114.204600*1e6))); //CCH
		}
		else if (portion == "CCHtoR34"){
			busLine.add(new GeoPoint((int)(22.421850*1e6),(int)(114.204600*1e6))); //CCH
			busLine.add(new GeoPoint((int)(22.42198*1e6),(int)(114.2043*1e6)));
			busLine.add(new GeoPoint((int)(22.42183*1e6),(int)(114.2040*1e6)));
			busLine.add(new GeoPoint((int)(22.42168*1e6),(int)(114.2038*1e6)));
			busLine.add(new GeoPoint((int)(22.42163*1e6),(int)(114.2035*1e6)));
			busLine.add(new GeoPoint((int)(22.421340*1e6),(int)(114.203450*1e6))); //R34 
		}
		else if(portion == "SPDtoPGH"){
			busLine.add(new GeoPoint((int)(22.417773*1e6),(int)(114.211350*1e6))); //SPD
			busLine.add(new GeoPoint((int)(22.4177*1e6),(int)(114.2123*1e6)));
			busLine.add(new GeoPoint((int)(22.4184*1e6),(int)(114.2127*1e6)));
			busLine.add(new GeoPoint((int)(22.4189*1e6),(int)(114.2128*1e6)));
			busLine.add(new GeoPoint((int)(22.4194*1e6),(int)(114.2128*1e6)));
			busLine.add(new GeoPoint((int)(22.4194*1e6),(int)(114.2126*1e6)));
			busLine.add(new GeoPoint((int)(22.4196*1e6),(int)(114.2124*1e6)));
			busLine.add(new GeoPoint((int)(22.4199*1e6),(int)(114.2125*1e6)));
			busLine.add(new GeoPoint((int)(22.420360*1e6),(int)(114.212200*1e6))); //PGH
		}
		//DOWN
		else if(portion == "SRRtoADM"){
			busLine.add(new GeoPoint((int)(22.419830*1e6),(int)(114.207024*1e6))); //SRR
			busLine.add(new GeoPoint((int)(22.41981*1e6),(int)(114.2061*1e6)));
	        busLine.add(new GeoPoint((int)(22.41983*1e6),(int)(114.2043*1e6)));
	        busLine.add(new GeoPoint((int)(22.41983*1e6),(int)(114.2043*1e6)));
			busLine.add(new GeoPoint((int)(22.41983*1e6),(int)(114.2043*1e6))); 
	        busLine.add(new GeoPoint((int)(22.41873*1e6),(int)(114.2043*1e6)));	        
	        busLine.add(new GeoPoint((int)(22.4187801e6),(int)(114.205260*1e6))); //ADM
		}
		else if(portion == "SPDtoCCS"){
			busLine.add(new GeoPoint((int)(22.415541*1e6),(int)(114.208218*1e6))); //CCS
			busLine.add(new GeoPoint((int)(22.41560*1e6),(int)(114.20823*1e6)));
			busLine.add(new GeoPoint((int)(22.41604*1e6),(int)(114.2085*1e6)));
			busLine.add(new GeoPoint((int)(22.41636*1e6),(int)(114.2090*1e6)));
			busLine.add(new GeoPoint((int)(22.41644*1e6),(int)(114.2096*1e6)));
			busLine.add(new GeoPoint((int)(22.41637*1e6),(int)(114.2101*1e6)));
			busLine.add(new GeoPoint((int)(22.41618*1e6),(int)(114.2104*1e6)));
			busLine.add(new GeoPoint((int)(22.41578*1e6),(int)(114.2109*1e6)));
			busLine.add(new GeoPoint((int)(22.41699*1e6),(int)(114.21220*1e6)));
	        busLine.add(new GeoPoint((int)(22.41710*1e6),(int)(114.21228*1e6)));
	        busLine.add(new GeoPoint((int)(22.41725*1e6),(int)(114.21232*1e6)));
	        busLine.add(new GeoPoint((int)(22.41759*1e6),(int)(114.21240*1e6)));
	        busLine.add(new GeoPoint((int)(22.41774*1e6),(int)(114.21111*1e6)));
	        busLine.add(new GeoPoint((int)(22.417773*1e6),(int)(114.211350*1e6))); //SPD
		}
		else if(portion =="FKHtoADM"){
			busLine.add(new GeoPoint((int)(22.419860*1e6),(int)(114.203270*1e6))); //FKH  
			busLine.add(new GeoPoint((int)(22.41983*1e6),(int)(114.2043*1e6))); 
	        busLine.add(new GeoPoint((int)(22.41873*1e6),(int)(114.2043*1e6)));	        
	        busLine.add(new GeoPoint((int)(22.4187801e6),(int)(114.205260*1e6))); //ADM
		}
		else if(portion == "ADMtoP5H"){
			busLine.add(new GeoPoint((int)(22.4187801e6),(int)(114.205260*1e6))); //ADM
			busLine.add(new GeoPoint((int)(22.41872*1e6),(int)(114.2054*1e6)));
	        busLine.add(new GeoPoint((int)(22.41856*1e6),(int)(114.2061*1e6)));
	        busLine.add(new GeoPoint((int)(22.41843*1e6),(int)(114.2076*1e6)));
	        busLine.add(new GeoPoint((int)(22.41844*1e6),(int)(114.2080*1e6)));
	        busLine.add(new GeoPoint((int)(22.41860*1e6),(int)(114.20843*1e6)));
			busLine.add(new GeoPoint((int)(22.418520*1e6),(int)(114.208750*1e6))); //P5H
		}
		else if(portion == "P5HtoSPD"){
			busLine.add(new GeoPoint((int)(22.418520*1e6),(int)(114.208750*1e6))); //P5H
			busLine.add(new GeoPoint((int)(22.41830*1e6),(int)(114.20900*1e6)));
			busLine.add(new GeoPoint((int)(22.41810*1e6),(int)(114.20944*1e6)));
			busLine.add(new GeoPoint((int)(22.41795*1e6),(int)(114.20977*1e6)));
			busLine.add(new GeoPoint((int)(22.41790*1e6),(int)(114.21028*1e6))); //SPU
			busLine.add(new GeoPoint((int)(22.417773*1e6),(int)(114.211350*1e6))); //SPD			
		}
		else if(portion == "SRRtoADM"){
			busLine.add(new GeoPoint((int)(22.419830*1e6),(int)(114.207024*1e6))); //SRR
			busLine.add(new GeoPoint((int)(22.41983*1e6),(int)(114.2043*1e6))); 
	        busLine.add(new GeoPoint((int)(22.41873*1e6),(int)(114.2043*1e6)));	
			busLine.add(new GeoPoint((int)(22.4187801e6),(int)(114.205260*1e6))); //ADM
		}
		else if(portion == "CCStoMTR"){
			busLine.add(new GeoPoint((int)(22.414670*1e6),(int)(114.210292*1e6))); //MTR			
			busLine.add(new GeoPoint((int)(22.41419*1e6),(int)(114.20989*1e6)));
			busLine.add(new GeoPoint((int)(22.41386*1e6),(int)(114.20969*1e6)));
			busLine.add(new GeoPoint((int)(22.41373*1e6),(int)(114.20955*1e6)));
			busLine.add(new GeoPoint((int)(22.41370*1e6),(int)(114.20938*1e6)));
			busLine.add(new GeoPoint((int)(22.41376*1e6),(int)(114.20919*1e6)));
			busLine.add(new GeoPoint((int)(22.41389*1e6),(int)(114.20908*1e6)));
			busLine.add(new GeoPoint((int)(22.41406*1e6),(int)(114.20897*1e6)));
			busLine.add(new GeoPoint((int)(22.41425*1e6),(int)(114.20875*1e6)));
			busLine.add(new GeoPoint((int)(22.41456*1e6),(int)(114.20838*1e6)));
			busLine.add(new GeoPoint((int)(22.41505*1e6),(int)(114.20818*1e6)));
			busLine.add(new GeoPoint((int)(22.41528*1e6),(int)(114.20818*1e6)));
			busLine.add(new GeoPoint((int)(22.41560*1e6),(int)(114.20823*1e6))); 
			busLine.add(new GeoPoint((int)(22.415541*1e6),(int)(114.208218*1e6)));
		}
		return busLine;
	}

	public void PredictedStop(Poi stop){		
		if(nextStop != stop){
			this.nextStop = stop;
		}
	}
	
	public void LastStop(Poi stop){
		if(lastStop != stop){
			this.lastStop = stop;
		}
	}
}
