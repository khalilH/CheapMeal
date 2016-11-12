package exceptions;

/**
 * Classe permettant de représenter toutes les exceptions de notre application
 */
public class MyException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Code d'erreur
	 */
	private int code;

	public MyException(String message, int code) {
		super(message);
		this.code = code;
	}

	/**
	 * Permet d'accéder au code d'erreur d'une exception
	 * @return le code d'erreur de l'exception
	 */
	public int getCode() {
		return code;
	}
}
