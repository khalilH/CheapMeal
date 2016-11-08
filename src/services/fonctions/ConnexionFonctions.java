package services.fonctions;


import java.sql.SQLException;


import org.json.JSONException;
import org.json.JSONObject;

import exceptions.AuthenticationException;
import exceptions.InformationUtilisateurException;
import exceptions.NonValideException;
import exceptions.ParametreManquantException;
import util.ErrorCode;
import util.ServiceTools;
import util.bdTools.RequeteStatic;


public class ConnexionFonctions {
	public static String Connexion(String login, String mdp) 
			throws ParametreManquantException, 
					NonValideException, 
					AuthenticationException {
		
		//Verification parametre invalides
		if(login == null || mdp == null)
			throw new ParametreManquantException("Parametres(s) Manquant(s)", ErrorCode.PARAMETRE_MANQUANT);
		
		if(login.equals("") || mdp.equals(""))
			throw new ParametreManquantException("Parametres(s) Manquant(s)", ErrorCode.PARAMETRE_MANQUANT);
		
		if (mdp.length() < 6)
			throw new NonValideException("Mot de passe trop court", ErrorCode.PASSWORD_INVALIDE);
		
		//Verifier si lutilisateur existe
		if(RequeteStatic.checkIdentifiantsValide(login, mdp)){
			String cle;
			if(RequeteStatic.isSessionCree(login)){ 			// Si Lutilisateur est connecte
				RequeteStatic.updateDateExpirationAvecLogin(login);
				cle = RequeteStatic.recupererTokenAvecLogin(login); 
			}else{
				cle = RequeteStatic.createSessionFromLogin(login);
			}
			return cle;
		}
		else{ 			// Identifiants invalide
			if(!RequeteStatic.isLoginDisponible(login)){
				throw new AuthenticationException("Mot de passe incorrect", ErrorCode.PASSWORD_INCORRECT);
			}else{
				throw new AuthenticationException("Utilisateur inexistant", ErrorCode.UTILISATEUR_INEXISTANT);
			}
		}
	}

	public static int isConnecte(String key) throws InformationUtilisateurException {
		if(key == null)
			throw new NullPointerException("La cle n'a pas �t� fournie");
		if(key.equals(""))
			throw new InformationUtilisateurException("Cle vide");
		if(key.length() != 32)
			throw new InformationUtilisateurException("Cle corrompu");
		
		if(ServiceTools.isCleActive(key)){
			return RequeteStatic.obtenirIdSessionAvecCle(key);
		}else{
			throw new InformationUtilisateurException("Cl� n'est pas active");
		}

	}
}
