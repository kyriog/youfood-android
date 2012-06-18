package com.supinfo.youfood.handler;

import com.supinfo.youfood.adapter.RightCartAdapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Message;

public class CheckoutHandler extends BaseHandler {
	private ProgressDialog progress;
	private AlertDialog alert;
	private RightCartAdapter adapter;
	private String payKey;
	
	public CheckoutHandler(ProgressDialog p, AlertDialog ad, RightCartAdapter rca, String pK) {
		progress = p;
		alert = ad;
		payKey = pK;
		adapter = rca;
	}

	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		switch(msg.arg1) {
		case STATUS_START:
			progress.setTitle("Veuillez patienter…");
			progress.setMessage("Votre commande est en cours de transmission.\nMerci de bien vouloir patienter…");
			progress.show();
			break;
		case STATUS_OK:
			progress.dismiss();
			adapter.clearCart();
			alert.setTitle("Bonne nouvelle !");
			alert.setMessage("Votre commande a bien été envoyée.\nMerci de patienter le temps que nos cuisiniers s'occupent de vous mitonner de bons petits plats ! ;)");
			alert.show();
			break;
		case STATUS_ERROR:
			progress.dismiss();
			alert.setTitle("Une erreur s'est produite");
			alert.setMessage("Nous sommes désolé de ce désagrément, une erreur s'est produite.\nNous vous prions de bien vouloir vous rendre à l'accueil accompagné de cette tablette pour obtenir de l'assistance.\nN'oubliez pas d'indiquer l'identifiant de la transaction au serveur : " + payKey);
			alert.show();
		}
	}
}
