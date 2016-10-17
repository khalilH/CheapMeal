package services.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import services.RecupMdpServices;

public class RecupMdp  extends HttpServlet{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//TODO Recup le mdp crypte et le decrypte cote client avec Bcrypt
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			
			/* Recuperation des parametres */
			String email = request.getParameter("mail");
	
			
			/* Traitement du service */
			try {
				JSONObject result = RecupMdpServices.recupMdp(email);
				
				/* Ecriture de la reponse */
				PrintWriter writer = response.getWriter();
				response.setContentType("application/json");
				writer.println(result.toString());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
	}


}