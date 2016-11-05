package services.fonctions;
import java.util.Random;

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
			String mdp = genererRandomMDP();
			RequeteStatic.changerMdpAvecId(RequeteStatic.obtenirIdAvecLogin(login), mdp);
			String body ="Hi,\n"
					+ "Vous avez initialiser une procedure de reinitialisation de mot de passe.\n"
					+ "Voici vos identifiants : \n"
					+ "Login : "+login+"\n"
					+ "Mot de passe provisoire: "+mdp+"\n\n"
					+ "Une fois connecte, n'oubliez pas de changer votre mot de passe."
					+ "Aurevoir et a bientot sur CheapMeal :)";
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
