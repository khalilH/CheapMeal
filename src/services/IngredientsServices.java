package services;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import exceptions.MyException;
import services.fonctions.IngredientsFonctions;
import util.ServiceTools;

public class IngredientsServices {

	/**
	 * Permet de construire la reponse formatee permettant l'utilisation
	 * du plugin Ajax AutoComplete for jQuery
	 * @param query la sous-chaine a utiliser pour l'autocompletion
	 * @return JSONObject contenant la reponse formatee ou "error" avec un message d'erreur
	 * @throws JSONException s'il y a eut une erreur a la creation du JSONObject 
	 */
	public static JSONObject getListeIngredients(String query) throws JSONException {
		try {
			List<String> ingredients = IngredientsFonctions.getListeIngredients(query);
			JSONArray suggestions = new JSONArray(ingredients);
			JSONObject res = new JSONObject()
					.put("suggestions", suggestions);
			return res;
		} catch (MyException e) {
			return ServiceTools.serviceRefused(e.getMessage(), e.getCode());
		}


	}

	/**
	 * Permer de faire appel aux fonctions constituant le service "Put Ingredients"
	 * @param fileName le nom du fichier contenant la liste des ingredients
	 * @return JSONObject contenant "success" ou "error" avec un message d'erreur
	 * @throws JSONException s'il y a eut une erreur a la creation du JSONObject 
	 */
	public static JSONObject putListeIngredients(String fileName) throws JSONException {
		try {
			IngredientsFonctions.putListeIngredients(fileName);
			return ServiceTools.serviceAccepted("Liste d'ingredients utilisable mise a jour");
		} catch (MyException e) {
			return ServiceTools.serviceRefused(e.getMessage(), e.getCode());
		}

	}
}
