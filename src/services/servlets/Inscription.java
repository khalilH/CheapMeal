package services.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import services.UtilisateurServices;
import util.RequestParameter;

public class Inscription extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response){
		
		/* Lecture des parametres */
		String login = request.getParameter(RequestParameter.LOGIN);
		String mdp = request.getParameter(RequestParameter.MOT_DE_PASSE);
		String prenom = request.getParameter(RequestParameter.PRENOM);
		String nom = request.getParameter(RequestParameter.NOM);
		String email = request.getParameter(RequestParameter.EMAIL);
		
		/* Traitement des services */
		try {
			JSONObject res = UtilisateurServices.inscription(login, mdp, prenom, nom, email);
			
			/* Ecriture de la reponse */
			PrintWriter writer = response.getWriter();
			response.setContentType("application/json");
			writer.println(res.toString());
			
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
