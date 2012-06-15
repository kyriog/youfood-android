package com.supinfo.youfood.adapter;

import java.util.ArrayList;

import com.supinfo.youfood.listener.AddToCartListener;
import com.supinfo.youfood.model.Product;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CategoryAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<Product> products;
	
	public CategoryAdapter(Context context, ArrayList<Product> products) {
		this.context = context;
		this.products = products;
	}

	public int getCount() {
		return products.size();
	}

	public Product getItem(int location) {
		return products.get(location);
	}

	public long getItemId(int location) {
		return location;
	}

	public View getView(int location, View convertView, ViewGroup parent) {
		Product product = getItem(location);
		
		LinearLayout view = new LinearLayout(context);
		LinearLayout itemLayout = new LinearLayout(context);
		Button checkout = new Button(context);
		TextView name = new TextView(context);
		TextView description = new TextView(context);
		
		AddToCartListener listener = new AddToCartListener(product);
		checkout.setText("Commander");
		checkout.setOnClickListener(listener);
		
		name.setTextSize(25);
		name.setText(product.getName() + " – " + product.getPrice() + " €");
		
		description.setText(product.getDescription());
		
		itemLayout.setOrientation(LinearLayout.VERTICAL);
		itemLayout.addView(name);
		itemLayout.addView(description);
		
		view.setOrientation(LinearLayout.HORIZONTAL);
		view.addView(itemLayout, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1));
		view.addView(checkout, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT, 0));
		
		return view;
	}
}
