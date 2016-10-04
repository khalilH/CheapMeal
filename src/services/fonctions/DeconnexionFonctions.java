package services.fonctions;

import java.security.KeyException;

public class DeconnexionFonctions {

	
	/* Ajouter les Exception throws par la fonction */
	public static void deconnexion(String key) throws NullPointerException, KeyException {
		
		if (key == null) throw new NullPointerException("Arguments manquants");
		
		if(key.length() != 32) throw new KeyException("cle invalide");
		
//		if (!AuthenticationTools.onGoingSessionKey(key)) throw new ExpiredSessionException("Your session has timed out");
		
//		 Fermeture session
//		AuthentificationTools.destroyKey(key);	
	}

}
