package services.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONException;
import org.json.JSONObject;

import services.UtilisateurServices;
import util.RequestParameter;

public class ChangerMdp extends HttpServlet {

	private static final long serialVersionUID = 6707840367155991458L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response){

		/* Lecture des parametres */
		String cle = StringEscapeUtils.escapeHtml3(request.getParameter(RequestParameter.CLE));
		String currentMdp = StringEscapeUtils.escapeHtml3(request.getParameter(RequestParameter.MOT_DE_PASSE));
		String newMdp = StringEscapeUtils.escapeHtml3(request.getParameter(RequestParameter.MOT_DE_PASSE_NEW));
		String confirmationMdp = StringEscapeUtils.escapeHtml3(request.getParameter(RequestParameter.MOT_DE_PASSE_CONFIRMATION));
		
		/* Traitement des services */
		try {
			JSONObject res = UtilisateurServices.changerMotDePasse(cle, currentMdp, newMdp, confirmationMdp);

			/* Ecriture de la reponse */
			PrintWriter writer = response.getWriter();
			response.setContentType("application/json");
			writer.println(res.toString());
			writer.close();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
