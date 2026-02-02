package com.banking;

import static org.junit.jupiter.api.Assertions.*;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.banking.entity.Account;
import com.banking.entity.Customer;
import com.banking.entity.KycProfile;
import com.banking.entity.RefNotificationChannel;
import com.banking.util.HibernateUtil;

import java.util.List;

/**
 * Test class to verify Hibernate configuration and database connectivity
 */
public class HibernateConfigTest {

    private static SessionFactory sessionFactory;

    @BeforeAll
    public static void setup() {
        System.out.println("Initializing Hibernate SessionFactory for tests...");
        sessionFactory = HibernateUtil.getSessionFactory();
        assertNotNull(sessionFactory, "SessionFactory should not be null");
    }

    @AfterAll
    public static void tearDown() {
        if (sessionFactory != null) {
            System.out.println("Closing SessionFactory...");
            HibernateUtil.shutdown();
        }
    }

    @Test
    public void testSessionFactoryCreation() {
        System.out.println("\n[TEST] Testing SessionFactory creation...");
        assertNotNull(sessionFactory, "SessionFactory should be initialized");
        assertFalse(sessionFactory.isClosed(), "SessionFactory should not be closed");
        System.out.println("✓ SessionFactory is properly initialized");
    }

    @Test
    public void testDatabaseConnection() {
        System.out.println("\n[TEST] Testing database connection...");
        Session session = null;
        try {
            session = sessionFactory.openSession();
            assertNotNull(session, "Session should not be null");
            assertTrue(session.isConnected(), "Session should be connected to database");
            System.out.println("✓ Database connection successful");
        } catch (Exception e) {
            fail("Failed to open session: " + e.getMessage());
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Test
    public void testRefNotificationChannelEntity() {
        System.out.println("\n[TEST] Testing RefNotificationChannel entity mapping...");
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Query<RefNotificationChannel> query = session.createQuery(
                "FROM RefNotificationChannel", RefNotificationChannel.class);
            List<RefNotificationChannel> channels = query.list();
            
            assertNotNull(channels, "Channels list should not be null");
            System.out.println("✓ Retrieved " + channels.size() + " notification channels");
            
            for (RefNotificationChannel channel : channels) {
                assertNotNull(channel.getId(), "Channel ID should not be null");
                assertNotNull(channel.getChannelName(), "Channel name should not be null");
                System.out.println("  - " + channel.getChannelName() + " (Cost: " + channel.getMonthlyCost() + ")");
            }
        } catch (Exception e) {
            fail("Failed to query RefNotificationChannel: " + e.getMessage());
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Test
    public void testKycProfileEntity() {
        System.out.println("\n[TEST] Testing KycProfile entity mapping...");
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Query<KycProfile> query = session.createQuery(
                "FROM KycProfile", KycProfile.class);
            List<KycProfile> profiles = query.list();
            
            assertNotNull(profiles, "KYC profiles list should not be null");
            System.out.println("✓ Retrieved " + profiles.size() + " KYC profiles");
        } catch (Exception e) {
            fail("Failed to query KycProfile: " + e.getMessage());
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Test
    public void testCustomerEntity() {
        System.out.println("\n[TEST] Testing Customer entity mapping...");
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Query<Customer> query = session.createQuery(
                "FROM Customer", Customer.class);
            List<Customer> customers = query.list();
            
            assertNotNull(customers, "Customers list should not be null");
            System.out.println("✓ Retrieved " + customers.size() + " customers");
        } catch (Exception e) {
            fail("Failed to query Customer: " + e.getMessage());
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Test
    public void testAccountEntity() {
        System.out.println("\n[TEST] Testing Account entity mapping...");
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Query<Account> query = session.createQuery(
                "FROM Account", Account.class);
            List<Account> accounts = query.list();
            
            assertNotNull(accounts, "Accounts list should not be null");
            System.out.println("✓ Retrieved " + accounts.size() + " accounts");
        } catch (Exception e) {
            fail("Failed to query Account: " + e.getMessage());
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Test
    public void testTransactionManagement() {
        System.out.println("\n[TEST] Testing transaction management...");
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            
            // Verify transaction is active
            assertTrue(session.getTransaction().isActive(), "Transaction should be active");
            System.out.println("✓ Transaction started successfully");
            
            // Rollback the transaction (we're just testing, not making actual changes)
            session.getTransaction().rollback();
            assertFalse(session.getTransaction().isActive(), "Transaction should not be active after rollback");
            System.out.println("✓ Transaction rollback successful");
        } catch (Exception e) {
            fail("Failed to manage transaction: " + e.getMessage());
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Test
    public void testMultipleSessions() {
        System.out.println("\n[TEST] Testing multiple concurrent sessions...");
        Session session1 = null;
        Session session2 = null;
        try {
            session1 = sessionFactory.openSession();
            session2 = sessionFactory.openSession();
            
            assertNotNull(session1, "First session should not be null");
            assertNotNull(session2, "Second session should not be null");
            assertNotEquals(session1, session2, "Sessions should be different instances");
            
            assertTrue(session1.isConnected(), "First session should be connected");
            assertTrue(session2.isConnected(), "Second session should be connected");
            
            System.out.println("✓ Multiple sessions created successfully");
        } catch (Exception e) {
            fail("Failed to create multiple sessions: " + e.getMessage());
        } finally {
            if (session1 != null) session1.close();
            if (session2 != null) session2.close();
        }
    }
}
