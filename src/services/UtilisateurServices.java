package services;

import java.security.KeyException;
import java.sql.SQLException;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.naming.NamingException;

import org.json.JSONException;
import org.json.JSONObject;

import exceptions.IDException;
import exceptions.InformationUtilisateurException;
import exceptions.SessionExpireeException;
import services.fonctions.UtilisateurFonctions;
import util.ServiceTools;

public class UtilisateurServices {

	//TODO remplacer les 0 par les bons codes d'erreurs
	
	public static JSONObject inscription(String login, String mdp, String prenom, String nom, String email) throws JSONException {
		try{
			UtilisateurFonctions.inscription(login, mdp, prenom, nom, email);
			return ServiceTools.serviceAccepted("Utilisateur créé");
		}catch (SQLException sqle) {
			return ServiceTools.serviceRefused(sqle.getMessage(), 0);
		}catch (IDException ide) {
			return ServiceTools.serviceRefused(ide.getMessage(), 0);
		}catch (InformationUtilisateurException iue){
			return ServiceTools.serviceRefused(iue.getMessage(), 0);
		}
	}
	
	public static JSONObject changerMotDePasse(String cle, String oldMdp, String newMdp) throws JSONException{
		try {
			UtilisateurFonctions.changerMotDePasse(cle, oldMdp, newMdp);
			return ServiceTools.serviceAccepted("Mot de passe modifié");
		} catch (SQLException sqle) {
			return ServiceTools.serviceRefused(sqle.getMessage(), 0);
		} catch (InformationUtilisateurException iue) {
			return ServiceTools.serviceRefused(iue.getMessage(), 0);
		} catch (SessionExpireeException see) {
			return ServiceTools.serviceRefused(see.getMessage(), 0);
		} catch (IDException ide){
			return ServiceTools.serviceRefused(ide.getMessage(), 0);
		}
	}

	public static JSONObject changerEmail(String cle, String newEmail) throws JSONException{
			try {
				UtilisateurFonctions.changerEmail(cle, newEmail);
				return ServiceTools.serviceAccepted("Adresse mail modifiee");
			} catch (NullPointerException e) {
				return ServiceTools.serviceRefused(e.getMessage(), 0);
			} catch (SessionExpireeException e) {
				return ServiceTools.serviceRefused(e.getMessage(), 0);
			} catch (InformationUtilisateurException e) {
				return ServiceTools.serviceRefused(e.getMessage(), 0);
			} catch (IDException e) {
				return ServiceTools.serviceRefused(e.getMessage(), 0);
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
		} catch (NullPointerException npe) {
			return ServiceTools.serviceRefused(npe.getMessage(), -1);
		} catch (KeyException ke) {
			return ServiceTools.serviceRefused(ke.getMessage(), -1);
		} catch (SQLException sqle) {
			return ServiceTools.serviceRefused(sqle.getMessage(), -1);
		} catch (SessionExpireeException see) {
			return ServiceTools.serviceAccepted("Deconnexion");
		}
	}
	
	
	public static JSONObject recupMdp(String email) throws JSONException {
		try {
			String result = UtilisateurFonctions.recup(email);
			return ServiceTools.serviceAccepted(result);
			
		} catch (NullPointerException npe) {
			return ServiceTools.serviceRefused(npe.getMessage(), -1);
		} catch (InformationUtilisateurException e) {
			return ServiceTools.serviceRefused(e.getMessage(), -1);
		} catch (AddressException e) {
			return ServiceTools.serviceRefused(e.getMessage(), -1);
		} catch (NamingException e) {
			return ServiceTools.serviceRefused(e.getMessage(), -1);
		} catch (MessagingException e) {
			return ServiceTools.serviceRefused(e.getMessage(), -1);
		}
	}


}
