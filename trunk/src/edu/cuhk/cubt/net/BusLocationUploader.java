package edu.cuhk.cubt.net;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.message.BasicNameValuePair;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;

public class BusLocationUploader {

	private static final String tag = "BusLocationUploader";
	private static final String URL = "http://14.136.104.206:8080/servlet/FYPServer";
	private static final String HTTP_AGENT = "CUBT_HTTP_AGENT";
	private static final String NAME_CMSG = "cmsg";
	
	private static final int SUGGEST_ID = 1; 
	
	private boolean isNew = true;
	private String id;
	
	public BusLocationUploader(){

		suggest();
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
	
	private void postHelper(int action,String value){
		List<NameValuePair> postParam = new ArrayList<NameValuePair>();
		postParam.add(new BasicNameValuePair(NAME_CMSG, value));	
		fireRequest(action,postParam);		
	}
	
	private void postHelper(String value){		
		postHelper(0,value);
	}
	
	
	public void fireRequest(int action, List<NameValuePair> postParam){
		new HttpTask(action).execute(postParam);
	}

	private void requestCallback(int action, String response){
		Log.i(tag, response);	
		switch(action){
			case SUGGEST_ID:
				id = response;
				break;
		}		
	}

	static String postRequest(List<NameValuePair> postParam){
		final AndroidHttpClient httpClient = AndroidHttpClient.newInstance(HTTP_AGENT);
	    final HttpPost post = new HttpPost(URL);
	    
	    String response = null;
	    try {
			post.setEntity(new UrlEncodedFormEntity(postParam));
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
	
	private class HttpTask extends AsyncTask<List<NameValuePair>,Void,String>{
		private final int action;
		
		public HttpTask(int action){
			this.action = action;
		}
		
		@Override
		protected String doInBackground(List<NameValuePair>... arg0) {
			return postRequest(arg0[0]);
		}
		
		@Override
	    protected void onPostExecute(String response) {
	        requestCallback(action,response);
	    }		
	}
	
}
