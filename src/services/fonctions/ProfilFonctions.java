package services.fonctions;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.BasicDBObject;

import exceptions.MyException;
import exceptions.NonDisponibleException;
import exceptions.NonValideException;
import exceptions.ParametreManquantException;
import exceptions.SessionExpireeException;
import util.ErrorCode;
import util.ServiceTools;
import util.bdTools.RequeteStatic;

public class ProfilFonctions {

	/**
	 * Permet de creer ou de mettre a jour la biographie de l'utilisateur
	 * @param cle la cle de session
	 * @param bio la biographie
	 * @throws MyException
	 */
	public static void ajouterBio(String cle, String bio) throws MyException {
		if (cle == null || bio == null || cle.equals("") || bio.equals("")) 
			throw new ParametreManquantException("Parametres(s) Manquant(s)", ErrorCode.PARAMETRE_MANQUANT);

		if (cle.length() != 32)
			throw new NonValideException("Cle invalide", ErrorCode.CLE_INVALIDE);

		if (bio.length() > 250) {
			throw new NonValideException("Biographie trop longue", ErrorCode.BIO_INVALIDE);
		}

		if (!ServiceTools.isCleActive(cle)) 
			throw new SessionExpireeException("Votre session a expiree", ErrorCode.SESSION_EXPIREE);

		int id = RequeteStatic.obtenirIdSessionAvecCle(cle);
		if (id == -1)
			throw new exceptions.SQLException(ErrorCode.ERREUR_INTERNE, ErrorCode.SQL_EXCEPTION);

		RequeteStatic.ajouterBioProfil(id, bio);
	}

	/**
	 * 
	 * @param cle
	 * @param login
	 * @return
	 * @throws MyException
	 * @throws JSONException 
	 */
	public static JSONObject afficherProfil(String cle, String login) throws MyException, JSONException  {
		if (cle == null || login == null || login.equals("") || login.equals("")) 
			throw new ParametreManquantException("Parametres(s) Manquant(s)", ErrorCode.PARAMETRE_MANQUANT);

		if (cle.length() != 32)
			throw new NonValideException("Cle invalide", ErrorCode.CLE_INVALIDE);

		if (!ServiceTools.isCleActive(cle)) 
			throw new SessionExpireeException("Votre session a expiree", ErrorCode.SESSION_EXPIREE);

		if(RequeteStatic.isLoginDisponible(login))
			throw new NonDisponibleException("Ce nom d'utilisateur est deja utilise", ErrorCode.LOGIN_NON_DISPO);

		JSONObject jb = new JSONObject();
		String bio = RequeteStatic.recupBio(login);
		jb.put("Bio",bio );
		ArrayList<BasicDBObject> recettes;
		try {
			recettes = RecetteFonctions.getRecettesFromLogin(login);
		} catch (Exception e) {
			throw new exceptions.MongoDBException(ErrorCode.ERREUR_INTERNE, ErrorCode.MONGO_EXCEPTION);
		}
		jb.put("recettes",recettes);
		jb.put("Login", login);
		return jb;
	}

}

