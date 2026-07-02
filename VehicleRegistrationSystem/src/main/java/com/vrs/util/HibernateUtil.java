package com.vrs.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            // Use default config file from classpath (src/main/resources/hibernate.cfg.xml)
            return new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("❌ SessionFactory creation failed: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
            System.out.println("✅ Hibernate SessionFactory shut down.");
        }
    }
}
