package services.fonctions;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.FileImageOutputStream;
import javax.servlet.http.Part;

import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.BasicDBObject;

import exceptions.MyException;
import exceptions.NonDisponibleException;
import exceptions.NonValideException;
import exceptions.ParametreManquantException;
import exceptions.SessionExpireeException;
import util.ErrorCode;
import util.ServiceTools;
import util.bdTools.RequeteStatic;

public class ProfilFonctions {

	/**
	 * Permet de creer ou de mettre a jour la biographie de l'utilisateur
	 * 
	 * @param cle
	 *            la cle de session
	 * @param bio
	 *            la biographie
	 * @throws MyException
	 */
	public static void ajouterBio(String cle, String bio) throws MyException {
		if (cle == null || bio == null || cle.equals("") || bio.equals(""))
			throw new ParametreManquantException("Parametres(s) Manquant(s)", ErrorCode.PARAMETRE_MANQUANT);

		if (cle.length() != 32)
			throw new NonValideException("Cle invalide", ErrorCode.CLE_INVALIDE);

		if (bio.length() > 250) {
			throw new NonValideException("Biographie trop longue", ErrorCode.BIO_INVALIDE);
		}

		if (!ServiceTools.isCleActive(cle))
			throw new SessionExpireeException("Votre session a expiree", ErrorCode.SESSION_EXPIREE);

		int id = RequeteStatic.obtenirIdSessionAvecCle(cle);
		if (id == -1)
			throw new exceptions.SQLException(ErrorCode.ERREUR_INTERNE, ErrorCode.SQL_EXCEPTION);

		RequeteStatic.ajouterBioProfil(id, bio);
	}

	/**
	 * Recupere les informations d'un utilisateur
	 * @param cle
	 * @param login
	 * @return
	 * @throws MyException
	 * @throws JSONException
	 */
	public static JSONObject afficherProfil(String cle, String login) throws MyException, JSONException {
		if (login == null || login.equals(""))
			throw new ParametreManquantException("Parametres(s) Manquant(s)", ErrorCode.PARAMETRE_MANQUANT);

		if (cle != null) {
			if (cle.length() != 32)
				throw new NonValideException("Cle invalide", ErrorCode.CLE_INVALIDE);

			if (!ServiceTools.isCleActive(cle))
				throw new SessionExpireeException("Votre session a expiree", ErrorCode.SESSION_EXPIREE);
		}
		if (RequeteStatic.isLoginDisponible(login))
			throw new NonDisponibleException("Ce nom d'utilisateur n'existe pas", ErrorCode.LOGIN_NON_DISPO);

		JSONObject jb = new JSONObject();
		JSONObject profil = new JSONObject();
		String bio = RequeteStatic.recupBio(login);
		profil.put("bio", bio);
		profil.put("login", login);
		File f = new File("/var/lib/tomcat8/webapps/images/profil/" + login + ".png");
		if (f.exists())
			profil.put("photo", login);
		else
			profil.put("photo", "genericImage");
		ArrayList<BasicDBObject> recettes;
		try {
			recettes = RecetteFonctions.getRecettesFromLogin(login);
		} catch (Exception e) {
			throw new exceptions.MongoDBException(ErrorCode.ERREUR_INTERNE, ErrorCode.MONGO_EXCEPTION);
		}
		jb.put("recettes", recettes);
		jb.put("profil", profil);
		return jb;
	}
	/**
	 * Permet d'ajouter une photo de profil à un utilisateur
	 * @param cle
	 * @param login
	 * @param photo
	 * @throws ParametreManquantException
	 * @throws NonValideException
	 * @throws SessionExpireeException
	 * @throws IOException
	 */
	public static void uploadImage(String cle, String login, Part photo)
			throws ParametreManquantException, NonValideException, SessionExpireeException, IOException {
		if (cle == null || photo == null || login == null || login.equals("") || cle.equals(""))
			throw new ParametreManquantException("Parametres(s) Manquant(s)", ErrorCode.PARAMETRE_MANQUANT);

		if (cle.length() != 32)
			throw new NonValideException("Cle invalide", ErrorCode.CLE_INVALIDE);

		if (!ServiceTools.isCleActive(cle))
			throw new SessionExpireeException("Votre session a expiree", ErrorCode.SESSION_EXPIREE);

		InputStream inputStream = photo.getInputStream();
		BufferedImage image = ImageIO.read(inputStream);
		JPEGImageWriteParam jpegParams = new JPEGImageWriteParam(null);
		jpegParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		jpegParams.setCompressionQuality(0.6f);
		final ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
		writer.setOutput(new FileImageOutputStream(
		  new File("/var/lib/tomcat8/webapps/images/profil/" + login + ".png")));
		writer.write(null, new IIOImage(image, null, null), jpegParams);
	}

}
