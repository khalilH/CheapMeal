package exceptions;

/**
 * Exception levee lorsqu'un login ou une adresse mail sont deja utilise par 
 * un autre utilisateur
 */
public class NonDisponibleException extends MyException {

	private static final long serialVersionUID = 1L;

	public NonDisponibleException(String message, int code){
		super(message, code);
	}
	
}
