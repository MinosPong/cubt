package edu.cuhk.cubt.net;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import edu.cuhk.cubt.sccm.classifier.BusClassifier.BusEventObject;

import android.util.Log;

public class BusLocationUploader {

	private static final String tag = "BusLocationUploader";
	private static final String NAME_CMSG = "cmsg";
	
	private static final int SUGGEST_ID = 1; 
	
	private boolean isNew = true;
	private String id;
	
	public BusLocationUploader(){
		suggest();
	}
	
	public BusLocationUploader(String id){
		this.id = id;
	}
	
	public void suggest(){
		String value = "suggest";
		postHelper(SUGGEST_ID,value);	
	}
	
	private void add(double latitude, double longtitude){
		if(id == null) return;
		isNew = false;
		String value = "add/" + id + "/" + latitude + "/" + longtitude;
		postHelper(value);				
	}
	
	public void updateLocation(double latitude, double longtitude){
		if(id == null) return;
		if(isNew){
			add(latitude,longtitude);
			return;
		}
		String value = "update/" + id + "/" + latitude + "/" + longtitude;
		postHelper(value);	
	}
	
	public void remove(){
		if(id == null) return;
		String value = "delete/" + id ;	
		postHelper(value);
	}
	
	
	public void addStopEvent(BusEventObject evt){
		if(id == null) return;
		String value = "passedadd/" + id + "/" + evt.getEnterTime() + "/"
			+ evt.getLeaveTime() + "/" + evt.getStop().getName() + "/" + evt.getEvent();
		postHelper(value);
	}
	
	
	@SuppressWarnings("unchecked")
	private void postHelper(int action,String value){
		List<NameValuePair> postParam = new ArrayList<NameValuePair>();
		postParam.add(new BasicNameValuePair(NAME_CMSG, value));
		
		new BusLocationUploaderHttpTask(action).execute(postParam);	
	}
	
	private void postHelper(String value){		
		postHelper(0,value);
	}
	

	private void requestCallback(int action, String response){
		Log.i(tag, response);	
		switch(action){
			case SUGGEST_ID:
				id = response;
				break;
		}		
	}
	
	private class BusLocationUploaderHttpTask extends CubtHttpTask{
		private final int action;
		
		public BusLocationUploaderHttpTask(int action){
			this.action = action;
		}
		
		@Override
	    protected void onPostExecute(String response) {
	        requestCallback(action,response);
	    }		
	}
	
}
