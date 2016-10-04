package services.fonctions;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import Util.AuthenticationTools;
import Util.DBStatic;
import exceptions.IDException;
import exceptions.InformationUtilisateurException;

public class UtilisateurFonctions {

	public static void inscription(String login, String mdp, String prenom, String nom, String email) throws InformationUtilisateurException, IDException, SQLException{

		/* Verification des parametres */
		if(login == null || mdp == null || prenom == null || nom == null || email == null)
			throw new InformationUtilisateurException("Informations non complètes");

		if(login == "" || mdp == "" || prenom == "" || nom == "" || email == "")
			throw new InformationUtilisateurException("Nom d'utilisateur, mot de passe ou adresse mail invalide");

		if(mdp.length() < 6)
			throw new InformationUtilisateurException("Mot de passe trop court");
		
		if(!AuthenticationTools.loginLibre(login))
			throw new InformationUtilisateurException("Ce nom d'utilisateur n'est pas libre");

		if(!AuthenticationTools.emailLibre(email))
			throw new InformationUtilisateurException("Cette adresse email est déjà utilisée");

		/* Creation de l'utilisateur dans la base SQL */
		ajoutUtilisateurBD(login, mdp, nom, prenom, email);
		int id = AuthenticationTools.getIdAvecLogin(login);
		if(id == 0)
			throw new IDException("Utilisateur inexistant");
		
		//TODO Créer le profile ICI 
		//ProfileFonctions.creerProfile(id);
		
	}
	
	private static void ajoutUtilisateurBD(String login, String mdp, String nom, String prenom, String email) throws SQLException{
		Connection c = DBStatic.getSQLConnection();
		Statement stmt = c.createStatement();
		stmt.executeUpdate("insert into `UTILISATEURS` (login, mdp, prenom, nom, mail) values ('"+login+"','"+mdp+"','"+prenom+"','"+nom+"','"+email+"');");
		stmt.close();
		c.close();
	}

}
