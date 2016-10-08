package services.fonctions;

import java.sql.SQLException;

import exceptions.IDException;
import exceptions.InformationUtilisateurException;
import exceptions.SessionExpireeException;
import util.bdTools.RequeteStatic;

public class UtilisateurFonctions {

	public static void inscription(String login, String mdp, String prenom, String nom, String email) throws InformationUtilisateurException, IDException, SQLException{

		/* Verification des parametres */
		if(login == null || mdp == null || prenom == null || nom == null || email == null)
			throw new InformationUtilisateurException("Informations non completes");

		if(login.equals("") || mdp.equals("") || prenom.equals("") || nom.equals("") || email.equals(""))
			throw new InformationUtilisateurException("Nom d'utilisateur, mot de passe ou adresse mail invalide");

		if(mdp.length() < 6)
			throw new InformationUtilisateurException("Mot de passe trop court");

		if(!RequeteStatic.isLoginDisponible(login))
			throw new InformationUtilisateurException("Ce nom d'utilisateur n'est pas libre");

		if(!RequeteStatic.isEmailDisponible(email))
			throw new InformationUtilisateurException("Cette adresse email est deja utilisee");

		/* Creation de l'utilisateur dans la base SQL */
		RequeteStatic.ajoutUtilisateur(login, mdp, nom, prenom, email);
		int id = RequeteStatic.obtenirIdAvecLogin(login);
		if(id == -1)
			throw new IDException("Utilisateur inexistant");


	}

	public static void changerMotDePasse(String key, String newMdp, String oldMdp) throws SQLException, InformationUtilisateurException, SessionExpireeException, IDException{

		/* Verifier les parametres */
		if(key == null)
			throw new NullPointerException("Cle session manquante");
		
		if (key.length() != 32) 
			throw new InformationUtilisateurException("Cle invalide");
		
		if (oldMdp == null || newMdp == null)
			throw new NullPointerException("Missing argument");
		
		/* Verifier si la personne a une session active */

		if(!RequeteStatic.isCleActive(key))
			throw new SessionExpireeException("Votre session a expiree");

		/* Verifier si le mot de passe contient au moins 6 caracteres */
		if(newMdp.length() < 6)
			throw new InformationUtilisateurException("Le mot de passe doit contenir au moins 6 caracteres");
		

		/* Verifier si le mot de passe est different de l'ancien */
		if(oldMdp.equals(newMdp))
			throw new InformationUtilisateurException("Le mot de passe doit etre different de l'ancien");
		

		/* Changement du mdp */
		int id = RequeteStatic.obtenirIdSessionAvecCle(key);
		if(id == -1)
			throw new IDException("Utilisateur inconnu");
		
		RequeteStatic.changerMdpAvecId(id, newMdp);
	}



}
