package services.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import services.RecetteServices;
import util.RequestParameter;

public class SupprimerRecette extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		/* Lecture des parametres */
		String idRecette = request.getParameter(RequestParameter.ID_RECETTE);
		String cle = request.getParameter(RequestParameter.CLE);

		try {
			/* Traitement des services */
			JSONObject res = RecetteServices.supprimerRecette(idRecette,cle);

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
