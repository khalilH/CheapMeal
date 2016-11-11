package services;

import java.util.List;

import javax.servlet.http.Part;

import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.MongoClientException;

import exceptions.MyException;
import services.fonctions.RecetteFonctions;
import util.ServiceTools;

public class RecetteServices {

	public static JSONObject getRecettesAccueil(String cle) throws JSONException {

		try {
			return RecetteFonctions.getRecettesAccueil(cle);
		} catch (MyException e) {
			return ServiceTools.serviceRefused(e.getMessage(), e.getCode());
		}

	}

	/**
	 * Permet à un utilisateur d'ajouter une recette
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
			List<String> preparation, 
			Part photo) throws JSONException{

		try {
			RecetteFonctions.ajouterRecette(titre, cle, ingredients, quantites, mesures, preparation, photo);
			return ServiceTools.serviceAccepted("La recette a ete ajoutee");
		} catch (MyException e) {
			return ServiceTools.serviceRefused(e.getMessage(), e.getCode());
		}
	}

	/**
	 * Permet à un utilisateur de supprimer une recette
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
	 * Permet à un utilisateur de noter une recette
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
	 * Permet à un utilisateur d'afficher les informations d'une recette
	 * @param id
	 * @param cle
	 * @return
	 * @throws JSONException
	 */
	public static JSONObject afficherRecette(String id,String cle) throws JSONException{
		try {
			return RecetteFonctions.afficherRecette(id,cle);
		} catch (MongoClientException e) {
			return ServiceTools.serviceRefused(e.getMessage(), e.getCode());
		} catch (MyException e) {
			return ServiceTools.serviceRefused(e.getMessage(), e.getCode());
		}
	}

	/**
	 * Permet d'interroger l'API Datagram pour estimer le prix d'une recette
	 * @param id l'identifiant de la recette
	 * @param cle la cle de session d'un utilsateur connecte
	 * @return {Success: prixEstime}
	 * @throws JSONException
	 */
	public static JSONObject afficherPrixRecette(String id, String cle) throws JSONException {
		double prix;
		try {
			prix = RecetteFonctions.afficherPrixRecette(id, cle);
			return ServiceTools.serviceAccepted(prix+"");
		} catch (MyException e) {
			return ServiceTools.serviceRefused(e.getMessage(), e.getCode());
		}
	
	}
}
