package Util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class RequeteStatic {

	public static void deleteFromSessions(Statement statement, String cle) throws SQLException {
		statement.executeUpdate("delete from SESSIONS where cleSession='"+cle+"'");
	}

	public static ResultSet selectDateExpirationFromSessions(Statement statement, String cle) throws SQLException {
		return statement.executeQuery("select dateExpiration from `SESSIONS` where cleSession='"+cle+"';");
	}
	

	
}
