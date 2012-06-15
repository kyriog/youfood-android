package com.supinfo.youfood.listener;

import android.view.View;
import android.view.View.OnClickListener;

import com.supinfo.youfood.adapter.RightCartAdapter;
import com.supinfo.youfood.model.CartProduct;
import com.supinfo.youfood.model.Product;

public class AddToCartListener implements OnClickListener {
	private Product product;
	private static RightCartAdapter adapter;
	
	public static void setCartAdapter(RightCartAdapter a) {
		adapter = a;
	}
	
	public AddToCartListener(Product p) {
		product = p;
	}

	public void onClick(View v) {
		CartProduct cartProduct = adapter.getCartProductFor(product);
		if(cartProduct == null) {
			adapter.addProductToCart(product);
		} else {
			cartProduct.addQuantity();
			adapter.notifyDataSetChanged();
		}
	}
}
