package services;

import org.json.JSONException;
import org.json.JSONObject;

import exceptions.MyException;
import services.fonctions.ProfilFonctions;
import util.ServiceTools;

public class ProfilServices {


	/**
	 * Permet de faire appel aux fonction permettant d'ajouter ou de mettre a
	 * jour la biographie d'un utilisateur
	 * @param cle la cle de session de l' utilisateur
	 * @param bio la biographie de l'utilisateur
	 * @return JSONObject contenant "success" ou "error" avec un message d'erreur
	 * @throws JSONException : s'il y a eut une erreur a la creation ou manipulation du JSONObject 
	 */
	public static JSONObject ajouterBio(String cle, String bio) throws JSONException{
		try {
			ProfilFonctions.ajouterBio(cle, bio);
			return ServiceTools.serviceAccepted("Biographie modifiee");
		} catch (MyException e) {
			return ServiceTools.serviceRefused(e.getMessage(), e.getCode());
		}

	}

	/**
	 * 
	 * @param cle
	 * @param login
	 * @return
	 * @throws JSONException
	 */
	public static JSONObject afficherProfil(String cle,String login) throws JSONException{
		try {
			JSONObject jb = ProfilFonctions.afficherProfil(cle, login);
			return jb;
		} catch (MyException e) {
			return ServiceTools.serviceRefused(e.getMessage(), e.getCode());
		}

	}

}
