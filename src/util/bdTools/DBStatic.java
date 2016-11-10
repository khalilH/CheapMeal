package util.bdTools;

import java.net.UnknownHostException;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientException;
import com.mongodb.client.MongoDatabase;

/**
 */
public class DBStatic {
	
	private static MongoClient mongoClient;
	
	/**
	 * Creer une connection mongo
	 * @return
	 * @throws UnknownHostException
	 * @throws MongoClientException
	 */
	public static MongoDatabase getMongoConnection() throws UnknownHostException, MongoClientException{
 		mongoClient = new MongoClient("localhost", 27130);
 		MongoDatabase res = mongoClient.getDatabase(MongoFactory.DATABASE_NAME);
 		return res;
 	}
	
	/**
	 * 
	 */
	public static final void closeMongoDBConnection() {
		try {
			mongoClient.close();
		} catch (Exception e) {
			System.err.println("Error in terminating connection");
		}
		
	}
}