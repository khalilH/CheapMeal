package services;

import java.io.IOException;

import javax.servlet.http.Part;

import org.json.JSONException;
import org.json.JSONObject;

import exceptions.MyException;
import services.fonctions.ProfilFonctions;
import util.ServiceTools;

public class ProfilServices {


	/**
	 * Permet de faire appel aux fonction permettant d'ajouter ou de mettre a
	 * jour la biographie d'un utilisateur
	 * @param cle la cle de session de l' utilisateur
	 * @param bio la biographie de l'utilisateur
	 * @return JSONObject contenant "success" ou "error" avec un message d'erreur
	 * @throws JSONException : s'il y a eut une erreur a la creation ou manipulation du JSONObject 
	 */
	public static JSONObject ajouterBio(String cle, String bio) throws JSONException{
		try {
			ProfilFonctions.ajouterBio(cle, bio);
			return ServiceTools.serviceAccepted("Biographie modifiee");
		} catch (MyException e) {
			return ServiceTools.serviceRefused(e.getMessage(), e.getCode());
		}

	}

	/**
	 *  Permet d'affiche le profil d'un utilisateur
	 * @param cle de l'utilisateur
	 * @param login de la personne dont on souhaite afficher le profil
	 * @return
	 * @throws JSONException
	 */
	public static JSONObject afficherProfil(String cle,String login) throws JSONException{
		try {
			JSONObject jb = ProfilFonctions.afficherProfil(cle, login);
			return jb;
		} catch (MyException e) {
			return ServiceTools.serviceRefused(e.getMessage(), e.getCode());
		}

	}
	
	public static JSONObject uploadImage(String cle ,String login, Part photo) throws JSONException {
		try {
			ProfilFonctions.uploadImage(cle,login, photo);
			return ServiceTools.serviceAccepted("Image ajouté");
		} catch (MyException e) {
			return ServiceTools.serviceRefused(e.getMessage(), e.getCode());
		} catch (IOException e) {
			return ServiceTools.serviceRefused(e.getMessage(), 100);
			//TODO Change code
		}
	}
}
