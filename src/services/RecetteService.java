package services;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import exceptions.MyException;
import services.fonctions.RecetteFonctions;
import util.ServiceTools;

public class RecetteService {

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
			List<String> preparation) throws JSONException{

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
				RecetteFonctions.noterRecette(cle, idRecette, note);
				return ServiceTools.serviceAccepted("La recette a ete notee");
			} catch (MyException e) {
				return ServiceTools.serviceRefused(e.getMessage(), e.getCode());
			}
	}
}
