package Util.BDTools;

import org.hibernate.Session;

import Util.Hibernate.HibernateUtil;
import Util.Hibernate.Model.Utilisateurs;

public class Toz {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		s.beginTransaction();
		s.save(new Utilisateurs("issa94", "123456", "Issa", "Mahamat", "issa@toz.fr"));
		s.getTransaction().commit();
		s.close();
	}

}
