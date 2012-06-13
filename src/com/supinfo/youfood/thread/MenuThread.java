package com.supinfo.youfood.thread;

import java.net.URI;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Message;
import android.util.Log;

import com.supinfo.youfood.handler.MenuHandler;
import com.supinfo.youfood.model.Category;
import com.supinfo.youfood.model.Product;
import com.supinfo.youfood.preferences.YoufoodPreferences;

public class MenuThread extends Thread {
	private MenuHandler handler;
	private String androidId;
	
	public MenuThread(MenuHandler h, String aI) {
		handler = h;
		androidId = aI;
	}

	@Override
	public void run() {
		Message msg;
		msg = handler.obtainMessage();
		msg.arg1 = MenuHandler.STATUS_START;
		handler.sendMessage(msg);
		
		String json;
		String result;
		try {
			json = sendRequest();
			result = parseStatus(json);
			msg = handler.obtainMessage();
			if("ok".equals(result)) {
				msg.arg1 = MenuHandler.STATUS_OK;
				msg.obj = parseProducts(json);
			} else {
				msg.arg1 = MenuHandler.STATUS_NOK;
			}
			handler.sendMessage(msg);
		} catch (HttpHostConnectException e) {
			sendError(MenuHandler.ERROR_HTTP);
			Log.e("error", "Error when communicating with the server", e);
		} catch (ConnectTimeoutException e) {
			sendError(MenuHandler.ERROR_TIMEOUT);
			Log.e("error", "The connexion timed-out", e);
		} catch (JSONException e) {
			sendError(MenuHandler.ERROR_JSON);
			Log.e("error", "Error when analyzing the JSON", e);
		} catch (Exception e) {
			sendError(MenuHandler.ERROR_UNKNOWN);
			Log.e("error", "An unknown error has occured", e);
		}
	}
	
	private String sendRequest() throws ConnectTimeoutException, HttpHostConnectException, Exception {
		BasicHttpParams basicHttpParams = new BasicHttpParams();
		// Connection must time out after 10 second to prevent infinite loop
		HttpConnectionParams.setConnectionTimeout(basicHttpParams, 10000);
		HttpClient httpClient = new DefaultHttpClient(basicHttpParams);
		
		HttpGet httpGet = new HttpGet();
		httpGet.setHeader("Accept", "application/json");
		URI uri = new URI(YoufoodPreferences.BASE_URL + "/get/" + androidId);
		httpGet.setURI(uri);
		
		HttpResponse response = httpClient.execute(httpGet);
		return EntityUtils.toString(response.getEntity()); 
	}
	
	private String parseStatus(String json) throws JSONException {
		JSONObject jsonObj = new JSONObject(json);
		String status = jsonObj.getString("status"); 
		return status;
	}
	
	private ArrayList<Category> parseProducts(String json) throws JSONException {
		ArrayList<Category> categories = new ArrayList<Category>();
		JSONObject jsonObj = new JSONObject(json);
		JSONArray jsonCategories = jsonObj.getJSONArray("category");
		int nbCategories = jsonCategories.length();
		for(int i = 0; i < nbCategories; i++) {
			JSONObject jsonCategory = jsonCategories.getJSONObject(i);
			Category category = new Category(jsonCategory.getInt("id"), jsonCategory.getString("name"));
			JSONArray jsonProducts = jsonCategory.getJSONArray("product");
			int nbProducts = jsonProducts.length();
			for(int j = 0; j < nbProducts; j++) {
				JSONObject jsonProduct = jsonProducts.getJSONObject(j);
				Product product = new Product();
				product.setId(jsonProduct.getInt("id"));
				product.setName(jsonProduct.getString("name"));
				product.setDescription(jsonProduct.getString("description"));
				product.setPrice(jsonProduct.getDouble("price"));
				category.addProduct(product);
			}
			categories.add(category);
		}
		return categories;
	}
	
	private void sendError(int errorCode) {
		Message msg = handler.obtainMessage();
		msg.arg1 = MenuHandler.STATUS_ERROR;
		msg.arg2 = errorCode;
		handler.sendMessage(msg);
	}
}
