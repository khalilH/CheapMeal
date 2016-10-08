package Util.BDTools;	

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import Util.Hibernate.HibernateUtil;
import Util.Hibernate.Model.Sessions;
import Util.Hibernate.Model.Utilisateurs;


public class RequeteStatic {
	/**
	 * 
	 * 	Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		s.beginTransaction();
		
				Exemple de creation d'une entite
	
	Integer id  =(Integer) s.save(new Utilisateurs("toz", "1234", "issa", "toz", "1223@"));
	
				Suppression dune entite
				
	s.createQuery("delete from Sessions where cleSession = :cleSession")
						.setParameter("cleSession", cle)
						.executeUpdate();
	s.getTransaction().commit();

				Update dune entite
				-> En SQL Parce qu'il ya 2 table
	s.createSQLQuery("update SESSIONS s, UTILISATEURS u set s.dateExpiration = :time where s.idSession = u.id and u.login = :u_login")
						.setParameter("u_login", login)
						.setParameter("time", time)
						.executeUpdate();
	s.getTransaction().commit();
				
				Get dune entite
				
					Sessions s_user =(Sessions) s.createQuery(" from Sessions where cleSession = :cleSession")
					.setParameter("cleSession", cle)
					.uniqueResult();
				
	**/
	
	
	/**
	 * Supprime la session d'un utilisateur grace à son Token
	 * @param cle
	 */
	public static void supprimerSessionAvecCle(String cle){
		Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		s.beginTransaction();
		s.createQuery("delete from Sessions where cleSession = :cleSession")
						.setParameter("cleSession", cle)
						.executeUpdate();
	
		s.getTransaction().commit();
	}

	public static Timestamp getDateExpirationAvecCle( String cle)  {
		Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		s.beginTransaction();
		Timestamp s_timestamp=(Timestamp) s.createQuery("Select s.dateExpiration from Sessions s where s.cleSession = :cleSession")
					.setParameter("cleSession", cle)
					.uniqueResult();
		s.close();
		return s_timestamp;
	}

	public static boolean verifierUtilisateurExiste(String login)  {
		
		Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		s.beginTransaction();
		Utilisateurs user =(Utilisateurs) s.createQuery("from Utilisateurs u where login = :login")
					.setParameter("login", login)
					.uniqueResult();
		s.close();
		return user != null;
	}

	public static boolean checkIdentifiantsValide(String login,String mdp)  {
		Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		s.beginTransaction();
		Utilisateurs user =(Utilisateurs) s.createQuery("from Utilisateurs u where login = :login and mdp = :mdp")
					.setParameter("login", login)
					.setParameter("mdp", mdp)
					.uniqueResult();
		s.close();
		return user != null;
	}

	public static boolean isSessionCree( String login)  {
		Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		s.beginTransaction();
		Sessions session =(Sessions) s.createQuery("select s from Utilisateurs u,Sessions s where u.login = :login and u.id = s.idSession")
					.setParameter("login", login)
					.uniqueResult();
		s.close();
		return session != null;
	}

	public static void updateDateExpirationAvecLogin( String login, Timestamp time)  {
		
		Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		s.beginTransaction();
		s.createSQLQuery("update SESSIONS s, UTILISATEURS u set s.dateExpiration = :time where s.idSession = u.id and u.login = :u_login")
						.setParameter("u_login", login)
						.setParameter("time", time)
						.executeUpdate();
	
		s.getTransaction().commit();
	
	}
	public static String recupererTokenAvecLogin( String login)  {
		Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		s.beginTransaction();
		String session_token =(String) s.createQuery("select s.cleSession from Utilisateurs u,Sessions s where u.login = :login and u.id = s.idSession")
					.setParameter("login", login)
					.uniqueResult();
		s.close();
		return session_token;
	}

	public static void createSessionFromId( Utilisateurs u, Timestamp time, String key)  {
		Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		s.beginTransaction();
		Sessions s_user = new Sessions(u.getId(),key,time);
		s_user.setU(u);
		s.save(s_user);
		s.getTransaction().commit();
		
	}

	public static void updateDateExpirationAvecCle( String cle)  {
		Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		s.beginTransaction();
		s.createSQLQuery("update SESSIONS set dateExpiration=date_add(now(), INTERVAL 30 MINUTE) where cleSession = :cle")
						.setParameter("cle", cle)
						.executeUpdate();
		s.getTransaction().commit();
	}
	
	public static void ajoutUtilisateur( String login, String mdp, String nom, String prenom, String email) {
		Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		s.beginTransaction();
		s.save(new Utilisateurs(login, mdp, prenom, nom, email));
		s.getTransaction().commit();
	}
	
	public static String verifierEmail( String email) {
		Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		s.beginTransaction();
		String u_mail =(String) s.createQuery("select u.mail from Utilisateurs u where u.mail= :mail")
					.setParameter("mail", email)
					.uniqueResult();
		s.close();
		return u_mail;
	}
	
	public static int obtenirIdAvecLogin( String login) {
		Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		s.beginTransaction();
		Integer u_id =(Integer) s.createQuery("select u.id from Utilisateurs u where u.login= :login")
					.setParameter("login", login)
					.uniqueResult();
		s.close();
		if(u_id == null)
			return -1;
		else
			return u_id;
	}
	
	public static String obtenirMdpAvecLogin( String login) {
		Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		s.beginTransaction();
		String u_mdp =(String) s.createQuery("select u.mdp from Utilisateurs u where u.login= :login")
					.setParameter("login", login)
					.uniqueResult();
		s.close();
		return u_mdp;
	}
	
	public static void changerMdpAvecId( int id, String mdp) {
		Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		s.beginTransaction();
		s.createQuery("update Utilisateurs u set u.mdp = :mdp where u.id = :id")
					.setParameter("mdp", mdp)
					.setParameter("id", id)
					.executeUpdate();
		s.getTransaction().commit();
		
	}
	
	public static int obtenirIdSessionAvecCle( String cle) {
		Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		s.beginTransaction();
		Integer s_id =(Integer) s.createQuery("select s.idSession from Sessions s where s.cleSession = :cle")
					.setParameter("cle", cle)
					.uniqueResult();
		s.close();
		if(s_id == null)
			return -1;
		else
			return s_id;
	}
}
