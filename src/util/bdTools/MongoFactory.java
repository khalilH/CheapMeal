package util.bdTools;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.json.JSONException;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClientException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import exceptions.RecetteException;
import util.hibernate.model.Utilisateurs;

public class MongoFactory {

	public static final String AUTEUR = "auteur";
	public static final String INGREDIENTS = "ingredients";
	public static final String PREPARATION = "preparation";
	public static final String ID_AUTEUR = "idAuteur";
	public static final String LOGIN_AUTEUR = "loginAuteur";
	public static final String TITRE = "titre";
	public static final String NOTE = "note";
	public static final String NOTE_MOYENNE = "moyenne";
	public static final String NOMBRE_NOTE = "nbNotes";
	public static final String ID_USER = "idUser";
	public static final String ID_RECETTE = "idRecette"; 
	public static final String IDS_RECETTE = "idsRecette";
	public static final String NOM_INGREDIENT = "nomIngredient";
	public static final String QUANTITE = "quantite";
	public static final String MESURE = "mesure";
	public static final String EAN = "ean";
	public static final String DATE = "date";
	public static final String PRIX_AU_KG = "prix";
	
	
	public static final String COLLECTION_RECETTE = "Recettes";
	public static final String COLLECTION_INGREDIENTS= "ingredients";
	public static final String COLLECTION_UTILISATEUR_NOTES = "UtilisateurNotes";

	public static String getAuteur() {
		return AUTEUR;
	}

	public static String getIngredients() {
		return INGREDIENTS;
	}

	public static String getPreparation() {
		return PREPARATION;
	}

	public static String getIdAuteur() {
		return ID_AUTEUR;
	}

	public static String getLoginAuteur() {
		return LOGIN_AUTEUR;
	}

	public static String getTitre() {
		return TITRE;
	}

	public static String getNote() {
		return NOTE;
	}

	public static String getNoteMoyenne() {
		return NOTE_MOYENNE;
	}

	public static String getNombreNote() {
		return NOMBRE_NOTE;
	}

	public static String getIdUser() {
		return ID_USER;
	}

	public static String getIdRecette() {
		return ID_RECETTE;
	}

	public static String getIdsRecette(){
		return IDS_RECETTE;
	}

