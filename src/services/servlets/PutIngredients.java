package services.servlets;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import services.IngredientsServices;

public class PutIngredients extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {
			/* Traitement du service */
			String path = "/var/lib/tomcat8/webapps";
			path += getServletContext().getContextPath();
			String fileName = path+File.separator+"ingredients.csv";
			JSONObject result = IngredientsServices.putListeIngredients(fileName);
			/* Ecriture de la reponse */
			response.setContentType("application/json");
			PrintWriter printWriter = response.getWriter();
			printWriter.println(result);
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
