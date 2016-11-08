package util;

//-1 Missing arguments (ParametreManquantException)
//10 Invalid login  (NonValideException)
//11 Invalid password
//12 Invalid email
//13 Invalid cleSession 
//20 login not available (NonDisponibleException)
//21 email not available  
//30 mot de passe incorrect (AuthenticationException)
//31 compte utilisateur inexistant
//32 mot de passe identique (changerMotDePasse)
//40 Session Expiree (SessionExpireeException)
//22 user does not exist, not found 
//30 Session Expired
//40 Friends Error
//50 Tweet Error
//60 Size limit exceeded
//70 Erreur de JSON
//80 Erreur de SQL
//90 Erreur de MONGO
//10000 Erreur de JAVA
//100000 Erreur de JSON

public class ErrorCode {
	public static final int PARAMETRE_MANQUANT = -1;
	
	public static final int LOGIN_INVALIDE = 10;
	public static final int PASSWORD_INVALIDE = 11;
	public static final int EMAIL_INVALIDE = 12;
	public static final int CLE_INVALIDE = 13;
	
	public static final int LOGIN_NON_DISPO= 20;
	public static final int EMAIL_NON_DISPO= 21;
	
	public static final int PASSWORD_INCORRECT = 30;
	public static final int UTILISATEUR_INEXISTANT = 31;
	public static final int PASSWORD_IDENTIQUE = 30;
	
	public static final int SESSION_EXPIREE = 40;
	
	public static final int SQL_EXCEPTION = 80;
}
