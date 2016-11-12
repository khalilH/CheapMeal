package util;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Properties;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.NamingException;
import javax.servlet.http.Part;

import org.json.JSONException;
import org.json.JSONObject;

import util.bdTools.RequeteStatic;
import util.hibernate.HibernateUtil;

/**
 * 
 * @author khalil
 *
 */
public class ServiceTools {
	
	/**
	 * Message de refus lorsqu'il ya erreur dans un service
	 * @param message le message associe a l'erreur
	 * @param codeErreur le code d'erreur associe
	 * @return JSONObject contenant les informationss relatives a l'erreur
	 * @throws JSONException s'il y a eut une erreur lors de la creation ou de la manipulation de l'objet JSON
	 */
	public static JSONObject serviceRefused(String message, int codeErreur) throws JSONException{
		JSONObject res = new JSONObject();
		res.put("erreur", codeErreur);
		res.put("message", message);
		return res;
	}
	
	/**
	 * Message de validation lorsqu'un service ofnctionne
	 * @param message le message associe
	 * @return JSONObject contenant le message de confirmation
	 * @throws JSONException s'il y a eut une erreur lors de la creation ou de la manipulation de l'objet JSON
	 */
	public static JSONObject serviceAccepted(String message) throws JSONException{
		return new JSONObject().put("success", message);
	}
	
	/**
	 * 
	 * @param json l'objet JSON contenant les informations liees a la conf
	 * @return JSONObject contenant les informations liees a la confirmation
	 * @throws JSONException s'il y a eut une erreur lors de la creation ou de la manipulation de l'objet JSON
	 */
	public static JSONObject serviceAccepted(JSONObject json) throws JSONException{
		return new JSONObject().put("success", json);
	}
	
	/**
	 * Regex permettant de savoir si le parametre est un mail
	 * @param email l'adresse email a verifier
	 * @return boolean true si le format de l'email est valide, false sinon
	 */
	public static boolean isEmailValide(String email){
		Pattern pattern = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9]+.[a-zA-Z]{2,3}$");
		Matcher m = pattern.matcher(email);
		return m.matches();
	}
	
	/**
	 * Permet d'envoyer un email a un utilisateur
	 * @param body le corps du mail
	 * @param subject le sujet du mail
	 * @param email l'adresse email destinataire
	 * @throws NamingException
	 * @throws AddressException lorsque l'adresse n'est pas valide
	 * @throws MessagingException lorsqu'il y a eut une erreur d'envoi de mail
	 */
	public static void sendEmail(String body, String subject, String email) throws NamingException, AddressException, MessagingException{
	    Properties properties = new Properties();
	    properties.setProperty("mail.smtp.host", "smtp-mail.outlook.com");
	    properties.put("mail.smtp.starttls.enable","true");
	    properties.put("mail.smtp.auth", "true");
	    properties.put("mail.transport.protocol", "smtp");
	    Session session = Session.getInstance(properties,new Authenticator(
	    		) {
	    	protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("ladips3@hotmail.fr", "3eppvps3");
            }
		});

		//Message a envoyer
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress("ladips3@hotmail.fr"));
		InternetAddress to[] = new InternetAddress[1];
		to[0] = new InternetAddress(email);
		message.setRecipients(Message.RecipientType.TO, to);
		message.setSubject("CHEAPMEAL : "+subject);
		message.setContent(body, "text/plain");
		Transport.send(message);
	}

	/**
	 * Permet de récupérer les parametre d'un formulaire encode en multipart
	 * @throws MessagingException lorsqu'il y a eut une erreur d'envoi de mail
	 * @throws IOException lorsqu'il y a eut une erreur a la lecture du fichier
	 */
	
	public static String getValueFromPart(Part p) throws IOException, MessagingException{
		Scanner c = new Scanner(p.getInputStream());
		String ret = c.nextLine();
		while(c.hasNextLine())
			ret+= c.nextLine()+" ";
		c.close();
		return ret;
	}
	/**
	 * Code de generation aleatoire d'une cle de session de 32 caracteres
	 * @return une cle aleatoire
	 */
	public static String createKey(){
		double aux1, aux2;
		int sc;
		int codeAscii;
		String res = "";
		for(int i=0; i<32; i++){
			aux1 = Math.random()*3;
			sc = (int) aux1;
			switch(sc){
			case 0: 
				//Chiffre (code Ascii entre 48 et 57)
				aux2 = Math.random()*10+48;
				codeAscii = (int) aux2;
				res += (char)codeAscii;
				break;
			case 1:
				//Lettre majuscule (code Ascii entre 65 et 90)
				aux2 = Math.random()*26+65;
				codeAscii = (int) aux2;
				res += (char)codeAscii;
				break;
			case 2:
				//Lettre minuscule (code ascii entre 97 et 122)
				aux2 = Math.random()*26+97;
				codeAscii = (int) aux2;
				res += (char)codeAscii;
				break;
			}			
		}
		return res;
	}
	
	/**
	 * Permet de verifier si une cle de session est toujours active et la met a jour si elle l'est
	 * @param cle la cle de session a verifier
	 * @return true si la cle de session est active
	 */
	public static boolean isCleActive(String cle) {
		Timestamp dateExpiration = getDateExpirationAvecCle(cle),
				currentTime = new Timestamp(System.currentTimeMillis());
		boolean res = dateExpiration != null && currentTime.before(dateExpiration);
		if(res)
			RequeteStatic.updateDateExpirationAvecCle(cle);
		return res;
	}
	
	/**
	 * Permet d'obtenir la date d'expiration d'une cle
	 * @param cle la cle session utilisateur
	 * @return le timestamp de la date d'expiration
	 */
	public static Timestamp getDateExpirationAvecCle(String cle)  {
		org.hibernate.Session s = HibernateUtil.getSessionFactory().openSession();
		s.beginTransaction();
		Timestamp s_timestamp=(Timestamp) s.createQuery("Select s.dateExpiration from Sessions s where s.cleSession = :cleSession")
					.setParameter("cleSession", cle)
					.uniqueResult();
		s.getTransaction().commit();
		s.close();
		return s_timestamp;
	}

}
