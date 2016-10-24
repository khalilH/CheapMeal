package services.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import services.ProfilServices;

public class AfficherBio extends  HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public AfficherBio() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/* Lecture des parametres */
		String cle = request.getParameter("cle");
		String login = request.getParameter("login");
		
				
		try {
			/* Traitement des services */
			JSONObject res = ProfilServices.afficherBio(cle, login);
			
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
