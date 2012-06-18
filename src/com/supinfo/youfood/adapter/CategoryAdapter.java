package com.supinfo.youfood.adapter;

import java.util.ArrayList;

import com.supinfo.youfood.listener.AddToCartListener;
import com.supinfo.youfood.listener.ZoomListener;
import com.supinfo.youfood.model.Product;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
		RelativeLayout checkoutLayout = new RelativeLayout(context);
		ImageView image = new ImageView(context);
		Button checkout = new Button(context);
		TextView name = new TextView(context);
		TextView description = new TextView(context);
		
		ZoomListener zlistener = new ZoomListener(context, product);
		image.setImageBitmap(product.getImage());
		image.setAdjustViewBounds(true);
		image.setMaxHeight(100);
		image.setMaxWidth(100);
		image.setPadding(0, 0, 5, 0);
		image.setOnClickListener(zlistener);
		
		AddToCartListener listener = new AddToCartListener(product);
		checkout.setText("Commander");
		checkout.setOnClickListener(listener);
		checkout.setPadding(30, 20, 30, 20);
		RelativeLayout.LayoutParams checkoutRules = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		checkoutRules.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
		checkout.setLayoutParams(checkoutRules);
		checkoutLayout.addView(checkout);
		
		name.setTextSize(30);
		name.setText(product.getName() + " – " + product.getPrice() + " €");
		
		description.setText(product.getDescription());
		description.setTextSize(20);
		
		itemLayout.setOrientation(LinearLayout.VERTICAL);
		itemLayout.addView(name);
		itemLayout.addView(description);
		
		view.setOrientation(LinearLayout.HORIZONTAL);
		view.addView(image);
		view.addView(itemLayout, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1));
		view.addView(checkoutLayout, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 0));
		
		return view;
	}
}
