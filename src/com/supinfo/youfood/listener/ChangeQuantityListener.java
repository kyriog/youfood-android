package com.supinfo.youfood.listener;

import com.supinfo.youfood.adapter.RightCartAdapter;
import com.supinfo.youfood.model.CartProduct;

import android.view.View;
import android.view.View.OnClickListener;

public class ChangeQuantityListener implements OnClickListener {
	public static final int TYPE_REMOVE = 0;
	public static final int TYPE_ADD = 1;
	
	private int type;
	private CartProduct cartProduct;
	private RightCartAdapter adapter;
	
	public ChangeQuantityListener(int t, CartProduct cP, RightCartAdapter a) {
		type = t;
		cartProduct = cP;
		adapter = a;
	}
	
	public void onClick(View arg0) {
		switch(type) {
		case TYPE_REMOVE:
			cartProduct.removeQuantity();
			adapter.verifyQuantityFor(cartProduct);
			break;
		case TYPE_ADD:
			cartProduct.addQuantity();
			break;
		}
		adapter.notifyDataSetChanged();
	}
}
