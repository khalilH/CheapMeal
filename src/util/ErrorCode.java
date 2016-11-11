package util;

//-1 Missing arguments (ParametreManquantException)
//10 Invalid login  (NonValideException)
//11 Invalid password
//12 Invalid email
//13 Invalid cleSession
//13 Invalid bio 
//14 invalid query
//15 invalid note
//20 login not available (NonDisponibleException)
//21 email not available  
//30 mot de passe incorrect (AuthenticationException)
//31 compte utilisateur inexistant
//32 mot de passe identique (changerMotDePasse)
//33 confirmation de mot de passe different du mot de passe indique (changerMdp et inscription)
//40 Session Expiree (SessionExpireeException)
//41 Cle non active (CleNonActiveException)
//22 user does not exist, not found 
//30 Session Expired
//40 Friends Error
//50 Recette inexistante
//60 Size limit exceeded
//70 Erreur de JSON
//80 Erreur de SQL
//90 Erreur de MONGO
//91 FileNotFoundException (ingredients.csv, *.png)
//92 Mail Exception (erreurs javax.mail)
//100 Erreur ElasticSearch
//10000 Erreur de JAVA
//100000 Erreur de JSON

/**
 * 
 * @author khalil
 *
 */
public class ErrorCode {
	
	public static final String ERREUR_INTERNE = "Erreur interne au serveur";
	public static final int PARAMETRE_MANQUANT = -1;
	
	public static final int LOGIN_INVALIDE = 10;
	public static final int PASSWORD_INVALIDE = 11;
	public static final int EMAIL_INVALIDE = 12;
	public static final int CLE_INVALIDE = 13;
	public static final int BIO_INVALIDE = 14;
	public static final int NOTE_INVALIDE = 15;
	
	public static final int LOGIN_NON_DISPO= 20;
	public static final int EMAIL_NON_DISPO= 21;
	
	public static final int PASSWORD_INCORRECT = 30;
	public static final int UTILISATEUR_INEXISTANT = 31;
	public static final int PASSWORD_IDENTIQUE = 32;
	public static final int PASSWORD_CONFIRMATION_DIFFERENT = 33;
	
	public static final int SESSION_EXPIREE = 40;
	public static final int CLE_NON_ACTIVE = 41;
	
	public static final int RECETTE_INEXISTANTE = 50;
	public static final int RECETTE_AUTRE_USER = 51;
	public static final int RECETTE_DEJA_NOTE= 52;
	public static final int RECETTE_ID_INVALIDE = 53;
	
	public static final int SQL_EXCEPTION = 80;

	public static final int MONGO_EXCEPTION = 90;
	public static final int FILE_NOT_FOUND_EXCEPTION = 91;
	public static final int MAIL_EXCEPTION = 92;
	
	public static final int ELASTIC_SEARCH_EXCEPTION = 92;
	
	
	
}
