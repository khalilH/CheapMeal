package exceptions;

/**
 * Exception levee lors d'une demande d'acces a une recette inexistante
 */
public class RecetteException extends MyException{

	private static final long serialVersionUID = 1L;

	public RecetteException(String msg, int code){
		super(msg, code);
	}
	
}
