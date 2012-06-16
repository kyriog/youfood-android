package com.supinfo.youfood.activity;

import java.util.ArrayList;

import com.supinfo.youfood.adapter.RightCartAdapter;
import com.supinfo.youfood.handler.HelpHandler;
import com.supinfo.youfood.handler.MenuHandler;
import com.supinfo.youfood.listener.AddToCartListener;
import com.supinfo.youfood.model.Category;
import com.supinfo.youfood.thread.HelpThread;
import com.supinfo.youfood.thread.MenuThread;

import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;

@SuppressWarnings("deprecation")
public class MenuActivity extends ActivityGroup implements DialogInterface.OnClickListener, View.OnClickListener {
	private ArrayList<Category> menu;
	private String androidId;

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
	
	public void onClick(View arg0) {
		ProgressDialog progress = new ProgressDialog(this);
		AlertDialog alert = new AlertDialog.Builder(this).create();
		HelpHandler handler = new HelpHandler(progress, alert);
		HelpThread thread = new HelpThread(handler, androidId);
		
		progress.setCancelable(false);
		
		alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", this);
		
		thread.start();
	}
	
	public void setMenu(ArrayList<Category> m) {
		menu = m;
	}
	
	public void displayMenu() {
		setContentView(R.layout.menu);
        TabHost mTabHost = (TabHost) findViewById(R.id.tabhost);
        mTabHost.setup(getLocalActivityManager());
        
        RightCartAdapter cartAdapter = new RightCartAdapter(this);
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
	}
}
