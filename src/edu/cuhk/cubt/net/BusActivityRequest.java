package edu.cuhk.cubt.net;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.util.Log;

public class BusActivityRequest {
	
	private static final String tag = "BusActivityRequest";
	private static final String NAME_CMSG = "cmsg";
	private static final String VALUE_CMSG = "info";
	
	public static class BusActivityRecord{
		int id;
		double latitude;
		double longitude;
		public BusActivityRecord(int id, double latitude, double longitude){
			this.id = id;
			this.latitude = latitude;
			this.longitude = longitude;
		}
		
		public int getId(){
			return id;
		}
		public double getLatitude(){
			return latitude;
		}
		public double getLongitude(){
			return longitude;
		}
	}
	
	public interface ResponseListener{
		public void onDataReceive(List<BusActivityRecord> buses);
	}
	
	/**
	 * Send a Bus Activity Info Request
	 * @param callback the ResponseListener
	 */
	@SuppressWarnings("unchecked")
	public static void sendRequest(ResponseListener callback){
		List<NameValuePair> postParam = new ArrayList<NameValuePair>();
		postParam.add(new BasicNameValuePair(NAME_CMSG, VALUE_CMSG));	
		new BusActivityHttpTask(callback).execute(postParam);
	}
	
	private static class BusActivityHttpTask extends CubtHttpTask{			

		private ResponseListener callback;
		public BusActivityHttpTask(ResponseListener callback) {
			this.callback = callback;
		}

		@Override
	    protected void onPostExecute(String response) {
	        try {
	        	JSONObject objs = new JSONObject(response);
	        	JSONArray array = objs.getJSONArray("items");
	        	ArrayList<BusActivityRecord> buses = new ArrayList<BusActivityRecord>();
	        	for(int i=0; i<array.length(); i++){
	        		JSONObject obj = array.getJSONObject(i);
	        		BusActivityRecord bus = 
	        			new BusActivityRequest.BusActivityRecord(
	        					obj.getInt("bid"),
	        					obj.getDouble("latitude"),
	        					obj.getDouble("longitude"));
	        		buses.add(bus);
	        	}	        	
	        	
				callback.onDataReceive(buses);
			} catch (JSONException e) {
				Log.e(tag, "JSONException" ,e);
			}
	    }		
	}

}
