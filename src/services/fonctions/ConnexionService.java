package services.fonctions;


import org.json.JSONException;
import org.json.JSONObject;

import Util.ServiceTools;

public class ConnexionService {
	public static JSONObject Connexion(String id, String mdp){
		JSONObject jb= new JSONObject();
		try{
			if(id == null || mdp == null){
				jb.put("Refuse", "Identifiants non fournis");
			}else{
				// Utilisation de BD
			}
		}catch ( JSONException e) {
			jb = ServiceTools.GestionDesErreur(e);
		}
		return jb;
	}
}
