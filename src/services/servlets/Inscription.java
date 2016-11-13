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

public class Inscription extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response){
		
		/* Lecture des parametres */
		String login = StringEscapeUtils.escapeHtml3(request.getParameter(RequestParameter.LOGIN));
		String mdp = StringEscapeUtils.escapeHtml3(request.getParameter(RequestParameter.MOT_DE_PASSE));
		String confirmationMdp = StringEscapeUtils.escapeHtml3(request.getParameter(RequestParameter.MOT_DE_PASSE_CONFIRMATION));
		String prenom = StringEscapeUtils.escapeHtml3(request.getParameter(RequestParameter.PRENOM));
		String nom = StringEscapeUtils.escapeHtml3(request.getParameter(RequestParameter.NOM));
		String email = StringEscapeUtils.escapeHtml3(request.getParameter(RequestParameter.EMAIL));
		
		/* Traitement des services */
		try {
			JSONObject res = UtilisateurServices.inscription(login, mdp, confirmationMdp, prenom, nom, email);
			
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
