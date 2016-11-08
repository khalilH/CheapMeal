package services.fonctions;

import java.security.KeyException;
import java.sql.SQLException;
import java.util.Random;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.naming.NamingException;

import exceptions.IDException;
import exceptions.InformationUtilisateurException;
import exceptions.SessionExpireeException;
import util.BCrypt;
import util.ServiceTools;
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

		if (!ServiceTools.isEmailValide(email)) 
			throw new InformationUtilisateurException("Email non valide");
		
		if(!RequeteStatic.isLoginDisponible(login))
			throw new InformationUtilisateurException("Ce nom d'utilisateur n'est pas libre");

		if(!RequeteStatic.isEmailDisponible(email))
			throw new InformationUtilisateurException("Cette adresse email est deja utilisee");

		/* Cryptage du mdp */
		String hashed = BCrypt.hashpw(mdp, BCrypt.gensalt());
		
		/* Creation de l'utilisateur dans la base SQL */
		RequeteStatic.ajoutUtilisateur(login, hashed, nom, prenom, email);
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

		if(!ServiceTools.isCleActive(key))
			throw new SessionExpireeException("Votre session a expiree");

		/* Verifier si le mot de passe contient au moins 6 caracteres */
		if(newMdp.length() < 6)
			throw new InformationUtilisateurException("Le mot de passe doit contenir au moins 6 caracteres");
		
		
		//TODO faire verification que l'ancien mot de passe est correct

		/* Verifier si le mot de passe est different de l'ancien */
		if(oldMdp.equals(newMdp))
			throw new InformationUtilisateurException("Le mot de passe doit etre different de l'ancien");
		

		/* Changement du mdp */
		int id = RequeteStatic.obtenirIdSessionAvecCle(key);
		if(id == -1)
			throw new IDException("Utilisateur inconnu");
		
		RequeteStatic.changerMdpAvecId(id, newMdp);
	}

	public static void changerEmail(String cle, String newEmail) 
			throws SessionExpireeException, NullPointerException, 
			InformationUtilisateurException, IDException {
		
		if (cle == null) 
			throw new NullPointerException("Cle de session manquante");
		
		if (cle.length() != 32) 
			throw new InformationUtilisateurException("Cle invalide");
		
		if (newEmail == null) 
			throw new NullPointerException("Adresse mail manquante");
		
		if (!ServiceTools.isEmailValide(newEmail)) 
			throw new InformationUtilisateurException("Email non valide");
		
		if (!ServiceTools.isCleActive(cle))
			throw new SessionExpireeException("Votre session a expiree");
		
		if (!RequeteStatic.isEmailDisponible(newEmail)) 
			throw new InformationUtilisateurException("Cette adresse email est deja utilisee");
		
		int id = RequeteStatic.obtenirIdSessionAvecCle(cle);
		if (id == -1)
			throw new IDException("Utilisateur inconnu");
		
		RequeteStatic.changerEmailAvecId(id, newEmail);
		
	}
	
	/**
	 * Permet de deconnecter un utilisateur en supprimant sa cle de session
	 * de la base de donnee
	 * @param cle la cle de session
	 * @throws NullPointerException cle non passee en parametre
	 * @throws KeyException cle non valide
	 * @throws SQLException 
	 * @throws SessionExpireeException la session a deja expire
	 */
	public static void deconnexion(String cle) throws NullPointerException, KeyException, SQLException, SessionExpireeException {
		
		if (cle == null)
			throw new NullPointerException("Arguments manquants");
		
		if(cle.length() != 32)
			throw new KeyException("Cle invalide");
		
		if (!ServiceTools.isCleActive(cle))
			throw new SessionExpireeException("Votre session a expiree");
		
		/* Fermeture session */
		RequeteStatic.supprimerSessionAvecCle(cle);
	}

	public static String recup(String email) throws InformationUtilisateurException, AddressException, NamingException, MessagingException {
		if(email == null)
			throw new NullPointerException("Email manquant");
		if(!ServiceTools.isEmailValide(email))
			throw new InformationUtilisateurException("Ne correspond pas a un email");
		
		if(!RequeteStatic.isEmailDisponible(email)){
			String login = RequeteStatic.obtenirLoginAvecMail(email);
			String mdp = genererRandomMDP();
			RequeteStatic.changerMdpAvecId(RequeteStatic.obtenirIdAvecLogin(login), mdp);
			String body ="Hi,\n"
					+ "Vous avez initialiser une procedure de reinitialisation de mot de passe.\n"
					+ "Voici vos identifiants : \n"
					+ "Login : "+login+"\n"
					+ "Mot de passe provisoire: "+mdp+"\n\n"
					+ "Une fois connecte, n'oubliez pas de changer votre mot de passe."
					+ "Au revoir et a bientot sur CheapMeal :)";
			String subject = "Reinitialisation de mot de passe";
			ServiceTools.sendEmail(body, subject, email);
		}else{
			throw new InformationUtilisateurException("Cette adresse email n'existe pas dans notre base de donnees.");
		}
		return "Un email a ete envoye a l'adresse indiquee";
	}

	public static String genererRandomMDP(){
		String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		Random rand = new Random();
		String res = "";
		for(int i=0; i<10; i++)
			res += alphabet.charAt(rand.nextInt(alphabet.length()));
		return res;
	}
}
