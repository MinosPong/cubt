package edu.cuhk.cubt.net;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.message.BasicNameValuePair;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;

public class HttpRequest {
	
	private static String tag = "HttpRequest";
	
	public static String DEFAULT_URI = "http://personal.ie.cuhk.edu.hk/~pkm007/fyp/echo.php";
	
	private static String uri = DEFAULT_URI;
	
	private static final String HTTP_AGENT = "CUBT HTTP AGENT";

	private static final String NAME_ACTION = "action";
	private static final String NAME_ID = "id";
	private static final String NAME_ROUTE = "route";
	private static final String NAME_LONGTITUDE = "long";
	private static final String NAME_LATITUDE = "lat";

	private static final String NAME_CMSG = "cmsg";
	
	
	public static String getNewId(){		
		//postParam.add(new BasicNameValuePair(NAME_ACTION, "new"));
		String value = "suggest";
		List<NameValuePair> postParam = new ArrayList<NameValuePair>();
		postParam.add(new BasicNameValuePair(NAME_CMSG, value));	
		String id = "0";
		id = fireRequest(postParam);
		return id;
	}
	
	public static void addId(String id, double latitude, double longtitude){
		String value = "add/" + id + "/" + latitude + "/" + longtitude;
		List<NameValuePair> postParam = new ArrayList<NameValuePair>();
		postParam.add(new BasicNameValuePair(NAME_CMSG, value));	
		fireRequest(postParam);				
	}
	
	public static void addId(String id){
		addId(id,0,0);
	}
	
	public static void updateLocation(String id, double latitude, double longtitude){
		String value = "update/" + id + "/" + latitude + "/" + longtitude;
		List<NameValuePair> postParam = new ArrayList<NameValuePair>();
		postParam.add(new BasicNameValuePair(NAME_CMSG, value));	
		fireRequest(postParam);		
	}
	
	public static void removeId(String id){
		String value = "delete/" + id ;
		List<NameValuePair> postParam = new ArrayList<NameValuePair>();
		postParam.add(new BasicNameValuePair(NAME_CMSG, value));	
		fireRequest(postParam);		
	}
	
	public static void fireRequest() throws IOException, IllegalStateException{
		List<NameValuePair> postParam = new ArrayList<NameValuePair>();	

		postParam.add(new BasicNameValuePair(NAME_ROUTE, "abc"));
		postParam.add(new BasicNameValuePair(NAME_LONGTITUDE, "abc"));
		postParam.add(new BasicNameValuePair(NAME_LATITUDE, "def"));
					
		fireRequest(postParam);
	}
	
	
	public static String fireRequest(List<NameValuePair> postParam){
		try{
			AndroidHttpClient httpClient = AndroidHttpClient.newInstance(HTTP_AGENT);
			HttpPost post = new HttpPost(uri);

			post.setEntity(new UrlEncodedFormEntity(postParam));
	
			ResponseHandler<String> responseHandler=new BasicResponseHandler();
			
			String response = httpClient.execute(post,responseHandler);
			Log.i(tag, response);
			httpClient.close();
			return response;
		}catch(IOException e){
			
		}catch(IllegalStateException e){
			
		}
		return null;
	}
	
	private interface HttpCallback{
		public void response(String response);
	}
	
	private class HttpTask extends AsyncTask<List<NameValuePair>,Void,String>{

		HttpCallback callback;
		public HttpTask(HttpCallback callback){
			this.callback = callback;
		}
		
		@Override
		protected String doInBackground(List<NameValuePair>... arg0) {
			AndroidHttpClient httpClient = AndroidHttpClient.newInstance(HTTP_AGENT);
			HttpPost post = new HttpPost(uri);

			try {
				List<NameValuePair> postParam = arg0[0];
				post.setEntity(new UrlEncodedFormEntity(postParam));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
	
			ResponseHandler<String> responseHandler=new BasicResponseHandler();
			
			String response = null;
			try {
				response = httpClient.execute(post,responseHandler);
				Log.i(tag, response);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			httpClient.close();
			return response;
		}
		
		@Override
	    protected void onPostExecute(String response) {
	        if(response == null) return;
	        callback.response(response);
	    }
		
	}
}
