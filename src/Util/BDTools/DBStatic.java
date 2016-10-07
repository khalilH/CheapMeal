package Util.BDTools;

import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientException;
import com.mongodb.client.MongoDatabase;


public class DBStatic {
	
	public static boolean mysql_pooling = false;
	public static final String mysql_host = "vps197081.ovh.net";
	public static final String mysql_db = "ladi";
	public static final String mysql_username = "ladi";
	public static final String mysql_password = "atoz";
	private static Database database = null;
	
	private static MongoClient mongoClient;
	
	public static Connection getSQLConnection() throws SQLException{
		if(DBStatic.mysql_pooling == false){
			try {
				Class.forName("com.mysql.jdbc.Driver");
				return(DriverManager.getConnection("jdbc:mysql://"+DBStatic.mysql_host+"/"+DBStatic.mysql_db,
						DBStatic.mysql_username, DBStatic.mysql_password));
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				return null;
			}
		}else{
			if(database == null)
				database = new Database("jdbc/db");
			return(database.getConnection());
		}
	}
	

	public static MongoDatabase getMongoConnection() throws UnknownHostException, MongoClientException{
 		mongoClient = new MongoClient("localhost", 27130);
 		MongoDatabase res = mongoClient.getDatabase("tests");
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
