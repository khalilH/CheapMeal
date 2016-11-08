package exceptions;

/**
 * 
 * @author khalil
 *
 */
public class NonDisponibleException extends MyException {

	private static final long serialVersionUID = 1L;

	public NonDisponibleException(String message, int code){
		super(message, code);
	}
	
}
