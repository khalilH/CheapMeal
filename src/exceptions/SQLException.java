package exceptions;

/**
 * 
 * @author khalil
 *
 */
public class SQLException extends MyException {

	private static final long serialVersionUID = 1L;

	public SQLException(String message, int code){
		super(message, code);
	}

}
