package services;

import java.net.UnknownHostException;

import org.json.JSONException;
import org.json.JSONObject;

import exceptions.NonValideException;
import exceptions.SessionExpireeException;
import services.fonctions.SearchFonctions;
import util.ServiceTools;

public class SearchServices {

	/**
	 * Permet de faire appel aux fonctions constituant le service du moteur de recherche
	 * @param query la requete de la recherche
	 * @return un JSONObject contenant les recettes satisfaisant la requete, ou
	 * "error" avec un message d'erreur
	 * @throws JSONException
	 */
	public static JSONObject search(String query, String cle) throws JSONException {

		try {
			return SearchFonctions.search(query, cle);
		} catch (UnknownHostException e) {
			return ServiceTools.serviceRefused(e.getMessage(), -1);
		} catch (NonValideException e) {
			return ServiceTools.serviceRefused(e.getMessage(), -1);
		} catch (SessionExpireeException e) {
			return ServiceTools.serviceRefused(e.getMessage(), -1);
		}
	}

	



}

