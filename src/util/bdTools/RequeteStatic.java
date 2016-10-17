package util.bdTools;	

import java.sql.Timestamp;

import org.hibernate.Session;

import util.hibernate.HibernateUtil;
import util.hibernate.model.Profils;
import util.hibernate.model.Sessions;
import util.hibernate.model.Utilisateurs;


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
	 * Supprime la session d'un utilisateur grace � son Token
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

	/**
	 * Permet d'obtenir la date d'expiration d'une cle
	 * @param cle
	 * @return le timestamp de la date d'expiration
	 */
	public static Timestamp getDateExpirationAvecCle(String cle)  {
		Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		s.beginTransaction();
		Timestamp s_timestamp=(Timestamp) s.createQuery("Select s.dateExpiration from Sessions s where s.cleSession = :cleSession")
					.setParameter("cleSession", cle)
					.uniqueResult();
		s.getTransaction().commit();
		return s_timestamp;
	}
	
	/**
	 * Permet de verifier si une cle de session est toujours active
	 * @param cle la cle de session a verifier
	 * @return true si la cle de session est active
	 */
	public static boolean isCleActive(String cle) {
		Timestamp dateExpiration = getDateExpirationAvecCle(cle),
				currentTime = new Timestamp(System.currentTimeMillis());
		
		return dateExpiration != null && currentTime.before(dateExpiration);
	}

	/**
	 * Verifie si un login est libre ou non
	 * @param login le login a tester
	 * @return true si le login est disponible, false sinon
	 */
	public static boolean isLoginDisponible(String login)  {
		
		Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		s.beginTransaction();
		Utilisateurs user =(Utilisateurs) s.createQuery("from Utilisateurs u where login = :login")
					.setParameter("login", login)
					.uniqueResult();
		s.getTransaction().commit();
		return user == null;
	}

	/**
	 * Verifie si le couple login/mdp est valide
	 * @param login 
	 * @param mdp
	 * @return true si le couple existe, false sinon
	 */
	public static boolean checkIdentifiantsValide(String login,String mdp)  {
		Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		s.beginTransaction();
		Utilisateurs user =(Utilisateurs) s.createQuery("from Utilisateurs u where login = :login and mdp = :mdp")
					.setParameter("login", login)
					.setParameter("mdp", mdp)
					.uniqueResult();
		s.getTransaction().commit();
		return user != null;
	}

	/**
	 * Verifie si une cle de session pour un utilisateur existe
	 * @param login
	 * @return true si une cle de session existe, false sinon 
	 */
	public static boolean isSessionCree(String login)  {
		Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		s.beginTransaction();
		Sessions session =(Sessions) s.createQuery("select s from Utilisateurs u,Sessions s where u.login = :login and u.id = s.idSession")
					.setParameter("login", login)
					.uniqueResult();
		s.getTransaction().commit();
		return session != null;
	}

	/**
	 * Met a jour la date d'expiration de la cle de session d'un
	 * utilisateur en utilisant son login (pour la connexion)
	 * @param login
	 */
	public static void updateDateExpirationAvecLogin(String login)  {
		Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		Timestamp time = new Timestamp(System.currentTimeMillis()+30*60*1000);
		s.beginTransaction();
		s.createSQLQuery("update SESSIONS s, UTILISATEURS u set s.dateExpiration = :time where s.idSession = u.id and u.login = :u_login")
						.setParameter("u_login", login)
						.setParameter("time", time)
						.executeUpdate();
		s.getTransaction().commit();
	}
	
	/**
	 * Permet de recuperer la cle de session d'un utilisateur a partir
	 * de son login
	 * @param login
	 * @return la cle de session associe a login
	 */
	public static String recupererTokenAvecLogin(String login)  {
		Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		s.beginTransaction();
		String session_token =(String) s.createQuery("select s.cleSession from Utilisateurs u,Sessions s where u.login = :login and u.id = s.idSession")
					.setParameter("login", login)
					.uniqueResult();
		s.getTransaction().commit();
		return session_token;
	}

	/**
	 * Permet de creer une creer cle de session pour l'utilisateur donne
	 * a partir de son login
	 * @param id le login de l'utilisateur
	 * @return la cle de session cree
	 */
	public static String createSessionFromLogin(String login) {
		int id = obtenirIdAvecLogin(login);
		Timestamp time = new Timestamp(System.currentTimeMillis()+30*60*1000);
		String cle = createKey();
		
		Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		s.beginTransaction();
		Utilisateurs user = (Utilisateurs) s.load(Utilisateurs.class, id);
		Sessions s1 = new Sessions(id, cle, time);
		s1.setUtilisateur(user);
		s.save(s1);
		s.getTransaction().commit();
		return cle;
		//TODO verifier que la cle generee n'existe pas deja (tres peu probable)
	}

	/**
	 * Met a jour la date d'expiration de la cle de session d'un
	 * utilisateur a partir de sa cle (utilisation de la cle)
	 * @param cle
	 */
	public static void updateDateExpirationAvecCle(String cle)  {
		Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		s.beginTransaction();
		s.createSQLQuery("update SESSIONS set dateExpiration=date_add(now(), INTERVAL 30 MINUTE) where cleSession = :cle")
						.setParameter("cle", cle)
						.executeUpdate();
		s.getTransaction().commit();
	}
	
	/**
	 * Permet d'ajouter un utilisateur (creation de compte)
	 * @param login le login
	 * @param mdp le mot de passe
	 * @param nom le nom
	 * @param prenom le prenom
	 * @param email l'adresse mail
	 * @return l'identifiant id avec lequel l'utilisateur a ete ajoute
	 * dans la base de donnee
	 */
	public static Integer ajoutUtilisateur(String login, String mdp, String nom, String prenom, String email) {
		Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		s.beginTransaction();
		Integer id = (Integer) s.save(new Utilisateurs(login, mdp, prenom, nom, email));
		s.getTransaction().commit();
		ajouterProfil(id);
		return id;
	}
	
	/**
	 * Permet de creer automatiquement le profil d'un utilisateur
	 * a la creation de son compte, sa biographie est vide
	 * @param id l'identifiant de l'utilisateur
	 */
	private static void ajouterProfil(int id) {
		Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		s.beginTransaction();
		Utilisateurs user = (Utilisateurs) s.load(Utilisateurs.class, id);
		Profils p1 = new Profils(id, null);
		p1.setUtilisateur(user);
		s.save(p1);
		s.getTransaction().commit();
	}

	/**
	 * Permet de verifier si un email est disponible
	 * @param email l'addresse mail a tester
	 * @return true si l'adresse mail est disponible, false sinon
	 */
	public static boolean isEmailDisponible(String email) {
		Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		s.beginTransaction();
		String u_mail =(String) s.createQuery("select u.mail from Utilisateurs u where u.mail= :mail")
					.setParameter("mail", email)
					.uniqueResult();
		s.getTransaction().commit();
		return u_mail == null;
	}
	
	/**
	 * Permet d'obtenir l'identifiant d'un utilisateur a partir 
	 * de son nom d'utilisateur
	 * @param login
	 * @return l'id de l'utilisateur si l'utilisateur existe, -1 sinon
	 */
	public static int obtenirIdAvecLogin(String login) {
		Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		s.beginTransaction();
		Integer u_id =(Integer) s.createQuery("select u.id from Utilisateurs u where u.login= :login")
					.setParameter("login", login)
					.uniqueResult();
		s.getTransaction().commit();
		if(u_id == null)
			return -1;
		else
			return u_id;
	}
	
	/**
	 * Permet de recuperer le mot de passe d'un utilisateur 
	 * a partir de son login
	 * @param login
	 * @return le mot de passe de l'utilisateur s'il existe
	 */
	public static String obtenirMdpAvecLogin(String login) {
		Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		s.beginTransaction();
		String u_mdp =(String) s.createQuery("select u.mdp from Utilisateurs u where u.login= :login")
					.setParameter("login", login)
					.uniqueResult();
		s.getTransaction().commit();
		return u_mdp;
	}
	
	/**
	 * Permet de changer le mot de passe d'un utilisateur avec son id
	 * @param id l'identifiant d'un utilisateur
	 * @param mdp le nouveau mot de passe
	 */
	public static void changerMdpAvecId(int id, String mdp) {
		Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		s.beginTransaction();
		s.createQuery("update Utilisateurs u set u.mdp = :mdp where u.id = :id")
					.setParameter("mdp", mdp)
					.setParameter("id", id)
					.executeUpdate();
		s.getTransaction().commit();
	}
	
	/**
	 * Permet de recuperer l'identifiant d'un utilisateur a partir de
	 * sa cle de session
	 * @param cle la cle de session
	 * @return l'id de l'utilisateur si sa cle de session existe,
	 * false sinon
	 */
	public static int obtenirIdSessionAvecCle(String cle) {
		Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		s.beginTransaction();
		Integer s_id =(Integer) s.createQuery("select s.idSession from Sessions s where s.cleSession = :cle")
					.setParameter("cle", cle)
					.uniqueResult();
		s.getTransaction().commit();
		if(s_id == null)
			return -1;
		else
			return s_id;
	}
	
	/**
	 * Code de generation aleatoire d'une cle de session de 32 caracteres
	 * @return une cle aleatoire
	 */
	private static String createKey(){
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
	
	/**
	 * Permet de changer l'adresse mail d'un utilisateur avec son id
	 * @param id l'identifiant d'un utilisateur
	 * @param email la nouvelle adresse mail
	 */
	public static void changerEmailAvecId(int id, String email) {
		Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		s.beginTransaction();
		s.createQuery("update Utilisateurs u set u.mail = :email where u.id = :id")
					.setParameter("mail", email)
					.setParameter("id", id)
					.executeUpdate();
		s.getTransaction().commit();
	}
	
//TODO a supprimer si inutile, methode remplace par createSessionFromLogin
//	public static void createSessionFromId(Utilisateurs u)  {
//		Session s = HibernateUtil.getSessionFactory().getCurrentSession();
//		Timestamp time = new Timestamp(System.currentTimeMillis()+30*60*1000);
//		String key = createKey();
//		s.beginTransaction();
//		Sessions s_user = new Sessions(u.getId(),key,time);
//		s_user.setUtilisateur(u);
//		s.save(s_user);
//		s.getTransaction().commit();
//	
//	}

}