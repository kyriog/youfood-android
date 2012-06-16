package com.supinfo.youfood.handler;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Message;

public class HelpHandler extends BaseHandler {
	private ProgressDialog progress;
	private AlertDialog alert;
	
	public HelpHandler(ProgressDialog p, AlertDialog ad) {
		progress = p;
		alert = ad;
	}

	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		switch(msg.arg1) {
		case STATUS_START:
			progress.setTitle("Veuillez patienter…");
			progress.setMessage("Votre demande d'aide est en cours de transmission.\nMerci de bien vouloir patienter…");
			progress.show();
			break;
		case STATUS_NOK:
			progress.setTitle("Encore un instant…");
			progress.setMessage("Votre demande d'aide a bien été transmise.\nMerci de bien vouloir patienter, un serveur répondra à votre demande dans quelques instants.");
			break;
		case STATUS_OK:
			progress.dismiss();
			alert.setTitle("Bonne nouvelle !");
			alert.setMessage("Votre patience vient d'être récompensée :\nUn serveur vient de répondre à votre demande d'aide, et se dirige actuellement vers votre table.\nPréparez vos questions ! :)");
			alert.show();
			break;
		case STATUS_ERROR:
			progress.dismiss();
			alert.setTitle("Une erreur s'est produite");
			alert.setMessage("Nous sommes désolé de ce désagrément, une erreur s'est produite.\nNous vous prions de bien vouloir vous rendre à l'accueil accompagné de cette tablette pour obtenir de l'assistance.");
			alert.show();
		}
	}
}
