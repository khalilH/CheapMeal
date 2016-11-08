package exceptions;

/**
 * 
 * @author khalil
 *
 */
public class RecetteException extends MyException{

	private static final long serialVersionUID = 1L;

	public RecetteException(String msg, int code){
		super(msg, code);
	}
	
}
