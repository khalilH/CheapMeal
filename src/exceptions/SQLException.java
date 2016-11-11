package exceptions;

/**
 * Exception levee lors d'interaction avec notre base de donne SQL
 */
public class SQLException extends MyException {

	private static final long serialVersionUID = 1L;

	public SQLException(String message, int code){
		super(message, code);
	}

}
