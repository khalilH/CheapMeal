package exceptions;

public class MyException extends Exception {

	private static final long serialVersionUID = 1L;
	private int code;
	
	public MyException(String message, int code) {
		super(message);
		this.code = code;
	}
	
	public int getCode() {
		return code;
	}
}
