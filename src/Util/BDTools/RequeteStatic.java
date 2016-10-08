package Util.BDTools;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;


public class RequeteStatic {

	public static void supprimerSessionAvecCle(Statement statement, String cle) throws SQLException {
		statement.executeUpdate("delete from SESSIONS where cleSession='"+cle+"'");
	}

	public static ResultSet getDateExpirationAvecCle(Statement statement, String cle) throws SQLException {
		return statement.executeQuery("select dateExpiration from `SESSIONS` where cleSession='"+cle+"';");
	}

	public static ResultSet verifierUtilisateurExiste(Statement st,String login) throws SQLException {
		return st.executeQuery("select * from UTILISATEURS u where login = '"+login+"';");
	}

	public static ResultSet checkIdentifiantsValide(Statement st,String login,String mdp) throws SQLException {
		return st.executeQuery("select * from UTILISATEURS u where login = '"+login+"' and mdp = '"+mdp+"';");
	}

	public static ResultSet isSessionCree(Statement st, String login) throws SQLException {
		return st.executeQuery("select idSession from SESSIONS s, UTILISATEURS u where idSession = u.id and u.login = '"+login+"';");
	}

	public static void updateDateExpirationAvecLogin(Statement st, String login, Timestamp time) throws SQLException {
		st.executeUpdate("update SESSIONS s, UTILISATEURS u set s.dateExpiration = '"+time+"' where idSession = u.id and u.login = '"+login+"' ;");
	}
	public static ResultSet recupererTokenAvecLogin(Statement st, String login) throws SQLException {
		return st.executeQuery("select cleSession from SESSIONS s, UTILISATEURS u where idSession = u.id and u.login = '"+login+"' ;");
	}

	public static void createSessionFromLogin(Statement st, String id, Timestamp time, String key) throws SQLException {
		st.executeUpdate("insert into SESSIONS values ('"+id+"','"+key+"','"+time+"');");
	}

	public static void updateDateExpirationAvecCle(Statement statement, String cle) throws SQLException {
		statement.executeUpdate("update SESSIONS set dateExpiration=date_add(now(), INTERVAL 30 MINUTE) where cleSession='"+cle+"';");
	}
	
	public static void ajoutUtilisateur(Statement st, String login, String mdp, String nom, String prenom, String email) throws SQLException{
		st.executeUpdate("insert into `UTILISATEURS` (login, mdp, prenom, nom, mail) values ('"+login+"','"+mdp+"','"+prenom+"','"+nom+"','"+email+"');");
	}
	
	public static ResultSet verifierEmail(Statement st, String email) throws SQLException{
		return st.executeQuery("select u.mail from UTILISATEURS u where u.mail='"+email+"';");
	}
	
	public static ResultSet obtenirIdAvecLogin(Statement st, String login) throws SQLException{
		return st.executeQuery("select u.id from UTILISATEURS u where u.login='"+login+"';");
	}
	
	public static ResultSet obtenirMdpAvecLogin(Statement st, String login) throws SQLException{
		return st.executeQuery("select u.mdp from UTILISATEURS u where u.login='"+login+"';");
	}
	
	public static void changerMdpAvecId(Statement st, int id, String mdp) throws SQLException{
		st.executeUpdate("update UTILISATEURS set u.mdp='"+mdp+"' where u.id='"+id+"';");
	}
	
	public static ResultSet obtenirIdSessionAvecCle(Statement st, String cle) throws SQLException{
		return st.executeQuery("select idSession from SESSIONS s where s.cleSession'"+cle+"';");
	}
// Comment
}
