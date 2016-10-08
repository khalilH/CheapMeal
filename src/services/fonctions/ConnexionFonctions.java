package services.fonctions;


import java.sql.SQLException;
import java.sql.Timestamp;

import org.hibernate.Session;
import org.json.JSONException;
import org.json.JSONObject;

import Util.AuthenticationTools;
import Util.Hibernate.HibernateUtil;
import Util.Hibernate.Model.Sessions;
import exceptions.InformationUtilisateurException;


public class ConnexionFonctions {
	public static JSONObject Connexion(String login, String mdp) throws JSONException, InformationUtilisateurException, SQLException{
		JSONObject jb = new JSONObject();
		//Verification parametre invalides
		if(login == null || mdp == null){
			throw new NullPointerException("Le login ou le mot de passe est null");
		}
		if(login == "" || mdp == "" || mdp.length() == 0){
			throw new InformationUtilisateurException("Login ou mot de passe  non valide");
		}
		//Verifier si lutilisateur existe
		if(AuthenticationTools.isIdentifiantsValide(login, mdp)){
			
			if(AuthenticationTools.isUtilisateurConnecte(login)){ 			// Si Lutilisateur est connecte
				String authToken = AuthenticationTools.updateAndRetrieveTokenTime(login);
				jb.put("Succes", "User's token has been replenished");
				jb.put("Token", authToken);
			}else{
//				String authToken = AuthenticationTools.addSessionFromLogin(login);
				Session s = HibernateUtil.getSessionFactory().getCurrentSession();
				jb.put("Succes", "User is properly connected");
				jb.put("Token", "abcd");
			}
		}
		else{ 			// Identifiants invalide

			if(AuthenticationTools.isUserInDatabase(login)){
				jb.put("Error", "Votre mot de passe est incorrect");
			}else{
				jb.put("Error", "Votre compte n'est pas repertori� dans notre base");
			}
		}
		
		return jb;
	}
}
