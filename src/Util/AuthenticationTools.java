package Util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class AuthenticationTools {

	public static boolean loginLibre(String login) throws SQLException{
		boolean res = true;
		Connection c;
		c = DBStatic.getSQLConnection();
		Statement stmt = c.createStatement();
		ResultSet rs = stmt.executeQuery("select u from UTILISATEURS where u.login='"+login+"';");
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
		ResultSet rs = stmt.executeQuery("select u from UTILISATEURS where u.mail='"+email+"';");
		if(rs.next())
			res = false;
		rs.close();
		stmt.close();
		c.close();
		return res;
	}
	
	public static int getIdAvecLogin(String login) throws SQLException{
		int id = 0;
		Connection c;
		c = DBStatic.getSQLConnection();
		Statement stmt = c.createStatement();
		ResultSet cur = stmt.executeQuery("select id from UTILISATEURS where login='"+login+"';");
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
	
	
	
}
