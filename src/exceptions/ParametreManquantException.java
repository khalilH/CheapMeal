package exceptions;

public class ParametreManquantException extends MyException {

	private static final long serialVersionUID = 1L;

	public ParametreManquantException(String message, int code){
		super(message, code);
	}

}
