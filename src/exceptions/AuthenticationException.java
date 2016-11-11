package exceptions;

/**
 * Excpetion levee lorsque le mot de passe d'un utilisateur tentant de se 
 * connecter n'est pas correct
 */
public class AuthenticationException extends MyException {

	private static final long serialVersionUID = 1L;

	public AuthenticationException(String message, int code){
		super(message, code);
	}
	
}
