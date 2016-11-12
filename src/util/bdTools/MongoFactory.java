package util.bdTools;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.FileImageOutputStream;
import javax.servlet.http.Part;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;

/**
 * 
 * @author khalil
 *
 */
public class MongoFactory {

	public static final String _ID = "_id";
	public static final String AUTEUR = "auteur";
	public static final String INGREDIENTS = "ingredients";
	public static final String PREPARATION = "preparation";
	public static final String ID_AUTEUR = "idAuteur";
	public static final String LOGIN_AUTEUR = "loginAuteur";
	public static final String TITRE = "titre";
	public static final String NOTE = "note";
	public static final String NOTE_MOYENNE = "moyenne";
	public static final String NOMBRE_NOTE = "nbNotes";
	public static final String USERS_NOTES = "usersNotes"; /* Pour les objets contenant {"idUser":1,"userNote":5} */
	public static final String USER_NOTE = "userNote";
	public static final String ID_USER = "idUser";
	public static final String ID_RECETTE = "idRecette"; 
	public static final String IDS_RECETTE = "idsRecette";
	public static final String NOM_INGREDIENT = "nomIngredient";
	public static final String QUANTITE = "quantite";
	public static final String MESURE = "mesure";
	public static final String EAN = "ean";
	public static final String DATE = "date";
	public static final String PRIX_AU_KG = "prix";
	public static final String PHOTO = "photo";

	public static final String DATABASE_NAME = "CheapMeal";
	
	public static final String COLLECTION_RECETTE = "Recettes";
	public static final String COLLECTION_INGREDIENTS= "ingredients";
	public static final String COLLECTION_UTILISATEUR_NOTES = "UtilisateurNotes";

	/**
	 * Creer un document contenant les informations relatives a une recette
	 * @param titre le titre de la recette
	 * @param idAuteur identifiant de l'auteur de la recette
	 * @param loginAuteur login du l'auteur de la recette
	 * @param listIng la liste des ingredients 
	 * @param quantites la liste des quantites correspondant aux ingredients de la recette
	 * @param mesures la liste des mesures utilises pour exprimer les quantites
	 * @param preparation la liste des etapes de preparation
	 * @param photo la phote de la recette
	 * @return BasicDBObject l'objet contenant toutes les informations de la recette
	 * @throws IOException lorsqu'il y a eut une erreur lors de la lecture ou de l'ecriture du fichier de photo
	 */
	public static BasicDBObject creerDocumentRecette(String titre, int idAuteur, String loginAuteur, 
			List<String> listIng, 
			List<Double> quantites, 
			List<String> mesures, 
			List<String> preparation, 
			Part photo) throws IOException {
		
		Date d = new Date();
		BasicDBObject document = new BasicDBObject(TITRE, titre);
		BasicDBObject auteur = creerDocumentAuteur(idAuteur, loginAuteur);
		document.append(AUTEUR, auteur);
		ArrayList<BasicDBObject> ingredients = new ArrayList<>();
		for(int i=0 ; i<listIng.size(); i++)
			ingredients.add(creerDocumentIngredient(listIng.get(i), quantites.get(i), mesures.get(i)));
		document.append(INGREDIENTS, ingredients);
		document.append(PREPARATION, preparation);
		BasicDBObject note = creerDocumentNote(0, 0);
		document.append(NOTE, note);
		document.append(DATE, d.getTime());
		
		if(photo !=null){
			InputStream inputStream = photo.getInputStream();
			BufferedImage image = ImageIO.read(inputStream);
			ObjectId oid = new ObjectId().get();
			document.put("_id", oid);
			JPEGImageWriteParam jpegParams = new JPEGImageWriteParam(null);
			jpegParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			jpegParams.setCompressionQuality(0.6f);
			final ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
			writer.setOutput(new FileImageOutputStream(
			  new File("/var/lib/tomcat8/webapps/images/"+oid.toString()+".png")));
			writer.write(null, new IIOImage(image, null, null), jpegParams);
			document.append(PHOTO,oid.toString());
		}
		return document;
	}

	/**
	 * Creer un document correspondant a l'auteur d'une recette
	 * @param idAuteur l'id de l'auteur
	 * @param login le nom d'utilisateur de l'auteur
	 * @return BasicDBObject l'objet contenant les informations relatives a l'auteur
	 */
	public static BasicDBObject creerDocumentAuteur(int idAuteur, String login){
		BasicDBObject document = new BasicDBObject(ID_AUTEUR, idAuteur);
		document.append(LOGIN_AUTEUR, login);
		return document;
	}

	/**
	 * Creer un document contenant les informations relatives a la note d'une recette 
	 * @param moyenne la note moyenne de la recette
	 * @param nbNotes le nombre de notes donne a la recette
	 * @return BasicDBObject l'objet contenant les informations relatives a la note d'une recette
	 */
	public static BasicDBObject creerDocumentNote(double moyenne, int nbNotes){
		BasicDBObject document = new BasicDBObject(NOTE_MOYENNE, moyenne);
		document.append(NOMBRE_NOTE, nbNotes);
		document.append(USERS_NOTES, new ArrayList<Integer>());
		return document;
	}
	
	/**
	 * Creer un document contenant les informations relatives a un ingredient dans une recette
	 * @param nomIngredient le nom de l'ingredient
	 * @param quantite la quantite associee
	 * @param mesure la mesure associee a la quantite
	 * @return BasicDBObject l'objet contenant les informations relatives a un ingredient d'une recette
	 */
	public static BasicDBObject creerDocumentIngredient(String nomIngredient, double quantite, String mesure){
		BasicDBObject document = new BasicDBObject(NOM_INGREDIENT, nomIngredient);
		document.append(QUANTITE, quantite);
		document.append(MESURE, mesure);
		return document;
	}
	
	/**
	 * Cree un document representant un ingredient, qui sera stocke dans la 
	 * collection ingredients
	 * @param nomIngredient le nom de l'ingredient
	 * @param ean le code barre de l'ingredient
	 * @param quantite la quantite (en g, cl, unite) du l'ingredient associe
	 * au code barre ean
	 * @return Objet JSON representant un ingredient
	 */
	public static BasicDBObject creerDocumentListeIngredient(String nomIngredient, String ean, double quantite) {
		return new BasicDBObject()
				.append(NOM_INGREDIENT, nomIngredient)
				.append(EAN, ean)
				.append(QUANTITE, quantite);
	}

	/**
	 * Cree un document representant un fruit ou un legume, qui sera stocke 
	 * dans la collection ingredients
	 * @param nomIngredient le nom du fruit ou legume
	 * @param prixAuKg le prix au kilo
	 * @param quantite la quantite en g 
	 * @return Objet JSON representant un fruit ou un legume
	 */
	public static BasicDBObject creerDocumentFruit(String nomIngredient, double prixAuKg, double quantite) {
		return new BasicDBObject().append(NOM_INGREDIENT, nomIngredient)
				.append(PRIX_AU_KG, prixAuKg)
				.append(QUANTITE, quantite);
	}


}
