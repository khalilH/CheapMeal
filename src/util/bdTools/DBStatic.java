package util.bdTools;

import java.net.UnknownHostException;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientException;
import com.mongodb.client.MongoDatabase;


public class DBStatic {
	
	private static MongoClient mongoClient;
	
	public static MongoDatabase getMongoConnection() throws UnknownHostException, MongoClientException{
 		mongoClient = new MongoClient("localhost", 27130);
 		MongoDatabase res = mongoClient.getDatabase("CheapMeal");
 		return res;
 	}
	
	public static final void closeMongoDBConnection() {
		try {
			mongoClient.close();
		} catch (Exception e) {
			System.err.println("Error in terminating connection");
		}
		
	}
}