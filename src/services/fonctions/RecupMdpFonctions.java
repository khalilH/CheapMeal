package services.fonctions;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.naming.NamingException;

import exceptions.InformationUtilisateurException;
import util.ServiceTools;
import util.bdTools.RequeteStatic;

public class RecupMdpFonctions {

	public static String recup(String email) throws InformationUtilisateurException, AddressException, NamingException, MessagingException {
		
		if(email == null)
			throw new NullPointerException("Email manquant");
		if(!ServiceTools.isEmailValide(email))
			throw new InformationUtilisateurException("Ne correspond pas a un email");
		
		if(!RequeteStatic.isEmailDisponible(email)){
			String login = RequeteStatic.obtenirLoginAvecMail(email);
			String mdp = RequeteStatic.obtenirMdpAvecLogin(login);
			String body ="Hi,\n"
					+ "Vous avez initialiser une procedure de recuperation de mot de passe.\n"
					+ "Voici vos identifiants : \n"
					+ "Login : "+login+"\n"
					+ "Mot de passe : "+mdp+"\n\n"
					+ "Aurevoir et a bientot sur CheapMeal :)";
			String subject = "Recuperation de mot de passe";
			ServiceTools.sendEmail(body, subject, email);
		}else{
			throw new InformationUtilisateurException("Ce mail n'existe pas");
		}
		
		
		return "Mot de passe recupere";
	}

}
