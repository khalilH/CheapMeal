package services.fonctions;

import java.net.UnknownHostException;
import java.util.List;

import com.mongodb.MongoClientException;

import exceptions.InformationUtilisateurException;
import exceptions.RecetteException;
import exceptions.SessionExpireeException;
import util.ServiceTools;
import util.bdTools.MongoFactory;
import util.bdTools.RequeteStatic;
import util.hibernate.model.Sessions;
import util.hibernate.model.Utilisateurs;

public class RecetteFonctions {

	public static void ajouterRecette(String titre, String key, List<String> listIng, List<String> prepa) throws RecetteException, InformationUtilisateurException, SessionExpireeException, MongoClientException, UnknownHostException{
		
		/* Verification des parametres */
		if(titre == null || key == null || listIng == null || prepa == null)
			throw new RecetteException("Informations non completes");
		
		if(titre.equals("") || listIng.stream().filter(i -> i.length() == 0).count() > 0
				|| prepa.stream().filter(p -> p.length() == 0).count() > 0) 
			throw new RecetteException("Titre, ingredient ou etape de preparation invalide");
		
		if (key.length() != 32) 
			throw new InformationUtilisateurException("Cle invalide");
		
		if(!ServiceTools.isCleActive(key))
			throw new SessionExpireeException("Votre session a expiree");
		
		/* Ajout de la recette */
		Sessions s = RequeteStatic.obtenirSession(key);
		Utilisateurs u = RequeteStatic.obtenirUtilisateur(s.getIdSession(), null);
		
		MongoFactory.ajouterRecette(titre, u.getId(), u.getLogin(), listIng, prepa);
		
	}
	
}
