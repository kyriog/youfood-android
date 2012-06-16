package com.supinfo.youfood.activity;

import java.util.ArrayList;

import com.paypal.android.MEP.CheckoutButton;
import com.paypal.android.MEP.PayPal;
import com.supinfo.youfood.adapter.RightCartAdapter;
import com.supinfo.youfood.handler.HelpHandler;
import com.supinfo.youfood.handler.MenuHandler;
import com.supinfo.youfood.listener.AddToCartListener;
import com.supinfo.youfood.model.Category;
import com.supinfo.youfood.preferences.YoufoodPreferences;
import com.supinfo.youfood.thread.HelpThread;
import com.supinfo.youfood.thread.MenuThread;

import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class MenuActivity extends ActivityGroup implements DialogInterface.OnClickListener, View.OnClickListener {
	private static final int PAYPAL_CHECKOUT_BUTTON_ID = 1456545584; // Random number!
	
	private ArrayList<Category> menu;
	private String androidId;
	private RightCartAdapter cartAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
		
		ProgressDialog progress = new ProgressDialog(this);
		AlertDialog alert = new AlertDialog.Builder(this).create();
		MenuHandler handler = new MenuHandler(progress, alert, this);
		MenuThread thread = new MenuThread(handler, androidId);
		
		alert.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", this);
		
		thread.start();
	}

	public void onClick(DialogInterface dialog, int which) {
		switch(which) {
		case DialogInterface.BUTTON_NEUTRAL:
			Intent intent = new Intent(this, AuthActivity.class);
			startActivity(intent);
			finish();
			break;
		case DialogInterface.BUTTON_POSITIVE:
			dialog.dismiss();
			break;
		}
	}
	
	public void onClick(View view) {
		switch(view.getId()) {
		case R.id.need_help:
			ProgressDialog progress = new ProgressDialog(this);
			AlertDialog alert = new AlertDialog.Builder(this).create();
			HelpHandler handler = new HelpHandler(progress, alert);
			HelpThread thread = new HelpThread(handler, androidId);
			
			progress.setCancelable(false);
			
			alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", this);
			
			thread.start();
			break;
		case R.id.go_checkout:
			PayPal paypal = PayPal.initWithAppID(this, YoufoodPreferences.PAYPAL_APP_ID, YoufoodPreferences.PAYPAL_ENV);
			CheckoutButton checkoutButton = paypal.getCheckoutButton(this, PayPal.BUTTON_278x43, CheckoutButton.TEXT_PAY);
			checkoutButton.setId(PAYPAL_CHECKOUT_BUTTON_ID);
			checkoutButton.setOnClickListener(this);
			checkoutButton.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			checkoutButton.setPadding(0, 0, 0, 10);
			
			TextView textView = new TextView(this);
			textView.setText("Votre panier contient " + cartAdapter.getGlobalQuantity() + (cartAdapter.getGlobalQuantity() > 1 ? " articles" : " article") + " pour un total de " + cartAdapter.getGlobalPrice() + " €.");
			textView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			textView.setPadding(0, 0, 0, 10);
			
			LinearLayout layout = new LinearLayout(this);
			layout.setOrientation(LinearLayout.VERTICAL);
			layout.setGravity(Gravity.CENTER);
			layout.addView(textView);
			layout.addView(checkoutButton);
			
			AlertDialog.Builder paymentDialog = new AlertDialog.Builder(this);
			paymentDialog.setTitle("Confirmez votre commande");
			paymentDialog.setView(layout);
			paymentDialog.setNegativeButton("Modifier ma commande", this);
			
			paymentDialog.create().show();
			break;
		}
	}
	
	public void setMenu(ArrayList<Category> m) {
		menu = m;
	}
	
	public void displayMenu() {
		setContentView(R.layout.menu);
        TabHost mTabHost = (TabHost) findViewById(R.id.tabhost);
        mTabHost.setup(getLocalActivityManager());
        
        cartAdapter = new RightCartAdapter(this);
		ListView cart = (ListView) findViewById(R.id.cart_right);
		cart.setAdapter(cartAdapter);
		AddToCartListener.setCartAdapter(cartAdapter);
        
        for(Category category : menu) {
        	Intent i = new Intent(this, CategoryActivity.class);
        	i.putExtra("category", category);
            mTabHost.addTab(mTabHost.newTabSpec("category_"+category.getId()).setIndicator(category.getName()).setContent(i));
        }
        
        Button needHelp = (Button) findViewById(R.id.need_help);
        needHelp.setOnClickListener(this);
        Button goCheckout = (Button) findViewById(R.id.go_checkout);
        goCheckout.setOnClickListener(this);
	}
}
