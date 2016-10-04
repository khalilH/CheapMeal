package services.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import services.UtilisateurServices;

public class Inscription extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response){
		
		/* Lecture des parametres */
		String login = request.getParameter("login");
		String mdp = request.getParameter("mdp");
		String prenom = request.getParameter("prenom");
		String nom = request.getParameter("nom");
		String email = request.getParameter("email");
		
		/* Traitement des services */
		
		try {
			JSONObject res = UtilisateurServices.inscription(login, mdp, prenom, nom, email);
			
			/* Ecriture de la reponse */
			PrintWriter writer = response.getWriter();
			response.setContentType("application/json");
			writer.println(res.toString());
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
