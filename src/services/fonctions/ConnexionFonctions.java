package services.fonctions;


import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;

import exceptions.InformationUtilisateurException;
import util.bdTools.RequeteStatic;


public class ConnexionFonctions {
	public static JSONObject Connexion(String login, String mdp) throws JSONException, InformationUtilisateurException, SQLException{
		JSONObject jb = new JSONObject();
		//Verification parametre invalides
		if(login == null || mdp == null){
			throw new NullPointerException("Le login ou le mot de passe est null");
		}
		if(login.equals("") || mdp.equals("") || login.length() < 6 || mdp.length() < 6 ){
			throw new InformationUtilisateurException("Login ou mot de passe non valide");
		}
		//Verifier si lutilisateur existe
		if(RequeteStatic.checkIdentifiantsValide(login, mdp)){
			
			if(RequeteStatic.isSessionCree(login)){ 			// Si Lutilisateur est connecte
				RequeteStatic.updateDateExpirationAvecLogin(login);
				String authToken = RequeteStatic.recupererTokenAvecLogin(login); 
				jb.put("Succes", "User's token has been replenished");
				jb.put("Token", authToken);
			}else{
				RequeteStatic.createSessionFromLogin(login);
				jb.put("Succes", "User is properly connected");
			}
		}
		else{ 			// Identifiants invalide

			if(!RequeteStatic.isLoginDisponible(login)){
				jb.put("Error", "Votre mot de passe est incorrect");
			}else{
				jb.put("Error", "Votre compte n'est pas repertorie dans notre base");
			}
		}
		
		return jb;
	}
}
