package util;

import java.sql.Timestamp;
import java.util.Properties;
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

import org.json.JSONException;
import org.json.JSONObject;

import util.hibernate.HibernateUtil;

public class ServiceTools {
	public static JSONObject serviceRefused(String message, int codeErreur) throws JSONException{
		JSONObject res = new JSONObject();
		res.put("erreur", codeErreur);
		res.put("message", message);
		return res;
	}
	
	public static JSONObject serviceAccepted(String message) throws JSONException{
		return new JSONObject().put("success", message);
	}
	public static boolean isEmailValide(String email){
		Pattern pattern = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9]+.[a-zA-Z]{2,3}$");
		Matcher m = pattern.matcher(email);
		return m.matches();
	}
	
	public static void sendEmail(String body, String subject, String email) throws NamingException, AddressException, MessagingException{
//		Context initCtx = new InitialContext();
//		Context envCtx = (Context) initCtx.lookup("java:comp/env");
//		//Configurations de la session. Tomcat peut instancier des objets sessions edja configurees pour se connecter a un serveur SMTP
//		Object ses = envCtx.lookup("mail/Session");
//		Session session = null;		
//		if (ses instanceof Session) {
//			 session = (Session) ses;
//			
//		}
		
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

//	public static JSONObject GestionDesErreur(Exception e){
//		JSONObject jb=null;
//		if(e instanceof MessagingException){
//			jb=ServiceTools.serviceRefused(""+e.toString(),10000);
//		}else{
//			if(e instanceof InstantiationException || e instanceof IllegalAccessException || e instanceof ClassNotFoundException || e instanceof SQLException){
//				jb=ServiceTools.serviceRefused(""+e.toString(),1000);
//			}else{
//				if(e instanceof JSONException){
//					jb=ServiceTools.serviceRefused(""+e.toString(),100);
//				}
//			}
//		}	
//		jb=ServiceTools.serviceRefused(""+e.toString(),100);
//		return jb;
//	}
	
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
	 * Permet de verifier si une cle de session est toujours active
	 * @param cle la cle de session a verifier
	 * @return true si la cle de session est active
	 */
	public static boolean isCleActive(String cle) {
		Timestamp dateExpiration = getDateExpirationAvecCle(cle),
				currentTime = new Timestamp(System.currentTimeMillis());
		
		return dateExpiration != null && currentTime.before(dateExpiration);
	}
	
	/**
	 * Permet d'obtenir la date d'expiration d'une cle
	 * @param cle
	 * @return le timestamp de la date d'expiration
	 */
	public static Timestamp getDateExpirationAvecCle(String cle)  {
		org.hibernate.Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		s.beginTransaction();
		Timestamp s_timestamp=(Timestamp) s.createQuery("Select s.dateExpiration from Sessions s where s.cleSession = :cleSession")
					.setParameter("cleSession", cle)
					.uniqueResult();
		s.getTransaction().commit();
		return s_timestamp;
	}

}
