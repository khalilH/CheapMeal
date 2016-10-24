package services.fonctions;

import java.net.UnknownHostException;
import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClientException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;

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

	public static void ajouterRecette(String titre, String key, List<String> listIng, List<String> prepa)
			throws RecetteException, InformationUtilisateurException, SessionExpireeException, MongoClientException,
			UnknownHostException {

		/* Verification des parametres */
		if (titre == null || key == null || listIng == null || prepa == null)
			throw new RecetteException("Informations non completes");

		if (titre.equals("") || listIng.stream().filter(i -> i.length() == 0).count() > 0
				|| prepa.stream().filter(p -> p.length() == 0).count() > 0)
			throw new RecetteException("Titre, ingredient ou etape de preparation invalide");

		if (key.length() != 32)
			throw new InformationUtilisateurException("Cle invalide");

		if (!ServiceTools.isCleActive(key))
			throw new SessionExpireeException("Votre session a expiree");

		/* Ajout de la recette */
		Sessions s = RequeteStatic.obtenirSession(key);
		Utilisateurs u = RequeteStatic.obtenirUtilisateur(s.getIdSession(), null);

		MongoDatabase database = DBStatic.getMongoConnection();
		MongoCollection<BasicDBObject> col = database.getCollection("Recettes", BasicDBObject.class);

		BasicDBObject document = MongoFactory.creerDocumentRecette(titre, u.getId(), u.getLogin(), listIng, prepa);

		col.insertOne(document);
		DBStatic.closeMongoDBConnection();

	}

	public static void supprimerRecette(String id, String key) throws RecetteException, InformationUtilisateurException, SessionExpireeException, MongoClientException, UnknownHostException {
		
		if(id == null || key == null)
			throw new RecetteException("Informations non completes");
		
		if(key.equals("") || id.equals(""))
			throw new RecetteException("La clé ou l'id n'est pas valide");
		
		if (key.length() != 32)
			throw new InformationUtilisateurException("Cle invalide");

		if (!ServiceTools.isCleActive(key))
			throw new SessionExpireeException("Votre session a expiree");
		
		//Verifier que c'est bien l'utilisateur qui est le propriétaire de la recette
		Sessions s = RequeteStatic.obtenirSession(key);
		Utilisateurs u = RequeteStatic.obtenirUtilisateur(s.getIdSession(), null);
		if(!MongoFactory.isOwnerOfRecipe(u.getId(),u.getLogin(),id))
			throw new RecetteException("Vous tentez de supprimer une recette qui ne vous appartient pas");
		
		// Creer la query qui va supprimer la recette avec le _id correspondant
		ObjectId _id = new ObjectId(id);
		BasicDBObject query = new BasicDBObject();
		query.put("_id", _id);
		
		//Supprime la recette et recupere le deleteresult
		MongoDatabase database = DBStatic.getMongoConnection();
		MongoCollection<BasicDBObject> col = database.getCollection("Recettes", BasicDBObject.class);
		DeleteResult del_res = col.deleteOne(query);


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
			throw new RecetteException("La note doit être comprise entre 0 et 5");
		
		// Si la recette est celle de l'utilisateur, ne peut pas la noter
		Sessions s = RequeteStatic.obtenirSession(key);
		Utilisateurs u = RequeteStatic.obtenirUtilisateur(s.getIdSession(), null);
		if(MongoFactory.isOwnerOfRecipe(u.getId(),u.getLogin(),idRecette))
			throw new RecetteException("Impossible de noter une recette qui vous appartient");
		
		// Query qui recup la recette
		ObjectId _id = new ObjectId(idRecette); //idRecette au format hex
		BasicDBObject query = new BasicDBObject();
		query.put("_id", _id);
		
		// Connexion a la database Mongo
		MongoDatabase database = DBStatic.getMongoConnection();
		MongoCollection<BasicDBObject> col = database.getCollection("Recettes", BasicDBObject.class);
		
		// Recuper la recette dans la bd 
		MongoCursor<BasicDBObject> cursor = col.find(query).iterator();

		BasicDBObject recette;
		if(cursor.hasNext())
			recette = cursor.next();
		else
			throw new RecetteException("Cette recette n'existe pas");
		
		// Modifier la recette 
		BasicDBObject noteDoc = (BasicDBObject) recette.get("note");
		double moyenne = noteDoc.getDouble("moyenne");
		int nbNotes = noteDoc.getInt("nbNotes") ;		
		double newMoyenne = (moyenne*nbNotes+note)/(nbNotes+1);
		noteDoc.replace("moyenne", newMoyenne);
		noteDoc.replace("nbNotes", nbNotes+1);
		recette.replace("note", noteDoc);

		BasicDBObject tmp = new BasicDBObject();
		tmp.put("$set", recette);
		col.updateOne(query, tmp);
		
		DBStatic.closeMongoDBConnection();
		
	}

}
