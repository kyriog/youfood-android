package com.supinfo.youfood.activity;

import com.supinfo.youfood.handler.AuthHandler;
import com.supinfo.youfood.thread.AuthThread;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.provider.Settings;

public class AuthActivity extends Activity implements OnClickListener, OnCancelListener {
	private ProgressDialog progress;
	private AlertDialog alert;
	private AuthThread thread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
		
		progress = new ProgressDialog(this);
		alert = new AlertDialog.Builder(this).create();
		AuthHandler handler = new AuthHandler(progress, alert, this, androidId);
		thread = new AuthThread(handler, androidId);
		
		progress.setOnCancelListener(this);
		progress.setButton(DialogInterface.BUTTON_NEGATIVE, "Annuler", this);
		alert.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", this);
		
		thread.start();
	}

	public void onClick(DialogInterface dialog, int which) {
		switch(which) {
		case DialogInterface.BUTTON_NEGATIVE:
			progress.cancel();
			break;
		case DialogInterface.BUTTON_NEUTRAL:
			alert.dismiss();
			thread.requestStop();
			finish();
		}
	}

	public void onCancel(DialogInterface arg0) {
		thread.requestStop();
		finish();
	}

}
