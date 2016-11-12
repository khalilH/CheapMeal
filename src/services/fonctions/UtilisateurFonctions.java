package services.fonctions;

import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

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
import util.RequestParameter;
import util.ServiceTools;
import util.bdTools.RequeteStatic;
import util.hibernate.model.Utilisateurs;

public class UtilisateurFonctions {
	
	/**
	 * Permet d'inscrire un utilisateur
	 * @param login le nom d'utilisateur
	 * @param mdp le mot de passe de l'utilisateur
	 * @param confirmationMdp la confirmation du mot de passe
	 * @param prenom le prenom de l'utilisateur
	 * @param nom le nom de l'utilisateur
	 * @param email l'adresse email de l'utilisateur
	 * @throws MyException lorsqu'il y une erreur
	 */
	public static void inscription(String login, String mdp, String confirmationMdp, String prenom, String nom, String email) 
			throws MyException {

		/* Verification des parametres */
		if(login == null || mdp == null || prenom == null || nom == null || email == null)
			throw new ParametreManquantException("Parametres(s) manquants(s)", ErrorCode.PARAMETRE_MANQUANT);

		if(login.equals("") || mdp.equals("") || prenom.equals("") || nom.equals("") || email.equals(""))
			throw new ParametreManquantException("Parametres(s) manquants(s)", ErrorCode.PARAMETRE_MANQUANT);
			
		if(mdp.length() < 6)
			throw new NonValideException("Mot de passe trop court", ErrorCode.PASSWORD_INVALIDE);
		
		if(!mdp.equals(confirmationMdp))
			throw new NonValideException("Confirmation du mot de passe different du mot de passe indique", ErrorCode.PASSWORD_CONFIRMATION_DIFFERENT);

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

	/**
	 * Permet de changer le mot de passe d'un utilisateur
	 * @param cle la cle session de l'utilisateur
	 * @param currentMdp le mot de passe courant de l'utilisateur
	 * @param newMdp le nouveau mot de passe 
	 * @param confirmationMdp la confirmation du mot de passe 
	 * @throws MyException lorsqu'il y a une erreur
	 */
	public static void changerMotDePasse(String cle, String currentMdp, String newMdp, String confirmationMdp) 
			throws MyException {

		/* Verifier les parametres */
		if(cle == null || currentMdp == null || confirmationMdp == null || newMdp == null)
			throw new ParametreManquantException("Parametres(s) manquant(s)", ErrorCode.PARAMETRE_MANQUANT);
		
		if(cle.equals("") || currentMdp.equals("") || confirmationMdp.equals("") || newMdp.equals(""))
			throw new ParametreManquantException("Parametres(s) manquant(s)", ErrorCode.PARAMETRE_MANQUANT);
		
		if (cle.length() != 32) 
			throw new NonValideException("Cle invalide", ErrorCode.CLE_INVALIDE);
		
		/* Verifier si le mot de passe contient au moins 6 caracteres */
		if(newMdp.length() < 6)
			throw new NonValideException("Mot de passe trop court", ErrorCode.PASSWORD_INVALIDE);
		
		/* Verifier si la personne a une session active */
		if(!ServiceTools.isCleActive(cle))
			throw new SessionExpireeException("Votre session a expiree", ErrorCode.SESSION_EXPIREE);
		
		Utilisateurs u = RequeteStatic.obtenirUtilisateur(RequeteStatic.obtenirIdSessionAvecCle(cle), null);
		if(!BCrypt.checkpw(currentMdp, u.getMdp()))
			throw new AuthenticationException("Mot de passe incorrect", ErrorCode.PASSWORD_INCORRECT);
		
		/* Verifier si le mot de passe est different de l'ancien */
		if(currentMdp.equals(newMdp))
			throw new AuthenticationException("Mot de passe identique a l'ancien", ErrorCode.PASSWORD_IDENTIQUE);

		/* Verifier si le nouveau mot de passe et la confirmation sont identiques */
		if(!newMdp.equals(confirmationMdp))
			throw new AuthenticationException("La confirmation n'est pas identique au nouveau mot de passe", ErrorCode.PASSWORD_CONFIRMATION_DIFFERENT);
		
		/* Changement du mdp */
		int id = RequeteStatic.obtenirIdSessionAvecCle(cle);
		if(id == -1)
			throw new exceptions.SQLException(ErrorCode.ERREUR_INTERNE, ErrorCode.SQL_EXCEPTION);
		
		RequeteStatic.changerMdpAvecId(id, newMdp);
	}

	/**
	 * Permet de changer le mail d'un utilisateur
	 * @param cle la cle session utilsiateur 
	 * @param newEmail la nouvelle adresse email de l'utilisateur
	 * @throws MyException lorsqu'il y a uen erreur
	 */
	public static void changerEmail(String cle, String newEmail) 
			throws MyException {
		
		if (cle == null || newEmail == null) 
			throw new ParametreManquantException("Parametres(s) manquants(s)", ErrorCode.PARAMETRE_MANQUANT);
		
		if (cle.equals("") || newEmail.equals("")) 
			throw new ParametreManquantException("Parametres(s) manquants(s)", ErrorCode.PARAMETRE_MANQUANT);
		
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
	 * Permet de connecter un utilisateur en lui creant une session
	 * @param login le nom d'utilisateur
	 * @param mdp le mot de passe
	 * @return JSONObject  contenant la reponse formatee 
	 * @throws MyException lorsqu'il y a une erreur
	 * @throws JSONException s'il y a eut une erreur a la creation du JSONObject 
	 */
	public static JSONObject connexion(String login, String mdp) 
			throws MyException, JSONException {
		
		//Verification parametre invalides
		if(login == null || mdp == null)
			throw new ParametreManquantException("Parametres(s) manquants(s)", ErrorCode.PARAMETRE_MANQUANT);
		
		if(login.equals("") || mdp.equals(""))
			throw new ParametreManquantException("Parametres(s) manquants(s)", ErrorCode.PARAMETRE_MANQUANT);
		
		if (mdp.length() < 6)
			throw new NonValideException("Mot de passe trop court", ErrorCode.PASSWORD_INVALIDE);
		
		//Verifier si lutilisateur existe
		if(RequeteStatic.checkIdentifiantsValide(login, mdp)){
			String cle;
			int id;
			if(RequeteStatic.isSessionCree(login)){ 			// Si Lutilisateur est connecte
				RequeteStatic.updateDateExpirationAvecLogin(login);
				cle = RequeteStatic.recupererTokenAvecLogin(login); 
			}else{
				cle = RequeteStatic.createSessionFromLogin(login);
			}
			
			id = RequeteStatic.obtenirIdAvecLogin(login);

			return new JSONObject()
					.put(RequestParameter.CLE, cle)
					.put(RequestParameter.ID, id);
		}
		else{// Identifiants invalide
			if(!RequeteStatic.isLoginDisponible(login)){
				throw new AuthenticationException("Mot de passe incorrect", ErrorCode.PASSWORD_INCORRECT);
			}else{
				throw new AuthenticationException("Utilisateur inexistant", ErrorCode.UTILISATEUR_INEXISTANT);
			}
		}
	}
	
	/**
	 * Permet de deconnecter un utilisateur en supprimant sa session
	 * de la base de donnee
	 * @param cle la cle de session utilisateur
	 * @throws MyException lorsqu'il y a une erreur
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
	 * Permet de savoir si un utilisateur est connecte
	 * @param cle la cle de session utilsiateur
	 * @return int l'id de l'utilisateur
	 * @throws MyException lorsqu'il y une erreur
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
	 * Permet à un utilisateur de récuperer son mot de passe perdu par mail
	 * @param email l'email de l'utilisateur
	 * @return string la chaine de caracteres contenant la confirmation d'envoi de mail
	 * @throws MyException lorsqu'il y une erreur
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
			throw new NonDisponibleException("Compte inexistant", ErrorCode.EMAIL_NON_DISPO);
		}
		return "Un email a ete envoye a l'adresse indiquee";
	}

	/**
	 * Genrere un mot de passe aléatoire
	 * @return la chaine de caractere contenant le mot de passe genere
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
