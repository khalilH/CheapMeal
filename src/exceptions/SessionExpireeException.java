package exceptions;

/**
 * Exception levee lors qu'une session a expiree
 */
public class SessionExpireeException extends MyException {

	private static final long serialVersionUID = 1L;

	public SessionExpireeException(String message, int code) {
		super(message, code);
	}

}
