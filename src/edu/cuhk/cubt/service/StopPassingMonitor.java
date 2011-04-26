package edu.cuhk.cubt.service;

import android.os.Handler;
import android.os.Message;
import edu.cuhk.cubt.db.DbStopPassed;
import edu.cuhk.cubt.sccm.SCCMEngine;
import edu.cuhk.cubt.sccm.classifier.PoiClassifier;
import edu.cuhk.cubt.ui.CubtService;

public class StopPassingMonitor implements IServiceMonitor{

	private SCCMEngine engine;
	DbStopPassed db;
	PoiClassifier poiClassifier;
	
	PoiClassifier.PoiChangeEventObject enterEvent;
	PoiClassifier.PoiChangeEventObject leaveEvent;
	
	@Override
	public void start(CubtService service) {
		engine = service.getSCCMEngine();
		db = new DbStopPassed(service.getApplicationContext());
		poiClassifier = engine.getClassifierManager().getClassifier(PoiClassifier.class);
		poiClassifier.addHandler(handler);
	}

	@Override
	public void stop(CubtService service) {
		poiClassifier.removeHandler(handler);
	}

	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			
			switch(msg.what){
			case PoiClassifier.STOP_ENTER_EVENT:
				enterEvent = (PoiClassifier.PoiChangeEventObject)msg.obj;
				break;
			
			case PoiClassifier.STOP_LEAVE_EVENT:
				if(enterEvent == null) break; 
				leaveEvent = (PoiClassifier.PoiChangeEventObject)msg.obj;
				if(enterEvent.getPoi() == null || enterEvent.getPoi() != leaveEvent.getPoi()) break;
				db.insert(enterEvent.getPoi().getName(), enterEvent.getCause().getTime(), leaveEvent.getCause().getTime(), 0);
				break;
			}
			super.handleMessage(msg);
		}
	};
	
}
