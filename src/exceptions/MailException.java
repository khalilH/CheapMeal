package exceptions;

/**
 * 
 * @author khalil
 *
 */
public class MailException extends MyException {

	private static final long serialVersionUID = 1L;

	public MailException(String message, int code){
		super(message, code);
	}

}
