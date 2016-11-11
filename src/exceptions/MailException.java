package exceptions;

/**
 * Exception levee lors d'une erreur lors de l'envoie d'un mail
 */
public class MailException extends MyException {

	private static final long serialVersionUID = 1L;

	public MailException(String message, int code){
		super(message, code);
	}

}
