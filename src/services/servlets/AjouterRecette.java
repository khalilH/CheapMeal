package services.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import services.RecetteService;

public class AjouterRecette extends HttpServlet {

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		/* Lecture des parametres */
		String titre = request.getParameter("titre");
		String key = request.getParameter("cle");
		String ingr = request.getParameter("ingredients");
		String quant = request.getParameter("quantites");
		String mesu = request.getParameter("mesures");
		String prepa = request.getParameter("preparation");

		/* Ici, parser la liste des ingredients et des etapes de preparation */
		
		ArrayList<String> ingredients = new ArrayList<String>();
		ArrayList<Double> quantites = new ArrayList<Double>();
		ArrayList<String> mesures = new ArrayList<String>();
		ArrayList<String> preparation = new ArrayList<String>();
		
		String[] ingr_tmp = ingr.split(",");
		String[] quantites_tmp = quant.split(",");
		String[] mesures_tmp = mesu.split(",");
		String[] prepa_tmp = prepa.split(",");

		for(String s : ingr_tmp)
			ingredients.add(s);

		for(String s : quantites_tmp)
			quantites.add(Double.parseDouble(s));
		
		for(String s : mesures_tmp)
			mesures.add(s);
		
		for(String s : prepa_tmp)
			preparation.add(s);
		
		try {
			/* Traitement des services */
			JSONObject res = RecetteService.ajouterRecette(titre, key, ingredients, quantites, mesures, preparation);
			
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

