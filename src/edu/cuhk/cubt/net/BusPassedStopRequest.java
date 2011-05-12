package edu.cuhk.cubt.net;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import edu.cuhk.cubt.bus.BusEventObject;
import edu.cuhk.cubt.bus.Stop;
import edu.cuhk.cubt.store.PoiData;

public class BusPassedStopRequest {
	private static final String NAME_CMSG = "cmsg";
	
	@SuppressWarnings("unchecked")
	public static void getPassedStop(String id, ResponseListener callback){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(NAME_CMSG, "passed/"+id));
		new BusPassedStopHttpTask(callback).execute(params );
	}
	
	public interface ResponseListener{
		public void onDataReceive(List<BusEventObject> stopEvent);
	}

	private static class BusPassedStopHttpTask extends CubtHttpTask{
		
		ResponseListener callback;
		
		public BusPassedStopHttpTask(ResponseListener callback){
			this.callback = callback;
		}
		
		@Override
	    protected void onPostExecute(String response) {
			
			try {
	        	JSONObject objs = new JSONObject(response);
	        	JSONArray array = objs.getJSONArray("items");
	        	List<BusEventObject> stopEvent = new ArrayList<BusEventObject>();
				for(int i=0; i<array.length(); i++){
	        		JSONObject obj = array.getJSONObject(i);
	        		stopEvent.add(new BusEventObject(
	        				obj.getInt("event"), 
	        				(Stop) PoiData.getByName(obj.getString("stop")), 
	        				obj.getLong("enterTime"), 
	        				obj.getLong("leavetime")));
	        	}	        	
	        	
	        	callback.onDataReceive(stopEvent);
			} catch (JSONException e) {
				Log.e(tag, "JSONException" ,e);
			}
			
	    }		
	}
}
