package edu.cuhk.cubt.ui;

import edu.cuhk.cubt.classifier.LocationClassifier;
import edu.cuhk.cubt.classifier.PoiClassifier;
import edu.cuhk.cubt.classifier.SpeedClassifier;
import edu.cuhk.cubt.sccm.LocationSensor;
import edu.cuhk.cubt.sccm.SCCMEngine;
import edu.cuhk.cubt.state.PoiState;
import edu.cuhk.cubt.state.State;
import edu.cuhk.cubt.state.event.StateChangeEvent;
import edu.cuhk.ie.cubt.R;
import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.widget.TextView;

public class TestUserStateActivity extends Activity {

	TextView textTime;
	
	TextView textGpsLongitude;
	TextView textGpsLatitude;
	TextView textGpsAltitude;
	TextView textLocationState;
	TextView textPoiState;

	TextView textSpeedValue;
	TextView textSpeedState;
	
	SCCMEngine engine;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userstate);
		findViews();
		//Intent intent = new Intent(this, CubtService.class);
		//startService(intent);
		
		engine = new SCCMEngine(this);
		engine.startEngine();
		engine.getClassifierManager().
			getClassifier(LocationClassifier.class).addHandler(handler);
		engine.getClassifierManager().
			getClassifier(PoiClassifier.class).addHandler(handler);
		engine.getClassifierManager().
			getClassifier(SpeedClassifier.class).addHandler(handler);
		engine.getLocationSensor().addHandler(handler);
	}
	
	
	
	private void findViews() {
		textTime = (TextView) findViewById(R.id.timeText);
		textGpsLongitude = (TextView) findViewById(R.id.longText);
		textGpsLatitude = (TextView) findViewById(R.id.latText);
		textGpsAltitude = (TextView) findViewById(R.id.altText);
		textLocationState = (TextView) findViewById(R.id.locationText);
		textPoiState = (TextView) findViewById(R.id.poiText);
		textSpeedValue = (TextView) findViewById(R.id.speedValue);
		textSpeedState = (TextView) findViewById(R.id.speedText);
	}



	Handler handler = new Handler(){
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg){
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

			case LocationSensor.MSG_NEW_LOCATION:
				updateLocation((Location) msg.obj);
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
