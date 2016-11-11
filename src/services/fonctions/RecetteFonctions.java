package services.fonctions;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Part;

import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClientException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import exceptions.MongoDBException;
import exceptions.MyException;
import exceptions.NonValideException;
import exceptions.ParametreManquantException;
import exceptions.RecetteException;
import exceptions.SessionExpireeException;
import util.ErrorCode;
import util.ExternalAPI;
import util.ServiceTools;
import util.bdTools.DBStatic;
import util.bdTools.MongoFactory;
import util.bdTools.RequeteStatic;
import util.hibernate.model.Sessions;
import util.hibernate.model.Utilisateurs;

public class RecetteFonctions {
	/**
	 * Recupere les 9 recette les plus recentes
	 * @param col la collection Mongo dans laquelle les recettes sont stockees
	 * @return ArrayList<BasicDBObject> la liste dont chaque element est un objet representant une recette
	 */
	public static ArrayList<BasicDBObject> getRecentRecipes(MongoCollection<BasicDBObject> col){
		ArrayList<BasicDBObject> list = new ArrayList<>();
		BasicDBObject sortQuery = new BasicDBObject("date",-1);
		MongoCursor<BasicDBObject> cursor = col.find().sort(sortQuery).limit(9).iterator();
		while(cursor.hasNext()){
			BasicDBObject obj = cursor.next();
			ObjectId oid = obj.getObjectId("_id");
			obj.replace("_id", oid.toString());
			list.add(obj);
		}
		return list;
	}
	/**
	 * Recupere les 9 recettes les mieux notes
	 * @param col la collection Mongo dans laquelle les recettes sont stockees 
	 * @return ArrayList<BasicDBObject> la liste dont chaque element est un objet representant une recette
	 */
	public static ArrayList<BasicDBObject> getBestRecipes(MongoCollection<BasicDBObject> col){
		ArrayList<BasicDBObject> list = new ArrayList<>();
		BasicDBObject sortQuery = new BasicDBObject(MongoFactory.NOTE+"."+MongoFactory.NOTE_MOYENNE,-1);

		MongoCursor<BasicDBObject> cursor = col.find().sort(sortQuery).limit(9).iterator();
		while(cursor.hasNext()){
			BasicDBObject obj = cursor.next();
			ObjectId oid = obj.getObjectId("_id");
			obj.replace("_id", oid.toString());
			list.add(obj);
		}
		return list;
	}
	/**
	 * Permet de recuperer les 9 recette les mieux notées et les 9 plus récentes
	 * @param cle la cle session utilisateur
	 * @return JSONObject contenant les recettes a afficher sur la page d'accueil
	 * @throws JSONException s'il y a eut une erreur a la creation du JSONObject 
	 * @throws NonValideException si la cle session n'a pas un format valide 
	 * @throws SessionExpireeException si la session utilisateur a expire
	 * @throws MongoDBException s'il y eut une lors de l'acces a la base Mongo
	 */
	public static JSONObject getRecettesAccueil(String cle) throws JSONException, NonValideException, SessionExpireeException, MongoDBException{
		if (cle != null) {
			if (cle.length() != 32)
				throw new NonValideException("Cle invalide", ErrorCode.CLE_INVALIDE);

			if (!ServiceTools.isCleActive(cle))
				throw new SessionExpireeException("Votre session a expiree", ErrorCode.SESSION_EXPIREE);
		}

		JSONObject jb = new JSONObject();
		MongoDatabase database;
		try {
			database = DBStatic.getMongoConnection();
		} catch (Exception e) {
			throw new MongoDBException(ErrorCode.ERREUR_INTERNE, ErrorCode.MONGO_EXCEPTION);
		}

		MongoCollection<BasicDBObject> col = database.getCollection(MongoFactory.COLLECTION_RECETTE, BasicDBObject.class);
		JSONArray recentRecipes = new JSONArray(getRecentRecipes(col));
		JSONArray bestRecipes = new JSONArray(getBestRecipes(col));
		jb.put("recettesRecentes", recentRecipes);
		jb.put("recettesBest", bestRecipes);
		DBStatic.closeMongoDBConnection();
		return jb;

	}


