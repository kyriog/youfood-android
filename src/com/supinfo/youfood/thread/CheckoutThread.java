package com.supinfo.youfood.thread;

import java.net.URI;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Message;
import android.util.Log;

import com.supinfo.youfood.handler.CheckoutHandler;
import com.supinfo.youfood.model.CartProduct;
import com.supinfo.youfood.preferences.YoufoodPreferences;

public class CheckoutThread extends Thread {
	private CheckoutHandler handler;
	private String androidId;
	private List<CartProduct> products;
	private String payKey;
	
	public CheckoutThread(CheckoutHandler h, String aI, List<CartProduct> p, String pK) {
		handler = h;
		androidId = aI;
		products = p;
		payKey = pK;
	}

	@Override
	public void run() {
		Message msg;
		msg = handler.obtainMessage();
		msg.arg1 = CheckoutHandler.STATUS_START;
		handler.sendMessage(msg);
		
		String cart;
		String json;
		String result;
		try {
			cart = generateJSON();
			json = sendRequest(cart);
			result = parseJSON(json);
			if("ok".equals(result)) {
				msg = handler.obtainMessage();
				msg.arg1 = CheckoutHandler.STATUS_OK;
				handler.sendMessage(msg);
			} else {
				sendError();
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
	
	private String generateJSON() throws JSONException {
		JSONArray jsonProducts = new JSONArray();
		for(CartProduct product : products) {
			JSONObject jsonProduct = new JSONObject();
			jsonProduct.put("id", product.getProduct().getId());
			jsonProduct.put("quantity", product.getQuantity());
			jsonProducts.put(jsonProduct);
		}
		
		JSONObject generated = new JSONObject();
		generated.put("android_id", androidId);
		generated.put("pay_key", payKey);
		generated.put("products", jsonProducts);
		
		return generated.toString();
	}
	
	private String sendRequest(String post) throws ConnectTimeoutException, HttpHostConnectException, Exception {
		BasicHttpParams basicHttpParams = new BasicHttpParams();
		// Connection must time out after 10 second to prevent infinite loop
		HttpConnectionParams.setConnectionTimeout(basicHttpParams, 10000);
		HttpClient httpClient = new DefaultHttpClient(basicHttpParams);
		
		HttpPost httpPost = new HttpPost();
		httpPost.setHeader("Accept", "application/json");
		httpPost.setEntity(new StringEntity(post));
		URI uri = new URI(YoufoodPreferences.API_URL + "/checkout");
		httpPost.setURI(uri);
		
		HttpResponse response = httpClient.execute(httpPost);
		return EntityUtils.toString(response.getEntity()); 
	}
	
	private String parseJSON(String json) throws JSONException {
		JSONObject jsonObj = new JSONObject(json);
		String status = jsonObj.getString("status"); 
		return status;
	}
	
	private void sendError() {
		Message msg = handler.obtainMessage();
		msg.arg1 = CheckoutHandler.STATUS_ERROR;
		handler.sendMessage(msg);
	}
}
