package services.fonctions;

import java.security.KeyException;
import java.sql.SQLException;

import Util.AuthenticationTools;
import exceptions.SessionExpireeException;

public class DeconnexionFonctions {

	
	/* Ajouter les Exception throws par la fonction */
	public static void deconnexion(String cle) throws NullPointerException, KeyException, SQLException, SessionExpireeException {
		
		if (cle == null) throw new NullPointerException("Arguments manquants");
		
		if(cle.length() != 32) throw new KeyException("Cle invalide");
		
		if (!AuthenticationTools.cleActive(cle)) throw new SessionExpireeException("Votre session a expiree");
		
		/* Fermeture session */
		AuthenticationTools.detruireCleSession(cle);
	}

}
