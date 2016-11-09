package services;

import java.net.UnknownHostException;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.MongoClientException;

import exceptions.MyException;
import exceptions.NonValideException;
import exceptions.ParametreManquantException;
import services.fonctions.RecetteFonctions;
import util.ErrorCode;
import util.ServiceTools;

public class RecetteServices {

	public static JSONObject getRecettesAccueil() throws JSONException {

		try {
			return RecetteFonctions.getRecettesAccueil();
		} catch (UnknownHostException e) {
			return ServiceTools.serviceRefused(e.getMessage(), -1);
		}

	}
	
	/**
	 * 
	 * @param titre
	 * @param cle
	 * @param ingredients
	 * @param quantites
	 * @param mesures
	 * @param preparation
	 * @return
	 * @throws JSONException
	 */
	public static JSONObject ajouterRecette(String titre, String cle, 
			List<String> ingredients, 
			List<Double> quantites, 
			List<String> mesures, 
			String preparation) throws JSONException{

		try {
			RecetteFonctions.ajouterRecette(titre, cle, ingredients, quantites, mesures, preparation);
			return ServiceTools.serviceAccepted("La recette a ete ajoutee");
		} catch (MyException e) {
			return ServiceTools.serviceRefused(e.getMessage(), e.getCode());
		}
	}

	/**
	 * 
	 * @param idRecette
	 * @param cle
	 * @return
	 * @throws JSONException
	 */
	public static JSONObject supprimerRecette(String idRecette, String cle) throws JSONException {
		try {
			RecetteFonctions.supprimerRecette(idRecette, cle);
			return ServiceTools.serviceAccepted("La recette a ete supprimee");
		} catch (MyException e) {
			return ServiceTools.serviceRefused(e.getMessage(), e.getCode());
		}
	}	


	/**
	 * 
	 * @param cle
	 * @param idRecette
	 * @param note
	 * @return
	 * @throws JSONException
	 */
	public static JSONObject noterRecette(String cle, String idRecette, int note) throws JSONException{
			try {
				return RecetteFonctions.noterRecette(cle, idRecette, note);
				//return ServiceTools.serviceAccepted("La recette a ete notee");
			} catch (MyException e) {
				return ServiceTools.serviceRefused(e.getMessage(), e.getCode());
			}
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws JSONException
	 */
	public static JSONObject afficherRecette(String id) throws JSONException{
			try {
				return RecetteFonctions.afficherRecette(id);
			} catch (MongoClientException e) {
				return ServiceTools.serviceRefused(e.getMessage(), e.getCode());
			} catch (UnknownHostException e) {
				return ServiceTools.serviceRefused(e.getMessage(), ErrorCode.MONGO_EXCEPTION);
			} catch (MyException e) {
				return ServiceTools.serviceRefused(e.getMessage(), e.getCode());
			}
	}
}
