package com.supinfo.youfood.activity;

import java.util.ArrayList;

import com.supinfo.youfood.handler.MenuHandler;
import com.supinfo.youfood.model.Category;
import com.supinfo.youfood.thread.MenuThread;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

public class MenuActivity extends Activity implements OnClickListener {
	private ArrayList<Category> menu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
		
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
		}
	}
	
	public void setMenu(ArrayList<Category> m) {
		menu = m;
	}
	
	public void displayMenu() {
		
	}
}
