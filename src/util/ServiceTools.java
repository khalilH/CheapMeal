package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.json.JSONException;
import org.json.JSONObject;

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
		Context initCtx = new InitialContext();
		Context envCtx = (Context) initCtx.lookup("java:comp/env");
		//Configurations de la session. Tomcat peut instancier des objets sessions edja configurees pour se connecter a un serveur SMTP
		Object ses = envCtx.lookup("mail/Session");
		Session session = null;		
		if (ses instanceof Session) {
			 session = (Session) ses;
			
		}
		
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
}
