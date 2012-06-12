package com.supinfo.youfood.handler;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;

public class AuthHandler extends Handler {
	public final static int STATUS_START = 1;
	public final static int STATUS_ERROR = 2;
	public final static int STATUS_OK = 3;
	public final static int STATUS_NOK = 4;
	
	public final static int ERROR_HTTP = 5;
	public final static int ERROR_TIMEOUT = 6;
	public final static int ERROR_JSON = 7;
	public final static int ERROR_UNKNOWN = 8;
	
	private ProgressDialog progress;
	private AlertDialog alert;
	private String androidId;
	
	public AuthHandler(ProgressDialog p, AlertDialog a, String aI) {
		progress = p;
		alert = a;
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