	/**
	 * Permet d'obtenir l'objet Json correspondant a la recette dont l'id est en parametre 
	 * @param id id de la recette
	 * @param cle la cle session utilisateur
	 * @return JSONObject contenant les informations de la recette
	 * @throws MyException lorsqu'il y a eut une erreur
	 * @throws JSONException s'il y a eut une erreur a la creation du JSONObject 
	 */
	public static JSONObject afficherRecette(String id, String cle) throws MyException, JSONException {
		if(id == null)
			throw new ParametreManquantException("Parametre(s) manquant(s)", ErrorCode.PARAMETRE_MANQUANT);
		if(id.equals(""))
			throw new NonValideException("Parametre non valide", ErrorCode.RECETTE_ID_INVALIDE);
		if (cle != null) {
			if (cle.length() != 32)
				throw new NonValideException("Cle invalide", ErrorCode.CLE_INVALIDE);

			if (!ServiceTools.isCleActive(cle))
				throw new SessionExpireeException("Votre session a expiree", ErrorCode.SESSION_EXPIREE);
		}
		BasicDBObject query = new BasicDBObject("_id", new ObjectId(id));
		MongoDatabase database;
		try {
			database = DBStatic.getMongoConnection();
		} catch (Exception e) {
			throw new MongoDBException(ErrorCode.ERREUR_INTERNE, ErrorCode.MONGO_EXCEPTION);
		}

		MongoCollection<BasicDBObject> col = database.getCollection(MongoFactory.COLLECTION_RECETTE, BasicDBObject.class);
		MongoCursor<BasicDBObject> cursor = col.find(query).iterator();
		BasicDBObject res = null;
		if(cursor.hasNext())
			res = cursor.next();
		res.put("prix", 0.0);
		return new JSONObject(res);
	}

	/**
	 * Permet d'obtenir le prix d'une recette
	 * @param id
	 * @param cle
	 * @return
	 * @throws MyException
	 * @throws JSONException
	 */
	public static double afficherPrixRecette(String id, String cle) throws MyException, JSONException {
		if(id == null)
			throw new ParametreManquantException("Parametre(s) manquant(s)", ErrorCode.PARAMETRE_MANQUANT);
		if(id.equals(""))
			throw new NonValideException("Parametre non valide", ErrorCode.RECETTE_ID_INVALIDE);
		if (cle != null) {
			if (cle.length() != 32)
				throw new NonValideException("Cle invalide", ErrorCode.CLE_INVALIDE);

			if (!ServiceTools.isCleActive(cle))
				throw new SessionExpireeException("Votre session a expiree", ErrorCode.SESSION_EXPIREE);
		}
		BasicDBObject query = new BasicDBObject("_id", new ObjectId(id));
		MongoDatabase database;
		try {
			database = DBStatic.getMongoConnection();
		} catch (Exception e) {
			throw new MongoDBException(ErrorCode.ERREUR_INTERNE, ErrorCode.MONGO_EXCEPTION);
		}

		MongoCollection<BasicDBObject> col = database.getCollection(MongoFactory.COLLECTION_RECETTE, BasicDBObject.class);
		MongoCursor<BasicDBObject> cursor = col.find(query).iterator();
		BasicDBObject res = null;
		if(cursor.hasNext())
			res = cursor.next();

		double prix = 0.0;
		if(res != null){
			BasicDBList ing = (BasicDBList) res.get(MongoFactory.INGREDIENTS);

			for (int i = 0; i<ing.size(); i++) {
				BasicDBObject o = (BasicDBObject) ing.get(i);
				JSONObject ref = IngredientsFonctions.getIngredient(o.getString(MongoFactory.NOM_INGREDIENT));
				if (ref.has(MongoFactory.PRIX_AU_KG)) {
					prix += o.getDouble(MongoFactory.QUANTITE)*ref.getDouble(MongoFactory.PRIX_AU_KG)/ref.getDouble(MongoFactory.QUANTITE);
				}
				else {
					try {
						double prixAPI = ExternalAPI.searchPrices(ref.getString(MongoFactory.EAN)).get(0);
						prix += o.getDouble(MongoFactory.QUANTITE)*prixAPI/ref.getDouble(MongoFactory.QUANTITE);
					} catch (UnirestException e) {
						throw new MyException("Erreu appel API", 666);
					}
				}
			}
		}

		return prix;
	}

	

