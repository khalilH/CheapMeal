package services;

import java.net.UnknownHostException;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.MongoClientException;

import exceptions.InformationUtilisateurException;
import exceptions.RecetteException;
import exceptions.SessionExpireeException;
import services.fonctions.RecetteFonctions;
import util.ServiceTools;

public class RecetteService {

	public static JSONObject ajouterRecette(String titre, String key, List<String> listIng, List<String> prepa) throws JSONException{
		try {
			RecetteFonctions.ajouterRecette(titre, key, listIng, prepa);
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
