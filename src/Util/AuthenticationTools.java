package Util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AuthenticationTools {

	public static boolean loginLibre(String login) throws SQLException{
		boolean res = true;
		Connection c;
		c = DBStatic.getSQLConnection();
		Statement stmt = c.createStatement();
		ResultSet rs = stmt.executeQuery("select u.login from UTILISATEURS u where u.login='"+login+"';");
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
		ResultSet rs = stmt.executeQuery("select u.mail from UTILISATEURS u where u.mail='"+email+"';");
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
	
	
}
