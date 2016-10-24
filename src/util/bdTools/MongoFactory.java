package util.bdTools;

import java.net.UnknownHostException;
import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClientException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class MongoFactory {

	private static String AUTEUR = "auteur";
	private static String INGREDIENTS = "ingredients";
	private static String PREPARATION = "preparation";
	private static String ID_AUTEUR = "idAuteur";
	private static String LOGIN_AUTEUR = "loginAuteur";
	private static String TITRE = "titre";
	
	public static BasicDBObject creerDocumentRecette(String titre, int idAuteur, String loginAuteur, List<String> listeIng, List<String> prepa) throws MongoClientException, UnknownHostException{
		BasicDBObject document = new BasicDBObject(TITRE, titre);
		BasicDBObject auteur = creerDocumentAuteur(idAuteur, loginAuteur);
		document.append(AUTEUR, auteur);
		document.append(INGREDIENTS, listeIng);
		document.append(PREPARATION, prepa);
		
		return document;
	}
	
	public static BasicDBObject creerDocumentAuteur(int id, String login){
		BasicDBObject document = new BasicDBObject(ID_AUTEUR, id);
		document.append(LOGIN_AUTEUR, login);
		return document;
	}

	public static boolean isOwnerOfRecipe(int id_auteur, String login, String id_recette) throws MongoClientException, UnknownHostException {
		BasicDBObject document = new BasicDBObject(AUTEUR+"."+LOGIN_AUTEUR, login);
		document.append(AUTEUR+"."+ID_AUTEUR, id_auteur);
		document.put("_id", new ObjectId(id_recette));
		MongoDatabase database = DBStatic.getMongoConnection();
		MongoCollection<BasicDBObject> col = database.getCollection("Recettes", BasicDBObject.class);
		MongoCursor<BasicDBObject> cursor = col.find(document).iterator();
		BasicDBObject obj = cursor.next();
		return obj!=null;
	}

}
