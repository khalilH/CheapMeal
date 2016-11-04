package test;

import javax.mail.MessagingException;
import javax.naming.NamingException;

import util.ServiceTools;

public class TestHibernate {

	public static void main(String[] args) {
//		System.out.println(RequeteStatic.obtenirIdSessionAvecCle("TGZZZ"));
		
//		Session s = HibernateUtil.getSessionFactory().getCurrentSession();
//		s.beginTransaction();
//		Integer id = (Integer) s.save(new Utilisateurs
//				("la12", "123456", "ladislas", "halifa", "ladi@toz.fr"));
//		s.getTransaction().commit();
//		
//		s = HibernateUtil.getSessionFactory().getCurrentSession();
//		s.beginTransaction();
//		Sessions s1 = new Sessions(id, "QWERTYUIOP", new Timestamp(System.currentTimeMillis()));
//		Utilisateurs user = (Utilisateurs) s.load(Utilisateurs.class, id);
//		s1.setUtilisateur(user);
//		s.save(s1);
//		
//		Profils p1 = new Profils(id, "TOZ MASTER");
//		p1.setUtilisateur(user);
//		s.save(p1);
//		
//		Admins a1 = new Admins(id);
//		s.save(a1);
//		s.getTransaction().commit();
		
		try {
			ServiceTools.sendEmail("Body integrate", "Toz","afuri@hotmail.fr");
		} catch (NamingException | MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
