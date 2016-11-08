package exceptions;

/**
 * 
 * @author khalil
 *
 */
public class MyException extends Exception {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	private int code;
	
	/**
	 * 
	 * @param message
	 * @param code
	 */
	public MyException(String message, int code) {
		super(message);
		this.code = code;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getCode() {
		return code;
	}
}
