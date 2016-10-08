package Test;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import org.hibernate.Session;

import Util.BDTools.RequeteStatic;
import Util.Hibernate.HibernateUtil;
import Util.Hibernate.Model.Sessions;
import Util.Hibernate.Model.Utilisateurs;
public class Test {

	public static void main(String[] args) {
		System.out.println(RequeteStatic.obtenirIdSessionAvecCle("TGZZZ"));
	}
}
