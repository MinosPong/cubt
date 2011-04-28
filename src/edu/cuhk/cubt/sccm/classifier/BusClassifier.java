package edu.cuhk.cubt.sccm.classifier;

import android.os.Message;
import android.text.format.Time;
import edu.cuhk.cubt.bus.Stop;
import edu.cuhk.cubt.state.BusState;
import edu.cuhk.cubt.state.SpeedState;
import edu.cuhk.cubt.state.State;
import edu.cuhk.cubt.state.event.StateChangeEvent;

public class BusClassifier extends AbstractClassifier<BusState>
	implements Classifier{
	
	public static final int BUS_ENTER_EVENT = 10701;
	public static final int BUS_LEAVE_EVENT = 10702;
	
	private static final int MSG_STOP_LEAVE_DELAY = 13101;
	private static final int STOP_LEAVE_DELAY_TIME = 20 * 1000;
	
	private Time stopLeaveTime = new Time();
	
	private Stop busStop;
	
	private boolean isInStop = false;
	private boolean isActivePeriod = false;
	
	PoiClassifier poiClassifier;
	SpeedClassifier speedClassifier;
	
	BusClassifier(PoiClassifier poiClassifier, SpeedClassifier speedClassifier){
		super(BusState.UNKNOWN);
		
		this.speedClassifier = speedClassifier;
		this.poiClassifier =  poiClassifier;
	}

	@Override
	protected void processClassification() {
		if(!isActivePeriod) return; //don't classify when in stop
		
		Time now = new Time();
		now.setToNow();		
		
		//inside delay time
		if(now.toMillis(false) < stopLeaveTime.toMillis(false) + STOP_LEAVE_DELAY_TIME){
			if(speedClassifier.getState() == SpeedState.NORMAL){
				setState(BusState.ONBUS);			
				isActivePeriod = false;
			}
		}else{
			setState(BusState.OFFBUS);		
			isActivePeriod = false;		
		}
	}

	private void onStopLeave() {
		if(!isInStop)return;
		stopLeaveTime.setToNow();
		isInStop = false;
		if(speedClassifier.getState() == SpeedState.NORMAL){
			this.setState(BusState.ONBUS);
		}else{
			isActivePeriod = true;
			mHandler.obtainMessage(MSG_STOP_LEAVE_DELAY);
			mHandler.sendEmptyMessageDelayed(MSG_STOP_LEAVE_DELAY, STOP_LEAVE_DELAY_TIME);
		}
	}

	private void onStopEnter() {
		if(isInStop) return;
		isActivePeriod = false;
		isInStop = true;
		if(getState() != BusState.ONBUS){
			busStop = (Stop) poiClassifier.getPoi();
			setState(BusState.WAITBUS);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	void handleMessage(Message msg){
		switch(msg.what){
		case State.TYPE_SPEED:
			StateChangeEvent<SpeedState> evt = (StateChangeEvent<SpeedState>) msg.obj;
			SpeedState state = evt.getNewState();
			if(state == SpeedState.NORMAL){
				processClassification();
			}
			break;

		case State.TYPE_POI:
			/*StateChangeEvent<PoiState> poiState = (StateChangeEvent<PoiState>) msg.obj;
			if(poiState.getNewState() == PoiState.INSIDE_BUS_STOP){
				onStopEnter();
			}
			if(poiState.getOldState() == PoiState.INSIDE_BUS_STOP){
				onStopLeave();
			}*/
			break;
			
		case PoiClassifier.STOP_ENTER_EVENT:
			onStopEnter();
			break;
			
		case PoiClassifier.STOP_LEAVE_EVENT:
			onStopLeave();			
			break;
			
		case MSG_STOP_LEAVE_DELAY:
			processClassification();				
			break;
		}	
	};

	@Override
	protected void onStart() {
		poiClassifier.addHandler(mHandler);
		speedClassifier.addHandler(mHandler);
	}

	@Override
	protected void onStop() {
		poiClassifier.removeHandler(mHandler);
		speedClassifier.removeHandler(mHandler);
	}

}
