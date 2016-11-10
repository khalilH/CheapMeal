package services.fonctions;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import exceptions.FichierNonTrouveException;
import exceptions.MyException;
import exceptions.ParametreManquantException;
import util.ErrorCode;
import util.bdTools.DBStatic;
import util.bdTools.MongoFactory;

public class IngredientsFonctions {

	/**
	 * Permet de recuperer la liste ingredients utilisables matchant la query
	 * @param query de l'autocomplete
	 * @return la liste des ingredients utilisables sous forme de String
	 * @throws MyException 
	 */
	public static ArrayList<String> getListeIngredients(String query) 
			throws MyException {

		if (query == null || query.equals(""))
			throw new ParametreManquantException("Parametre manquant", ErrorCode.PARAMETRE_MANQUANT);

		MongoDatabase database;
		try {
			database = DBStatic.getMongoConnection();
		} catch (Exception e) {
			throw new exceptions.MongoDBException(ErrorCode.ERREUR_INTERNE, ErrorCode.MONGO_EXCEPTION);
		}
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
	 * @throws MyException 
	 */
	//TODO essayer GoogleDrive API
	public static void putListeIngredients(String fileName) 
			throws MyException {

		MongoDatabase database;
		try {
			database = DBStatic.getMongoConnection();
		} catch (Exception e) {
			throw new exceptions.MongoDBException(ErrorCode.ERREUR_INTERNE, ErrorCode.MONGO_EXCEPTION);
		}
		MongoCollection<BasicDBObject> collection = database.getCollection(
				MongoFactory.COLLECTION_INGREDIENTS, BasicDBObject.class);
		collection.deleteMany(new BasicDBObject());

		try {
			Scanner scanner = new Scanner(new File(fileName));
			scanner.nextLine(); 
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] tab = line.split(",");
				BasicDBObject document;
				String nomIngredient = tab[1];
				double quantite = Double.parseDouble(tab[3]);
				if (tab[0].equals("i")) {
					String ean = tab[2];
					document = MongoFactory.creerDocumentListeIngredient(nomIngredient, ean, quantite);
				}
				else {
					double prix = Double.parseDouble(tab[2]);
					document = MongoFactory.creerDocumentFruit(nomIngredient, prix, quantite);
				}
				collection.insertOne(document);
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			throw new FichierNonTrouveException(ErrorCode.ERREUR_INTERNE, ErrorCode.FILE_NOT_FOUND_EXCEPTION);
		}

		DBStatic.closeMongoDBConnection();
	}
	
	public static String getIngredient(String nomIngredient) throws MyException {
		MongoDatabase database;
		try {
			database = DBStatic.getMongoConnection();
		} catch (Exception e) {
			throw new exceptions.MongoDBException(ErrorCode.ERREUR_INTERNE, ErrorCode.MONGO_EXCEPTION);
		}
		MongoCollection<BasicDBObject> collection = database.getCollection(
				MongoFactory.COLLECTION_INGREDIENTS, BasicDBObject.class);
		
		BasicDBObject o = new BasicDBObject(MongoFactory.NOM_INGREDIENT, nomIngredient);
		List<BasicDBObject> ingredients = (List<BasicDBObject>) collection.find(o);
		return null;
	}

}
