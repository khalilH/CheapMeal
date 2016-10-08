package services;

import java.sql.SQLException;

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
			return ServiceTools.serviceAccepted();
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
			return ServiceTools.serviceAccepted();
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

}
