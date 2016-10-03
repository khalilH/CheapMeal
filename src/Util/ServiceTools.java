package Util;

import java.sql.SQLException;

import javax.mail.MessagingException;

import org.json.JSONException;
import org.json.JSONObject;

public class ServiceTools {
	public static JSONObject serviceRefused(String message,int codeErreur){
		JSONObject j=new JSONObject();
		try{
			switch(codeErreur){
			case(-1) :
				j.put("Erreur",message);
			break;
			case(100) :
				j.put("Erreur",message);
			break;
			case(1000) :
				j.put("Erreur",message);
				break;
			case(10000) :
				j.put("Erreur","Erreur serveur mail");
			break;

			default:
				j.put("Erreur", "Erreur inconnue, contactez l'administrateur du service.");
			}
		}catch(JSONException e){
			e.printStackTrace();
		}
		return j;
	}
	public static JSONObject serviceAccepted(String mess){
		JSONObject j=new JSONObject();
		try{
			j.put("Accepted",mess);
		}catch(JSONException e){
			e.printStackTrace();
		}
		return j;
	}

	public static JSONObject GestionDesErreur(Exception e){
		JSONObject jb=null;
		if(e instanceof MessagingException){
			jb=ServiceTools.serviceRefused(""+e.toString(),10000);
		}else{
			if(e instanceof InstantiationException || e instanceof IllegalAccessException || e instanceof ClassNotFoundException || e instanceof SQLException){
				jb=ServiceTools.serviceRefused(""+e.toString(),1000);
			}else{
				if(e instanceof JSONException){
					jb=ServiceTools.serviceRefused(""+e.toString(),100);
				}
			}
		}	
		jb=ServiceTools.serviceRefused(""+e.toString(),100);
		return jb;
	}
}
