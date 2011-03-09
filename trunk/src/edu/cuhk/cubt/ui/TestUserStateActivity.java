package edu.cuhk.cubt.ui;

import java.util.EventObject;

import edu.cuhk.cubt.SCCMEngine;
import edu.cuhk.cubt.classifier.LocationClassifier;
import edu.cuhk.cubt.classifier.PoiClassifier;
import edu.cuhk.cubt.classifier.SpeedClassifier;
import edu.cuhk.cubt.state.PoiState;
import edu.cuhk.cubt.state.State;
import edu.cuhk.cubt.state.event.StateChangeEvent;
import edu.cuhk.ie.cubt.R;
import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
		@Override
		public void handleMessage(Message msg){
			@SuppressWarnings("unchecked")
			StateChangeEvent<State> evt = (StateChangeEvent<State>) msg.obj;
			switch(msg.what){
			case State.TYPE_LOCATION:
				updateLocation(engine.getClassifierManager()
						.getClassifier(LocationClassifier.class).getLocation());
				textLocationState.setText(evt.getNewState().getStateString());
				break;
			case State.TYPE_POI:
				updateLocation(engine.getClassifierManager()
						.getClassifier(PoiClassifier.class).getLocation());
				if(evt.getNewState() == PoiState.OUTSIDE_POI){
					textPoiState.setText(evt.getNewState().getStateString());					
				}else{
					textPoiState.setText(engine.getClassifierManager()
						.getClassifier(PoiClassifier.class).getPoi().getName());
				}				
				break;
			case State.TYPE_SPEED:
				textSpeedValue.setText("" + engine.getClassifierManager()
						.getClassifier(SpeedClassifier.class).getSpeed());
				textSpeedState.setText(engine.getClassifierManager()
						.getClassifier(SpeedClassifier.class).getState().getStateString());
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
