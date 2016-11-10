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
import util.RequestParameter;

public class AfficherProfil extends  HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public AfficherProfil() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/* Lecture des parametres */
		String cle = request.getParameter(RequestParameter.CLE);
		String login = request.getParameter(RequestParameter.LOGIN);
		try {
			/* Traitement des services */
			JSONObject res = ProfilServices.afficherProfil(cle, login);
			
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
