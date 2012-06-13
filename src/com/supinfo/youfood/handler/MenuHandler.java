package com.supinfo.youfood.handler;

import java.util.ArrayList;

import com.supinfo.youfood.activity.MenuActivity;
import com.supinfo.youfood.model.Category;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Message;

public class MenuHandler extends BaseHandler {
	private ProgressDialog progress;
	private AlertDialog alert;
	private MenuActivity activity;
	
	public MenuHandler(ProgressDialog p, AlertDialog a, MenuActivity ma) {
		progress = p;
		alert = a;
		activity = ma;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		switch(msg.arg1) {
		case STATUS_START:
			progress.setTitle("Chargement du menu…");
			progress.setMessage("Le menu est en cours de chargement.\nMerci de bien vouloir patienter…");
			progress.show();
			break;
		case STATUS_NOK:
			progress.dismiss();
			alert.setTitle("Hmm c'est embêtant…");
			alert.setMessage("Il semblerait que la tablette ne soit plus autorisée à accéder au menu.\nLa procédure d'enregistrement va redémarrer");
			alert.show();
			break;
		case STATUS_OK:
			progress.dismiss();
			activity.setMenu((ArrayList<Category>) msg.obj);
			activity.displayMenu();
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
