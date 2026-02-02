package com.banking.examples;

import com.banking.entity.*;
import com.banking.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Comprehensive CRUD Operations Examples
 * Demonstrates Create, Read, Update, Delete operations for all entities
 */
public class CRUDExamples {

    public static void main(String[] args) {
        System.out.println("=== Hibernate CRUD Operations Examples ===\n");
        
        // Run all examples
        createExamples();
        readExamples();
        updateExamples();
        deleteExamples();
        
        // Shutdown
        HibernateUtil.shutdown();
        System.out.println("\n=== All CRUD operations completed ===");
    }

    /**
     * CREATE Operations - Insert new records
     */
    public static void createExamples() {
        System.out.println("\n--- CREATE Operations ---");
        Session session = null;
        Transaction transaction = null;
        
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            
            // 1. Create KYC Profile
            System.out.println("\n1. Creating KYC Profile...");
            KycProfile kyc = new KycProfile();
            kyc.setPanNumber("ABCDE1234F");
            kyc.setRiskStatus("LOW");
            session.save(kyc);
            System.out.println("✓ Created KYC Profile with ID: " + kyc.getId());
            
            // 2. Create Customer with KYC (One-to-One relationship)
            System.out.println("\n2. Creating Customer with KYC...");
            Customer customer = new Customer();
            customer.setFullName("John Doe");
            customer.setEmail("john.doe@example.com");
            customer.setKycProfile(kyc);  // One-to-One mapping
            session.save(customer);
            System.out.println("✓ Created Customer with ID: " + customer.getId());
            
            // 3. Create Account (One-to-Many: Customer -> Accounts)
            System.out.println("\n3. Creating Account for Customer...");
            Account account = new Account();
            account.setAccountNumber("ACC" + System.currentTimeMillis());
            account.setBalance(new BigDecimal("10000.00"));
            account.setCustomer(customer);  // Many-to-One mapping
            session.save(account);
            System.out.println("✓ Created Account with ID: " + account.getId());
            
            // 4. Create Notification Channels
            System.out.println("\n4. Creating Notification Channels...");
            RefNotificationChannel smsChannel = new RefNotificationChannel();
            smsChannel.setChannelName("SMS_PREMIUM");
            smsChannel.setMonthlyCost(new BigDecimal("5.00"));
            session.save(smsChannel);
            
            RefNotificationChannel emailChannel = new RefNotificationChannel();
            emailChannel.setChannelName("EMAIL_PREMIUM");
            emailChannel.setMonthlyCost(new BigDecimal("0.00"));
            session.save(emailChannel);
            System.out.println("✓ Created Notification Channels");
            
            // 5. Subscribe Account to Channels (Many-to-Many relationship)
            System.out.println("\n5. Subscribing Account to Channels...");
            account.getSubscriptions().add(smsChannel);
            account.getSubscriptions().add(emailChannel);
            session.update(account);  // Many-to-Many mapping
            System.out.println("✓ Account subscribed to " + account.getSubscriptions().size() + " channels");
            
            // 6. Create Bank Transactions (One-to-Many: Account -> Transactions)
            System.out.println("\n6. Creating Bank Transactions...");
            BankTransaction deposit = new BankTransaction();
            deposit.setTxDate(new Date());
            deposit.setType("CREDIT");
            deposit.setAmount(new BigDecimal("5000.00"));
            deposit.setStatus("COMPLETED");
            deposit.setAccount(account);  // Many-to-One mapping
            session.save(deposit);
            
            BankTransaction withdrawal = new BankTransaction();
            withdrawal.setTxDate(new Date());
            withdrawal.setType("DEBIT");
            withdrawal.setAmount(new BigDecimal("1000.00"));
            withdrawal.setStatus("COMPLETED");
            withdrawal.setAccount(account);
            session.save(withdrawal);
            System.out.println("✓ Created 2 Bank Transactions");
            
            transaction.commit();
            System.out.println("\n✓ All CREATE operations completed successfully!");
            
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.err.println("Error in CREATE operations: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }

    /**
     * READ Operations - Retrieve and query records
     */
    public static void readExamples() {
        System.out.println("\n--- READ Operations ---");
        Session session = null;
        
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            
            // 1. Read by ID (get/load)
            System.out.println("\n1. Reading Customer by ID...");
            Customer customer = session.get(Customer.class, 1L);
            if (customer != null) {
                System.out.println("✓ Found: " + customer.getFullName() + " (" + customer.getEmail() + ")");
                
                // Accessing One-to-One relationship
                if (customer.getKycProfile() != null) {
                    System.out.println("  KYC: " + customer.getKycProfile().getPanNumber() + 
                                     " - " + customer.getKycProfile().getRiskStatus());
                }
            }
            
            // 2. Read All (HQL)
            System.out.println("\n2. Reading all Customers using HQL...");
            Query<Customer> query = session.createQuery("FROM Customer", Customer.class);
            List<Customer> customers = query.list();
            System.out.println("✓ Found " + customers.size() + " customer(s)");
            for (Customer c : customers) {
                System.out.println("  - " + c.getFullName() + " (" + c.getEmail() + ")");
            }
            
            // 3. Conditional Query (HQL with WHERE)
            System.out.println("\n3. Finding Customers by email domain...");
            Query<Customer> emailQuery = session.createQuery(
                "FROM Customer WHERE email LIKE :domain", Customer.class);
            emailQuery.setParameter("domain", "%example.com");
            List<Customer> filteredCustomers = emailQuery.list();
            System.out.println("✓ Found " + filteredCustomers.size() + " customer(s) with example.com email");
            
            // 4. Read with JOIN (accessing relationships)
            System.out.println("\n4. Reading Accounts with Customer details...");
            Query<Account> accountQuery = session.createQuery(
                "FROM Account a JOIN FETCH a.customer", Account.class);
            List<Account> accounts = accountQuery.list();
            System.out.println("✓ Found " + accounts.size() + " account(s)");
            for (Account acc : accounts) {
                System.out.println("  - Account: " + acc.getAccountNumber() + 
                                 " | Balance: " + acc.getBalance() +
                                 " | Owner: " + acc.getCustomer().getFullName());
                
                // Accessing Many-to-Many relationship
                if (!acc.getSubscriptions().isEmpty()) {
                    System.out.println("    Subscriptions: " + acc.getSubscriptions().size());
                    for (RefNotificationChannel channel : acc.getSubscriptions()) {
                        System.out.println("      * " + channel.getChannelName());
                    }
                }
            }
            
            // 5. Aggregate Query
            System.out.println("\n5. Calculating total balance across all accounts...");
            Query<BigDecimal> sumQuery = session.createQuery(
                "SELECT SUM(balance) FROM Account", BigDecimal.class);
            BigDecimal totalBalance = sumQuery.uniqueResult();
            System.out.println("✓ Total Balance: " + (totalBalance != null ? totalBalance : "0.00"));
            
            // 6. Read Transactions for an Account
            System.out.println("\n6. Reading Bank Transactions...");
            Query<BankTransaction> txQuery = session.createQuery(
                "FROM BankTransaction tx WHERE tx.account.id = :accountId ORDER BY tx.txDate DESC", 
                BankTransaction.class);
            txQuery.setParameter("accountId", 1L);
            txQuery.setMaxResults(5);
            List<BankTransaction> transactions = txQuery.list();
            System.out.println("✓ Found " + transactions.size() + " transaction(s)");
            for (BankTransaction tx : transactions) {
                System.out.println("  - " + tx.getType() + " | Amount: " + tx.getAmount() + 
                                 " | Status: " + tx.getStatus());
            }
            
            System.out.println("\n✓ All READ operations completed successfully!");
            
        } catch (Exception e) {
            System.err.println("Error in READ operations: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }

    /**
     * UPDATE Operations - Modify existing records
     */
    public static void updateExamples() {
        System.out.println("\n--- UPDATE Operations ---");
        Session session = null;
        Transaction transaction = null;
        
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            
            // 1. Simple Update
            System.out.println("\n1. Updating Customer email...");
            Customer customer = session.get(Customer.class, 1L);
            if (customer != null) {
                String oldEmail = customer.getEmail();
                customer.setEmail("john.doe.updated@example.com");
                session.update(customer);
                System.out.println("✓ Updated email from " + oldEmail + " to " + customer.getEmail());
            }
            
            // 2. Update with relationship change
            System.out.println("\n2. Updating Account balance...");
            Account account = session.get(Account.class, 1L);
            if (account != null) {
                BigDecimal oldBalance = account.getBalance();
                account.setBalance(account.getBalance().add(new BigDecimal("500.00")));
                session.update(account);
                System.out.println("✓ Updated balance from " + oldBalance + " to " + account.getBalance());
            }
            
            // 3. Bulk Update (HQL)
            System.out.println("\n3. Bulk updating transaction status...");
            Query<?> bulkUpdate = session.createQuery(
                "UPDATE BankTransaction SET status = :newStatus WHERE status = :oldStatus");
            bulkUpdate.setParameter("newStatus", "VERIFIED");
            bulkUpdate.setParameter("oldStatus", "COMPLETED");
            int updatedCount = bulkUpdate.executeUpdate();
            System.out.println("✓ Updated " + updatedCount + " transaction(s)");
            
            // 4. Update Many-to-Many relationship
            System.out.println("\n4. Removing a subscription from Account...");
            if (account != null && !account.getSubscriptions().isEmpty()) {
                int beforeSize = account.getSubscriptions().size();
                RefNotificationChannel channelToRemove = account.getSubscriptions().iterator().next();
                account.getSubscriptions().remove(channelToRemove);
                session.update(account);
                System.out.println("✓ Removed " + channelToRemove.getChannelName() + 
                                 " | Subscriptions: " + beforeSize + " -> " + account.getSubscriptions().size());
            }
            
            transaction.commit();
            System.out.println("\n✓ All UPDATE operations completed successfully!");
            
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.err.println("Error in UPDATE operations: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }

    /**
     * DELETE Operations - Remove records
     */
    public static void deleteExamples() {
        System.out.println("\n--- DELETE Operations ---");
        Session session = null;
        Transaction transaction = null;
        
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            
            // 1. Simple Delete
            System.out.println("\n1. Deleting a Bank Transaction...");
            BankTransaction tx = session.get(BankTransaction.class, 1L);
            if (tx != null) {
                String txInfo = tx.getType() + " - " + tx.getAmount();
                session.delete(tx);
                System.out.println("✓ Deleted transaction: " + txInfo);
            }
            
            // 2. Bulk Delete (HQL)
            System.out.println("\n2. Bulk deleting old transactions...");
            Query<?> bulkDelete = session.createQuery(
                "DELETE FROM BankTransaction WHERE status = :status");
            bulkDelete.setParameter("status", "CANCELLED");
            int deletedCount = bulkDelete.executeUpdate();
            System.out.println("✓ Deleted " + deletedCount + " transaction(s)");
            
            // Note: Cascade deletions would be demonstrated here if configured
            System.out.println("\n3. Delete with relationship handling...");
            System.out.println("   (Cascade operations depend on entity configuration)");
            
            transaction.commit();
            System.out.println("\n✓ All DELETE operations completed successfully!");
            
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.err.println("Error in DELETE operations: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }
}
