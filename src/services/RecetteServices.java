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

	/**
	 * Permet d'obtenur les recettes a afficher sur la page d'accueil
	 * @param cle la cle session utilisateur
	 * @return JSONObject contenant la reponse formatee ou "error" avec un message d'erreur
	 * @throws JSONException s'il y a eut une erreur a la creation du JSONObject 
	 */
	public static JSONObject getRecettesAccueil(String cle) throws JSONException {
		try {
			return RecetteFonctions.getRecettesAccueil(cle);
		} catch (MyException e) {
			return ServiceTools.serviceRefused(e.getMessage(), e.getCode());
		}

	}

	/**
	 * Permet à un utilisateur d'ajouter une recette
	 * @param titre le titre de la recette
	 * @param cle la cle session utilisateur
	 * @param ingredients la liste d'ingredients de la recette
	 * @param quantites la liste des quantites correspondant aux ingredients de la recette
	 * @param mesures la liste des mesures utilises pour exprimer les quantites
	 * @param preparation la liste des etapes de preparation
	 * @param photo la photo de la recette a upload
	 * @return JSONObject contenant la reponse formatee ou "error" avec un message d'erreur
	 * @throws JSONException s'il y a eut une erreur a la creation du JSONObject 
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
	 * @param idRecette l'id de la recette a supprimer
	 * @param cle la cle session utilisateur
	 * @return JSONObject contenant la reponse formatee ou "error" avec un message d'erreur
	 * @throws JSONException s'il y a eut une erreur a la creation du JSONObject 
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
	 * @param cle la cle session utilisateur
	 * @param idRecette l'id de la recette a noter
	 * @param note la note donnee a la recette
	 * @return JSONObject contenant la reponse formatee ou "error" avec un message d'erreur
	 * @throws JSONException s'il y a eut une erreur a la creation du JSONObject 
	 */
	public static JSONObject noterRecette(String cle, String idRecette, int note) throws JSONException{
		try {
			return RecetteFonctions.noterRecette(cle, idRecette, note);
		} catch (MyException e) {
			return ServiceTools.serviceRefused(e.getMessage(), e.getCode());
		}
	}

	/**
	 * Permet à un utilisateur d'afficher les informations d'une recette
	 * @param id l'id de la recette a afficher
	 * @param cle la cle session utilisateur
	 * @return JSONObject contenant la reponse formatee ou "error" avec un message d'erreur
	 * @throws JSONException s'il y a eut une erreur a la creation du JSONObject 
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
	 * @throws JSONException erreur JSON
	 */
	public static JSONObject afficherPrixRecette(String id, String cle) throws JSONException {
		try {
			java.text.DecimalFormat df = new java.text.DecimalFormat("0.##");
			double prix = RecetteFonctions.afficherPrixRecette(id, cle);
			return ServiceTools.serviceAccepted(df.format(prix)+"");
		} catch (MyException e) {
			return ServiceTools.serviceRefused(e.getMessage(), e.getCode());
		}
	
	}
}
