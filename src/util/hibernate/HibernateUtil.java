package util.hibernate;


import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import util.hibernate.model.Admins;
import util.hibernate.model.Profils;
import util.hibernate.model.Sessions;
import util.hibernate.model.Utilisateurs;

public class HibernateUtil {

	private static SessionFactory sessionFactory= buildSessionFactory();
	private static ServiceRegistry serviceRegistry;
  
    private static SessionFactory buildSessionFactory() {
        try {
        	// Create the SessionFactory from hibernate.cfg.xml
        	Configuration configuration = new Configuration()
        			.addAnnotatedClass(Sessions.class)
        			.addAnnotatedClass(Profils.class)
        			.addAnnotatedClass(Admins.class)
        			.addAnnotatedClass(Utilisateurs.class)
        			.configure();
     
            serviceRegistry = new StandardServiceRegistryBuilder().applySettings(
                    configuration.getProperties()).build();
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            return sessionFactory;
           
        }
        catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

}