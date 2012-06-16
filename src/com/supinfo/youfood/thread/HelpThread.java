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
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Message;
import android.util.Log;

import com.supinfo.youfood.handler.HelpHandler;
import com.supinfo.youfood.preferences.YoufoodPreferences;

public class HelpThread extends Thread {
	private boolean running = true;
	private HelpHandler handler;
	private String androidId;
	
	public HelpThread(HelpHandler h, String aI) {
		handler = h;
		androidId = aI;
	}
	
	public void requestStop() {
		running = false;
	}

	@Override
	public void run() {
		Message msg;
		msg = handler.obtainMessage();
		msg.arg1 = HelpHandler.STATUS_START;
		handler.sendMessage(msg);
		
		String json;
		String result;
		while(running) {
			try {
				json = sendRequest();
				result = parseJSON(json);
				if("ok".equals(result)) {
					msg = handler.obtainMessage();
					msg.arg1 = HelpHandler.STATUS_OK;
					handler.sendMessage(msg);
					requestStop();
				} else {
					msg = handler.obtainMessage();
					msg.arg1 = HelpHandler.STATUS_NOK;
					handler.sendMessage(msg);
					sleep(YoufoodPreferences.WAITING_BETWEEN_REQUESTS);
				}
			} catch (HttpHostConnectException e) {
				sendError();
				Log.e("error", "Error when communicating with the server", e);
			} catch (ConnectTimeoutException e) {
				sendError();
				Log.e("error", "The connexion timed-out", e);
			} catch (JSONException e) {
				sendError();
				Log.e("error", "Error when analyzing the JSON", e);
			} catch (Exception e) {
				sendError();
				Log.e("error", "An unknown error has occured", e);
			}
		}
	}
	
	private String sendRequest() throws ConnectTimeoutException, HttpHostConnectException, Exception {
		BasicHttpParams basicHttpParams = new BasicHttpParams();
		// Connection must time out after 10 second to prevent infinite loop
		HttpConnectionParams.setConnectionTimeout(basicHttpParams, 10000);
		HttpClient httpClient = new DefaultHttpClient(basicHttpParams);
		
		HttpGet httpGet = new HttpGet();
		httpGet.setHeader("Accept", "application/json");
		URI uri = new URI(YoufoodPreferences.BASE_URL + "/help/request/" + androidId);
		httpGet.setURI(uri);
		
		HttpResponse response = httpClient.execute(httpGet);
		return EntityUtils.toString(response.getEntity()); 
	}
	
	private String parseJSON(String json) throws JSONException {
		JSONObject jsonObj = new JSONObject(json);
		String status = jsonObj.getString("status"); 
		return status;
	}
	
	private void sendError() {
		Message msg = handler.obtainMessage();
		msg.arg1 = HelpHandler.STATUS_ERROR;
		handler.sendMessage(msg);
		requestStop();
	}
}
