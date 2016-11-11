package exceptions;

/**
 * Exception levee en reponse a une MongoConnexionException ou une UnknownHostException
 */
public class MongoDBException extends MyException {

	private static final long serialVersionUID = 1L;

	public MongoDBException(String message, int code){
		super(message, code);
	}

}
