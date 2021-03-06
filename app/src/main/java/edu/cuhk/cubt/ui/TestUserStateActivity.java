package edu.cuhk.cubt.ui;

import edu.cuhk.cubt.CubtApplication;
import edu.cuhk.cubt.sccm.LocationSensor;
import edu.cuhk.cubt.sccm.SCCMEngine;
import edu.cuhk.cubt.sccm.classifier.BusClassifier;
import edu.cuhk.cubt.sccm.classifier.LocationClassifier;
import edu.cuhk.cubt.sccm.classifier.PoiClassifier;
import edu.cuhk.cubt.sccm.classifier.SpeedClassifier;
import edu.cuhk.cubt.state.PoiState;
import edu.cuhk.cubt.state.State;
import edu.cuhk.cubt.state.event.StateChangeEvent;
import edu.cuhk.cubt.R;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class TestUserStateActivity extends Activity {

	private static final String TAG = "TestUserStateActivity";

	private static final int MENU_START = Menu.FIRST + 101;
	private static final int MENU_STOP = Menu.FIRST + 102;
	
	TextView textTime;
	TextView textGpsStatus;
	
	TextView textGpsLongitude;
	TextView textGpsLatitude;
	TextView textGpsAltitude;
	TextView textLocationState;
	TextView textPoiState;

	TextView textSpeedValue;
	TextView textSpeedState;
	TextView textBusState;
	
	SCCMEngine engine;
	private boolean mIsBound;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MENU_START, MENU_START, R.string.menu_start).setIcon(android.R.drawable.ic_menu_add);
		menu.add(0, MENU_STOP, MENU_STOP, R.string.menu_stop).setIcon(android.R.drawable.ic_menu_close_clear_cancel);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case MENU_START:
				((CubtApplication)getApplication()).startService();
				doBindService();
				return true;
			case MENU_STOP:
				doUnbindService();
				((CubtApplication)getApplication()).stopService();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//((CubtApplication)getApplication()).startService();
		
		doBindService();	
		
		setContentView(R.layout.userstate);
		findViews();
	}

	@Override
	protected void onDestroy(){
		doUnbindService();
		super.onDestroy();
	}
	
	void doBindService(){
		boolean bindResult = getApplicationContext().bindService(new Intent(TestUserStateActivity.this, CubtService.class), mConnection , 0);
		if(bindResult){
			mIsBound = true;
			Toast.makeText(TestUserStateActivity.this, "Connected with Service..." , Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(TestUserStateActivity.this, "Failed to connect with service..." , Toast.LENGTH_SHORT).show();
		}
	}
	
	void doUnbindService(){
		if(mIsBound){
			getApplicationContext().unbindService(mConnection);
            mIsBound = false;
    		Toast.makeText(TestUserStateActivity.this, "Disconnected with Service...", Toast.LENGTH_SHORT).show();
		}
	}	
	
	private ServiceConnection mConnection = new ServiceConnection(){

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			CubtService mService = ((CubtService.CubtServiceBinder)service).getService();
			engine = mService.getSCCMEngine();
			setHandler();
			Log.i(TAG,"Service Connected");
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			Log.i(TAG,"Service Disconnected");
			removeHandler();
			engine = null;
		}

	};

	private void removeHandler() {
		engine.getClassifierManager().
			getClassifier(LocationClassifier.class).removeHandler(mHandler);
		engine.getClassifierManager().
			getClassifier(PoiClassifier.class).removeHandler(mHandler);
		engine.getClassifierManager().
			getClassifier(SpeedClassifier.class).removeHandler(mHandler);
		engine.getClassifierManager()
			.getClassifier(BusClassifier.class).removeHandler(mHandler);
		engine.getLocationSensor().removeHandler(mHandler);
	}		
	
	private void setHandler(){
		engine.getClassifierManager().
			getClassifier(LocationClassifier.class).addHandler(mHandler);
		engine.getClassifierManager().
			getClassifier(PoiClassifier.class).addHandler(mHandler);
		engine.getClassifierManager().
			getClassifier(SpeedClassifier.class).addHandler(mHandler);
		engine.getClassifierManager()
			.getClassifier(BusClassifier.class).addHandler(mHandler);
		engine.getLocationSensor().addHandler(mHandler);		
	}
	
	
	private void findViews() {
		textTime = (TextView) findViewById(R.id.timeText);
		textGpsStatus = (TextView) findViewById(R.id.gpsStatusText);
		textGpsLongitude = (TextView) findViewById(R.id.longText);
		textGpsLatitude = (TextView) findViewById(R.id.latText);
		textGpsAltitude = (TextView) findViewById(R.id.altText);
		textLocationState = (TextView) findViewById(R.id.locationText);
		textPoiState = (TextView) findViewById(R.id.poiText);
		textSpeedValue = (TextView) findViewById(R.id.speedValue);
		textSpeedState = (TextView) findViewById(R.id.speedText);
		textBusState = (TextView) findViewById(R.id.busText);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState){
		super.onSaveInstanceState(outState);
	}
	

	Handler mHandler = new Handler(){
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg){
			if(engine == null) return;
			Time time = new Time();
			time.setToNow();
			textTime.setText(time.toString());

			StateChangeEvent<State> evt;
			
			switch(msg.what){
			case State.TYPE_LOCATION:
				evt = (StateChangeEvent<State>) msg.obj;
				textLocationState.setText(evt.getNewState().getStateString());
				break;
				
				
			case State.TYPE_POI:
				evt = (StateChangeEvent<State>) msg.obj;
				if(evt.getNewState() == PoiState.OUTSIDE_POI){
					textPoiState.setText(evt.getNewState().getStateString());					
				}else{
					textPoiState.setText(engine.getClassifierManager()
						.getClassifier(PoiClassifier.class).getPoi().getName());
				}
				
				break;
				
			case State.TYPE_SPEED:
				evt = (StateChangeEvent<State>) msg.obj;
				textSpeedValue.setText("" + engine.getClassifierManager()
						.getClassifier(SpeedClassifier.class).getSpeed());
				textSpeedState.setText(engine.getClassifierManager()
						.getClassifier(SpeedClassifier.class).getState().getStateString());
				break;
			
			case State.TYPE_BUS:
				evt = (StateChangeEvent<State>) msg.obj;
				textBusState.setText(engine.getClassifierManager()
						.getClassifier(BusClassifier.class).getState().getStateString());
				break;				
				
			case LocationSensor.MSG_NEW_LOCATION:
				updateLocation((Location) msg.obj);
				break;

			case LocationSensor.MSG_PROVIDER_DISABLE:
			case LocationSensor.MSG_PROVIDER_ENABLE:
			case LocationSensor.MSG_PROVIDER_STATUS_CHANGE:
				textGpsStatus.setText((CharSequence) msg.obj);
				break;
			}
			
		}

		private void updateLocation(Location location) {
			// TODO Auto-generated method stub
			textGpsLongitude.setText("Longitude" + location.getLongitude());
			textGpsLatitude.setText("Latitude" + location.getLatitude());
			textGpsAltitude.setText("Altitude" + location.getAltitude());
		}
	};
	
	
}
