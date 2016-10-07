package services.fonctions;

import java.security.KeyException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.security.sasl.AuthenticationException;

import Util.AuthenticationTools;
import Util.BDTools.DBStatic;
import Util.BDTools.RequeteStatic;
import exceptions.IDException;
import exceptions.InformationUtilisateurException;
import exceptions.SessionExpireeException;

public class UtilisateurFonctions {

	public static void inscription(String login, String mdp, String prenom, String nom, String email) throws InformationUtilisateurException, IDException, SQLException{

		/* Verification des parametres */
		if(login == null || mdp == null || prenom == null || nom == null || email == null)
			throw new InformationUtilisateurException("Informations non compl�tes");

		if(login.equals("") || mdp.equals("") || prenom.equals("") || nom.equals("") || email.equals(""))
			throw new InformationUtilisateurException("Nom d'utilisateur, mot de passe ou adresse mail invalide");

		if(mdp.length() < 6)
			throw new InformationUtilisateurException("Mot de passe trop court");

		if(!AuthenticationTools.loginLibre(login))
			throw new InformationUtilisateurException("Ce nom d'utilisateur n'est pas libre");

		if(!AuthenticationTools.emailLibre(email))
			throw new InformationUtilisateurException("Cette adresse email est d�j� utilis�e");

		/* Creation de l'utilisateur dans la base SQL */
		Connection c = DBStatic.getSQLConnection();
		Statement stmt = c.createStatement();
		RequeteStatic.ajoutUtilisateur(stmt, login, mdp, nom, prenom, email);
		stmt.close();
		c.close();
		int id = AuthenticationTools.obtenirIdAvecLogin(login);
		if(id == 0)
			throw new IDException("Utilisateur inexistant");

		//TODO Cr�er le profile ICI 
		//ProfileFonctions.creerProfile(id);

	}

	public static void changerMotDePasse(String key, String newMdp, String oldMdp) throws SQLException, InformationUtilisateurException, SessionExpireeException, IDException{

		/* Verifier les parametres */
		if(key == null)
			throw new NullPointerException("Clé session manquante");
		
		if (key.length() != 32) 
			throw new InformationUtilisateurException("Clé invalide");
		
		if (oldMdp == null || newMdp == null)
			throw new NullPointerException("Missing argument");
		
		/* Verifier si la personne a une session active */

		if(!AuthenticationTools.cleActive(key))
			throw new SessionExpireeException("Votre session a expirée");

		/* Verifier si le mot de passe contient au moins 6 caracteres */
		if(newMdp.length() < 6)
			throw new InformationUtilisateurException("Le mot de passe doit contenir au moins 6 caractères");
		

		/* Verifier si le mot de passe est different de l'ancien */
		if(oldMdp.equals(newMdp))
			throw new InformationUtilisateurException("Le mot de passe doit être différent de l'ancien");
		

		/* Changement du mdp */
		int id = AuthenticationTools.obtenirIdAvecCle(key);
		if(id == 0)
			throw new IDException("Utilisateur inconnu");
		
		Connection c = DBStatic.getSQLConnection();
		Statement stmt = c.createStatement();
		RequeteStatic.changerMdpAvecId(stmt, id, newMdp);
		
		stmt.close();
		c.close();
	}



}
