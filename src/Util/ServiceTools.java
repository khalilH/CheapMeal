package Util;

import org.json.JSONException;
import org.json.JSONObject;

public class ServiceTools {
	public static JSONObject serviceRefused(String message, int codeErreur) throws JSONException{
		JSONObject res = new JSONObject();
		res.put("erreur", codeErreur);
		res.put("message", message);
		return res;
	}
	
	public static JSONObject serviceAccepted() throws JSONException{
		return new JSONObject().put("JSON", "Success");
	}
	
	public static JSONObject serviceAccepted(String message) throws JSONException{
		return new JSONObject().put(message, "Success");
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
