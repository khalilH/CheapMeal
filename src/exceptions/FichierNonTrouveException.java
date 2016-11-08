package exceptions;

/**
 * 
 * @author khalil
 *
 */
public class FichierNonTrouveException extends MyException {

	private static final long serialVersionUID = 1L;

	public FichierNonTrouveException(String message, int code){
		super(message, code);
	}

}