	/**
	 * Permet d'jaouter une recette
	 * @param titre le titre de la recette
	 * @param cle la cle session utilisateur
	 * @param ingredients la liste des ingredients 
	 * @param quantites la liste des quantites correspondant aux ingredients de la recette
	 * @param mesures la liste des mesures utilises pour exprimer les quantites
	 * @param preparation la liste des etapes de preparation
	 * @throws MyException lorsqu'il y aeut une erreur
	 */
	public static void ajouterRecette(String titre, String cle, 
			List<String> ingredients, 
			List<Double> quantites, 
			List<String> mesures,
			List<String> preparation, 
			Part photo) throws MyException {

		/* Verification des parametres */
		if (titre == null || cle == null || ingredients == null || quantites == null || preparation == null || photo == null)
			throw new ParametreManquantException("Parametres(s) Manquant(s)", ErrorCode.PARAMETRE_MANQUANT);

		/* Mise en place de la liste des mesures compatibles */
		ArrayList<String> listMesures = new ArrayList<>();
		listMesures.add("kg");
		listMesures.add("g");
		listMesures.add("unite(s)");
		listMesures.add("l");
		listMesures.add("cl");
		listMesures.add("ml");

		if (titre.equals("") 
				|| preparation.size() == 0
				|| ingredients.size() == 0
				|| mesures.size() == 0
				|| quantites.size() == 0
				|| ingredients.stream().filter(i -> i.length() == 0).count() > 0 
				|| quantites.stream().filter(i -> i<=0).count() > 0 
				|| mesures.stream().filter(i -> !listMesures.contains(i)).count() > 0 
				|| ingredients.size() != mesures.size() 
				|| ingredients.size() != quantites.size() 
				|| mesures.size() != quantites.size())
			throw new ParametreManquantException("Parametres(s) Manquant(s)", ErrorCode.PARAMETRE_MANQUANT);

		if (cle.length() != 32)
			throw new NonValideException("Cle invalide", ErrorCode.CLE_INVALIDE);

		if (!ServiceTools.isCleActive(cle))
			throw new SessionExpireeException("Votre session a expiree", ErrorCode.SESSION_EXPIREE);

		/* Ajout de la recette */
		Sessions s = RequeteStatic.obtenirSession(cle);
		Utilisateurs u = RequeteStatic.obtenirUtilisateur(s.getIdSession(), null);

		try {
			MongoDatabase database = DBStatic.getMongoConnection();
			MongoCollection<BasicDBObject> col = database.getCollection("Recettes", BasicDBObject.class);

			BasicDBObject document = MongoFactory.creerDocumentRecette(titre, u.getId(), u.getLogin(), ingredients, quantites, mesures, preparation,photo);
			col.insertOne(document);
		} catch (Exception e) {
			throw new MongoDBException(ErrorCode.ERREUR_INTERNE+" "+e.getMessage(), ErrorCode.MONGO_EXCEPTION);
		}
		DBStatic.closeMongoDBConnection();

	}

	/**
	 * Permet de supprimer une recette
	 * @param idRecette l'id de la recette a supprimer
	 * @param cle la cle session utilisateur
	 * @throws MyException lorsqu'il y a eut une erreur
	 */
	public static void supprimerRecette(String idRecette, String cle) throws MyException {

		if(idRecette == null || cle == null || cle.equals("") || idRecette.equals(""))
			throw new ParametreManquantException("Parametres(s) Manquant(s)", ErrorCode.PARAMETRE_MANQUANT);

		if (cle.length() != 32)
			throw new NonValideException("Cle invalide", ErrorCode.CLE_INVALIDE);

		if (!ServiceTools.isCleActive(cle))
			throw new SessionExpireeException("Votre session a expiree", ErrorCode.SESSION_EXPIREE);

		//Verifier que la recette existe
		MongoDatabase database;
		try {
			database = DBStatic.getMongoConnection();
		} catch (Exception e) {
			throw new MongoDBException(ErrorCode.ERREUR_INTERNE, ErrorCode.MONGO_EXCEPTION);
		}
		MongoCollection<BasicDBObject> recetteCollection = database.getCollection(MongoFactory.COLLECTION_RECETTE, BasicDBObject.class);
		BasicDBObject query = new BasicDBObject();
		query.put(MongoFactory._ID, new ObjectId(idRecette));
		MongoCursor<BasicDBObject> cursor = recetteCollection.find(query).iterator();
		if(!cursor.hasNext())
			throw new RecetteException("La recette n'existe pas", ErrorCode.RECETTE_INEXISTANTE);


		//Verifier que c'est bien l'utilisateur qui est le proprietaire de la recette
		Sessions s = RequeteStatic.obtenirSession(cle);
		Utilisateurs u = RequeteStatic.obtenirUtilisateur(s.getIdSession(), null);
		if(!estAuteurRecette(u.getId(), u.getLogin(), idRecette, recetteCollection))
			throw new RecetteException("Vous tentez de supprimer une recette qui ne vous appartient pas", ErrorCode.RECETTE_AUTRE_USER);
		//Supprime la recette et recupere le deleteresult
		recetteCollection.deleteOne(query);
		DBStatic.closeMongoDBConnection();
	}

