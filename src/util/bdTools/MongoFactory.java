package util.bdTools;

import java.net.UnknownHostException;
<<<<<<< HEAD
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClientException;
import com.mongodb.client.MongoCollection;
=======
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClientException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
>>>>>>> origin/AfficherProfil
import com.mongodb.client.MongoDatabase;

public class MongoFactory {

	private static final String AUTEUR = "auteur";
	private static final String INGREDIENTS = "ingredients";
	private static final String PREPARATION = "preparation";
	private static final String ID_AUTEUR = "idAuteur";
	private static final String LOGIN_AUTEUR = "loginAuteur";
	private static final String TITRE = "titre";
	private static final String NOTE = "note";
	private static final String NOTE_MOYENNE = "moyenne";
	private static final String NOMBRE_NOTE = "nbNotes";
	private static final String ID_USER = "idUser";
	private static final String ID_RECETTE = "idRecette"; 

	
	public static BasicDBObject creerDocumentRecette(String titre, int idAuteur, String loginAuteur, List<String> listeIng, List<String> prepa) throws MongoClientException, UnknownHostException{
		BasicDBObject document = new BasicDBObject(TITRE, titre);
		BasicDBObject auteur = creerDocumentAuteur(idAuteur, loginAuteur);
		document.append(AUTEUR, auteur);
		document.append(INGREDIENTS, listeIng);
		document.append(PREPARATION, prepa);
		BasicDBObject note = creerDocumentNote(0, 0);
		document.append(NOTE, note);
		
		return document;
	}
	
	public static BasicDBObject creerDocumentAuteur(int id, String login){
		BasicDBObject document = new BasicDBObject(ID_AUTEUR, id);
		document.append(LOGIN_AUTEUR, login);
		return document;
	}
	
	public static BasicDBObject creerDocumentNote(double moyenne, int nbNotes){
		BasicDBObject document = new BasicDBObject(NOTE_MOYENNE, moyenne);
		document.append(NOMBRE_NOTE, nbNotes);
		return document;
	}
	
//	public static BasicDBObject updateDejaNote(String id, String idRecette, List<String> recettesDejaNotees){
//		recetteDejaNotees.add(idRecette);
//		BasicDBObject
//		
//	}


	public static boolean isOwnerOfRecipe(int id_auteur, String login, String id_recette) throws MongoClientException, UnknownHostException {
		BasicDBObject document = new BasicDBObject(AUTEUR+"."+LOGIN_AUTEUR, login);
		document.append(AUTEUR+"."+ID_AUTEUR, id_auteur);
		document.put("_id", new ObjectId(id_recette));
		MongoDatabase database = DBStatic.getMongoConnection();
		MongoCollection<BasicDBObject> col = database.getCollection("Recettes", BasicDBObject.class);
		MongoCursor<BasicDBObject> cursor = col.find(document).iterator();
		return cursor.hasNext();
	}

	public static ArrayList<BasicDBObject> getRecettesFromLogin(String login) throws MongoClientException, UnknownHostException, JSONException {
		BasicDBObject document = new BasicDBObject(AUTEUR+"."+LOGIN_AUTEUR, login);
		MongoDatabase database = DBStatic.getMongoConnection();
		MongoCollection<BasicDBObject> col = database.getCollection("Recettes", BasicDBObject.class);
		
		ArrayList<BasicDBObject> list = new ArrayList<BasicDBObject>();
		for(BasicDBObject obj : col.find(document)){
			list.add(obj);
		}
		return list;
	}

}
