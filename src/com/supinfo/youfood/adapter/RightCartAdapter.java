package com.supinfo.youfood.adapter;

import java.util.ArrayList;

import com.supinfo.youfood.model.CartProduct;
import com.supinfo.youfood.model.Product;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RightCartAdapter extends BaseAdapter {
	private ArrayList<CartProduct> cartProducts = new ArrayList<CartProduct>();
	
	private Context context;
	
	public RightCartAdapter(Context c) {
		context = c;
	}

	public int getCount() {
		return cartProducts.size();
	}

	public CartProduct getItem(int location) {
		return cartProducts.get(location);
	}

	public long getItemId(int location) {
		return location;
	}

	public View getView(int location, View convertView, ViewGroup parent) {
		CartProduct cartProduct = getItem(location);
		if(cartProduct.getQuantity() == 0) {
			cartProducts.remove(cartProduct);
			notifyDataSetChanged();
		}
		
		LinearLayout view = new LinearLayout(context);
		
		TextView quantity = new TextView(context);
		
		LinearLayout itemLayout = new LinearLayout(context);
		TextView name = new TextView(context);
		
		LinearLayout buttonsLayout = new LinearLayout(context);
		Button removeQuantity = new Button(context);
		Button addQuantity = new Button(context);
		
		removeQuantity.setText("-");
		addQuantity.setText("+");
		buttonsLayout.setOrientation(LinearLayout.HORIZONTAL);
		buttonsLayout.setGravity(Gravity.RIGHT);
		buttonsLayout.addView(removeQuantity);
		buttonsLayout.addView(addQuantity);
		
		name.setText(cartProduct.getProduct().getName());
		itemLayout.setOrientation(LinearLayout.VERTICAL);
		itemLayout.addView(name);
		itemLayout.addView(buttonsLayout, new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, Gravity.BOTTOM));
		
		quantity.setTextSize(50);
		quantity.setText(String.valueOf(cartProduct.getQuantity()));
		view.setOrientation(LinearLayout.HORIZONTAL);
		view.addView(quantity);
		view.addView(itemLayout, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		
		return view;
	}
	
	public CartProduct getCartProductFor(Product product) {
		for(CartProduct cartProduct : cartProducts) {
			if(product.equals(cartProduct.getProduct()))
				return cartProduct;
		}
		return null;
	}
	
	public void addProductToCart(Product product) {
		cartProducts.add(new CartProduct(product));
		notifyDataSetChanged();
	}
}
