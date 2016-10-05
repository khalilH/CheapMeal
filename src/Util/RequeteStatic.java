package Util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;


public class RequeteStatic {

	public static void deleteFromSessions(Statement statement, String cle) throws SQLException {
		statement.executeUpdate("delete from SESSIONS where cleSession='"+cle+"'");
	}

	public static ResultSet selectDateExpirationFromSessions(Statement statement, String cle) throws SQLException {
		return statement.executeQuery("select dateExpiration from `SESSIONS` where cleSession='"+cle+"';");
	}

	public static ResultSet checkUserExist(Statement st,String login) throws SQLException {
		return st.executeQuery("select * from UTILISATEURS u where login = '"+login+"';");
	}

	public static ResultSet checkCredentialsValid(Statement st,String login,String mdp) throws SQLException {
		return st.executeQuery("select * from UTILISATEURS u where login = '"+login+"' and mdp = '"+mdp+"';");
	}

	public static ResultSet checkSessionOpen(Statement st, String login) throws SQLException {
		return st.executeQuery("select idSession from SESSIONS s, UTILISATEURS u where idSession = u.id and u.login = '"+login+"';");
	}

	public static void updateTokenTime(Statement st, String login, Timestamp time) throws SQLException {
		st.executeUpdate("update from SESSIONS s, UTILISATEURS u set s.dateExpiration = "+time+" where idSession = u.id and u.login = '"+login+"' ;");
	}
	public static ResultSet retrieveTokenFromLogin(Statement st, String login) throws SQLException {
		return st.executeQuery("select cleSession from SESSIONS s, UTILISATEURS u where idSession = u.id and u.login = '"+login+"' ;");
	}

	public static void createSessionFromLogin(Statement st, String id, Timestamp time, String key) throws SQLException {
		st.executeUpdate("insert into SESSIONS values ('"+id+"','"+key+"','"+time+"');");
	}

	public static void updateDateExpirationInSession(Statement statement, String cle) throws SQLException {
		statement.executeUpdate("update SESSIONS set dateExpiration=date_add(now(), INTERVAL 30 MINUTE) where cleSession='"+cle+"';");
	}
	
}
