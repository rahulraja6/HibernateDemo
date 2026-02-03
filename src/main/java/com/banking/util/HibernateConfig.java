package com.banking.util;

import java.util.Properties;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import com.banking.entity.Account;
import com.banking.entity.AccountSubscription;
import com.banking.entity.BankTransaction;
import com.banking.entity.Customer;
import com.banking.entity.KycProfile;
import com.banking.entity.RefNotificationChannel;

public class HibernateConfig {

    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();

                // Hibernate settings equivalent to hibernate.cfg.xml's properties
                Properties settings = new Properties();
                
                // Database connection settings
                settings.put(Environment.DRIVER, "oracle.jdbc.driver.OracleDriver");
                settings.put(Environment.URL, "*********************************************");
                settings.put(Environment.USER, "*********************************************");
                settings.put(Environment.PASS, "*********************************************");
                
                // Dialect
                settings.put(Environment.DIALECT, "org.hibernate.dialect.Oracle10gDialect");
                
                // SQL settings
                settings.put(Environment.SHOW_SQL, "true");
                settings.put(Environment.FORMAT_SQL, "true");
                settings.put(Environment.USE_SQL_COMMENTS, "true");
                
                // Connection pool
                settings.put(Environment.POOL_SIZE, "10");
                
                // Current session context
                settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");

                configuration.setProperties(settings);

                // Add annotated entity classes
                configuration.addAnnotatedClass(RefNotificationChannel.class);
                configuration.addAnnotatedClass(KycProfile.class);
                configuration.addAnnotatedClass(Customer.class);
                configuration.addAnnotatedClass(Account.class);
                configuration.addAnnotatedClass(BankTransaction.class);
                configuration.addAnnotatedClass(AccountSubscription.class);

                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties()).build();

                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
                
                System.out.println("✓ Hibernate SessionFactory initialized with annotation-based configuration");
                
            } catch (Exception e) {
                System.err.println("Failed to create SessionFactory: " + e);
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}
