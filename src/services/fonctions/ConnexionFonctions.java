package services.fonctions;


import java.sql.SQLException;

import javax.security.sasl.AuthenticationException;

import org.json.JSONException;
import org.json.JSONObject;

import exceptions.InformationUtilisateurException;
import util.bdTools.RequeteStatic;


public class ConnexionFonctions {
	public static String Connexion(String login, String mdp) throws JSONException, InformationUtilisateurException, SQLException, AuthenticationException{
		JSONObject jb = new JSONObject();
//		Verification parametre invalides
		if(login == null || mdp == null){
			throw new NullPointerException("Le login ou le mot de passe est null");
		}
		if(login.equals("") || mdp.equals("") || mdp.length() < 6 ){
			throw new InformationUtilisateurException("Login ou mot de passe non valide");
		}
		//Verifier si lutilisateur existe
		if(RequeteStatic.checkIdentifiantsValide(login, mdp)){
			String cle;
			if(RequeteStatic.isSessionCree(login)){ 			// Si Lutilisateur est connecte
				RequeteStatic.updateDateExpirationAvecLogin(login);
				cle = RequeteStatic.recupererTokenAvecLogin(login); 
				jb.put("Success", "User's token has been replenished");
				jb.put("Token", cle);
			}else{
				cle = RequeteStatic.createSessionFromLogin(login);
				jb.put("Success", "User is properly connected");
				jb.put("Token", cle);
			}
			return cle;
		}
		else{ 			// Identifiants invalide

			if(!RequeteStatic.isLoginDisponible(login)){
				//jb.put("Error", "Votre mot de passe est incorrect");
				throw new AuthenticationException("Votre mot de passe est incorrect");
			}else{
				//jb.put("Error", "Votre compte n'est pas repertorie dans notre base");
				throw new AuthenticationException("Nom d'utilisateur inconnu");
			}
		}
		
		//return jb;
	}
}
