package services.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONException;
import org.json.JSONObject;

import services.RecetteServices;
import util.RequestParameter;

public class GetRecettesAccueil extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		/* Lecture des parametres */
		String cle = StringEscapeUtils.escapeHtml3(request.getParameter(RequestParameter.CLE));
		/* Traitement du service */
		try {
			JSONObject result = RecetteServices.getRecettesAccueil(cle);

			/* Ecriture de la reponse */
			response.setContentType("application/json");
			PrintWriter printWriter = response.getWriter();
			printWriter.println(result);
			printWriter.close();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}


}
