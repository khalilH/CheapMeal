package exceptions;

/**
 * 
 * @author khalil
 *
 */
public class AuthenticationException extends MyException {

	private static final long serialVersionUID = 1L;

	public AuthenticationException(String message, int code){
		super(message, code);
	}
	
}
