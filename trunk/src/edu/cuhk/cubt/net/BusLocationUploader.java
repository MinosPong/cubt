package edu.cuhk.cubt.net;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import edu.cuhk.cubt.bus.BusEventObject;

import android.util.Log;

/**
 * Upload Bus Location data to Server
 * Static function, Cannot call by two class in the sametime
 * @author PKM
 *
 */
public class BusLocationUploader {

	private static final String tag = "BusLocationUploader";
	private static final String NAME_CMSG = "cmsg";
	
	private static final int SUGGEST_ID = 1; 
	
	private static String id = "";
	
	public static void setId(String id){
		BusLocationUploader.id = id;
	}
	
	public static String getId(){
		return id;
	}
	
	public static void suggest(){
		String value = "suggest";
		postHelper(SUGGEST_ID,value);
	}
	
	public static void add(String id){
		add(id,0,0);
	}
	
	public static void add(String id, double latitude, double longtitude){
		setId(id);
		String value = "add/" + id + "/" + latitude + "/" + longtitude;
		postHelper(value);
	}
	
	public static void updateLocation(double latitude, double longtitude){
		String value = "update/" + id + "/" + latitude + "/" + longtitude;
		postHelper(value);	
	}
	
	public static void remove(){
		String value = "delete/" + id ;	
		postHelper(value);
		id ="";
	}	
	
	public static void addStopEvent(BusEventObject evt){
		String value = "passedadd/" + id + "/" + evt.getEnterTime() + "/"
			+ evt.getLeaveTime() + "/" + evt.getStop().getName() + "/" + evt.getEvent();
		postHelper(value);
	}
	
	
	@SuppressWarnings("unchecked")
	private static void postHelper(int action,String value){
		List<NameValuePair> postParam = new ArrayList<NameValuePair>();
		postParam.add(new BasicNameValuePair(NAME_CMSG, value));
		
		new BusLocationUploaderHttpTask(action).execute(postParam);	
	}
	
	private static void postHelper(String value){		
		postHelper(0,value);
	}
	

	private static void requestCallback(int action, String response){
		if(response!=null)	Log.i(tag, response);	
		switch(action){
			case SUGGEST_ID:
				id = response;
				break;
		}		
	}
	
	private static class BusLocationUploaderHttpTask extends CubtHttpTask{
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
