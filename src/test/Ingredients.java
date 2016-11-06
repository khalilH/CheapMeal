package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClientException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import util.bdTools.DBStatic;
import util.bdTools.MongoFactory;

public class Ingredients {

	public static void main(String[] args) {
		
		try {
			MongoDatabase database = DBStatic.getMongoConnection();
			MongoCollection<BasicDBObject> col = database.getCollection("ingredients", BasicDBObject.class);
			col.drop();
			List<BasicDBObject> list = new ArrayList<>();
			Scanner scanner = new Scanner(new File("ingredients.csv"));
			scanner.nextLine(); // Pour tej la premiere ligne
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] tab = line.split(",");
				BasicDBObject document = new BasicDBObject();
				document.put(MongoFactory.NOM_INGREDIENT, tab[0]);
				document.put(MongoFactory.EAN, tab[1]);
				list.add(document);
				System.out.println(document.toJson());
			}
			scanner.close();
			col.insertMany(list);
			DBStatic.closeMongoDBConnection();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MongoClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
