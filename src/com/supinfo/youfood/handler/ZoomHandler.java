package com.supinfo.youfood.handler;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.os.Message;
import android.widget.ImageView;

public class ZoomHandler extends BaseHandler implements OnClickListener, OnCancelListener {
	private ProgressDialog progress;
	private AlertDialog.Builder alert;
	private AlertDialog buildedAlert;
	private ImageView image;
	private Bitmap bmp;
	private String productName;
	
	public ZoomHandler(ProgressDialog p, AlertDialog.Builder a, ImageView i, String pN) {
		progress = p;
		alert = a;
		image = i;
		productName = pN;
	}

	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		switch(msg.arg1) {
		case STATUS_START:
			progress.setTitle("Chargement…");
			progress.setMessage("L'image est en cours de chargement.\nMerci de bien vouloir patienter…");
			progress.setCancelable(false);
			progress.show();
			break;
		case STATUS_OK:
			progress.dismiss();
			alert.setTitle(productName);
			bmp = (Bitmap) msg.obj;
			image.setImageBitmap(bmp);
			alert.setView(image);
			alert.setNeutralButton("Fermer", this);
			alert.setOnCancelListener(this);
			buildedAlert = alert.create();
			buildedAlert.show();
			break;
		case STATUS_ERROR:
			progress.dismiss();
			alert.setTitle("Une erreur s'est produite");
			alert.setMessage("Nous sommes désolé de ce désagrément, une erreur s'est produite.\nNous ne sommes pas en mesure de charger l'image.");
			alert.setNeutralButton("OK", null);
			alert.create().show();
		}
	}

	public void onClick(DialogInterface dialog, int which) {
		bmp.recycle();
	}

	public void onCancel(DialogInterface arg0) {
		bmp.recycle();
	}
}
