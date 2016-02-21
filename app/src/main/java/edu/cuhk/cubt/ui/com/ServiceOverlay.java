package edu.cuhk.cubt.ui.com;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import edu.cuhk.cubt.R;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.format.Time;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

import edu.cuhk.cubt.net.BusActivityRequest.BusActivityRecord;
import edu.cuhk.cubt.store.RouteData;
import edu.cuhk.cubt.ui.CubtMapView;

public class ServiceOverlay extends ItemizedOverlay<OverlayItem> {

	private ArrayList<ServiceOverlayItem> mOverlays = new ArrayList<ServiceOverlayItem>();
	private Context mContext;
	String routeName = RouteData.RE_MTR_R11;
	String pStop, dir = "Down Route", lStop;
	Time ptime;
	CubtMapView cubtMapView;
	
	
	public ServiceOverlay(Drawable defaultMarker, CubtMapView context) {
		super(boundCenterBottom(defaultMarker));
		mContext = context;
		cubtMapView = (CubtMapView) context;
		updateDisplay();
		// TODO Auto-generated constructor stub
	}

	public void updateDisplay(){
		clearOverlay();
		Iterator<GeoPoint> it = busInService().iterator();
		if(it != null){
			while(it.hasNext()){
			GeoPoint busLoc = it.next();
			addOverlay(
					new ServiceOverlayItem(busLoc, 
							lStop , 
							"Last Stop:"+ lStop + "\nPredicted Stop:"+ pStop + "\nDirection: " + dir,
							0)
					);
			}
		}
	}
	
	public void updateDisplay(Iterator<BusActivityRecord> it){
		clearOverlay();
		if(it != null){
			while(it.hasNext()){
				BusActivityRecord bus = it.next();
				GeoPoint busLoc = new GeoPoint((int)(bus.getLatitude()*1e6) , (int)(bus.getLongitude()*1e6));	
				addOverlay(
						new ServiceOverlayItem(busLoc, 
								lStop , 
								"Last Stop:"+ lStop + "\nPredicted Stop:"+ pStop + "\nDirection: " + dir,
								bus.getId()));
			}
		}
	}


	@Override
	protected boolean onTap(int index) {
	  ServiceOverlayItem item = mOverlays.get(index);
	  //cubtMapView.setDisplayRoute(routeName);		 //Move the above action to CubtMapView, then routeOverlay and stopOverlay can pervent from static
	  cubtMapView.doDisplayRouteInfo(item.getId());
	  /*
	  AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
	  dialog.setTitle(item.getTitle());
	  //dialog.setMessage(item.getSnippet());
	  //dialog.show();
	  
	  LayoutInflater inflater = cubtMapView.getLayoutInflater();
	  View layout = inflater.inflate(R.layout.toastview,(ViewGroup)cubtMapView.findViewById(R.id.toastview_root));
	  ImageView image = (ImageView) layout.findViewById(R.id.image);
	  image.setImageResource(R.drawable.bus2);
	  TextView text = (TextView) layout.findViewById(R.id.text);
	  text.setText("Last Stop:"+ lStop + "\nPredicted Stop:"+ pStop + "\nDirection: " + dir);
	  Toast toast = new Toast(mContext);
	  toast.setGravity(Gravity.TOP, 0, 150);
	  toast.setDuration(Toast.LENGTH_LONG);
	  toast.setView(layout);
	  toast.show();
	  */
	  
	  /*
	  Toast toast = Toast.makeText(mContext, "Last Stop:"+ lStop + "\nPredicted Stop:"+ pStop + "\nDirection: " + dir, Toast.LENGTH_LONG);
	  toast.setGravity(Gravity.TOP, 0, 130);
	  toast.show();
	  */
	  return true;
	}
	
	
	protected class ServiceOverlayItem extends OverlayItem{
		private long id;
		public ServiceOverlayItem(GeoPoint point, String title, String snippet, long id) {
			super(point, title, snippet);
			this.id = id;
		}		
		public long getId(){
			return id;
		}
	}

	@Override
	protected ServiceOverlayItem createItem(int i) {
		
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		return mOverlays.size();
		//return 0;
	}
		
	protected void addOverlay(ServiceOverlayItem overlay){
		mOverlays.add(overlay);
		setLastFocusedIndex(-1);
		populate();
	}
	
	protected void clearOverlay(){
		mOverlays.clear();
		setLastFocusedIndex(-1);
		populate();		
	}

	//Received bus locations
	
	public Collection<GeoPoint> busInService(){
		Collection<GeoPoint> bus = new ArrayList<GeoPoint>();
		//bus.add(new GeoPoint((int)(22.41988*1e6),(int)(114.20551*1e6)));
		//bus.add(new GeoPoint((int)(22.41608*1e6),(int)(114.2088*1e6)));
		return bus;
	}
	
}
