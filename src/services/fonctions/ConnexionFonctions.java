package services.fonctions;


import java.sql.SQLException;


import org.json.JSONException;
import org.json.JSONObject;

import exceptions.AuthenticationException;
import exceptions.InformationUtilisateurException;
import util.ServiceTools;
import util.bdTools.RequeteStatic;


public class ConnexionFonctions {
	public static String Connexion(String login, String mdp) throws JSONException, InformationUtilisateurException, SQLException, AuthenticationException{
		JSONObject jb = new JSONObject();
//		Verification parametre invalides
		if(login == null || mdp == null){
			throw new NullPointerException("Le login ou le mot de passe est null  "+mdp+ " et  "+login);
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
				throw new AuthenticationException("Votre mot de passe est incorrect");
			}else{
				throw new AuthenticationException("Nom d'utilisateur inconnu");
			}
		}
		
		//return jb;
	}

	public static int isConnecte(String key) throws InformationUtilisateurException {
		if(key == null)
			throw new NullPointerException("La cle n'a pas été fournie");
		if(key.equals(""))
			throw new InformationUtilisateurException("Cle vide");
		if(key.length() != 32)
			throw new InformationUtilisateurException("Cle corrompu");
		
		if(ServiceTools.isCleActive(key)){
			return RequeteStatic.obtenirIdSessionAvecCle(key);
		}else{
			throw new InformationUtilisateurException("Clé n'est pas active");
		}

	}
}
