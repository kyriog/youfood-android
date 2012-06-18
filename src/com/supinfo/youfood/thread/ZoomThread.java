package com.supinfo.youfood.thread;

import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Message;
import android.util.Log;

import com.supinfo.youfood.handler.ZoomHandler;
import com.supinfo.youfood.preferences.YoufoodPreferences;

public class ZoomThread extends Thread {
	private ZoomHandler handler;
	private String imageLocation;
	
	public ZoomThread(ZoomHandler h, String i) {
		handler = h;
		imageLocation = i;
	}
	
	@Override
	public void run() {
		Message msg;
		msg = handler.obtainMessage();
		msg.arg1 = ZoomHandler.STATUS_START;
		handler.sendMessage(msg);
		
		try {
			Bitmap image = sendRequest();
			if(image != null) {
				msg = handler.obtainMessage();
				msg.arg1 = ZoomHandler.STATUS_OK;
				msg.obj = image;
				handler.sendMessage(msg);
			} else {
				msg = handler.obtainMessage();
				msg.arg1 = ZoomHandler.STATUS_ERROR;
				handler.sendMessage(msg);
			}
		} catch (HttpHostConnectException e) {
			sendError();
			Log.e("error", "Error when communicating with the server", e);
		} catch (ConnectTimeoutException e) {
			sendError();
			Log.e("error", "The connexion timed-out", e);
		} catch (Exception e) {
			sendError();
			Log.e("error", "An unknown error has occured", e);
		}
	}
	
	private Bitmap sendRequest() throws ConnectTimeoutException, HttpHostConnectException, Exception {
		BasicHttpParams basicHttpParams = new BasicHttpParams();
		// Connection must time out after 10 second to prevent infinite loop
		HttpConnectionParams.setConnectionTimeout(basicHttpParams, 10000);
		HttpClient httpClient = new DefaultHttpClient(basicHttpParams);
		
		HttpGet httpGet = new HttpGet();
		URI uri = new URI(YoufoodPreferences.BASE_URL + "/" + imageLocation);
		httpGet.setURI(uri);
		
		HttpResponse response = httpClient.execute(httpGet);
		return BitmapFactory.decodeStream(response.getEntity().getContent()); 
	}
	
	private void sendError() {
		Message msg = handler.obtainMessage();
		msg.arg1 = ZoomHandler.STATUS_ERROR;
		handler.sendMessage(msg);
	}
}
