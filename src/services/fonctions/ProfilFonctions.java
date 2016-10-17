package services.fonctions;

import exceptions.IDException;
import exceptions.InformationUtilisateurException;
import exceptions.SessionExpireeException;
import util.bdTools.RequeteStatic;

public class ProfilFonctions {

	public static void ajouterBio(String cle, String bio) throws 
	InformationUtilisateurException, NullPointerException, 
	SessionExpireeException, IDException {
		if (cle == null) 
			throw new NullPointerException("Cle de session manquante");

		if (cle.length() != 32)
			throw new InformationUtilisateurException("Cle non valide");
		
		if (bio == null)
			throw new NullPointerException("Biographie manquante");
		
		if (bio.length() > 250) {
			throw new InformationUtilisateurException("Biographie trop longue");
		}
		
		if (!RequeteStatic.isCleActive(cle)) 
			throw new SessionExpireeException("Votre session a expiree");

		int id = RequeteStatic.obtenirIdSessionAvecCle(cle);
		if (id == -1)
			throw new IDException("Utilisateur inconnu");
	
		RequeteStatic.ajouterBioProfil(id, bio);
	}

}

