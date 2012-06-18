package com.supinfo.youfood.listener;

import com.supinfo.youfood.model.Product;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class ZoomListener implements OnClickListener {
	private Context context;
	private Product product;
	
	public ZoomListener(Context c, Product p) {
		context = c;
		product = p;
	}
	
	public void onClick(View arg0) {
		ImageView image = new ImageView(context);
		image.setImageBitmap(product.getImage());
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(product.getName());
		builder.setView(image);
		builder.setPositiveButton("Fermer", null);
		AlertDialog alert = builder.create();
		
		alert.show();
	}
}
