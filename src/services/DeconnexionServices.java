package services;

import java.security.KeyException;

import org.json.JSONException;
import org.json.JSONObject;

import Util.ServiceTools;
import services.fonctions.DeconnexionFonctions;

public class DeconnexionServices {

	/* Harmoniser serviceRefused avec les bons codes d'erreur/succes */
	/* Mettre les differentes exception a catch, attention a l'ordre */
	
	public static JSONObject deconnexion(String key) throws JSONException {
		try {
			DeconnexionFonctions.deconnexion(key);
			JSONObject result = new JSONObject();
			result.put("message", "Deconnexion !");
			return result;
		} catch (NullPointerException npe) {
			return ServiceTools.serviceRefused(npe.getMessage(), -1);
		} catch (KeyException ke) {
			return ServiceTools.serviceRefused(ke.getMessage(), -1);
		}
	}
	
}
