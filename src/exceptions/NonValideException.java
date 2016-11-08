package exceptions;

/**
 * 
 * @author khalil
 *
 */
public class NonValideException extends MyException {

	private static final long serialVersionUID = 1L;

	public NonValideException(String message, int code){
		super(message, code);
	}

}
