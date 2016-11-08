package services.fonctions;

import java.security.KeyException;
import java.sql.SQLException;
import java.util.Random;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.naming.NamingException;

import exceptions.AuthenticationException;
import exceptions.IDException;
import exceptions.InformationUtilisateurException;
import exceptions.NonDisponibleException;
import exceptions.NonValideException;
import exceptions.ParametreManquantException;
import exceptions.SessionExpireeException;
import util.BCrypt;
import util.ErrorCode;
import util.ServiceTools;
import util.bdTools.RequeteStatic;

public class UtilisateurFonctions {
	

	public static void inscription(String login, String mdp, String prenom, String nom, String email) 
			throws NonDisponibleException, NonValideException, 
				exceptions.SQLException, ParametreManquantException{

		/* Verification des parametres */
		if(login == null || mdp == null || prenom == null || nom == null || email == null)
			throw new ParametreManquantException("Parametres(s) Manquant(s)", ErrorCode.PARAMETRE_MANQUANT);

		if(login.equals("") || mdp.equals("") || prenom.equals("") || nom.equals("") || email.equals(""))
			throw new ParametreManquantException("Parametres(s) Manquant(s)", ErrorCode.PARAMETRE_MANQUANT);
			
		if(mdp.length() < 6)
			throw new NonValideException("Mot de passe trop court", ErrorCode.PASSWORD_INVALIDE);

		if (!ServiceTools.isEmailValide(email)) 
			throw new NonValideException("Email non valide", ErrorCode.EMAIL_INVALIDE);
		
		if(!RequeteStatic.isLoginDisponible(login))
			throw new NonDisponibleException("Ce nom d'utilisateur est deja utilise", ErrorCode.LOGIN_NON_DISPO);

		if(!RequeteStatic.isEmailDisponible(email))
			throw new NonDisponibleException("Cette adresse email est deja utilisee", ErrorCode.EMAIL_NON_DISPO);

		/* Cryptage du mdp */
		String hashed = BCrypt.hashpw(mdp, BCrypt.gensalt());
		
		/* Creation de l'utilisateur dans la base SQL */
		RequeteStatic.ajoutUtilisateur(login, hashed, nom, prenom, email);
		int id = RequeteStatic.obtenirIdAvecLogin(login);
		if(id == -1)
			throw new exceptions.SQLException("Erreur interne au serveur", ErrorCode.SQL_EXCEPTION);


	}

	//TODO verifier pas de soucis changement d'ordre des parametres
	public static void changerMotDePasse(String cle, String newMdp, String oldMdp) 
			throws ParametreManquantException, NonValideException, 
			SessionExpireeException, AuthenticationException, 
			exceptions.SQLException {

		/* Verifier les parametres */
		if(cle == null || oldMdp == null || newMdp == null)
			throw new ParametreManquantException("Parametres(s) Manquant(s)", ErrorCode.PARAMETRE_MANQUANT);
		
		if(cle.equals("") || oldMdp.equals("") || newMdp.equals(""))
			throw new ParametreManquantException("Parametres(s) Manquant(s)", ErrorCode.PARAMETRE_MANQUANT);
		
		if (cle.length() != 32) 
			throw new NonValideException("Cle invalide", ErrorCode.CLE_INVALIDE);
		
		/* Verifier si le mot de passe contient au moins 6 caracteres */
		if(newMdp.length() < 6)
			throw new NonValideException("Mot de passe trop court", ErrorCode.PASSWORD_INVALIDE);
		
		/* Verifier si la personne a une session active */
		if(!ServiceTools.isCleActive(cle))
			throw new SessionExpireeException("Votre session a expiree", ErrorCode.SESSION_EXPIREE);
		
		//TODO faire verification que l'ancien mot de passe est correct 

		/* Verifier si le mot de passe est different de l'ancien */
		if(oldMdp.equals(newMdp))
			throw new AuthenticationException("Mot de passe identique a l'ancien", ErrorCode.PASSWORD_IDENTIQUE);
		

		/* Changement du mdp */
		int id = RequeteStatic.obtenirIdSessionAvecCle(cle);
		if(id == -1)
			throw new exceptions.SQLException("Erreur interne", ErrorCode.SQL_EXCEPTION);
		
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
	 * @throws ParametreManquantException cle non passee en parametre
	 * @throws NonValideException cle non valide
	 * @throws SessionExpireeException la session a deja expire
	 */
	public static void deconnexion(String cle) 
			throws ParametreManquantException, 
					NonValideException, 
					SessionExpireeException {
		
		if (cle == null)
			throw new ParametreManquantException("Arguments manquants", ErrorCode.PARAMETRE_MANQUANT);
		
		if(cle.length() != 32)
			throw new NonValideException("Cle invalide", ErrorCode.CLE_INVALIDE);
		
		if (!ServiceTools.isCleActive(cle))
			throw new SessionExpireeException("Votre session a expiree", ErrorCode.SESSION_EXPIREE);
		
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
