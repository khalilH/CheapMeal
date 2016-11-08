package exceptions;

public class SessionExpireeException extends MyException {

	private static final long serialVersionUID = 1L;

	public SessionExpireeException(String message, int code) {
		super(message, code);
	}

}
