package com.supinfo.youfood.preferences;

import com.paypal.android.MEP.PayPal;

public class YoufoodPreferences {
	private YoufoodPreferences() {}
	
	public static String BASE_URL = "http://192.168.0.13/api.php";
	public static int WAITING_BETWEEN_REQUESTS = 5000;
	
	public static final int PAYPAL_ENV = PayPal.ENV_SANDBOX;
	public static final String PAYPAL_APP_ID = "APP-80W284485P519543T";
	public static final String PAYPAL_RECIPIENT = "";
}
