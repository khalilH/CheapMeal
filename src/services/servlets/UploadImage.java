package services.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.json.JSONException;
import org.json.JSONObject;

import services.ProfilServices;
import util.ServiceTools;

@MultipartConfig

public class UploadImage extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public UploadImage() {
		super();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/* Lecture des parametres */
		try {
			String cle  = ServiceTools.getValueFromPart(request.getPart("cle"));
			String login  = ServiceTools.getValueFromPart(request.getPart("login"));
			Part photo = request.getPart("file");
			
		
			JSONObject res = ProfilServices.uploadImage(cle,login,photo);
			
			/* Ecriture de la reponse */
			PrintWriter writer = response.getWriter();
			response.setContentType("application/json");
			writer.println(res.toString());


		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
