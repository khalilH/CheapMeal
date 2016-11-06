package services;

import java.net.UnknownHostException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClientException;

import exceptions.InformationUtilisateurException;
import exceptions.RecetteException;
import exceptions.SessionExpireeException;
import services.fonctions.RecetteFonctions;
import util.ServiceTools;

public class RecetteService {

	public static JSONObject ajouterRecette(String titre, String key, List<String> listIng, List<Double> listQuant, List<String> listMesures, List<String> prepa) throws JSONException{
		try {
			RecetteFonctions.ajouterRecette(titre, key, listIng, listQuant, listMesures, prepa);
			return ServiceTools.serviceAccepted("La recette a ete ajoutee");
		} catch (MongoClientException mce) {
			return ServiceTools.serviceRefused(mce.getMessage(), -1);
		} catch (UnknownHostException uhe) {
			return ServiceTools.serviceRefused(uhe.getMessage(), -1);
		} catch (RecetteException re) {
			return ServiceTools.serviceRefused(re.getMessage(), -1);
		} catch (InformationUtilisateurException iue) {
			return ServiceTools.serviceRefused(iue.getMessage(), -1);
		} catch (SessionExpireeException see) {
			return ServiceTools.serviceRefused(see.getMessage(), -1);
		}
	}

	public static JSONObject supprimerRecette(String id_recette, String key) throws JSONException {
		try {
			RecetteFonctions.supprimerRecette(id_recette, key);
			return ServiceTools.serviceAccepted("La recette a ete supprimée");
		} catch (MongoClientException mce) {
			return ServiceTools.serviceRefused(mce.getMessage(), -1);
		} catch (UnknownHostException uhe) {
			return ServiceTools.serviceRefused(uhe.getMessage(), -1);
		} catch (RecetteException re) {
			return ServiceTools.serviceRefused(re.getMessage(), -1);
		} catch (InformationUtilisateurException iue) {
			return ServiceTools.serviceRefused(iue.getMessage(), -1);
		} catch (SessionExpireeException see) {
			return ServiceTools.serviceRefused(see.getMessage(), -1);
		}	}

	public static JSONObject getListeIngredients(String query) throws JSONException {
		try {
			JSONObject res = new JSONObject();
		
			
			List<BasicDBObject> ingredients;
			ingredients = RecetteFonctions.getListeIngredients(query);
			JSONArray suggestions = new JSONArray(ingredients);
//			res.put("query", "Unit");
			res.put("suggestions", suggestions);
			return res;
		} catch (MongoClientException e) {
			return ServiceTools.serviceRefused(e.getMessage(), -1);
		} catch (UnknownHostException e) {
			return ServiceTools.serviceRefused(e.getMessage(), -1);
		}
	}

	public static JSONObject noterRecette(String key, String idRecette, int note) throws JSONException{
		try {
			RecetteFonctions.noterRecette(key, idRecette, note);
			return ServiceTools.serviceAccepted("La recette a ete notee");
		} catch (MongoClientException mce) {
			return ServiceTools.serviceRefused(mce.getMessage(), -1);
		} catch (UnknownHostException uhe) {
			return ServiceTools.serviceRefused(uhe.getMessage(), -1);
		} catch (RecetteException re) {
			return ServiceTools.serviceRefused(re.getMessage(), -1);
		} catch (InformationUtilisateurException iue) {
			return ServiceTools.serviceRefused(iue.getMessage(), -1);
		} catch (SessionExpireeException see) {
			return ServiceTools.serviceRefused(see.getMessage(), -1);
		}
	}
}
