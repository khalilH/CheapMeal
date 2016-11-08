package services.fonctions;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClientException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import util.bdTools.DBStatic;
import util.bdTools.MongoFactory;

public class IngredientsFonctions {

	/**
	 * Permet de recuperer la liste ingredients utilisables matchant la query
	 * @param query de l'autocomplete
	 * @return la liste des ingredients utilisables sous forme de String
	 * @throws MongoClientException
	 * @throws UnknownHostException
	 * @throws NullPointerException query non fournie
	 */
	public static ArrayList<String> getListeIngredients(String query) 
			throws MongoClientException, UnknownHostException, 
			NullPointerException {
		
		if (query == null || query.equals(""))
			throw new NullPointerException("le champ query ne doit pas etre vide");
		
		MongoDatabase database = DBStatic.getMongoConnection();
		MongoCollection<BasicDBObject> collection = database
				.getCollection(MongoFactory.COLLECTION_INGREDIENTS, BasicDBObject.class);
		
		BasicDBObject o = new BasicDBObject();
		o.put(MongoFactory.NOM_INGREDIENT, Pattern.compile(query,Pattern.CASE_INSENSITIVE));
		
		ArrayList<String> list = new ArrayList<>();
		for (BasicDBObject ingredient : collection.find(o)) {
			list.add(ingredient.getString(MongoFactory.NOM_INGREDIENT));
		}
		DBStatic.closeMongoDBConnection();
		return list;
	}
	
	/**
	 * Permet de reconstruire la liste des ingredients utilisables et de les stocker
	 * dans la collection ingredients dans MongoDB
	 * @param fileName le fichier contenant la liste des ingredients
	 * @throws MongoClientException
	 * @throws UnknownHostException
	 * @throws FileNotFoundException
	 */
	//TODO essayer GoogleDrive API
	public static void putListeIngredients(String fileName) throws MongoClientException, UnknownHostException, FileNotFoundException {
		MongoDatabase database = DBStatic.getMongoConnection();
		MongoCollection<BasicDBObject> collection = database.getCollection(
				MongoFactory.COLLECTION_INGREDIENTS, BasicDBObject.class);
		collection.deleteMany(new BasicDBObject());
		
		Scanner scanner = new Scanner(new File(fileName));
		scanner.nextLine(); 
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			String[] tab = line.split(",");
			BasicDBObject document;
			if (tab[0].equals("i")) {
				document = MongoFactory.creerDocumentListeIngredient(tab[1], tab[2], Double.parseDouble(tab[3]));
			}
			else {
				document = MongoFactory.creerDocumentFruit(tab[1], Double.parseDouble(tab[2]), Double.parseDouble(tab[3]));
			}
			collection.insertOne(document);
		}
		scanner.close();
		DBStatic.closeMongoDBConnection();
	}
	
}
