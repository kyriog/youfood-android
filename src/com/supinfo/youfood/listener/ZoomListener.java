package com.supinfo.youfood.listener;

import com.supinfo.youfood.handler.ZoomHandler;
import com.supinfo.youfood.model.Product;
import com.supinfo.youfood.thread.ZoomThread;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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
		ProgressDialog progress = new ProgressDialog(context);
		AlertDialog.Builder alert = new AlertDialog.Builder(context);
		ImageView image = new ImageView(context);
		
		ZoomHandler handler = new ZoomHandler(progress, alert, image, product.getName());
		ZoomThread thread = new ZoomThread(handler, product.getImage());
		
		thread.start();
	}
}
