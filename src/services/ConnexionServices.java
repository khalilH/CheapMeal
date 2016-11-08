package services;

import org.json.JSONException;
import org.json.JSONObject;

import exceptions.AuthenticationException;
import exceptions.InformationUtilisateurException;
import exceptions.NonValideException;
import exceptions.ParametreManquantException;
import services.fonctions.ConnexionFonctions;
import util.ServiceTools;

public class ConnexionServices {
	public static JSONObject connexion(String login, String mdp) throws JSONException {
		String result;
		try {
			result = ConnexionFonctions.Connexion(login,mdp);
			return ServiceTools.serviceAccepted(result);
		} catch (ParametreManquantException e) {
			return ServiceTools.serviceRefused(e.getMessage(), e.getCode());
		} catch (NonValideException e) {
			return ServiceTools.serviceRefused(e.getMessage(), e.getCode());
		} catch (AuthenticationException e) {
			return ServiceTools.serviceRefused(e.getMessage(), e.getCode());
		}

	}

	public static JSONObject isConnecte(String key) throws JSONException{
		String result;
		try {
			result = ConnexionFonctions.isConnecte(key)+"";
			return ServiceTools.serviceAccepted(result);
		} catch (InformationUtilisateurException e) {
			return ServiceTools.serviceRefused(e.getMessage(), -1);
		}

	}
}
