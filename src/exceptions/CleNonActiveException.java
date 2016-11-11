package exceptions;

/**
 * Exception levee si une cle de session a expiree
 */
public class CleNonActiveException extends MyException {

	private static final long serialVersionUID = 1L;

	public CleNonActiveException(String message, int code){
		super(message, code);
	}
	
}
