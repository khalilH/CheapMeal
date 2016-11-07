package services;

import java.sql.SQLException;


import org.json.JSONException;
import org.json.JSONObject;

import exceptions.AuthenticationException;
import exceptions.InformationUtilisateurException;
import services.fonctions.ConnexionFonctions;
import util.ServiceTools;

public class ConnexionServices {
	public static JSONObject connexion(String login, String mdp) throws JSONException {
		try {
			String result = ConnexionFonctions.Connexion(login,mdp);
			return ServiceTools.serviceAccepted(result);
		} catch (NullPointerException npe) {
			return ServiceTools.serviceRefused(npe.getMessage(), -1);
		} catch (SQLException sqle) {
			return ServiceTools.serviceRefused(sqle.getMessage(), -1);
		} catch (InformationUtilisateurException e) {
			return ServiceTools.serviceRefused(e.getMessage(), -1);
		} catch (AuthenticationException ae) {
			return ServiceTools.serviceRefused(ae.getMessage(), -1);
		}
	}
}
