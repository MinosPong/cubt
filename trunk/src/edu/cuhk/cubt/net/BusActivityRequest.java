package edu.cuhk.cubt.net;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;

public class BusActivityRequest {
	
	private static final String tag = "BusActivityRequest";
	private static final String URL = NetSettings.URL;
	private static final String HTTP_AGENT = "CUBT_HTTP_AGENT";
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
		new HttpTask(callback).execute(postParam);
	}
	
	
	private static class HttpTask extends AsyncTask<List<NameValuePair>,Void,String>{
		private final ResponseListener callback;
		
		public HttpTask(ResponseListener callback){
			this.callback = callback;
		}
		
		@Override
		protected String doInBackground(List<NameValuePair>... postParam) {
			final AndroidHttpClient httpClient = AndroidHttpClient.newInstance(HTTP_AGENT);
		    final HttpPost post = new HttpPost(URL);

		    String response = null;

		    
		    try {
				post.setEntity(new UrlEncodedFormEntity(postParam[0]));
				ResponseHandler<String> responseHandler=new BasicResponseHandler();
				response = httpClient.execute(post,responseHandler);
			} catch (Exception e) {
				post.abort();
				Log.w(tag, "Error while connecting " + URL + e.toString());
			} finally {
				if(httpClient != null){
					httpClient.close();
				}
			}	
			return response;		
			
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
