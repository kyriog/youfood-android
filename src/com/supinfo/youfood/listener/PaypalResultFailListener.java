package com.supinfo.youfood.listener;

import com.supinfo.youfood.activity.MenuActivity;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class PaypalResultFailListener implements OnClickListener {
	private MenuActivity activity;
	
	public PaypalResultFailListener(MenuActivity a) {
		activity = a;
	}
	
	public void onClick(DialogInterface dialog, int which) {
		switch(which) {
		case DialogInterface.BUTTON_POSITIVE:
			activity.launchPaypalPayment();
			break;
		case DialogInterface.BUTTON_NEUTRAL:
			activity.callHelp();
			break;
		}
	}
}
