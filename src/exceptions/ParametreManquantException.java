package exceptions;

/**
 * Exception levee lorsque les parametres obligatoires d'une requete 
 * ne sont pas transmis ou sont vide
 */
public class ParametreManquantException extends MyException {

	private static final long serialVersionUID = 1L;

	public ParametreManquantException(String message, int code){
		super(message, code);
	}

}
