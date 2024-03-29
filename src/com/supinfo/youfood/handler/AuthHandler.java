package com.supinfo.youfood.handler;

import com.supinfo.youfood.activity.MenuActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Message;

public class AuthHandler extends BaseHandler {
	private ProgressDialog progress;
	private AlertDialog alert;
	private Activity activity;
	private String androidId;
	
	public AuthHandler(ProgressDialog p, AlertDialog ad, Activity a, String aI) {
		progress = p;
		alert = ad;
		activity = a;
		androidId = aI;
	}

	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		switch(msg.arg1) {
		case STATUS_START:
			progress.setTitle("Vérification…");
			progress.setMessage("Vérification du statut de la tablette.\nMerci de bien vouloir patienter…");
			progress.show();
			break;
		case STATUS_NOK:
			progress.setTitle("En attente d'acceptation…");
			progress.setMessage("Cette tablette n'est pas encore acceptée pour accéder au menu.\nMerci de vous rendre dans l'administration et d'accepter cette tablette.\nIdentifiant de la tablette : "+androidId);
			break;
		case STATUS_OK:
			progress.dismiss();
			Intent intent = new Intent(activity, MenuActivity.class);
			activity.startActivity(intent);
			activity.finish();
			break;
		case STATUS_ERROR:
			progress.dismiss();
			String error = "";
			switch(msg.arg2) {
			case ERROR_HTTP:
				error = "Erreur lors de la communication avec le serveur.";
				break;
			case ERROR_TIMEOUT:
				error = "Le serveur n'a pas répondu dans le délai imparti.";
				break;
			case ERROR_JSON:
				error = "Erreur lors de l'analyse du JSON.";
				break;
			case ERROR_UNKNOWN:
				error = "Une erreur inconnue s'est produite.";
				break;
			}
			error += "\nVeuillez contacter votre administrateur système.";
			alert.setTitle("Une erreur s'est produite");
			alert.setMessage(error);
			alert.show();
		}
	}
}