	public static BasicDBObject creerDocumentRecette(String titre, int idAuteur, String loginAuteur, List<String> listIng, List<Double> listQuant, List<String> listMesure, List<String> prepa) throws MongoClientException, UnknownHostException{
		Date d = new Date();
		BasicDBObject document = new BasicDBObject(TITRE, titre);
		BasicDBObject auteur = creerDocumentAuteur(idAuteur, loginAuteur);
		document.append(AUTEUR, auteur);
		ArrayList<BasicDBObject> ingredients = new ArrayList<>();
		for(int i=0 ; i<listIng.size(); i++)
			ingredients.add(creerDocumentIngredient(listIng.get(i), listQuant.get(i), listMesure.get(i)));
		document.append(INGREDIENTS, ingredients);
		document.append(PREPARATION, prepa);
		BasicDBObject note = creerDocumentNote(0, 0);
		document.append(NOTE, note);
		document.append(DATE, d.getTime());

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
	
	public static BasicDBObject creerDocumentIngredient(String nom, double quantite, String mesure){
		BasicDBObject document = new BasicDBObject(NOM_INGREDIENT, nom);
		document.append(QUANTITE, quantite);
		document.append(MESURE, mesure);
		return document;
	}
	
	/**
	 * Cree un document representant un ingredient, qui sera stocke dans la 
	 * collection ingredients
	 * @param nom le nom de l'ingredient
	 * @param ean le code barre de l'ingredient
	 * @param quantite la quantite (en g, cl, unite) du l'ingredient associe
	 * au code barre ean
	 * @return Objet JSON representant un ingredient
	 */
	public static BasicDBObject creerDocumentListeIngredient(String nom, String ean, double quantite) {
		return new BasicDBObject()
				.append(NOM_INGREDIENT, nom)
				.append(EAN, ean)
				.append(QUANTITE, quantite);
	}

	/**
	 * Cree un document representant un fruit ou un legume, qui sera stocke 
	 * dans la collection ingredients
	 * @param nom le nom du fruit ou legume
	 * @param le prix au kilo
	 * @param quantite la quantite en g 
	 * @return Objet JSON representant un fruit ou un legume
	 */
	public static BasicDBObject creerDocumentFruit(String nom, double prix, double quantite) {
		return new BasicDBObject().append(NOM_INGREDIENT, nom)
				.append(PRIX_AU_KG, prix)
				.append(QUANTITE, quantite);
	}

	//	public static BasicDBObject updateDejaNote(String id, String idRecette, List<String> recettesDejaNotees){
	//		recetteDejaNotees.add(idRecette);
	//		BasicDBObject
	//		
	//	}


	public static boolean isOwnerOfRecipe(int id_auteur, String login, String idRecette) throws MongoClientException, UnknownHostException {
		BasicDBObject document = new BasicDBObject(AUTEUR+"."+LOGIN_AUTEUR, login);
		document.append(AUTEUR+"."+ID_AUTEUR, id_auteur);
		document.put("_id", new ObjectId(idRecette));
		MongoDatabase database = DBStatic.getMongoConnection();
		MongoCollection<BasicDBObject> col = database.getCollection(COLLECTION_RECETTE, BasicDBObject.class);
		MongoCursor<BasicDBObject> cursor = col.find(document).iterator();
		return cursor.hasNext();
	}

	public static ArrayList<BasicDBObject> getRecettesFromLogin(String login) throws MongoClientException, UnknownHostException, JSONException {
		BasicDBObject document = new BasicDBObject(AUTEUR+"."+LOGIN_AUTEUR, login);
		MongoDatabase database = DBStatic.getMongoConnection();
		MongoCollection<BasicDBObject> col = database.getCollection(COLLECTION_RECETTE, BasicDBObject.class);

		ArrayList<BasicDBObject> list = new ArrayList<BasicDBObject>();
		for(BasicDBObject obj : col.find(document)){
			list.add(obj);
		}
		DBStatic.closeMongoDBConnection();
		return list;
	}
	
	public static boolean noterRecette(Utilisateurs u, String key, String idRecette, int note) throws RecetteException, MongoClientException, UnknownHostException{
		/* Notation de la recette */
		boolean res;
		MongoDatabase database = DBStatic.getMongoConnection();
		
		// Query qui recup la recette
		ObjectId _id = new ObjectId(idRecette); //idRecette au format hex
		BasicDBObject query = new BasicDBObject();
		query.put("_id", _id);

		// Connexion a la database Mongo
		database = DBStatic.getMongoConnection();
		MongoCollection<BasicDBObject> col = database.getCollection(COLLECTION_RECETTE, BasicDBObject.class);

		// Recuper la recette dans la bd 
		MongoCursor<BasicDBObject> cursor = col.find(query).iterator();

		BasicDBObject recette;
		if(cursor.hasNext())
			recette = cursor.next();
		else
			throw new RecetteException("Cette recette n'existe pas");

		if(aDejaNote(database, u, idRecette)){
			// Modifier la recette 
			BasicDBObject noteDoc = (BasicDBObject) recette.get(MongoFactory.NOTE);
			double moyenne = noteDoc.getDouble(MongoFactory.NOTE_MOYENNE);
			int nbNotes = noteDoc.getInt(MongoFactory.NOMBRE_NOTE) ;		
			double newMoyenne = (moyenne*nbNotes+note)/(nbNotes+1);
			noteDoc.replace(MongoFactory.NOTE_MOYENNE, newMoyenne);
			noteDoc.replace(MongoFactory.NOMBRE_NOTE, nbNotes+1);
			recette.replace(MongoFactory.NOTE, noteDoc);

			BasicDBObject tmp = new BasicDBObject();
			tmp.put("$set", recette);
			col.updateOne(query, tmp);
			res = true;
		}else{
			res = false;
		}
		
		DBStatic.closeMongoDBConnection();
		return res;
		
	}

	public static void majNotationRecette(Utilisateurs u, String idRecette) throws MongoClientException, UnknownHostException{
		/* Dire que l'utilisateur a noté la recette (ajout dans la collection Mongo */
		MongoDatabase database = DBStatic.getMongoConnection();
		MongoCollection<BasicDBObject> col = database.getCollection(COLLECTION_UTILISATEUR_NOTES, BasicDBObject.class);
		BasicDBObject query = new BasicDBObject();
		query.put(MongoFactory.LOGIN_AUTEUR, u.getLogin());
		MongoCursor<BasicDBObject> cursor = col.find(query).iterator();
		BasicDBObject doc;
		if(cursor.hasNext()){
			// Si le doc correspondant a l'utilisateur existe, mettre a jour les ids
			doc = cursor.next();
			ArrayList<ObjectId> list = (ArrayList<ObjectId>) doc.get(MongoFactory.IDS_RECETTE);
			list.add(new ObjectId(idRecette));
			doc.replace(IDS_RECETTE, list);

			BasicDBObject tmp = new BasicDBObject();
			tmp.put("$set", doc);
			col.updateOne(query, doc);
		}else{
			// Si le doc n'existe pas, l'inserer
			doc = new BasicDBObject(ID_USER, u.getId());
			doc.append(IDS_RECETTE, new ArrayList<>());
			col.insertOne(doc);
		}
		DBStatic.closeMongoDBConnection();
	}
	
	public static boolean aDejaNote(MongoDatabase database, Utilisateurs u, String idRecette){
		MongoCollection<BasicDBObject> col = database.getCollection(COLLECTION_UTILISATEUR_NOTES, BasicDBObject.class);
		BasicDBObject query = new BasicDBObject();
		query.put(MongoFactory.LOGIN_AUTEUR, u.getLogin()); 
		MongoCursor<BasicDBObject> cursor = col.find(query).iterator();
		BasicDBObject doc;
		if(cursor.hasNext()){
			doc = cursor.next();
			ObjectId oid = new ObjectId(idRecette);
			ArrayList<ObjectId> list = (ArrayList<ObjectId>) doc.get(IDS_RECETTE);
			if(list.contains(oid))
				return true;
			else
				return false;
		}else{
			return false;
		}
	}


}
