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
	 * Permet de recuperer les ingredients matchant la query
	 * @param query de l'autocomplete
	 * @return une liste de {"value":"ingredients", "data":"data_value"}
	 * data_value est le code barre EAN, ou le prix d'un fruit ou legume
	 * @throws MongoClientException
	 * @throws UnknownHostException
	 * @throws NullPointerException query non fournie
	 */
	public static ArrayList<BasicDBObject> getListeIngredients(String query) 
			throws MongoClientException, UnknownHostException, 
			NullPointerException {
		
		if (query == null || query.equals(""))
			throw new NullPointerException("le champ query ne doit pas etre vide");
		
		MongoDatabase database = DBStatic.getMongoConnection();
		MongoCollection<BasicDBObject> col = database
				.getCollection(MongoFactory.COLLECTION_INGREDIENTS, BasicDBObject.class);
		
		BasicDBObject o = new BasicDBObject();
		o.put(MongoFactory.NOM_INGREDIENT, Pattern.compile(query));
		
		ArrayList<BasicDBObject> list = new ArrayList<>();
		for (BasicDBObject ingredient : col.find(o)) {
			BasicDBObject suggestion = new BasicDBObject();
			if (ingredient.containsField(MongoFactory.EAN)) {
				suggestion.put("data", ingredient.getString(MongoFactory.EAN));
			}
			else if (ingredient.containsField(MongoFactory.PRIX_AU_KG)){
				suggestion.put("data", ingredient.getString(MongoFactory.PRIX_AU_KG));
			}
			suggestion.put("value", ingredient.getString(MongoFactory.NOM_INGREDIENT));
			list.add(suggestion);
		}
		DBStatic.closeMongoDBConnection();
		return list;
	}

	public static ArrayList<String> putListeIngredients(String fileName) throws MongoClientException, UnknownHostException, FileNotFoundException {
		MongoDatabase database = DBStatic.getMongoConnection();
		MongoCollection<BasicDBObject> col = database.getCollection(
				MongoFactory.COLLECTION_INGREDIENTS, BasicDBObject.class);
		col.deleteMany(new BasicDBObject());
		ArrayList<String> list = new ArrayList<>();
		Scanner scanner = new Scanner(new File(fileName));
		scanner.nextLine(); // Pour tej la premiere ligne
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			String[] tab = line.split(",");
			BasicDBObject document;
			if (tab[0].equals("i")) {
				document = MongoFactory.creerDocumentListeIngredient(tab[1], tab[2]);
			}
			else {
				document = MongoFactory.creerDocumentFruit(tab[1], Double.parseDouble(tab[2]));
			}
			list.add(line);
			col.insertOne(document);
		}
		scanner.close();
		DBStatic.closeMongoDBConnection();
		return list;
	}
	
}
