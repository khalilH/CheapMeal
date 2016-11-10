package services.fonctions;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Part;

import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import util.ServiceTools;
import util.bdTools.DBStatic;
import util.bdTools.MongoFactory;
import util.bdTools.RequeteStatic;
import util.hibernate.model.Sessions;
import util.hibernate.model.Utilisateurs;

public class RecetteFonctions {

	public static ArrayList<BasicDBObject> getRecentRecipes() throws MongoClientException, UnknownHostException {
		ArrayList<BasicDBObject> list = new ArrayList<>();
		BasicDBObject sortQuery = new BasicDBObject("date",-1);
		MongoDatabase database = DBStatic.getMongoConnection();
		MongoCollection<BasicDBObject> col = database.getCollection(MongoFactory.COLLECTION_RECETTE, BasicDBObject.class);
		MongoCursor<BasicDBObject> cursor = col.find().sort(sortQuery).limit(9).iterator();
		while(cursor.hasNext()){
			BasicDBObject obj = cursor.next();
			ObjectId oid = obj.getObjectId("_id");
			obj.replace("_id", oid.toString());
			list.add(obj);
		}
		return list;
	}

	public static ArrayList<BasicDBObject> getBestRecipes() throws MongoClientException, UnknownHostException {
		ArrayList<BasicDBObject> list = new ArrayList<>();
		BasicDBObject sortQuery = new BasicDBObject(MongoFactory.NOTE+"."+MongoFactory.NOTE_MOYENNE,-1);
		MongoDatabase database = DBStatic.getMongoConnection();
		MongoCollection<BasicDBObject> col = database.getCollection(MongoFactory.COLLECTION_RECETTE, BasicDBObject.class);
		MongoCursor<BasicDBObject> cursor = col.find().sort(sortQuery).limit(9).iterator();
		while(cursor.hasNext()){
			BasicDBObject obj = cursor.next();
			ObjectId oid = obj.getObjectId("_id");
			obj.replace("_id", oid.toString());
			list.add(obj);
		}
		return list;
	}

	public static JSONObject getRecettesAccueil() throws JSONException, MongoClientException, UnknownHostException{
		JSONObject jb = new JSONObject();
		JSONArray recentRecipes = new JSONArray(getRecentRecipes());
		JSONArray bestRecipes = new JSONArray(getBestRecipes());
		jb.put("recettesRecentes", recentRecipes);
		jb.put("recettesBest", bestRecipes);
		return jb;

	}


	/**
	 * Permet d'obtenir l'objet Json correspondant a la recette dont l'id est en parametre 
	 * @param id id de la recette
	 * @return JSONObject contenant les informations de la recette
	 * @throws MyException
	 * @throws MongoClientException
	 * @throws UnknownHostException
	 */
	public static JSONObject afficherRecette(String id) throws MyException {
		if(id == null)
			throw new ParametreManquantException("Parametre(s) manquant(s)", ErrorCode.PARAMETRE_MANQUANT);
		if(id.equals(""))
			throw new NonValideException("Parametre non valide", ErrorCode.RECETTE_ID_INVALIDE);

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

		if(res != null){
			// TODO Ajouter le prix calcule via l'API
		}

		return new JSONObject(res);
	}


	/**
	 * 
	 * @param titre
	 * @param cle
	 * @param ingredients
	 * @param quantites
	 * @param mesures
	 * @param preparation
	 * @throws MyException
	 */
	public static void ajouterRecette(String titre, String cle, 
			List<String> ingredients, 
			List<Double> quantites, 
			List<String> mesures,
			String preparation,
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
				|| ingredients.stream().filter(i -> i.length() == 0).count() > 0 
				|| quantites.stream().filter(i -> i<=0).count() > 0 
				|| mesures.stream().filter(i -> !listMesures.contains(i)).count() > 0 
				|| preparation.equals("")
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
	 * 
	 * @param idRecette
	 * @param cle
	 * @throws MyException
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
	 * 
	 * @param id_auteur
	 * @param login
	 * @param idRecette
	 * @param recettesCollection
	 * @return
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
	 * 
	 * @param cle
	 * @param idRecette
	 * @param note
	 * @throws MyException
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
		MongoCollection<BasicDBObject> notesCollection = 
				database.getCollection(MongoFactory.COLLECTION_UTILISATEUR_NOTES, BasicDBObject.class);
		if(estAuteurRecette(u.getId(), u.getLogin(), idRecette, recettesCollection))
			throw new RecetteException("Vous tentez de supprimer une recette qui ne vous appartient pas", ErrorCode.RECETTE_AUTRE_USER);


		// Noter la recette
		BasicDBObject objetNote = noterRecetteParId(u, cle, idRecette, note, recettesCollection, notesCollection);

		// Dire que l'utilisateur a note la recette (ajout dans collection Mongo)
		if(objetNote == null)
			throw new RecetteException("Vous avez deja note cette recette", ErrorCode.RECETTE_DEJA_NOTE);

		DBStatic.closeMongoDBConnection();

		return new JSONObject(objetNote);
	}

	/**
	 * 
	 * @param login
	 * @return
	 * @throws MongoDBException
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

	/**
	 * 
	 * @param u
	 * @param key
	 * @param idRecette
	 * @param note
	 * @param recettesCollection
	 * @param notesCollection
	 * @return
	 * @throws RecetteException
	 */
	public static BasicDBObject noterRecetteParId(Utilisateurs u, String key, String idRecette, int note, 
			MongoCollection<BasicDBObject> recettesCollection,
			MongoCollection<BasicDBObject> notesCollection ) throws RecetteException {

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

		if(!aDejaNote(notesCollection, u, idRecette)){
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
	 * 
	 * @param u
	 * @param idRecette
	 * @param notesCollection
	 */
	/*public static void majNotationRecette(Utilisateurs u, String idRecette,
			MongoCollection<BasicDBObject> notesCollection) {

		// Dire que l'utilisateur a note la recette
		BasicDBObject query = new BasicDBObject();
		query.put(MongoFactory.ID_RECETTE, arg1)
		query.put(MongoFactory., u.getLogin());
		MongoCursor<BasicDBObject> cursor = notesCollection.find(query).iterator();
		BasicDBObject doc;
		if(cursor.hasNext()){
			// Si le doc correspondant a l'utilisateur existe, mettre a jour les ids
			doc = cursor.next();
			ArrayList<ObjectId> list = (ArrayList<ObjectId>) doc.get(MongoFactory.IDS_RECETTE);
			list.add(new ObjectId(idRecette));
			doc.replace(MongoFactory.IDS_RECETTE, list);

			BasicDBObject tmp = new BasicDBObject();
			tmp.put("$set", doc);
			notesCollection.updateOne(query, doc);
		}else{
			// Si le doc n'existe pas, l'inserer
			doc = new BasicDBObject(MongoFactory.ID_USER, u.getId());
			doc.append(MongoFactory.IDS_RECETTE, new ArrayList<>());
			notesCollection.insertOne(doc);
		}
	}*/

	/**
	 * 
	 * @param recettes
	 * @param u
	 * @param idRecette
	 * @return
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



}
