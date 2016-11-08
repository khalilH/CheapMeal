package services.fonctions;

import java.net.UnknownHostException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClientException;

import exceptions.IDException;
import exceptions.InformationUtilisateurException;
import exceptions.SessionExpireeException;
import util.ServiceTools;
import util.bdTools.MongoFactory;
import util.bdTools.RequeteStatic;

public class ProfilFonctions {

	/**
	 * Permet de creer ou de mettre a jour la biographie de l'utilisateur
	 * @param cle la cle de session
	 * @param bio la biographie
	 * @throws InformationUtilisateurException 
	 * @throws NullPointerException
	 * @throws SessionExpireeException
	 * @throws IDException
	 */
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
		
		if (!ServiceTools.isCleActive(cle)) 
			throw new SessionExpireeException("Votre session a expiree");

		int id = RequeteStatic.obtenirIdSessionAvecCle(cle);
		if (id == -1)
			throw new IDException("Utilisateur inconnu");
	
		RequeteStatic.ajouterBioProfil(id, bio);
	}

	public static JSONObject afficherProfil(String cle, String login) throws InformationUtilisateurException, SessionExpireeException, JSONException, MongoClientException, UnknownHostException {
		if (cle == null) 
			throw new NullPointerException("Cle de session manquante");

		if (cle.length() != 32)
			throw new InformationUtilisateurException("Cle non valide");
		
		if(login == null || login.equals(""))
			throw new NullPointerException("Login manquant");
		
		if (!ServiceTools.isCleActive(cle)) 
			throw new SessionExpireeException("Votre session a expiree");
		
		if(RequeteStatic.isLoginDisponible(login))
			throw new InformationUtilisateurException("L'utilisateur n'existe pas");
		
		JSONObject jb = new JSONObject();
		String bio = RequeteStatic.recupBio(login);
		jb.put("Bio",bio );
		ArrayList<BasicDBObject> recettes = MongoFactory.getRecettesFromLogin(login);
		jb.put("recettes",recettes);
		jb.put("Login", login);
		return jb;
	}

}

