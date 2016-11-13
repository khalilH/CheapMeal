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

public class Deconnexion extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		/* Recuperation des parametres */
		String cle = StringEscapeUtils.escapeHtml3(request.getParameter(RequestParameter.CLE));
		
		/* Traitement du service */
		try {
			JSONObject result = UtilisateurServices.deconnexion(cle);
			
			/* Ecriture de la reponse */
			PrintWriter writer = response.getWriter();
			response.setContentType("application/json");
			writer.println(result.toString());
			writer.close();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}

}
