package services;

import org.json.JSONException;
import org.json.JSONObject;

import exceptions.MyException;
import services.fonctions.UtilisateurFonctions;
import util.ServiceTools;

public class UtilisateurServices {

	/**
	 * Permet d'inscrire un utilisateur
	 * @param login le nom d'utilisateur
	 * @param mdp le mot de passe de l'utilisateur
	 * @param prenom le prenom de l'utilisateur
	 * @param nom le nom de l'utilisateur
	 * @param email l'adresse email de l'utilisateur
	 * @return JSONObject contenant la reponse formatee ou "error" avec un message d'erreur
	 * @throws JSONException s'il y a eut une erreur a la creation du JSONObject 
	 */
	public static JSONObject inscription(String login, String mdp, String confirmationMdp, String prenom, String nom, String email) throws JSONException {
		try {
			UtilisateurFonctions.inscription(login, mdp, confirmationMdp, prenom, nom, email);
			return ServiceTools.serviceAccepted("Utilisateur cree");
		} catch (MyException e) {
			return ServiceTools.serviceRefused(e.getMessage(), e.getCode());
		}	
	}

	/**
	 * Permet a un utilisateur de changer de mot de passe
	 * @param cle la cle session de l'utilisateur
	 * @param currentMdp le mot de passe courant de l'utilisateur
	 * @param newMdp le nouveau mot de passe 
	 * @param confirmationMdp la confirmation du mot de passe 
	 * @return  JSONObject contenant la reponse formatee ou "error" avec un message d'erreur
	 * @throws JSONException s'il y a eut une erreur a la creation du JSONObject 
	 */
	public static JSONObject changerMotDePasse(String cle, String currentMdp, String newMdp, String confirmationMdp) throws JSONException{
		try {
			UtilisateurFonctions.changerMotDePasse(cle, currentMdp, newMdp, confirmationMdp);
			return ServiceTools.serviceAccepted("Mot de passe modifie");
		} catch (MyException e) {
			return ServiceTools.serviceRefused(e.getMessage(), e.getCode());
		}
	}

	/**
	 * Permet à un utilisateur de changer de mail
	 * @param cle la cle session utilsiateur 
	 * @param newEmail la nouvelle adresse email de l'utilisateur
	 * @return JSONObject contenant la reponse formatee ou "error" avec un message d'erreur
	 * @throws JSONException s'il y a eut une erreur a la creation du JSONObject 
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
	 * @throws JSONException s'il y a eut une erreur a la creation ou manipulation du JSONObject 
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
	 * Permet à un utilisateur de recupere un mot de passe
	 * @param email l'email de l'utilisateurs
	 * @return JSONObject contenant la reponse formatee ou "error" avec un message d'erreur
	 * @throws JSONException s'il y a eut une erreur a la creation du JSONObject 
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
	 * Permet de connecter un utilisateur en lui creant une session
	 * @param login le nom d'utilisateur
	 * @param mdp le mot de passe
	 * @return JSONObject contenant la reponse formatee ou "error" avec un message d'erreur
	 * @throws JSONException s'il y a eut une erreur a la creation du JSONObject 
	 */
	public static JSONObject connexion(String login, String mdp) throws JSONException {
		JSONObject result;
		try {
			result = UtilisateurFonctions.connexion(login,mdp);
			//return ServiceTools.serviceAccepted(result);
			return result;
		} catch (MyException e) {
			return ServiceTools.serviceRefused(e.getMessage(), e.getCode());
		}
	}

	/**
	 * Permet de savoir si un utilisateur est connecte
	 * @param cle la cle de session utilsiateur
	 * @return JSONObject contenant la reponse formatee ou "error" avec un message d'erreur
	 * @throws JSONException s'il y a eut une erreur a la creation du JSONObject 
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
