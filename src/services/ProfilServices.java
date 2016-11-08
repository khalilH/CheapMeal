package services;

import java.net.UnknownHostException;

import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.MongoClientException;

import exceptions.IDException;
import exceptions.InformationUtilisateurException;
import exceptions.SessionExpireeException;
import services.fonctions.ProfilFonctions;
import util.ServiceTools;

public class ProfilServices {

	//TODO harmoniser les codes d'erreur

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
		} catch (NullPointerException e) {
			return ServiceTools.serviceRefused(e.getMessage(), 0);
		} catch (InformationUtilisateurException e) {
			return ServiceTools.serviceRefused(e.getMessage(), 0);
		} catch (SessionExpireeException e) {
			return ServiceTools.serviceRefused(e.getMessage(), 0);
		} catch (IDException e) {
			return ServiceTools.serviceRefused(e.getMessage(), 0);
		}
	}

	public static JSONObject afficherProfil(String cle,String login) throws JSONException{
		try {
			JSONObject jb = ProfilFonctions.afficherProfil(cle, login);
			return jb;
		} catch (NullPointerException e) {
			return ServiceTools.serviceRefused(e.getMessage(), 0);
		} catch (InformationUtilisateurException e) {
			return ServiceTools.serviceRefused(e.getMessage(), 0);
		} catch (SessionExpireeException e) {
			return ServiceTools.serviceRefused(e.getMessage(), 0);
		} catch (MongoClientException e) {
			return ServiceTools.serviceRefused(e.getMessage(), 0);
		} catch (UnknownHostException e) {
			return ServiceTools.serviceRefused(e.getMessage(), 0);
		}
	}

}
