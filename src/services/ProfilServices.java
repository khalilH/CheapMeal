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

	public static JSONObject afficherBio(String cle,String login) throws JSONException{
		try {
			JSONObject jb = ProfilFonctions.afficherBio(cle, login);
			return jb;
		} catch (NullPointerException e) {
			return ServiceTools.serviceRefused(e.getMessage(), 0);
		} catch (InformationUtilisateurException e) {
			return ServiceTools.serviceRefused(e.getMessage(), 0);
		} catch (SessionExpireeException e) {
			return ServiceTools.serviceRefused(e.getMessage(), 0);
		} catch (MongoClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
