package services.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import services.RecetteService;

public class NoterRecette extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException{
		
		/* Lecture des parametres */
		String key = request.getParameter("cle");
		String idRecette = request.getParameter("idRecette");
		String noteStr = request.getParameter("note");
		
		int note;
		if(noteStr != null)
			note = Integer.parseInt(noteStr);
		else
			note = -1;

		try {
			JSONObject res = RecetteService.noterRecette(key, idRecette, note);
			
			/* Ecriture de la reponse */
			PrintWriter writer = response.getWriter();
			response.setContentType("application/json");
			writer.println(res.toString());
			
		} catch (JSONException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
		
	
	}
	
}
