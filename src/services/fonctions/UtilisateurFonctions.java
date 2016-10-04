package services.fonctions;

import org.json.JSONObject;

public class UtilisateurFonctions {

	public void inscription(String login, String mdp, String prenom, String nom, String email) throws Exception{

		/* Verification des parametres */
		if(login == null || mdp == null || prenom == null || nom == null || email == null)
			throw new Exception();

		if(login == "" || mdp == "" || prenom == "" || nom == "" || email == "")
			throw new Exception();

		if(mdp.length() < 6)
			throw new Exception();
		
		//Ici verifier si le login est available
		
		//Ici verifier si l'email est available
		
		/* Creation de l'utilisateur dans la base SQL */
		
		
	}

}
