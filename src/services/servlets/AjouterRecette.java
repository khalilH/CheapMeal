package services.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.json.JSONException;
import org.json.JSONObject;

import services.RecetteServices;
import util.RequestParameter;
import util.ServiceTools;
@MultipartConfig
public class AjouterRecette extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {
			/* Lecture des parametres */
			String titre = ServiceTools.getValueFromPart(request.getPart(RequestParameter.TITRE));
			String cle = ServiceTools.getValueFromPart(request.getPart(RequestParameter.CLE));
			String ingr = ServiceTools.getValueFromPart(request.getPart(RequestParameter.INGREDIENTS));
			String quant = ServiceTools.getValueFromPart(request.getPart(RequestParameter.QUANTITES));
			String mesu = ServiceTools.getValueFromPart(request.getPart(RequestParameter.MESURES));
			String preparation = ServiceTools.getValueFromPart(request.getPart(RequestParameter.PREPARATION));
			Part photo = request.getPart(RequestParameter.FILE);

			/* Ici, parser la liste des ingredients et des etapes de preparation */

			ArrayList<String> ingredients = new ArrayList<String>();
			ArrayList<Double> quantites = new ArrayList<Double>();
			ArrayList<String> mesures = new ArrayList<String>();

			String[] ingr_tmp = ingr.split(",");
			String[] quantites_tmp = quant.split(",");
			String[] mesures_tmp = mesu.split(",");

			for(String s : ingr_tmp)
				ingredients.add(s);

			for(String s : quantites_tmp)
				quantites.add(Double.parseDouble(s));

			for(String s : mesures_tmp)
				mesures.add(s);

			/* Traitement des services */
			JSONObject res = RecetteServices.ajouterRecette(titre, cle, ingredients, quantites, mesures, preparation, photo);

			/* Ecriture de la reponse */
			PrintWriter writer = response.getWriter();
			response.setContentType("application/json");
			writer.println(res.toString());
		} catch (JSONException e) {
			PrintWriter writer = response.getWriter();
			response.setContentType("text/plain");
			writer.println(e.toString());
		} catch (IOException e) {
			PrintWriter writer = response.getWriter();
			response.setContentType("text/plain");
			writer.println(e.toString());
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

}

