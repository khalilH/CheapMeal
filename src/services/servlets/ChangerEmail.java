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

/**
 * Servlet implementation class ChangerEmail
 */
public class ChangerEmail extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ChangerEmail() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		/* lecture des parametres */
		String cle = request.getParameter(RequestParameter.CLE);
		String newEmail = request.getParameter(RequestParameter.EMAIL_NEW);
		
		/* Traitement des services */
		try {
			JSONObject res = UtilisateurServices.changerEmail(cle, newEmail);
			
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
