package services;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.naming.NamingException;

import org.json.JSONException;
import org.json.JSONObject;

import exceptions.InformationUtilisateurException;
import services.fonctions.RecupMdpFonctions;
import util.ServiceTools;

public class RecupMdpServices {
	public static JSONObject recupMdp(String email) throws JSONException {
		try {
			String result = RecupMdpFonctions.recup(email);
			return ServiceTools.serviceAccepted(result);
			
		} catch (NullPointerException npe) {
			return ServiceTools.serviceRefused(npe.getMessage(), -1);
		} catch (InformationUtilisateurException e) {
			return ServiceTools.serviceRefused(e.getMessage(), -1);
		} catch (AddressException e) {
			return ServiceTools.serviceRefused(e.getMessage(), -1);
		} catch (NamingException e) {
			return ServiceTools.serviceRefused(e.getMessage(), -1);
		} catch (MessagingException e) {
			return ServiceTools.serviceRefused(e.getMessage(), -1);
		}
	}
	
}
