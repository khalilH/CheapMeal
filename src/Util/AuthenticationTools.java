package Util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;

public class AuthenticationTools {

	public static boolean loginLibre(String login) throws SQLException{
		boolean res = true;
		Connection c;
		c = DBStatic.getSQLConnection();
		Statement stmt = c.createStatement();
		//TODO renommer checkUserExist en français
		ResultSet rs = RequeteStatic.checkUserExist(stmt, login);
		if(rs.next())
			res = false;
		rs.close();
		stmt.close();
		c.close();
		return res;
	}
	
	public static boolean emailLibre(String email) throws SQLException{
		boolean res = true;
		Connection c;
		c = DBStatic.getSQLConnection();
		Statement stmt = c.createStatement();
		ResultSet rs = RequeteStatic.verifierEmail(stmt, email);
		if(rs.next())
			res = false;
		rs.close();
		stmt.close();
		c.close();
		return res;
	}
	
	public static int obtenirIdAvecLogin(String login) throws SQLException{
		int id = 0;
		Connection c;
		c = DBStatic.getSQLConnection();
		Statement stmt = c.createStatement();
		ResultSet cur = RequeteStatic.obtenirIdAvecLogin(stmt, login);
		if(cur.next())
			id = cur.getInt("id");
		cur.close();
		stmt.close();
		c.close();
		return id;
	}
	
	/** Supprime une cle de session de la base de donnee
	 * @param cle la cle a supprimer
	 * @throws SQLException
	 */
	public static void detruireCleSession(String cle) throws SQLException {
		Connection c = DBStatic.getSQLConnection();
		Statement st = c.createStatement();
		RequeteStatic.deleteFromSessions(st, cle);
		st.close();
		c.close();
	}
	
	/**
	 * Verifie si une cle de session est active (qu'elle n'a pas expiree)
	 * @param cle la cle d'une session
	 * @return si la cle est utilisable, false sinon
	 * @throws SQLException
	 */
	public static boolean cleActive(String cle) throws SQLException {
		boolean res = false;
		Connection c = DBStatic.getSQLConnection();
		Statement st = c.createStatement();
		ResultSet cursor = RequeteStatic.selectDateExpirationFromSessions(st, cle);
		if (cursor.next()) {
			Timestamp dateExpiration = cursor.getTimestamp("dateExpiration");
			Timestamp curTime = new Timestamp(System.currentTimeMillis());
			if(curTime.before(dateExpiration)) {
				res = true;
				/* On refresh la cle a chaque fois qu'elle est utilisee */
				rafraichirCle(cle);
			}
		}
		cursor.close();
		st.close();
		c.close();
		return res;
	}

	public static boolean userExists(String id) throws SQLException {
		Connection c = DBStatic.getSQLConnection();
		Statement st = c.createStatement();
		boolean res = false;
		
		ResultSet cursor = RequeteStatic.checkUserExist(st,id);
		if(cursor.first())
			res = true;
		
		cursor.close();
		st.close();
		c.close();
		return res;
	}
	
	public static boolean credentialsAreValid(String id,String mdp) throws SQLException {
		Connection c = DBStatic.getSQLConnection();
		Statement st = c.createStatement();
		boolean res = false;
		
		ResultSet cursor = RequeteStatic.checkCredentialsValid(st, id, mdp);
		if(cursor.first())
			res = true;
		
		cursor.close();
		st.close();
		c.close();
		return res;
	}

	/**
	 * Verifie si un utilisateur est connecte sur le site
	 * @param login le login de l'utilisateur 
	 * @return true si son id est present dans la table SESSION, false sinon
	 * @throws SQLException
	 */
	public static boolean isSessionOpen(String login) throws SQLException {
		Connection c = DBStatic.getSQLConnection();
		Statement st = c.createStatement();
		boolean res = false;
		ResultSet cursor = RequeteStatic.checkSessionOpen(st, login);
		if(cursor.first())
			res = true;
		cursor.close();
		st.close();
		c.close();
		return res;
	}

	public static String updateAndRetrieveTokenTime(String login) throws SQLException {
		Connection c = DBStatic.getSQLConnection();
		Statement st = c.createStatement();
		long timetoAdd=30*60*1000; // 30 minutes in milliseconds
		Timestamp time = new Timestamp(new Date().getTime() + timetoAdd);
		String s = "";
		RequeteStatic.updateTokenTime(st,login, time);
		ResultSet cursor = RequeteStatic.retrieveTokenFromLogin(st, login);
		if(cursor.first())
			 s = cursor.getString("cleSession");
		else	
			throw new SQLException("Should retrieve the token but is not");
		cursor.close();
		st.close();
		c.close();
		return s;
	}

	public static String addSessionFromLogin(String login) throws SQLException {
		Connection c = DBStatic.getSQLConnection();
		Statement st = c.createStatement();
		long timetoAdd=30*60*1000; // 30 minutes in milliseconds
		Timestamp time = new Timestamp(new Date().getTime() + timetoAdd);
		String key = AuthenticationTools.createKey(); 
		//TODO peut etre check si la cle genere n'existe pas deja meme si peu probable 
		String idofUser="";
		
		ResultSet user = RequeteStatic.checkUserExist(st, login);
		if(user.first())
			idofUser = user.getString("id");
		else
			throw new SQLException("Should retrieve the idOfuser but is not");
		RequeteStatic.createSessionFromLogin(st,idofUser,time,key);
		
		st.close();
		c.close();
		return key;
	}
	

	/**
	 * Reactive une cle, en mettant dateExpiration a la date courante + 30 minutes 
	 * @param cle la cle de session
	 * @throws SQLException
	 */
	public static void rafraichirCle(String cle) throws SQLException {
		Connection c = DBStatic.getSQLConnection();
		Statement st = c.createStatement();
		RequeteStatic.updateDateExpirationInSession(st, cle);
		st.close();
		c.close();
	}
	
	/**
	 * Code de generation aleatoire d'une cle de session de 32 caracteres
	 * @return une cle aleatoire
	 */
	public static String createKey(){
		double aux1, aux2;
		int sc;
		int codeAscii;
		String res = "";
		for(int i=0; i<32; i++){
			aux1 = Math.random()*3;
			sc = (int) aux1;
			switch(sc){
			case 0: 
				//Chiffre (code Ascii entre 48 et 57)
				aux2 = Math.random()*10+48;
				codeAscii = (int) aux2;
				res += (char)codeAscii;
				break;
			case 1:
				//Lettre majuscule (code Ascii entre 65 et 90)
				aux2 = Math.random()*26+65;
				codeAscii = (int) aux2;
				res += (char)codeAscii;
				break;
			case 2:
				//Lettre minuscule (code ascii entre 97 et 122)
				aux2 = Math.random()*26+97;
				codeAscii = (int) aux2;
				res += (char)codeAscii;
				break;
			}			
		}
		return res;
	}
	

	public static int obtenirIdAvecCle(String cle) throws SQLException{
		int res = 0;
		Connection c = DBStatic.getSQLConnection();
		Statement st = c.createStatement();
		ResultSet rs = RequeteStatic.obtenirIdAvecCle(st, cle);
		if(rs.next())
			res = rs.getInt("idSession");
		rs.close();
		st.close();
		c.close();
		return res;
	}

	
}
