package services.fonctions;


import org.json.JSONException;
import org.json.JSONObject;

import Util.ServiceTools;

public class ConnexionService {
	public static JSONObject Connexion(String id, String mdp){
		JSONObject jb= new JSONObject();
		
		//Verification parametre invalides
		if(id == null || mdp == null){
			jb.put("Refuse", "Identifiants non fournis");
			return jb;
		}
		if(id="" || mdp = "" || mdp.length() == 0){
			jb.put("Refuse", "Identifiants incorrects");
		}
		return jb;
	}
}