	/**
	 * Permet de savoir si un utilisateur est l'auteur d'une recette
	 * @param id_auteur l'id de l'utilisateur
	 * @param login le nom d'utilisateur
	 * @param idRecette l'id de la recette
	 * @param recettesCollection la collection Mongo contenant les recettes
	 * @return boolean vrai si l'utilisateur est l'auteur de la recette, faux sinon
	 */
	public static boolean estAuteurRecette(int id_auteur, String login, String idRecette
			, MongoCollection<BasicDBObject> recettesCollection) {
		BasicDBObject document = new BasicDBObject(MongoFactory.AUTEUR+"."+MongoFactory.LOGIN_AUTEUR, login);
		document.append(MongoFactory.AUTEUR+"."+MongoFactory.ID_AUTEUR, id_auteur);
		document.put(MongoFactory._ID, new ObjectId(idRecette));
		MongoCursor<BasicDBObject> cursor = recettesCollection.find(document).iterator();
		return cursor.hasNext();
	}

	/**
	 * Permet de noter une recette
	 * @param cle la cle session utilisateur
	 * @param idRecette l'id de la recette a noter
	 * @param note la note donne a la recette
	 * @throws MyException s'il y a eut une erreur
	 */
	public static JSONObject noterRecette(String cle, String idRecette, int note) 
			throws MyException{

		if(idRecette == null || cle == null || idRecette.equals(""))
			throw new ParametreManquantException("Parametres(s) Manquant(s)", ErrorCode.PARAMETRE_MANQUANT);

		if(cle.length() != 32)
			throw new NonValideException("Cle invalide", ErrorCode.CLE_INVALIDE);

		if(!ServiceTools.isCleActive(cle))
			throw new SessionExpireeException("Votre session a expiree", ErrorCode.SESSION_EXPIREE);

		if(note < 0 || note > 5)
			throw new NonValideException("La note doit etre comprise entre 0 et 5", ErrorCode.NOTE_INVALIDE);

		// Si la recette est celle de l'utilisateur, on ne peut pas la noter
		Sessions s = RequeteStatic.obtenirSession(cle);
		Utilisateurs u = RequeteStatic.obtenirUtilisateur(s.getIdSession(), null);

		MongoDatabase database;
		try {
			database = DBStatic.getMongoConnection();
		} catch (Exception e) {
			throw new MongoDBException(ErrorCode.ERREUR_INTERNE, ErrorCode.MONGO_EXCEPTION);
		}
		MongoCollection<BasicDBObject> recettesCollection = 
				database.getCollection(MongoFactory.COLLECTION_RECETTE, BasicDBObject.class);
		if(estAuteurRecette(u.getId(), u.getLogin(), idRecette, recettesCollection))
			throw new RecetteException("Vous tentez de supprimer une recette qui ne vous appartient pas", ErrorCode.RECETTE_AUTRE_USER);


		// Noter la recette
		BasicDBObject objetNote = noterRecetteParId(u, cle, idRecette, note, recettesCollection);

		// Dire que l'utilisateur a note la recette (ajout dans collection Mongo)
		if(objetNote == null)
			throw new RecetteException("Vous avez deja note cette recette", ErrorCode.RECETTE_DEJA_NOTE);

		DBStatic.closeMongoDBConnection();

		return new JSONObject(objetNote);
	}

