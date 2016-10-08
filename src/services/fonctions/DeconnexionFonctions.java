package services.fonctions;

import java.security.KeyException;
import java.sql.SQLException;

import exceptions.SessionExpireeException;
import util.bdTools.RequeteStatic;

public class DeconnexionFonctions {

	
	/* Ajouter les Exception throws par la fonction */
	public static void deconnexion(String cle) throws NullPointerException, KeyException, SQLException, SessionExpireeException {
		
		if (cle == null)
			throw new NullPointerException("Arguments manquants");
		
		if(cle.length() != 32)
			throw new KeyException("Cle invalide");
		
		if (!RequeteStatic.isCleActive(cle))
			throw new SessionExpireeException("Votre session a expiree");
		
		/* Fermeture session */
		RequeteStatic.supprimerSessionAvecCle(cle);
	}

}
