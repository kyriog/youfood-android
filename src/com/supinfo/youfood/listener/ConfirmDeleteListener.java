package com.supinfo.youfood.listener;

import com.supinfo.youfood.adapter.RightCartAdapter;
import com.supinfo.youfood.model.CartProduct;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;

public class ConfirmDeleteListener implements OnClickListener, OnCancelListener {
	private static RightCartAdapter adapter;
	
	private CartProduct cartProduct; 
	
	public static void setAdapter(RightCartAdapter a) {
		adapter = a;
	}
	
	public ConfirmDeleteListener(CartProduct cP) {
		cartProduct = cP;
	}
	
	public void onClick(DialogInterface dialog, int which) {
		switch(which) {
		case DialogInterface.BUTTON_POSITIVE:
			adapter.removeFromCart(cartProduct);
			break;
		case DialogInterface.BUTTON_NEGATIVE:
			cancelDeletion();
			break;
		}
	}
	
	public void onCancel(DialogInterface arg0) {
		cancelDeletion();
	}
	
	protected void cancelDeletion() {
		cartProduct.addQuantity();
		adapter.notifyDataSetChanged();
	}
}
