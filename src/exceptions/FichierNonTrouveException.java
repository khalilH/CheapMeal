package exceptions;

/**
 * Exception levee si le fichier ingredients.csv contenant la liste
 * des ingredients disponible n'est pas present dans WebContent
 */
public class FichierNonTrouveException extends MyException {

	private static final long serialVersionUID = 1L;

	public FichierNonTrouveException(String message, int code){
		super(message, code);
	}

}
