package edu.cuhk.cubt.net;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import android.net.http.AndroidHttpClient;
import android.util.Log;

public class HttpRequest {
	
	private static String tag = "HttpRequest";
	
	public static String DEFAULT_URI = "http://xxx.xxxx.com/xxsaf.php?safasf";
	
	private String uri;

	private static final String NAME_HOST = "hostNum";
	private static final String NAME_TARGET = "targetNum";


	private static final String HTTP_AGENT = "CUBT HTTP AGENT";
	
	public void fireRequest() throws IOException, IllegalStateException{
		AndroidHttpClient httpClient = AndroidHttpClient.newInstance(HTTP_AGENT);
		HttpPost post = new HttpPost(uri);
		
		List<NameValuePair> postParam = new ArrayList<NameValuePair>();	
		
		postParam.add(new BasicNameValuePair(NAME_HOST, "abc"));
		postParam.add(new BasicNameValuePair(NAME_TARGET, "def"));
					
		post.setEntity(new UrlEncodedFormEntity(postParam));
		HttpResponse response = httpClient.execute(post);
		
		Log.i(tag, response.getEntity().getContent().toString());
	
		httpClient.close();
	}
}
