package services;

import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;

import Util.ServiceTools;
import exceptions.IDException;
import exceptions.InformationUtilisateurException;
import services.fonctions.UtilisateurFonctions;

public class UtilisateurServices {

	//TODO remplacer les 0 par les bons codes d'erreurs
	
	public static JSONObject inscription(String login, String mdp, String prenom, String nom, String email) throws JSONException {
		try{
			UtilisateurFonctions.inscription(login, mdp, prenom, nom, email);
			return ServiceTools.serviceAccepted();
		}catch (SQLException sqle) {
			return ServiceTools.serviceRefused(sqle.getMessage(), 0);
		}catch (IDException ide) {
			return ServiceTools.serviceRefused(ide.getMessage(), 0);
		}catch (InformationUtilisateurException iue){
			return ServiceTools.serviceRefused(iue.getMessage(), 0);
		}
	}

}
