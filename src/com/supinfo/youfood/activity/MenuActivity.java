package com.supinfo.youfood.activity;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.paypal.android.MEP.CheckoutButton;
import com.paypal.android.MEP.PayPal;
import com.paypal.android.MEP.PayPalActivity;
import com.paypal.android.MEP.PayPalPayment;
import com.supinfo.youfood.adapter.RightCartAdapter;
import com.supinfo.youfood.handler.CheckoutHandler;
import com.supinfo.youfood.handler.HelpHandler;
import com.supinfo.youfood.handler.MenuHandler;
import com.supinfo.youfood.listener.AddToCartListener;
import com.supinfo.youfood.listener.PaypalResultFailListener;
import com.supinfo.youfood.model.Category;
import com.supinfo.youfood.preferences.YoufoodPreferences;
import com.supinfo.youfood.thread.CheckoutThread;
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
	private static final int PAYPAL_REQUEST_CODE = 1;
	
	private PayPal paypal;
	private AlertDialog finalCheckoutDialog;
	
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
		MenuThread thread = new MenuThread(handler, androidId, this);
		
		alert.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", this);
		
		thread.start();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode) {
		case PAYPAL_REQUEST_CODE:
			AlertDialog alert;
			PaypalResultFailListener listener = new PaypalResultFailListener(this);
			switch(resultCode) {
			case RESULT_OK:
				alert = new AlertDialog.Builder(this).create();
				alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", this);
				ProgressDialog progress = new ProgressDialog(this);
				String payKey = data.getStringExtra(PayPalActivity.EXTRA_PAY_KEY);
				CheckoutHandler handler = new CheckoutHandler(progress, alert, cartAdapter, payKey);
				CheckoutThread thread = new CheckoutThread(handler, androidId, cartAdapter.getCartProducts(), payKey);
				thread.start();
				break;
			case RESULT_CANCELED:
				alert = new AlertDialog.Builder(this).create();
				alert.setTitle("Ahem…");
				alert.setMessage("Il semblerait que vous ayez annulé votre paiement.\nQue souhaitez-vous faire ?");
				alert.setButton(DialogInterface.BUTTON_POSITIVE, "Réessayer", listener);
				alert.setButton(DialogInterface.BUTTON_NEUTRAL, "Appeler à l'aide", listener);
				alert.setButton(DialogInterface.BUTTON_NEGATIVE, "Annuler", listener);
				alert.show();
				break;
			case PayPalActivity.RESULT_FAILURE:
				alert = new AlertDialog.Builder(this).create();
				alert.setTitle("Oups…");
				alert.setMessage("Une erreur s'est produite lors de votre paiement.\nQue souhaitez-vous faire ?");
				alert.setButton(DialogInterface.BUTTON_POSITIVE, "Réessayer", listener);
				alert.setButton(DialogInterface.BUTTON_NEUTRAL, "Appeler à l'aide", listener);
				alert.setCancelable(false);
				alert.show();
				break;
			}
			break;
		}
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
			callHelp();
			break;
		case R.id.go_checkout:
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
			
			finalCheckoutDialog = paymentDialog.create();
			finalCheckoutDialog.show();
			break;
		case PAYPAL_CHECKOUT_BUTTON_ID:
			launchPaypalPayment();
			finalCheckoutDialog.dismiss();
			break;
		}
	}
	
	public void setPayPalObject(PayPal p) {
		paypal = p;
	}
	
	public void setMenu(ArrayList<Category> m) {
		menu = m;
	}
	
	public void displayMenu() {
		setContentView(R.layout.menu);
        TabHost mTabHost = (TabHost) findViewById(R.id.tabhost);
        mTabHost.setup(getLocalActivityManager());
        
        Button needHelp = (Button) findViewById(R.id.need_help);
        needHelp.setOnClickListener(this);
        Button goCheckout = (Button) findViewById(R.id.go_checkout);
        goCheckout.setOnClickListener(this);
        
        cartAdapter = new RightCartAdapter(this, goCheckout);
		ListView cart = (ListView) findViewById(R.id.cart_right);
		cart.setAdapter(cartAdapter);
		AddToCartListener.setCartAdapter(cartAdapter);
        
        for(Category category : menu) {
        	Intent i = new Intent(this, CategoryActivity.class);
        	i.putExtra("category", category);
            mTabHost.addTab(mTabHost.newTabSpec("category_"+category.getId()).setIndicator(category.getName()).setContent(i));
        }
	}
	
	public void callHelp() {
		ProgressDialog progress = new ProgressDialog(this);
		AlertDialog alert = new AlertDialog.Builder(this).create();
		HelpHandler handler = new HelpHandler(progress, alert);
		HelpThread thread = new HelpThread(handler, androidId);
		
		progress.setCancelable(false);
		
		alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", this);
		
		thread.start();
	}
	
	public void launchPaypalPayment() {
		PayPalPayment payment = new PayPalPayment();
		payment.setSubtotal(BigDecimal.valueOf(cartAdapter.getGlobalPrice()));
		payment.setCurrencyType("EUR");
		payment.setRecipient(YoufoodPreferences.PAYPAL_RECIPIENT);
		payment.setMerchantName("Restaurants YouFood");
		
		PayPal paypal = PayPal.getInstance();
		paypal.setShippingEnabled(false);
		Intent intent = paypal.checkout(payment, this);
		startActivityForResult(intent, PAYPAL_REQUEST_CODE);
	}
}
