package test;

import java.sql.Timestamp;

import org.hibernate.Session;

import util.hibernate.HibernateUtil;
import util.hibernate.model.Admins;
import util.hibernate.model.Profils;
import util.hibernate.model.Sessions;
import util.hibernate.model.Utilisateurs;

public class TestHibernate {

	public static void main(String[] args) {
//		System.out.println(RequeteStatic.obtenirIdSessionAvecCle("TGZZZ"));
		
		Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		s.beginTransaction();
		Integer id = (Integer) s.save(new Utilisateurs
				("la12", "123456", "ladislas", "halifa", "ladi@toz.fr"));
		s.getTransaction().commit();
		
		s = HibernateUtil.getSessionFactory().getCurrentSession();
		s.beginTransaction();
		Sessions s1 = new Sessions(id, "QWERTYUIOP", new Timestamp(System.currentTimeMillis()));
		Utilisateurs user = (Utilisateurs) s.load(Utilisateurs.class, id);
		s1.setUtilisateur(user);
		s.save(s1);
		
		Profils p1 = new Profils(id, "TOZ MASTER");
		p1.setUtilisateur(user);
		s.save(p1);
		
		Admins a1 = new Admins(id);
		s.save(a1);
		s.getTransaction().commit();
	}
}
