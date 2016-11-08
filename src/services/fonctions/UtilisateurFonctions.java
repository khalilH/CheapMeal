package services.fonctions;

import java.util.Random;

import exceptions.AuthenticationException;
import exceptions.CleNonActiveException;
import exceptions.MailException;
import exceptions.MyException;
import exceptions.NonDisponibleException;
import exceptions.NonValideException;
import exceptions.ParametreManquantException;
import exceptions.SessionExpireeException;
import util.BCrypt;
import util.ErrorCode;
import util.ServiceTools;
import util.bdTools.RequeteStatic;

public class UtilisateurFonctions {
	
	/**
	 * 
	 * @param login
	 * @param mdp
	 * @param prenom
	 * @param nom
	 * @param email
	 * @throws MyException
	 */
	public static void inscription(String login, String mdp, String prenom, String nom, String email) 
			throws MyException {

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
			throw new exceptions.SQLException(ErrorCode.ERREUR_INTERNE, ErrorCode.SQL_EXCEPTION);


	}

	//TODO verifier pas de soucis changement d'ordre des parametres
	/**
	 * 
	 * @param cle
	 * @param newMdp
	 * @param oldMdp
	 * @throws MyException
	 */
	public static void changerMotDePasse(String cle, String newMdp, String oldMdp) 
			throws MyException {

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
			throw new exceptions.SQLException(ErrorCode.ERREUR_INTERNE, ErrorCode.SQL_EXCEPTION);
		
		RequeteStatic.changerMdpAvecId(id, newMdp);
	}

	/**
	 * 
	 * @param cle
	 * @param newEmail
	 * @throws MyException
	 */
	public static void changerEmail(String cle, String newEmail) 
			throws MyException {
		
		if (cle == null || newEmail == null) 
			throw new ParametreManquantException("Parametres(s) Manquant(s)", ErrorCode.PARAMETRE_MANQUANT);
		
		if (cle.equals("") || newEmail.equals("")) 
			throw new ParametreManquantException("Parametres(s) Manquant(s)", ErrorCode.PARAMETRE_MANQUANT);
		
		if (cle.length() != 32) 
			throw new NonValideException("Cle invalide", ErrorCode.CLE_INVALIDE);
		
		if (!ServiceTools.isEmailValide(newEmail)) 
			throw new NonValideException("Email non valide", ErrorCode.EMAIL_INVALIDE);
		
		if (!ServiceTools.isCleActive(cle))
			throw new SessionExpireeException("Votre session a expiree", ErrorCode.SESSION_EXPIREE);
		
		if (!RequeteStatic.isEmailDisponible(newEmail)) 
			throw new NonDisponibleException("Cette adresse email est deja utilisee", ErrorCode.EMAIL_NON_DISPO);
		
		int id = RequeteStatic.obtenirIdSessionAvecCle(cle);
		if (id == -1)
			throw new exceptions.SQLException("Erreur interne au serveur", ErrorCode.SQL_EXCEPTION);
		
		RequeteStatic.changerEmailAvecId(id, newEmail);
	}
	
	/**
	 * 
	 * @param login
	 * @param mdp
	 * @return
	 * @throws MyException
	 */
	public static String Connexion(String login, String mdp) 
			throws MyException {
		
		//Verification parametre invalides
		if(login == null || mdp == null)
			throw new ParametreManquantException("Parametres(s) Manquant(s)", ErrorCode.PARAMETRE_MANQUANT);
		
		if(login.equals("") || mdp.equals(""))
			throw new ParametreManquantException("Parametres(s) Manquant(s)", ErrorCode.PARAMETRE_MANQUANT);
		
		if (mdp.length() < 6)
			throw new NonValideException("Mot de passe trop court", ErrorCode.PASSWORD_INVALIDE);
		
		//Verifier si lutilisateur existe
		if(RequeteStatic.checkIdentifiantsValide(login, mdp)){
			String cle;
			if(RequeteStatic.isSessionCree(login)){ 			// Si Lutilisateur est connecte
				RequeteStatic.updateDateExpirationAvecLogin(login);
				cle = RequeteStatic.recupererTokenAvecLogin(login); 
			}else{
				cle = RequeteStatic.createSessionFromLogin(login);
			}
			return cle;
		}
		else{ 			// Identifiants invalide
			if(!RequeteStatic.isLoginDisponible(login)){
				throw new AuthenticationException("Mot de passe incorrect", ErrorCode.PASSWORD_INCORRECT);
			}else{
				throw new AuthenticationException("Utilisateur inexistant", ErrorCode.UTILISATEUR_INEXISTANT);
			}
		}
	}
	
