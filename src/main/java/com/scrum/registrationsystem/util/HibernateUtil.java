package com.scrum.registrationsystem.util;

import javax.swing.JOptionPane;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtil {

	private static final SessionFactory sessionFactory = buildSessionFactory();

	private static SessionFactory buildSessionFactory() {
		try {
			Configuration configuration = new Configuration();
			configuration.configure();
			configuration.addAnnotatedClass(com.scrum.registrationsystem.entities.User.class);
                        configuration.addAnnotatedClass(com.scrum.registrationsystem.entities.Fines.class);
                        configuration.addAnnotatedClass(com.scrum.registrationsystem.entities.Register.class);


			ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
					.applySettings(configuration.getProperties()).build();

			return configuration.buildSessionFactory(serviceRegistry);
		} catch (HibernateException ex) {
			System.err.println("Initial SessionFactory creation failed." + ex);
			JOptionPane.showMessageDialog(null,
					"A ocurrido un error en la base de datos. Por favor contactese con el administrador.",
					"Error en la base de datos",
					JOptionPane.ERROR_MESSAGE);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public static void shutdown() {
		getSessionFactory().close();
	}
}
