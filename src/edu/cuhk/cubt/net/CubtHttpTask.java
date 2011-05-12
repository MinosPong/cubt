package edu.cuhk.cubt.net;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

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


public class CubtHttpTask extends AsyncTask<List<NameValuePair>,Void,String>{

	public interface HttpCallback<U>{
		public void response(U response);
	}
	

	protected static final String tag = "CubtHttpTask";
	private static final String HTTP_AGENT = "CUBT_HTTP_AGENT";
	private static final String NAME_PASSKEY = "passkey";
	
	@Override
	protected String doInBackground(List<NameValuePair>... arg0) {
		final AndroidHttpClient httpClient = AndroidHttpClient.newInstance(HTTP_AGENT);
		HttpPost post = new HttpPost(NetSettings.URL);

		try {
			List<NameValuePair> postParam = arg0[0];
			postParam.add(new BasicNameValuePair(NAME_PASSKEY, NetSettings.PASS_VALUE));
			post.setEntity(new UrlEncodedFormEntity(postParam));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		ResponseHandler<String> responseHandler=new BasicResponseHandler();
		
		String response = "";
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
	
}