	/**
	 * Permet de noter une recette par son id
	 * @param u l'objet correspondant a l'utilisateur (model hibernate)
	 * @param key la cle session utilisateur
	 * @param idRecette l'id de la recette a note
	 * @param note la note donnee a la recette
	 * @param recettesCollection la collection Mongo contenant toutes les recettes
	 * @return BasicDBObject contenant les informations relatives a la note de la recette
	 * @throws RecetteException lorsque la recette n'existe pas
	 */
	public static BasicDBObject noterRecetteParId(Utilisateurs u, String key, String idRecette, int note, 
			MongoCollection<BasicDBObject> recettesCollection) throws RecetteException {

		/* Notation de la recette */

		BasicDBObject res = new BasicDBObject();

		// Query qui recup la recette
		ObjectId _id = new ObjectId(idRecette); //idRecette au format hex
		BasicDBObject query = new BasicDBObject();
		query.put(MongoFactory._ID, _id);

		// Recupere la recette dans la bd 
		MongoCursor<BasicDBObject> cursor = recettesCollection.find(query).iterator();

		BasicDBObject recette;

		if(cursor.hasNext())
			recette = cursor.next();
		else
			throw new RecetteException("Cette recette n'existe pas", ErrorCode.RECETTE_INEXISTANTE);

		if(!aDejaNote(recettesCollection, u, idRecette)){
			// Modifier la recette 
			BasicDBObject noteDoc = (BasicDBObject) recette.get(MongoFactory.NOTE);
			double moyenne = noteDoc.getDouble(MongoFactory.NOTE_MOYENNE);
			int nbNotes = noteDoc.getInt(MongoFactory.NOMBRE_NOTE);
			ArrayList<BasicDBObject> usersNotes = (ArrayList<BasicDBObject>) noteDoc.get(MongoFactory.USERS_NOTES);
			usersNotes.add(new BasicDBObject(MongoFactory.ID_USER, u.getId())
					.append(MongoFactory.USER_NOTE, note));
			double newMoyenne = (moyenne*nbNotes+note)/(nbNotes+1);
			noteDoc.replace(MongoFactory.NOTE_MOYENNE, newMoyenne);
			noteDoc.replace(MongoFactory.NOMBRE_NOTE, nbNotes+1);
			noteDoc.replace(MongoFactory.USERS_NOTES, usersNotes);
			recette.replace(MongoFactory.NOTE, noteDoc);

			BasicDBObject tmp = new BasicDBObject();
			tmp.put("$set", recette);
			recettesCollection.updateOne(query, tmp);

			res.append(MongoFactory.NOTE_MOYENNE, newMoyenne);
			res.append(MongoFactory.NOMBRE_NOTE, nbNotes+1);
			res.append(MongoFactory.USERS_NOTES, usersNotes);
		}else{
			res = null;
		}
		return res;
	}

	/**
	 * Permet de savoir si un utilisateur a deja note une recette
	 * @param recettes la collection Mongo contenant les recettes
	 * @param u l'objet representant l'utilisateur (model hibernate)
	 * @param idRecette l'id de la recette
	 * @return boolean vrai si l'utilisateur a deja note la recette, faux sinon
	 */
	public static boolean aDejaNote(MongoCollection<BasicDBObject> recettes, Utilisateurs u, String idRecette){
		BasicDBObject query = new BasicDBObject();
		query.put(MongoFactory.ID_RECETTE, idRecette); 
		MongoCursor<BasicDBObject> cursor = recettes.find(query).iterator();
		BasicDBObject doc;
		int id = u.getId();
		if(cursor.hasNext()){
			doc = cursor.next();
			ArrayList<BasicDBObject> list = (ArrayList<BasicDBObject>) doc.get(MongoFactory.USERS_NOTES);
			for(BasicDBObject bdo : list){
				if(bdo.getInt(MongoFactory.ID_USER) == id)
					return true;
			}
		}
		return false;
	}


	/**
	 * Permet de récuperer les recettes d'un utilisateur
	 * @param login le nom d'utilisateur
	 * @return ArrayList<BasicDBObject> la liste dont chaque element est un objet representant une recette
	 * @throws MongoDBException s'il y eut une lors de l'acces a la base Mongo
	 */
	public static ArrayList<BasicDBObject> getRecettesFromLogin(String login) throws MongoDBException {
		MongoDatabase database;
		try {
			database = DBStatic.getMongoConnection();
		} catch (Exception e) {
			throw new MongoDBException(ErrorCode.ERREUR_INTERNE, ErrorCode.MONGO_EXCEPTION);
		}
		MongoCollection<BasicDBObject> recettesCollection = 
				database.getCollection(MongoFactory.COLLECTION_RECETTE, BasicDBObject.class);
		BasicDBObject document = new BasicDBObject(MongoFactory.AUTEUR+"."+MongoFactory.LOGIN_AUTEUR, login);
		ArrayList<BasicDBObject> list = new ArrayList<BasicDBObject>();
		for(BasicDBObject obj : recettesCollection.find(document)){
			ObjectId oid = obj.getObjectId("_id");
			obj.replace("_id", oid.toString());
			list.add(obj);
		}
		return list;
	}

}
