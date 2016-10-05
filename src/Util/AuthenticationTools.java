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
				/* raffraichir la cle */
				// rafraichirCle(cle)
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
		String key = "ABCD"; //TODO Generate proper unique authentication token 
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
