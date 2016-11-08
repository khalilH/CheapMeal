package services.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import services.UtilisateurServices;
import util.RequestParameter;

public class ChangerMdp extends HttpServlet {

	private static final long serialVersionUID = 6707840367155991458L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response){

		/* Lecture des parametres */
		String cle = request.getParameter(RequestParameter.CLE);
		String oldMdp = request.getParameter(RequestParameter.MOT_DE_PASSE_OLD);
		String newMdp = request.getParameter(RequestParameter.MOT_DE_PASSE_NEW);
		
		/* Traitement des services */
		try {
			JSONObject res = UtilisateurServices.changerMotDePasse(cle, oldMdp, newMdp);

			/* Ecriture de la reponse */
			PrintWriter writer = response.getWriter();
			response.setContentType("application/json");
			writer.println(res.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
