package services.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import services.UtilisateurServices;

public class ChangerMdp extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response){

		/* Lecture des parametres */
		String cle = request.getParameter("cle");
		String oldMdp = request.getParameter("oldMdp");
		String newMdp = request.getParameter("newMdp");
		
		/* Traitement des services */
		try {
			JSONObject res = UtilisateurServices.changerMotDePasse(cle, oldMdp, newMdp);

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
