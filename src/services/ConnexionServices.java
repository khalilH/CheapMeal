package services;

import java.sql.SQLException;

import org.hibernate.Session;
import org.json.JSONException;
import org.json.JSONObject;

import Util.ServiceTools;
import Util.Hibernate.HibernateUtil;
import Util.Hibernate.Model.Sessions;
import exceptions.InformationUtilisateurException;
import services.fonctions.ConnexionFonctions;

public class ConnexionServices {
	public static JSONObject connexion(String login, String mdp) throws JSONException {
		try {
			JSONObject result = ConnexionFonctions.Connexion(login,mdp);
			

			
			return result;
		} catch (NullPointerException npe) {
			return ServiceTools.serviceRefused(npe.getMessage(), -1);
		} catch (SQLException sqle) {
			return ServiceTools.serviceRefused(sqle.getMessage(), -1);
		} catch (InformationUtilisateurException e) {
			return ServiceTools.serviceRefused(e.getMessage(), -1);
		}
	}
}
