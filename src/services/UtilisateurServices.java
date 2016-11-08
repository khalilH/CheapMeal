package services;

import org.json.JSONException;
import org.json.JSONObject;

import exceptions.MyException;
import services.fonctions.UtilisateurFonctions;
import util.ServiceTools;

public class UtilisateurServices {

	/**
	 * 
	 * @param login
	 * @param mdp
	 * @param prenom
	 * @param nom
	 * @param email
	 * @return
	 * @throws JSONException
	 */
	public static JSONObject inscription(String login, String mdp, String prenom, String nom, String email) throws JSONException {
		try {
			UtilisateurFonctions.inscription(login, mdp, prenom, nom, email);
			return ServiceTools.serviceAccepted("Utilisateur cree");
		} catch (MyException e) {
			return ServiceTools.serviceRefused(e.getMessage(), e.getCode());
		}	
	}
	
	/**
	 * 
	 * @param cle
	 * @param oldMdp
	 * @param newMdp
	 * @return
	 * @throws JSONException
	 */
	public static JSONObject changerMotDePasse(String cle, String oldMdp, String newMdp) throws JSONException{
		try {
			UtilisateurFonctions.changerMotDePasse(cle, oldMdp, newMdp);
			return ServiceTools.serviceAccepted("Mot de passe modifie");
		} catch (MyException e) {
			return ServiceTools.serviceRefused(e.getMessage(), e.getCode());
		}
	}

	/**
	 * 
	 * @param cle
	 * @param newEmail
	 * @return
	 * @throws JSONException
	 */
	public static JSONObject changerEmail(String cle, String newEmail) throws JSONException{
		try {
			UtilisateurFonctions.changerEmail(cle, newEmail);
			return ServiceTools.serviceAccepted("Adresse mail modifiee");
		} catch (MyException e) {
			return ServiceTools.serviceRefused(e.getMessage(), e.getCode());
		}
	}

	/**
	 * Permet de faire appel aux fonction constituant le service "Deconnexion"
	 * @param key la cle de session de l'utilisateur a deconnecter
	 * @return JSONObject contenant "success" ou "error" avec un message d'erreur
	 * @throws JSONException : s'il y a eut une erreur a la creation ou manipulation du JSONObject 
	 */
	public static JSONObject deconnexion(String key) throws JSONException {
		try {
			UtilisateurFonctions.deconnexion(key);
			return ServiceTools.serviceAccepted("Deconnexion");
		} catch (MyException e) {
			return ServiceTools.serviceRefused(e.getMessage(), e.getCode());
		}
	}

	/**
	 * 
	 * @param email
	 * @return
	 * @throws JSONException
	 */
	public static JSONObject recupererMdp(String email) throws JSONException {
		try {
			String result = UtilisateurFonctions.recupererMdp(email);
			return ServiceTools.serviceAccepted(result);
		} catch (MyException e) {
			return ServiceTools.serviceRefused(e.getMessage(), e.getCode());
		}
	}

	/**
	 * 
	 * @param login
	 * @param mdp
	 * @return
	 * @throws JSONException
	 */
	public static JSONObject connexion(String login, String mdp) throws JSONException {
		String result;
		try {
			result = UtilisateurFonctions.Connexion(login,mdp);
			return ServiceTools.serviceAccepted(result);
		} catch (MyException e) {
			return ServiceTools.serviceRefused(e.getMessage(), e.getCode());
		}
	}

	/**
	 * 
	 * @param cle
	 * @return
	 * @throws JSONException
	 */
	public static JSONObject isConnecte(String cle) throws JSONException{
		try {
			String result = UtilisateurFonctions.isConnecte(cle)+"";
			return ServiceTools.serviceAccepted(result);
		} catch (MyException e) {
			return ServiceTools.serviceRefused(e.getMessage(), e.getCode());
		}
	}


}
