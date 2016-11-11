package util.bdTools;

import java.net.UnknownHostException;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientException;
import com.mongodb.client.MongoDatabase;

/**
 */
public class DBStatic {

	private static MongoClient mongoClient = null;
	private static MongoDatabase res = null;

	/**
	 * Creer une connection mongo
	 * @return
	 * @throws UnknownHostException
	 * @throws MongoClientException
	 */
	public static MongoDatabase getMongoConnection() throws UnknownHostException, MongoClientException{
		if (res == null) {
			mongoClient = new MongoClient("localhost", 27130);
			res = mongoClient.getDatabase(MongoFactory.DATABASE_NAME);
		}
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