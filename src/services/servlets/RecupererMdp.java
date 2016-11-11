package services.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import services.UtilisateurServices;
import util.RequestParameter;

public class RecupererMdp  extends HttpServlet{
	
	
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			
			/* Recuperation des parametres */
			String email = request.getParameter(RequestParameter.EMAIL);
	
			/* Traitement du service */
			try {
				JSONObject result = UtilisateurServices.recupererMdp(email);
				
				/* Ecriture de la reponse */
				PrintWriter writer = response.getWriter();
				response.setContentType("application/json");
				writer.println(result.toString());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
	}

}