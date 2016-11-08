package services.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import services.IngredientsServices;

public class GetIngredients extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		/* Lecture des parametres */
		String query = request.getParameter("query");
		
		/* Traitement du service */
		try {
			JSONObject result = IngredientsServices.getListeIngredients(query);

			/* Ecriture de la reponse */
			response.setContentType("application/json");
			PrintWriter printWriter = response.getWriter();
			printWriter.println(result);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
