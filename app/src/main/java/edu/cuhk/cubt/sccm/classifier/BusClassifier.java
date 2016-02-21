package edu.cuhk.cubt.sccm.classifier;

import android.os.Message;
import android.text.format.Time;
import edu.cuhk.cubt.bus.BusEventObject;
import edu.cuhk.cubt.bus.Stop;
import edu.cuhk.cubt.state.BusState;
import edu.cuhk.cubt.state.SpeedState;
import edu.cuhk.cubt.state.State;
import edu.cuhk.cubt.state.event.StateChangeEvent;

public class BusClassifier extends AbstractClassifier<BusState>
	implements Classifier{
	
	private static final int MSG_STOP_LEAVE_DELAY = 13101;
	private static final int STOP_LEAVE_DELAY_TIME = 20 * 1000;	//20 seconds
	
	private Time stopLeaveTime = new Time();
	private boolean isInStop = false;
	private boolean isActivePeriod = false;
	
	private PoiClassifier.PoiChangeEventObject stopEnterEvent;
	private PoiClassifier.PoiChangeEventObject stopLeaveEvent;
	
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
		
		//inside delay/checking period
		if(now.toMillis(false) < stopLeaveTime.toMillis(false) + STOP_LEAVE_DELAY_TIME){
			if(speedClassifier.getState() == SpeedState.NORMAL){
				//Speed Matched, Bus Enter/ or Passed Stop
				if(getState() == BusState.ONBUS){
					//STOP PASSED
					this.notifyOutgoingHandlers(BusEventObject.BUS_STOP_PASSED_EVENT, 
							new BusEventObject(BusEventObject.BUS_STOP_PASSED_EVENT,
									(Stop) stopEnterEvent.getPoi(),
									stopEnterEvent.getCause().getTime(),
									stopLeaveEvent.getCause().getTime()
									));
				}else{
					//BUS ENTER
					this.notifyOutgoingHandlers(BusEventObject.BUS_ENTER_EVENT, 
							new BusEventObject(BusEventObject.BUS_ENTER_EVENT,
									(Stop) stopEnterEvent.getPoi(),
									stopEnterEvent.getCause().getTime(),
									stopLeaveEvent.getCause().getTime()
									));
				}
				
				setState(BusState.ONBUS);			
				isActivePeriod = false;
			}
		}else{
			if(getState() == BusState.ONBUS){
				//BUS_LEAVE_EVENT
				this.notifyOutgoingHandlers(BusEventObject.BUS_EXIT_EVENT, 
						new BusEventObject(BusEventObject.BUS_EXIT_EVENT,
								(Stop) stopEnterEvent.getPoi(),
								stopEnterEvent.getCause().getTime(),
								stopLeaveEvent.getCause().getTime()
								));
			}			
			setState(BusState.OFFBUS);		
			isActivePeriod = false;		
		}
	}

	private void onStopLeave(PoiClassifier.PoiChangeEventObject evt){
		this.stopLeaveEvent = evt;
		
		onStopLeave();
	}
	private void onStopLeave() {
		if(!isInStop)return;
		stopLeaveTime.setToNow();
		isInStop = false;

		//Here if the stop stay period < 15s, assume it is a noise
		if(stopLeaveEvent.getTime() - stopEnterEvent.getTime() < 15000)
			return;
		
		isActivePeriod = true;
		processClassification();
		mHandler.sendEmptyMessageDelayed(MSG_STOP_LEAVE_DELAY, STOP_LEAVE_DELAY_TIME);

	}

	private void onStopEnter(PoiClassifier.PoiChangeEventObject evt){
		//Only remove the last event if the LeaveEventPOI EnterEventPOI are diff or the time diff is > 15s
		if(	stopLeaveEvent == null || 
			stopLeaveEvent.getPoi() != evt.getPoi() || 
			evt.getTime() - stopLeaveEvent.getTime() > 15000){
		
			this.stopEnterEvent = evt;
		}

		this.stopLeaveEvent = null;
		
		//TODO Eliminate noise Stops
		onStopEnter();
	}
	
	private void onStopEnter() {
		if(isInStop) return;
		isActivePeriod = false;
		isInStop = true;
		if(getState() != BusState.ONBUS){
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
			onStopEnter((PoiClassifier.PoiChangeEventObject) msg.obj);
			break;
			
		case PoiClassifier.STOP_LEAVE_EVENT:
			onStopLeave((PoiClassifier.PoiChangeEventObject) msg.obj);			
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