	/**
	 * Permet de deconnecter un utilisateur en supprimant sa cle de session
	 * de la base de donnee
	 * @param cle la cle de session
	 * @throws MyException
	 */
	public static void deconnexion(String cle) 
			throws MyException {
		if (cle == null)
			throw new ParametreManquantException("Arguments manquants", ErrorCode.PARAMETRE_MANQUANT);
		
		if(cle.length() != 32)
			throw new NonValideException("Cle invalide", ErrorCode.CLE_INVALIDE);
		
		if (!ServiceTools.isCleActive(cle))
			throw new SessionExpireeException("Votre session a expiree", ErrorCode.SESSION_EXPIREE);
		
		/* Fermeture session */
		RequeteStatic.supprimerSessionAvecCle(cle);
	}


	/**
	 * 
	 * @param cle
	 * @return
	 * @throws MyException
	 */
	public static int isConnecte(String cle) 
			throws MyException {
		
		if(cle == null || cle.equals(""))
			throw new ParametreManquantException("Arguments manquants", ErrorCode.PARAMETRE_MANQUANT);

		if(cle.length() != 32)
			throw new NonValideException("Cle invalide", ErrorCode.CLE_INVALIDE);
		
		if(ServiceTools.isCleActive(cle)){
			return RequeteStatic.obtenirIdSessionAvecCle(cle);
		}else{
			throw new CleNonActiveException("Cle non active", ErrorCode.CLE_NON_ACTIVE);
		}
	}
	
	/**
	 * 
	 * @param email
	 * @return
	 * @throws MyException
	 */
	public static String recupererMdp(String email) 
			throws MyException {
		
		if(email == null || email.equals(""))
			throw new ParametreManquantException("Arguments manquants", ErrorCode.PARAMETRE_MANQUANT);
		
		if(!ServiceTools.isEmailValide(email))
			throw new NonValideException("Email non valide", ErrorCode.EMAIL_INVALIDE);
		
		if(!RequeteStatic.isEmailDisponible(email)){
			String login = RequeteStatic.obtenirLoginAvecMail(email);
			String mdp = genererRandomMDP();
			RequeteStatic.changerMdpAvecId(RequeteStatic.obtenirIdAvecLogin(login), mdp);
			String body ="Bonjour "+login+",\n"
					+ "Vous avez initialise une procedure de reinitialisation de mot de passe.\n"
					+ "Voici vos identifiants : \n"
					+ "Login : "+login+"\n"
					+ "Mot de passe provisoire: "+mdp+"\n\n"
					+ "Une fois connecte, n'oubliez pas de changer votre mot de passe.\n"
					+ "Au revoir et a bientot sur CheapMeal :)";
			String subject = "Reinitialisation de votre mot de passe";
			try {
				ServiceTools.sendEmail(body, subject, email);
			} catch (Exception e) {
				throw new MailException(ErrorCode.ERREUR_INTERNE, ErrorCode.MAIL_EXCEPTION);
			} 

		}else{
			throw new NonDisponibleException("Cette adresse email est deja utilisee", ErrorCode.EMAIL_NON_DISPO);
		}
		return "Un email a ete envoye a l'adresse indiquee";
	}

	/**
	 * 
	 * @return
	 */
	public static String genererRandomMDP(){
		String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		Random rand = new Random();
		String res = "";
		for(int i=0; i<10; i++)
			res += alphabet.charAt(rand.nextInt(alphabet.length()));
		return res;
	}
}
