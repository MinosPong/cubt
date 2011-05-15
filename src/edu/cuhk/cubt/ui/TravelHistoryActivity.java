package edu.cuhk.cubt.ui;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

import edu.cuhk.cubt.R;
import edu.cuhk.cubt.db.DbTravelLocation;
import edu.cuhk.cubt.ui.com.TravelHistoryOverlay;
import edu.cuhk.cubt.util.CuhkLocation;

import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class TravelHistoryActivity extends MapActivity{

	private static final String tag = "TravelHistoryActivity";
	MapView mapView;
	ListView listView;
	TravelHistoryOverlay travelHistoryOverlay;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.travellocation);
		findViews();
		updateTravelHistory();
	}

	private void findViews() {
		mapView = (MapView) findViewById(R.id.travelmapview);
	    mapView.getController().setZoom(16);
	    mapView.setBuiltInZoomControls(true);
	    
	    Location location = CuhkLocation.getInstance();
	    mapView.getController().animateTo(new GeoPoint(
	    		(int)(location.getLatitude() * 1E6),
	    		(int)(location.getLongitude() * 1E6)));
	    
	    travelHistoryOverlay = new TravelHistoryOverlay(this.getResources().getDrawable(R.drawable.stop), mapView);
	    
	    
	    boolean result = mapView.getOverlays().add(travelHistoryOverlay);


		listView = (ListView)findViewById(R.id.travellocationlist);
	    listView.setOnItemClickListener (new OnItemClickListener (){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long id) {
				Log.i(tag, "onItemClick");

				Cursor cursor = loadTravelData(id);

				Log.i(tag, "ID :" + id + " cursor cnt:" + cursor.getCount() );
				if(cursor != null){
					startManagingCursor(cursor);
					travelHistoryOverlay.setRoute(cursor);
					cursor.close();
				}
				mapView.invalidate();
			}
	    });
	}
	
	private Cursor loadTravelData(long id){
		DbTravelLocation db = DbTravelLocation.getInstance(TravelHistoryActivity.this);
		Cursor cursor = db.getLocations(new String[] {	
				DbTravelLocation.TravelLocationColumns._ID,
				DbTravelLocation.TravelLocationColumns.TIME,
				DbTravelLocation.TravelLocationColumns.LATITUDE,
				DbTravelLocation.TravelLocationColumns.LONGITUDE
			}, id);
		return cursor;
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	private void updateTravelHistory(){
		DbTravelLocation db = DbTravelLocation.getInstance(this);
		Cursor cursor = db.getTravel();
		if(cursor != null){
			Log.i("Cursor Count:",cursor.getCount()+"");
			startManagingCursor(cursor);
			SimpleCursorAdapter adapter = new SimpleCursorAdapter (this, R.layout.travelhistory_list_item, cursor, 
					new String[] {	DbTravelLocation.TravelLocationColumns._ID,
									DbTravelLocation.TravelLocationColumns.LONGITUDE,
									DbTravelLocation.TravelLocationColumns.LATITUDE
								}, 
					new int[] {R.id.tid, R.id.enter_time, R.id.leave_time});
			adapter.setViewBinder(travelViewBinder);
			listView.setAdapter(adapter);
		}
		
	}
	
	private SimpleCursorAdapter.ViewBinder travelViewBinder = new SimpleCursorAdapter.ViewBinder(){

		@Override
		public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
			if(columnIndex >= 1  ){
				Time t = new Time();
				t.set(cursor.getLong(columnIndex));	
				((TextView)view).setText(t.format("%Y/%m/%d %H:%M:%S"));
				return true;
			}
			return false;
		}
		
	};
	
}
