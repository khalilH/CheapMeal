package services;

import java.security.KeyException;
import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;

import exceptions.SessionExpireeException;
import services.fonctions.DeconnexionFonctions;
import util.ServiceTools;

public class DeconnexionServices {

	
	public static JSONObject deconnexion(String key) throws JSONException {
		try {
			DeconnexionFonctions.deconnexion(key);
			return ServiceTools.serviceAccepted("Deconnexion");
		} catch (NullPointerException npe) {
			return ServiceTools.serviceRefused(npe.getMessage(), -1);
		} catch (KeyException ke) {
			return ServiceTools.serviceRefused(ke.getMessage(), -1);
		} catch (SQLException sqle) {
			return ServiceTools.serviceRefused(sqle.getMessage(), -1);
		} catch (SessionExpireeException see) {
			return ServiceTools.serviceRefused(see.getMessage(), -1);
		}
	}
	
}
