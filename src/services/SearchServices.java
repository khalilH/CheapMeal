package services;

import java.net.UnknownHostException;

import org.json.JSONException;
import org.json.JSONObject;

import exceptions.MyException;
import services.fonctions.SearchFonctions;
import util.ErrorCode;
import util.ServiceTools;

public class SearchServices {

	/**
	 * Permet de faire appel aux fonctions constituant le service du moteur de recherche
	 * @param query la requete de la recherche
	 * @param cle la cle de session d'un utilisateur connecte
	 * @return un JSONObject contenant les 20 premieres recettes satisfaisant 
	 * la requete, ou "error" avec un message d'erreur
	 * @throws JSONException s'il y a eut une erreur a la creation de l'objet JSON
	 */
	public static JSONObject search(String query, String cle) throws JSONException {

		try {
			return SearchFonctions.search(query, cle);
		} catch (UnknownHostException e) {
			return ServiceTools.serviceRefused(ErrorCode.ERREUR_INTERNE, ErrorCode.ELASTIC_SEARCH_EXCEPTION);
		} catch (MyException e) {
			return ServiceTools.serviceRefused(e.getMessage(), e.getCode());
		} 
		
	}

	



}

