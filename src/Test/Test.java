package Test;

import java.sql.Timestamp;

import org.hibernate.Session;

import Util.Hibernate.HibernateUtil;
import Util.Hibernate.Model.Sessions;
import Util.Hibernate.Model.Utilisateurs;
public class Test {

	public static void main(String[] args) {
		Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		s.beginTransaction();
		Integer id  =(Integer) s.save(new Utilisateurs("toz", "1234", "issa", "toz", "1223@"));
		System.out.println("Id is "+id);
		s.getTransaction().commit();
		
		s = HibernateUtil.getSessionFactory().getCurrentSession();
		s.beginTransaction();
		Sessions s1 = new Sessions(1, "ABCDETOZ", new Timestamp(0));
		Utilisateurs user=(Utilisateurs) s.load(Utilisateurs.class, 1);
		s1.setU(user);
		s.save(s1);
		s.getTransaction().commit();
	}

}
