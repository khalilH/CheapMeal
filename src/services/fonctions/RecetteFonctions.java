package services.fonctions;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClientException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import exceptions.InformationUtilisateurException;
import exceptions.RecetteException;
import exceptions.SessionExpireeException;
import util.ServiceTools;
import util.bdTools.DBStatic;
import util.bdTools.MongoFactory;
import util.bdTools.RequeteStatic;
import util.hibernate.model.Sessions;
import util.hibernate.model.Utilisateurs;

public class RecetteFonctions {

	
	public static void ajouterRecette(String titre, String key, List<String> listIng, List<Double> listQuant, List<String> listMesures, List<String> prepa)
			throws RecetteException, InformationUtilisateurException, SessionExpireeException, MongoClientException,
			UnknownHostException {

		/* Verification des parametres */
		if (titre == null || key == null || listIng == null || listQuant == null || prepa == null)
			throw new RecetteException("Informations non completes");

		/* Mise en place de la liste des mesures compatibles */
		ArrayList<String> mesures = new ArrayList<>();
		mesures.add("kg");
		mesures.add("g");
		mesures.add("unite(s)");
		mesures.add("l");
		mesures.add("cl");
		mesures.add("ml");
		
		if (titre.equals("") || listIng.stream().filter(i -> i.length() == 0).count() > 0 || listQuant.stream().filter(i -> i<=0).count() > 0 
				|| listMesures.stream().filter(i -> !mesures.contains(i)).count() > 0 || prepa.stream().filter(p -> p.length() == 0).count() > 0)
			throw new RecetteException("Titre, ingredients, quantites ou etape de preparation invalide");

		if (key.length() != 32)
			throw new InformationUtilisateurException("Cle invalide");

		if (!ServiceTools.isCleActive(key))
			throw new SessionExpireeException("Votre session a expiree");

		/* Ajout de la recette */
		Sessions s = RequeteStatic.obtenirSession(key);
		Utilisateurs u = RequeteStatic.obtenirUtilisateur(s.getIdSession(), null);

		MongoDatabase database = DBStatic.getMongoConnection();
		MongoCollection<BasicDBObject> col = database.getCollection("Recettes", BasicDBObject.class);

		BasicDBObject document = MongoFactory.creerDocumentRecette(titre, u.getId(), u.getLogin(), listIng, listQuant, listMesures, prepa);

		col.insertOne(document);
		DBStatic.closeMongoDBConnection();

	}

	public static void supprimerRecette(String id_recette, String key) throws RecetteException, InformationUtilisateurException, SessionExpireeException, MongoClientException, UnknownHostException {

		if(id_recette == null || key == null)
			throw new RecetteException("Informations non completes");

		if(key.equals("") || id_recette.equals(""))
			throw new RecetteException("La cle ou l'id n'est pas valide");

		if (key.length() != 32)
			throw new InformationUtilisateurException("Cle invalide");

		if (!ServiceTools.isCleActive(key))
			throw new SessionExpireeException("Votre session a expiree");

		//Verifier que la recette existe
		MongoDatabase database = DBStatic.getMongoConnection();
		MongoCollection<BasicDBObject> col = database.getCollection("Recettes", BasicDBObject.class);
		BasicDBObject query = new BasicDBObject();
		query.put("_id", new ObjectId(id_recette));
		MongoCursor<BasicDBObject> cursor = col.find(query).iterator();
		if(!cursor.hasNext())
			throw new RecetteException("La recette n'existe pas");


		//Verifier que c'est bien l'utilisateur qui est le propriï¿½taire de la recette
		Sessions s = RequeteStatic.obtenirSession(key);
		Utilisateurs u = RequeteStatic.obtenirUtilisateur(s.getIdSession(), null);
		if(!MongoFactory.isOwnerOfRecipe(u.getId(),u.getLogin(),id_recette))
			throw new RecetteException("Vous tentez de supprimer une recette qui ne vous appartient pas");

		//Supprime la recette et recupere le deleteresult
		col.deleteOne(query);

	}

	public static void noterRecette(String key, String idRecette, int note) throws RecetteException, InformationUtilisateurException, SessionExpireeException, MongoClientException, UnknownHostException{

		if(idRecette == null || key == null)
			throw new RecetteException("Informations non completes");

		if(idRecette.equals(""))
			throw new RecetteException("id de la recette invalide");

		if(key.length() != 32)
			throw new InformationUtilisateurException("Cle invalide");

		if(!ServiceTools.isCleActive(key))
			throw new SessionExpireeException("Votre session a expiree");

		if(note < 0 || note > 5)
			throw new RecetteException("La note doit etre comprise entre 0 et 5");

		// Si la recette est celle de l'utilisateur, ne peut pas la noter
		Sessions s = RequeteStatic.obtenirSession(key);
		Utilisateurs u = RequeteStatic.obtenirUtilisateur(s.getIdSession(), null);
		if(MongoFactory.isOwnerOfRecipe(u.getId(),u.getLogin(),idRecette))
			throw new RecetteException("Impossible de noter une recette qui vous appartient");


		// Noter la recette
		boolean notation = MongoFactory.noterRecette(u, key, idRecette, note);

		// Dire que l'utilisateur a noté la recette (ajout dans collection Mongo)
		if(notation)
			MongoFactory.majNotationRecette(u, idRecette);
		else
			throw new RecetteException("Vous avez deja note cette recette");

	}
	
	/**
	 * Permet de recuperer les ingredients matchant la query
	 * @param query de l'autocomplete
	 * @return une liste de {"value":"ingredients", "data":"EAN"}
	 * @throws MongoClientException
	 * @throws UnknownHostException
	 */
	public static ArrayList<BasicDBObject> getListeIngredients(String query) throws MongoClientException, UnknownHostException {
		BasicDBObject document = new BasicDBObject();
		document.put(MongoFactory.NOM_INGREDIENT, Pattern.compile(query));
		MongoDatabase database = DBStatic.getMongoConnection();
		MongoCollection<BasicDBObject> col = database.getCollection(MongoFactory.COLLECTION_INGREDIENTS, BasicDBObject.class);
		ArrayList<BasicDBObject> list = new ArrayList<>();
		for (BasicDBObject obj : col.find(document)) {
			BasicDBObject suggestion = new BasicDBObject();
			suggestion.put("data", obj.getString(MongoFactory.EAN));
			suggestion.put("value", obj.getString(MongoFactory.NOM_INGREDIENT));
			list.add(suggestion);
		}
		DBStatic.closeMongoDBConnection();
		return list;
	}

}
