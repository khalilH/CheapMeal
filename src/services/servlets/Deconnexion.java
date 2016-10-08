package services.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import services.DeconnexionServices;

public class Deconnexion extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		/* Recuperation des parametres */
		String cle = request.getParameter("cle");
		
		/* Traitement du service */
		try {
			JSONObject result = DeconnexionServices.deconnexion(cle);
			
			/* Ecriture de la reponse */
			PrintWriter writer = response.getWriter();
			response.setContentType("application/json");
			writer.println(result.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		
	}

}
