package services.fonctions;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.json.JSONException;
import org.json.JSONObject;


import Util.DBStatic;


public class ConnexionService {
	public static JSONObject Connexion(String id, String mdp) throws JSONException{
		JSONObject jb= new JSONObject();
		Connection c=null;
		ResultSet r=null;
		Statement st=null;
		
		//Verification parametre invalides
		if(id == null || mdp == null){
			jb.put("Refuse", "Identifiants non fournis");
			return jb;
		}
		if(id == "" || mdp == "" || mdp.length() == 0){
			jb.put("Refuse", "Identifiants incorrects");
			return jb;
		}
		
//		c=DBStatic.getMyConnection();
//		st=DBStatic.connexionBD(c);
		
		
		
		//Verifier si lutilisateur existe
		// r = RequeteStatic.VerifyOwner(st, id, mdp);
//		if(r.first()){
//			r.close();
//			r = RequeteStatic.VerifySessionOpen(st,id);
			// Si Lutilisateur est connecte
//				idlogin=r.getInt("idlogin");
//				st.executeUpdate("update session set date_exp='"+newtime+"', clef='"+clef+"' where login='"+id+"'");
//				jb=ServiceTools.serviceAccepted("User session time has been updated");
//				jb.put("key",clef);
//				jb.put("id",idlogin);
//				jb.put("login",i);
		
			// Si Lutilisateur nest pas connecte 
	//			jb=ServiceTools.serviceAccepted("You are logged in for 30mn");
	//			idlogin=r.getInt("id");
	//			jb.put("key",clef);
	//			jb.put("id",idlogin);
	//			jb.put("login",id);
	//			st.executeUpdate("insert into session values('"+id+"','"+newtime+"','"+clef+"','"+idlogin+"')");
//		}else{
//			jb=ServiceTools.serviceRefused("Verifiez votre mot de passe ou votre login", -1);
//		}
		
		
		//Donner un jeton dauthentification
		return jb;
	}
}
